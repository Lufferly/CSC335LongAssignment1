package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import MusicStore.MusicStore;

class musicStoreTests {
	private MusicStore testMusicStore;
	
	public musicStoreTests() {
		testMusicStore = new MusicStore("data/testAlbums.txt");
	}
	
	@Test
	void testGetAllAlbums() {
		ArrayList<String> allAlbums = testMusicStore.getAllAlbums();
		assertEquals(allAlbums.size(), 2);
		
		assertEquals(allAlbums.get(0), "1,a,Rock,1000");
		assertEquals(allAlbums.get(1), "hello,world,Punk,3000");
	}
	
	@Test
	void testGetAllSongs() {
		ArrayList<String> allSongs = testMusicStore.getAllSongs();
		assertEquals(allSongs.size(), 6);
		
		assertEquals(allSongs.get(0), "x,a");
		assertEquals(allSongs.get(1), "y,a");
		assertEquals(allSongs.get(2), "z,a");
		assertEquals(allSongs.get(3), "this,world");
		assertEquals(allSongs.get(4), "code,world");
		assertEquals(allSongs.get(5), "stinks!,world");
	}
	
	@Test
	void testSearchForAlbumByName() {
		ArrayList<String> test1Oracle = new ArrayList<String>();
		test1Oracle.add("hello,world,Punk,3000\n    this,world\n    code,world\n    stinks!,world\n");
		assertEquals(testMusicStore.searchForAlbumByName("hello"), test1Oracle);
		assertEquals(testMusicStore.searchForAlbumByName("h"), test1Oracle);
		assertEquals(testMusicStore.searchForAlbumByName("lo"), test1Oracle);
	}
	
	@Test
	void testSearchForAlbumByNameEmpty() {
		assertEquals(testMusicStore.searchForAlbumByName("oiefqj"), new ArrayList<String>());
	}
	
	@Test
	void testSearchForSongByName() {
		ArrayList<String> test1Oracle = new ArrayList<String>();
		test1Oracle.add("code,world,hello");
		assertEquals(testMusicStore.searchForSongsByName("code"), test1Oracle);
		assertEquals(testMusicStore.searchForSongsByName("od"), test1Oracle);
		assertEquals(testMusicStore.searchForSongsByName("c"), test1Oracle);
		
		assertEquals(testMusicStore.searchForSongsByName("fjeqo"), new ArrayList<String>());
	}
	
	@Test 
	void testSearchForAlbumByAuthor() {
		ArrayList<String> test1Oracle = new ArrayList<String>();
		test1Oracle.add("hello,world,Punk,3000\n    this,world\n    code,world\n    stinks!,world\n");
		assertEquals(testMusicStore.searchForAlbumByAuthor("world"), test1Oracle);
		assertEquals(testMusicStore.searchForAlbumByAuthor("w"), test1Oracle);
		assertEquals(testMusicStore.searchForAlbumByAuthor("rl"), test1Oracle);
	
		assertEquals(testMusicStore.searchForAlbumByAuthor("oiefqj"), new ArrayList<String>());
	}
	
	@Test
	void testSearchForSongByAuthor() {
		ArrayList<String> test1Oracle = new ArrayList<String>();
		test1Oracle.add("this,world,hello");
		test1Oracle.add("code,world,hello");
		test1Oracle.add("stinks!,world,hello");
		assertEquals(testMusicStore.searchForSongsByAuthor("world"), test1Oracle);
		assertEquals(testMusicStore.searchForSongsByAuthor("o"), test1Oracle);
		assertEquals(testMusicStore.searchForSongsByAuthor("ld"), test1Oracle);
		
		assertEquals(testMusicStore.searchForSongsByAuthor("fjeqo"), new ArrayList<String>());
	}
}
