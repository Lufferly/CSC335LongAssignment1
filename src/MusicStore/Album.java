package MusicStore;

import java.util.ArrayList;

public class Album {
	private String name;
	private String author;
	private String genre;
	private int year;
	private ArrayList<Song> songs;
	
	public Album(String name, String author, String genre, int year,
			ArrayList<Song> songs) {
		this.name = name;
		this.author = author;
		this.genre = genre;
		this.year = year;
		this.songs = new ArrayList<Song> (songs);
	}
	
	public String getName() { return name; }	// Name getter
	
	public String getAuthor() { return author; }	// author getter
	
	public String getGenre() { return genre; }		// genre getter
	
	public int getYear() { return year; }		// year getter
	
	/* Getter method to return Array List with string representation of songs (immutable) */
	public ArrayList<String> getSongs() {
		ArrayList<String> newList = new ArrayList<String>();
		for (int i = 0; i < songs.size(); i++) {
			newList.add(songs.get(i).toString()); //Add String representation to new list
		}
		return newList;		// Return list with strings, instead of song objects
	}
	
}
