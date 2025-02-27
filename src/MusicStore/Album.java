package MusicStore;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Album {
	private String name;
	private String author;
	private String genre;
	private String year;
	private ArrayList<Song> songs;
	
	public Album(Album album) {
		this.name = album.getName();
		this.author = album.getAuthor();
		this.genre = album.getGenre();
		this.year = album.getYear();
		for (Song song: album.getSongObjects()){
			this.songs.add(song);
		}
	}

	// Overloaded constructor for creating an album based on a given File
	//	this will also create song objects based on the file
	public Album(String fileName) {
		// Read in the file
		File albumFile = new File(fileName);
		Scanner scanner;
		try {
			scanner = new Scanner(albumFile);
		} catch (Exception FileNotFoundException) {
			System.out.print("Error! Album Constructor was given a file path that does not exist!");
			System.out.println(fileName);
			System.exit(1); // Probably a better way to do this, better ask teach
			return; // So that the ide does not complain
		}
		
		// Fill in the fields as given by the file
		// First line contains all the information other than the songs, comma seperated
		String[] firstLine = scanner.nextLine().split(",");
		this.name = firstLine[0];
		this.author = firstLine[1];
		this.genre = firstLine[2];
		this.year = firstLine[3];

		// Fill in the songs from the rest of the file
		this.songs = new ArrayList<Song>();
		String thisLine;
		while (scanner.hasNext()) {  // Check to see if we hit the end of the file
			// Every line is a song
			thisLine = scanner.nextLine();
			songs.add(new Song(thisLine, this.author));
		}

		// We are done with the file
		scanner.close();
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
		return this.name + ", " + this.author;
	}
}
