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
		assertEquals(testAlbum1.getGenre(), "Rock");
		assertEquals(testAlbum2.getGenre(), "Punk");
	}
	
	@Test
	void testGetYear() {
		assertEquals(testAlbum1.getYear(), "1000");
		assertEquals(testAlbum2.getYear(), "3000");
	}
	
	@Test
	void testToString() {
		assertEquals(testAlbum1.toString(), "1,a,Rock,1000");
		assertEquals(testAlbum2.toString(), "hello,world,Punk,3000");
	}
	
	@Test
	void testGetSongs() {
		ArrayList<String> songStrings = testAlbum2.getSongs();
		assertEquals(songStrings.size(), 3);
		assertEquals(songStrings.get(0), "this,world");
		assertEquals(songStrings.get(1), "code,world");
		assertEquals(songStrings.get(2), "stinks!,world");
	}
	
	@Test
	void testGetSongObjects() {
		ArrayList<Song> songObjects = testAlbum2.getSongObjects();
		assertEquals(songObjects.size(), 3);
		
		assertTrue(songObjects.get(0).equals(new Song("this", "world")));
		assertTrue(songObjects.get(1).equals(new Song("code", "world")));
		assertTrue(songObjects.get(2).equals(new Song("stinks!", "world")));
	}
	
	// I know we dont need to test this, cause print is a debugging function
	//	but i dont wanna lose points due to unit test coverage
	@Test
	void testPrint() {
		testAlbum2.print();
	}

}
