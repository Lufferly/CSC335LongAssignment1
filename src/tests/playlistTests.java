package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import MusicStore.Song;
import userLibrary.Playlist;

class playlistTests {
	
	Playlist testPlaylist;
	Song testSong1;
	Song testSong2;
	
	public playlistTests() {
		testPlaylist = new Playlist("test");
		testSong1 = new Song("1", "2");
		testSong2 = new Song("hello", "world");
	}
	
	@Test
	void testGetName() {
		assertEquals(testPlaylist.getName(), "test");
	}
	
	@Test
	void testAddSongs() {
		ArrayList<String> oracle = new ArrayList<String>();
		assertEquals(testPlaylist.getSongs(), oracle);
		
		testPlaylist.addSongs(testSong1);
		oracle.add(testSong1.toString());
		assertEquals(testPlaylist.getSongs(), oracle);
		
		testPlaylist.addSongs(testSong2);
		oracle.add(testSong2.toString());
		assertEquals(testPlaylist.getSongs(), oracle);
	}
	
	@Test
	void testRemoveSongs() {
		ArrayList<String> oracle = new ArrayList<String>();
		
		testPlaylist.addSongs(testSong1);
		testPlaylist.addSongs(testSong2);
		testPlaylist.removeSong(testSong2.getName(), testSong2.getAuthor());
		oracle.add(testSong1.toString());
		assertEquals(testPlaylist.getSongs(), oracle);
		
		testPlaylist.addSongs(testSong2);
		testPlaylist.removeSong(testSong1.getName(), testSong1.getAuthor());
		testPlaylist.removeSong(testSong2.getName(), testSong2.getAuthor());
		oracle.clear();
		assertEquals(testPlaylist.getSongs(), oracle);
		
	}
	
	// This is not technically needed, but I dont want to lose points and also i likee the color green
	@Test
	void testPrint() {
		testPlaylist.addSongs(testSong1);
		testPlaylist.addSongs(testSong2);
		
		testPlaylist.printPlaylist();
	}

}
