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
	Song testSong3;
	Song testSong4;
	
	public playlistTests() {
		testPlaylist = new Playlist("test");
		testSong1 = new Song("1", "2");
		testSong2 = new Song("hello", "world");
		testSong3 = new Song("abc", "xyz", "rock");
		testSong4 = new Song("XYZ", "abc", "punk");
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
	
	@Test
	void testMaxSizeSimple() {
		testPlaylist.addSongs(testSong1);
		testPlaylist.addSongs(testSong2);
		
		testPlaylist.maxLength(999);
		assertEquals(testPlaylist.size(), 2);
		testPlaylist.maxLength(0);
		assertEquals(testPlaylist.size(), 0);
	}
	
	@Test
	void testMaxSizeComplicated() {
		testPlaylist.addSongs(testSong1);
		testPlaylist.addSongs(testSong2);
		
		testPlaylist.maxLength(1);
		assertEquals(testPlaylist.size(), 1);
		
		// Playlists can have copies of songs
		testPlaylist.addSongs(testSong2);
		testPlaylist.addSongs(testSong2);
		testPlaylist.addSongs(testSong2);
		
		testPlaylist.maxLength(2);
		assertEquals(testPlaylist.size(), 2);
	}
	
	@Test
	void testSizeZero() {
		assertEquals(testPlaylist.size(), 0);
	}
	
	@Test
	void testSizeSimple() {
		testPlaylist.addSongs(testSong1);
		testPlaylist.addSongs(testSong2);
		assertEquals(testPlaylist.size(), 2);
	}
	
	@Test
	void testSizeHarder() {
		testPlaylist.addSongs(testSong1);
		testPlaylist.addSongs(testSong2);
		
		testPlaylist.removeSong(testSong1.getName(), testSong1.getAuthor());
		assertEquals(testPlaylist.size(), 1);
		testPlaylist.removeSong(testSong2.getName(), testSong2.getAuthor());
		assertEquals(testPlaylist.size(), 0);
	}
	
	@Test
	void testGetSongObjectsZero() {
		assertEquals(testPlaylist.getSongObjects().size(), 0);
	}
	
	@Test
	void testGetSongObjectsSimple() {
		testPlaylist.addSongs(testSong1);
		testPlaylist.addSongs(testSong2);
		
		ArrayList<Song> songObjects = testPlaylist.getSongObjects();
		assertFalse(songObjects.size() <= 0);
		assertTrue(songObjects.get(0).equals(testSong1));
		assertTrue(songObjects.get(1).equals(testSong2));
	}
	
	@Test
	void testGetSongObjectsRemoveSong() {
		testPlaylist.addSongs(testSong1);
		testPlaylist.addSongs(testSong2);
		
		testPlaylist.removeSong(testSong1.getName(), testSong1.getAuthor());
		
		ArrayList<Song> songObjects = testPlaylist.getSongObjects();
		assertFalse(songObjects.size() <= 0);
		assertTrue(songObjects.get(0).equals(testSong2));
	}
	
	@Test
	void testSortByName() {
		testPlaylist.addSongs(testSong1); //1, 2
		testPlaylist.addSongs(testSong2); //hello, world
		testPlaylist.addSongs(testSong3); //abc, xyz, rock
		testPlaylist.addSongs(testSong4); //XYZ, abc, punk
		
		testPlaylist.sortPlaylistByName();
		
		// This gets the songs in their order
		ArrayList<Song> songObjects = testPlaylist.getSongObjects();
		assertEquals(songObjects.size(), 4);
		assertTrue(songObjects.get(0).equals(testSong1));
		assertTrue(songObjects.get(1).equals(testSong3));
		assertTrue(songObjects.get(2).equals(testSong2));
		assertTrue(songObjects.get(3).equals(testSong4));
		
	}
	
	@Test
	void testSortByAuthor() {
		testPlaylist.addSongs(testSong1); //1, 2
		testPlaylist.addSongs(testSong2); //hello, world
		testPlaylist.addSongs(testSong3); //abc, xyz, rock
		testPlaylist.addSongs(testSong4); //XYZ, abc, punk
		
		testPlaylist.sortPlaylistByAuthor();
		
		// This gets the songs in their order
		ArrayList<Song> songObjects = testPlaylist.getSongObjects();
		assertEquals(songObjects.size(), 4);
		assertTrue(songObjects.get(0).equals(testSong1));
		assertTrue(songObjects.get(1).equals(testSong4));
		assertTrue(songObjects.get(2).equals(testSong2));
		assertTrue(songObjects.get(3).equals(testSong3));
		
	}
	
	@Test
	void testSortByRating() {
		testSong1.setRating(3);
		testSong2.setRating(1);
		testSong3.setRating(5);
		testSong4.setRating(2);
		testPlaylist.addSongs(testSong1); //1, 2
		testPlaylist.addSongs(testSong2); //hello, world
		testPlaylist.addSongs(testSong3); //abc, xyz, rock
		testPlaylist.addSongs(testSong4); //XYZ, abc, punk
		
		testPlaylist.sortPlaylistByRating();
		
		// This gets the songs in their order
		ArrayList<Song> songObjects = testPlaylist.getSongObjects();
		assertEquals(songObjects.size(), 4);
		assertTrue(songObjects.get(0).equals(testSong3));
		assertTrue(songObjects.get(1).equals(testSong1));
		assertTrue(songObjects.get(2).equals(testSong4));
		assertTrue(songObjects.get(3).equals(testSong2));
		
	}
	
	// Test a simple save and load
	@Test
	void testSaveAndLoadSimple() {
		// Save and load
		String testPlaylistData = testPlaylist.playlistData();
		Playlist testPlaylistCopy = Playlist.playlistFromPlaylistData(testPlaylistData);
		
		// Validate the load
		assertEquals(testPlaylist.getName(), testPlaylistCopy.getName());
	}
	
	// Test a more complicated save and load
	@Test
	void testSaveAndLoadComplicated() {
		// Complicate
		testPlaylist.addSongs(testSong1);
		testPlaylist.addSongs(testSong2);
		
		// Save and load
		String testPlaylistData = testPlaylist.playlistData();
		Playlist testPlaylistCopy = Playlist.playlistFromPlaylistData(testPlaylistData);
		
		// Validate the load
		assertEquals(testPlaylist.getName(), testPlaylistCopy.getName());
		ArrayList<String> testPlaylistSongs = testPlaylist.getSongs();
		ArrayList<String> testPlaylistCopySongs = testPlaylist.getSongs();
		for (int songIndex = 0; songIndex < testPlaylistSongs.size(); songIndex++) {
			String song1 = testPlaylistSongs.get(songIndex);
			String song2 = testPlaylistCopySongs.get(songIndex);
			assertEquals(song1, song2);
		}
	}
	
	// This is not technically needed, but I dont want to lose points and also i likee the color green
	@Test
	void testPrint() {
		testPlaylist.printPlaylist();
		
		testPlaylist.addSongs(testSong1);
		testPlaylist.addSongs(testSong2);
		
		testPlaylist.printPlaylist();
	}

}
