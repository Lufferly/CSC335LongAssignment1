package MusicStore;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Album {
	private String name;
	private String author;
	private String genre;
	private String year;
	private ArrayList<Song> songs;

	// null constructor for setting up albums
	public Album() {
		name = null;
		author = null;
		genre = null;
		year = null;
		songs = new ArrayList<Song>();
	}

	// Copy constructor for an album, ignores songs
	public Album(Album album) {
		name = album.name;
		author = album.author;
		genre = album.genre;
		year = album.year;
		songs = new ArrayList<Song>();
	}

	// Overloaded constructor for creating an album based on a given File
	//	this will also create song objects based on the file
	public Album(String fileName) {
		// Read in the file
		BufferedReader albumFile;
        try {
            albumFile = new BufferedReader(new FileReader(fileName));
        } catch (Exception FileNotFoundException) {
            System.out.println("[!] ERROR! FILE NOT FOUND!");
            System.out.println(fileName);
            System.exit(1);
            return;
        }
		// Fill in the fields as given by the file
		// First line contains all the information other than the songs, comma seperated
		String[] firstLine;
		try {
            firstLine = albumFile.readLine().split(",");
        } catch (Exception IOException) {
            System.out.println("[!] ERROR! FILE NOT FOUND!");
            System.exit(1);
            return;
        }
		this.name = firstLine[0];
		this.author = firstLine[1];
		this.genre = firstLine[2];
		this.year = firstLine[3];

		// Fill in the songs from the rest of the file
		this.songs = new ArrayList<Song>();
		String thisLine;
		try {
            thisLine = albumFile.readLine();
        } catch (Exception IOException) {
            System.out.println("[!] No songs in file " + fileName);
            return;
        }
		while (thisLine != null) {  // Check to see if we hit the end of the file
			// Every line is a song
			songs.add(new Song(thisLine, this.author));
			try {
				thisLine = albumFile.readLine();
			} catch (Exception IOException)  {
				break;
			}
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getGenre() {
		return genre;
	}

	// Add a song to this album
	//	Will not add a song to the album if that song already exists
	public void addSong(Song song) {
		// Check for duplicates
		for (Song thisSong : songs) {
			if (thisSong.equals(song)) {
				return;
			}
		}

		// No duplicates found, add teh song
		songs.add(new Song(song));
	}

	// Check to see if a song is in this album
	//	returns true if there is the given song in the album, false otherwise
	public boolean hasSong(Song song) {
		for (Song thisSong : songs) {
			if (thisSong.equals(song)) {
				return true;
			}
		}

		// Song not found
		return false;
	}

	// Deletes a song from the album
	public void removeSong(Song song) {
		for (Song thisSong : songs) {
			if (thisSong.equals(song)) {
				songs.remove(thisSong);
				return;	// Albums dont allow duplicate songs
			}
		}
	}

	// Returns the number of songs in this album
	public int numSongs() {
		return songs.size();
	}
		
	// Getter method to return Array List with string representation of songs (immutable)
	public ArrayList<String> getSongs() {
		ArrayList<String> newList = new ArrayList<String>();
		for (int i = 0; i < songs.size(); i++) {
			newList.add(songs.get(i).toString()); //Add String representation to new list
		}
		return newList;		// Return list with strings, instead of song objects
	}

	// Getter method to return Array List with Song objects
	public ArrayList<Song> getSongObjects() {
		ArrayList<Song> newList = new ArrayList<Song>();
		for (int i = 0; i < songs.size(); i++) {
			newList.add(new Song(songs.get(i))); // Add copy of Song object to new list
		}
		return newList;		// Return list with strings, instead of song objects
	}

	public String getYear() {
		return year;
	}
	
	// Equals method for comparing two albums
	//	two albums are considered equal if they have the same name, author, genre, and year
	//	note that this equals method does not care about the songs inside of an album
	public boolean equals(Album otherAlbum) {
		return this.name.equals(otherAlbum.name) &&
			this.author.equals(otherAlbum.author) &&
			this.genre.equals(otherAlbum.genre) &&
			this.year.equals(otherAlbum.year);
	}

	// Prints out a representation of the object
	public void print() {
		System.out.println("Author:" + author + 
			"\nName: " + name + 
			"\nGenre: " + genre +
			"\nYear: " + year);
		for (Song song : songs) {
			song.print();
			System.out.print("|");
		}
		System.out.println();
	}

	public String toString() {
		return this.name + "," + this.author + "," + this.genre + "," + this.year;
	}

	// Returns a string that represents all the data we need to reconstruct an album
	//	if bool_includeSongs is true, include the songs, otherwise do not
	public String albumData(boolean bool_includeSongs) {
		String dataString = "";
		// All of the data in key value pairs
		dataString += "name:" + name + ";";
		dataString += "author:" + author + ";";
		dataString += "genre:" + genre + ";";
		dataString += "year:" + year + ";";
		// Add all the songs
		// We have the song tag, and then the data for all of the songs, but the first piece of 
		//	data for the songs is how many characters long the next song's data lasts for. So its like:
		//		songs:125;[a song data string that is 125 characters long];32;[and so on...];
		//	a length of zero lets us know we have no more songs
		dataString += "songs:";
		if (bool_includeSongs == true) {  // Only include the songs if this bool is true
			for (Song song : songs) {
				String thisSongData = song.songData();
				dataString += Integer.toString(thisSongData.length()) + ";"; // find the length
				dataString += thisSongData; // Song data puts a semicolon at the end for us
			}
		}
		dataString += "0;";  // No more songs in the album

		return dataString;
	}

	// Creates an album from the data from albumData()
	public static Album albumFromAlbumData(String albumData) {
		Album newAlbum = new Album();

		// Break the albumData into the data segments BEFORE all of the songs
		int totalDataSegments = 5;  // How many data segments the album has, this must be updated as more is added to the album class
		ArrayList<String[]> albumDataList = new ArrayList<String[]>();
		for (String data : albumData.split(";", totalDataSegments)) {
			// break the data into its key value pairs and add it to the list
			String[] keyValuePair = new String[] {data.split(":", 2)[0], data.split(":", 2)[1]};
			albumDataList.add(keyValuePair);
		}

		// Now read the keys and use the values to construct the new album
		for (String[] keyValue : albumDataList) {
			// Get the key and the value
			String key = keyValue[0];
			String value = keyValue[1];

			// The key tells us where the data should be put and how to process it
			if (key.equals("name")) {
				newAlbum.name = value;
			} else if (key.equals("author")) {
				newAlbum.author = value;
			} else if (key.equals("genre")) {
				newAlbum.genre = value;
			} else if (key.equals("year")) {
				newAlbum.year = value;
			} else if (key.equals("songs")) {
				// Construct the song array from the value
				newAlbum.songs = songArrayFromAlbumData(value);
			}
		}

		return newAlbum;
	}

	// Helper function for albumFromAlbumData; Constructs the list of songs from the song data string
	//	constructed in albumData()
	public static ArrayList<Song> songArrayFromAlbumData(String songDataString) {
		ArrayList<Song> songArray = new ArrayList<Song>();

		// The first data segment in every song segment tells us how many characters we need
		//	to skip to get to the next song data segment; ie:
		//		125;[a song data string that is 125 characters long];32;[and so on...];
		//	a length of zero lets us know we have no more songs
		String[] splitData = songDataString.split(";", 2);
		int nextSongOffset = Integer.parseInt(splitData[0]);
		String remainingData = splitData[1];
		while (nextSongOffset > 0) {  // a length of zero lets us know we have no more songs
			// Create and add the next song
			String thisSongData = remainingData.substring(0, nextSongOffset);
			Song thisSong = Song.songFromSongData(thisSongData);
			songArray.add(thisSong);
			// Find the next song to process
			splitData = remainingData.substring(nextSongOffset).split(";", 2);
			nextSongOffset = Integer.parseInt(splitData[0]);
			remainingData = splitData[1];
		}

		return songArray;
	}
}
