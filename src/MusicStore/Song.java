package MusicStore;

public class Song {
	
	private String name;		// The name of the song
	private String author;		// The author of the song
	private boolean favorite; 	// Whether the song is favorite or not
	private String rating;		// Save rating as a String
	
	public Song(String songName, String authorName) {
		this.name = songName;
		this.author = authorName;
		favorite = false;
		rating = "";
	}

	public Song(Song song) {
		this.name = song.name;
		this.author = song.author;
		this.favorite = song.favorite;
		this.rating = song.rating;
	}
	
	public String getName() { return name; } // Returns name of the song
	
	public String getAuthor() { return author; }	// Returns name of the author
	
	public void makeFavorite() { this.favorite = true; }	// Makes a song favorite	
	
	public void unfavorite() { this.favorite = false; }		// Makes song not favorite
	
	public boolean getFavorite() { return favorite; }	// Returns if song is favorite
	
	public String getRating() { return rating; }	// Returns rating as a String
	
	// Sets new rating and makes song favorite automatically if rating is 5
	public void setRating (String newRating) {
		this.rating = newRating;
		if (rating.equals("5")) { 
			favorite = true;
		}
	}
	
	// Return a String representation of Song to refer to it
	@Override
	public String toString() {
		return name + ", " + author; 
	}

	// Prints out the name of the song
	public void print() {
		System.out.print(name);
	}
}
