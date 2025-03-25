package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import MusicStore.Song;

class songTests {
	private Song testSong1;
	private Song testSong2;
	
	public songTests() {
		testSong1 = new Song("1","2");
		testSong2 = new Song("hello", "world", "edm");
	}
	
	@Test
	void testGetName() {
		assertEquals(testSong1.getName(), "1");
		assertEquals(testSong2.getName(), "hello");
	}
	
	@Test
	void testGetAuthor() {
		assertEquals(testSong1.getAuthor(), "2");
		assertEquals(testSong2.getAuthor(), "world");
	}
	
	@Test
	void testCopyCreator() {
		Song copySong = new Song(testSong2);
		
		assertEquals(copySong.getName(), "hello");
		assertEquals(copySong.getAuthor(), "world");
	}
	
	@Test
	void testMakeFavorite() {
		assertFalse(testSong1.isFavourite());
		testSong1.makeFavorite();
		assertTrue(testSong1.isFavourite());
	}
	
	@Test
	void testUnfavorite() {
		testSong2.makeFavorite();
		testSong2.unfavorite();
		assertFalse(testSong2.isFavourite());
	}
	
	// Test if equals works for true cases
	@Test
	void testEqualsTrue() {
		Song testSong3 = new Song(testSong2);
		assertTrue(testSong2.equals(testSong3));
		assertTrue(testSong3.equals(testSong2));
	}
	
	// Test if equals works for false cases
	@Test
	void testEqualsFalse() {
		assertFalse(testSong2.equals(testSong1));
		assertFalse(testSong1.equals(testSong2));
		
		Song testSong3 = new Song("hello", "2");
		assertFalse(testSong2.equals(testSong3));
		assertFalse(testSong1.equals(testSong3));
	}
	
	// Test setting rating
	@Test
	void testSetRating() {
		assertEquals(testSong2.getRating(), 0);
		testSong2.setRating(5);
		assertEquals(testSong2.getRating(), 5);
	}
	
	// Test setting rating multiple times
	@Test
	void testRerating() {
		testSong2.setRating(1);
		assertEquals(testSong2.getRating(), 1);
		testSong2.setRating(2);
		assertEquals(testSong2.getRating(), 2);
		testSong2.setRating(3);
		assertEquals(testSong2.getRating(), 3);
		testSong2.setRating(4);
		assertEquals(testSong2.getRating(), 4);
		testSong2.setRating(5);
		assertEquals(testSong2.getRating(), 5);
		testSong2.setRating(6);
		assertEquals(testSong2.getRating(), 6);
		testSong2.setRating(0);
		assertEquals(testSong2.getRating(), 0);
	}
	
	// Test the toString
	@Test
	void testToString() {
		assertEquals(testSong1.toString(), "1, 2, null");
		assertEquals(testSong2.toString(), "hello, world, edm");
	}
	
	// Test the toString after rating
	@Test
	void testToStringRating() {
		testSong2.setRating(5);
		assertEquals(testSong2.toString(), "hello, world, *****, edm");
		testSong2.setRating(3);
		assertEquals(testSong2.toString(), "hello, world, ***, edm");
		testSong2.setRating(0);
		assertEquals(testSong2.toString(), "hello, world, edm");
	}
	
	
	// Test the functions related to saving
	//	this tests songData() and songFromSongData(), since neither of them
	//	make sense to test alone
	@Test 
	void testSimulateSongSaveBase() {
		String testSong1Data = testSong1.songData();
		
		// Check that the recreated song is the same as the original song
		Song copySong1 = Song.songFromSongData(testSong1Data);
		
		assertTrue(testSong1.equals(copySong1));
		assertTrue(copySong1.getGenre().equals("null"));
		assertEquals(testSong1.getPlays(), copySong1.getPlays());
		assertEquals(testSong1.getRating(), copySong1.getRating());
		assertEquals(testSong1.isFavourite(), copySong1.isFavourite());
	}
	
	@Test
	void testSimulateSongSaveComplicated() {
		// Modify the song
		testSong2.makeFavorite();
		testSong2.playsong();
		testSong2.setRating(3);
		
		// Simulate a save and load
		String testSong2Data = testSong2.songData();
		Song copySong2 = Song.songFromSongData(testSong2Data);
		
		// Check the songs are equal
		assertTrue(testSong2.equals(copySong2));
		assertTrue(testSong2.getGenre().equals(copySong2.getGenre()));
		assertEquals(testSong2.getPlays(), copySong2.getPlays());
		assertEquals(testSong2.getRating(), copySong2.getRating());
		assertEquals(testSong2.isFavourite(), copySong2.isFavourite());
	}
}
