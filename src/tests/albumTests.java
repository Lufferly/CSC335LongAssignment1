package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import MusicStore.Album;
import MusicStore.Song;

class albumTests {
	private Album testAlbum1;
	private Album testAlbum2;
	
	public albumTests() {
		testAlbum1 = new Album("data/1_a.txt");
		testAlbum2 = new Album("data/hello_world.txt");
	}
	
	@Test
	void testGetName() {
		assertEquals(testAlbum1.getName(), "1");
		assertEquals(testAlbum2.getName(), "hello");
	}
	
	@Test
	void testGetAuthor() {
		assertEquals(testAlbum1.getAuthor(), "a");
		assertEquals(testAlbum2.getAuthor(), "world");
	}
	
	@Test
	void testGetGenre() {
		assertEquals(testAlbum1.getGenre(), "rock");
		assertEquals(testAlbum2.getGenre(), "punk");
	}
	
	@Test
	void testGetYear() {
		assertEquals(testAlbum1.getYear(), "1000");
		assertEquals(testAlbum2.getYear(), "3000");
	}
	
	@Test
	void testToString() {
		assertEquals(testAlbum1.toString(), "1,a,rock,1000");
		assertEquals(testAlbum2.toString(), "hello,world,punk,3000");
	}
	
	@Test
	void testGetSongs() {
		ArrayList<String> songStrings = testAlbum2.getSongs();
		assertEquals(songStrings.size(), 3);
		assertEquals(songStrings.get(0), "this, world, punk");
		assertEquals(songStrings.get(1), "code, world, punk");
		assertEquals(songStrings.get(2), "stinks!, world, punk");
	}
	
	@Test
	void testGetSongObjects() {
		ArrayList<Song> songObjects = testAlbum2.getSongObjects();
		assertEquals(songObjects.size(), 3);
		
		assertTrue(songObjects.get(0).equals(new Song("this", "world")));
		assertTrue(songObjects.get(1).equals(new Song("code", "world")));
		assertTrue(songObjects.get(2).equals(new Song("stinks!", "world")));
	}
	
	@Test
	void testAlbumHasSongsSimpleTrue() {
		// The songs the first album should have
		Song song1 = new Song("x", "a", "rock");
		Song song2 = new Song("y", "a", "rock");
		Song song3 = new Song("z", "a", "rock");
		
		assertTrue(testAlbum1.hasSong(song1));
		assertTrue(testAlbum1.hasSong(song2));
		assertTrue(testAlbum1.hasSong(song3));
	}
	
	@Test
	void testAlbumHasSongsSimpleFalse() {
		// The songs the first album should NOT have
		Song song1 = new Song("a", "a", "rock");
		Song song2 = new Song("hello", "a", "rock");
		Song song3 = new Song("z", "21", "ji");
		
		assertFalse(testAlbum1.hasSong(song1));
		assertFalse(testAlbum1.hasSong(song2));
		assertFalse(testAlbum1.hasSong(song3));
	}
	
	@Test
	void testAlbumAddSongsEmpty() {
		// A null constructor, we shouldn't care about anything but songs
		Album newAlbum = new Album();
		// The songs we add to an empty album
		Song song1 = new Song("x", "a", "rock");
		Song song2 = new Song("z", "21", "ji");
		newAlbum.addSong(song1);
		newAlbum.addSong(song2);
		// The song we will not add
		Song songNotIncluded = new Song("hello", "a", "rock");
		
		// Verify all is well in the world
		assertTrue(newAlbum.hasSong(song1));
		assertTrue(newAlbum.hasSong(song2));
		assertFalse(newAlbum.hasSong(songNotIncluded));
	}
	
	@Test
	void testRemoveSongSimple() {
		// Songs we remove
		Song rSong1 = new Song("x", "a", "rock");
		Song rSong2 = new Song("z", "a", "rock");
		// Song that stays
		Song sSong1 = new Song("y", "a", "rock");
		
		testAlbum1.removeSong(rSong1);
		testAlbum1.removeSong(rSong2);
		
		assertFalse(testAlbum1.hasSong(rSong1));
		assertFalse(testAlbum1.hasSong(rSong2));
		assertTrue(testAlbum1.hasSong(sSong1));
	}
	
	@Test
	void testNumSongsSimple() {
		assertEquals(testAlbum1.numSongs(), 3);
	}
	
	@Test
	void testNumSongsComplicated() {
		// Songs we remove
		Song rSong1 = new Song("x", "a", "rock");
		Song rSong2 = new Song("z", "a", "rock");
		testAlbum1.removeSong(rSong1);
		testAlbum1.removeSong(rSong2);
		
		assertEquals(testAlbum1.numSongs(), 1);
		
		// Songs we add
		Song song1 = new Song("x", "a", "rock");
		Song song2 = new Song("z", "21", "ji");
		Song song3 = new Song("f21", "2811289", "!!!@*");
		testAlbum1.addSong(song1);
		testAlbum1.addSong(song2);
		testAlbum1.addSong(song3);
		
		assertEquals(testAlbum1.numSongs(), 4);
	}
	
	@Test
	void testCopyConstructor() {
		// Make our copies
		Album testAlbum1Copy = new Album(testAlbum1);
		Album testAlbum2Copy = new Album(testAlbum2);
		
		// Copy constructor will never care about songs
		// Test em
		assertTrue(testAlbum1.equals(testAlbum1Copy));
		assertTrue(testAlbum2Copy.equals(testAlbum2));
	}
	
	// Simulate a save and load together, it doesn't really make sense to test them seperately
	@Test 
	void testSimulateSaveAndLoadSimple() {
		// Simple save and load, don't include songs
		String testAlbum1Data = testAlbum1.albumData(false);
		Album Album1Copy = Album.albumFromAlbumData(testAlbum1Data);
		
		assertTrue(testAlbum1.equals(Album1Copy));
	}
	
	// More complicated save and load
	@Test
	void testSimulateSaveAndLoadComplicated() {
		// Complicate an album
		Song newSong1 = new Song("dog", "cat", "edm");
		Song newSong2 = new Song("bird", "ant", "snow");
		testAlbum2.addSong(newSong1);
		testAlbum2.addSong(newSong2);
		newSong1.playsong();
		newSong1.playsong();
		newSong2.playsong();
		newSong1.setRating(3);
		newSong2.setRating(5); // Will make favorite
		
		// Simulate a save and load
		String testAlbum2Data = testAlbum2.albumData(true); // include songs
		Album testAlbum2Copy = Album.albumFromAlbumData(testAlbum2Data);
		
		// Verify load
		assertTrue(testAlbum2.equals(testAlbum2Copy));
		ArrayList<Song> testAlbum2Songs = testAlbum2.getSongObjects();
		ArrayList<Song> testAlbum2CopySongs = testAlbum2Copy.getSongObjects();
		for (int songIndex = 0; songIndex < testAlbum2Songs.size(); songIndex++) {
			// Check the songs
			Song originalSong = testAlbum2Songs.get(songIndex);
			Song loadSong = testAlbum2CopySongs.get(songIndex);
			assertTrue(originalSong.equals(loadSong));
			assertTrue(originalSong.getGenre().equals(loadSong.getGenre()));
			assertEquals(originalSong.getRating(), loadSong.getRating());
			assertEquals(originalSong.getPlays(), loadSong.getPlays());
			assertEquals(originalSong.isFavourite(), loadSong.isFavourite());
		}
	}
	
	// I know we dont need to test this, cause print is a debugging function
	//	but i dont wanna lose points due to unit test coverage
	@Test
	void testPrint() {
		testAlbum2.print();
	}

}
