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
	
	public String getName() {
		return name;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getGenre() {
		return genre;
	}
	
	public int getYear() {
		return year;
	}
	
	
}
