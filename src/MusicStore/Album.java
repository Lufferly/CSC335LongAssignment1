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
		return this.name + "," + this.author + "," + this.genre + "," + this.year;
	}
}
