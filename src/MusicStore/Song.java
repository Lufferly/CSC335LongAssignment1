package MusicStore;

import java.util.ArrayList;

public class Song {
	
	private String name;		// The name of the song
	private String author;		// The author of the song
	private boolean favorite; 	// Whether the song is favorite or not
	private int rating;		    // Save rating as a String
	private int plays;
	
	public Song(String songName, String authorName) {
		this.name = songName;
		this.author = authorName;
		favorite = false;
		rating = 0;
		plays = 0;
	}

	public Song(Song song) {
		this.name = song.name;
		this.author = song.author;
		this.favorite = song.favorite;
		this.rating = song.rating;
		this.plays = song.plays;
	}

	public void playsong() { plays++; }			// Play a song once

	public int getPlays() { return this.plays; }		// Get the plays of this song
	
	public String getName() { return name; } // Returns name of the song
	
	public String getAuthor() { return author; }	// Returns name of the author
	
	public void makeFavorite() { this.favorite = true; }	// Makes a song favorite	

	public boolean isFavourite() { return this.favorite; }	// Returns if song is favorite
	
	public void unfavorite() { this.favorite = false; }		// Makes song not favorite
	
	public int getRating() { return rating; }	// Returns rating as a String
	
	// Sets new rating and makes song favorite automatically if rating is 5
	public void setRating (int newRating) {
		this.rating = newRating;
	}
	
	// Check if two songs equal eachother
	// They equal eachother if they have the same name and author
	public boolean equals(Song newSong) {
		if (!this.name.equals(newSong.getName())) return false;
		if (!this.author.equals(newSong.getAuthor())) return false;
		return true;
	}

	// Return a String representation of Song to refer to it
	@Override
	public String toString() {
		String stars = "";
		if (rating > 0) {
			for (int i = 0; i < rating; i++) {
				stars = stars + '*';
			}
		}

		if (rating > 0) {
			return name + ", " + author + ", " + stars;
		} else {
			return name + ", " + author;
		}
	}

	// Prints out the name of the song (for debugging)
	public void print() {
		System.out.print(name);
	}

	// A string representation of all the songs data, for writing into a save file
	//	or situations where we want a more concrete description of a song
	public String songData() {
		String dataString = "";
		// Add all the data
		dataString += "name:" + name + ";";
		dataString += "author:" + author + ";";
		dataString += "favorite:" + Boolean.toString(favorite) + ";";
		dataString += "rating:" + Integer.toString(rating) + ";";
		dataString += "plays:" + Integer.toString(plays) + ";";

		return dataString;
	}

	// Create a song object based on the dataString from songData()
	public static Song songFromSongData(String songData) {
		Song newSong = new Song(null, null);
		
		// Split the songData string into its individual data segments
		ArrayList<String[]> songDataList = new ArrayList<>();
		for (String data : songData.split(";")) {
			// If the string is empty, skip it
			if (data.equals("")) {
				continue;
			}
			// Get the key value pair for this data segment and add it to the songDataList
			String[] keyValuePair = new String[] {data.split(":")[0], data.split(":")[1]};
			songDataList.add(keyValuePair);
		}

		// Read all of the data into the new song
		for (String[] keyValue : songDataList) {
			String key = keyValue[0];
			String value = keyValue[1];
			// We do it this way so it's easier to modify the song class
			if (key.equals("name")) {
				newSong.name = value;
			} else if (key.equals("author")) {
				newSong.author = value;
			} else if (key.equals("favorite")) {
				newSong.favorite = Boolean.parseBoolean(value);
			} else if (key.equals("rating")) {
				newSong.rating = Integer.parseInt(value);
			} else if (key.equals("plays")) {
				newSong.plays = Integer.parseInt(value);
			}
		}


		return newSong;
	}

	public void setPlays(int newPlays) { this.plays = newPlays; }
}
