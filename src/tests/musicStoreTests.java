package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import MusicStore.Album;
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
		
		assertEquals(allAlbums.get(0), "1,a,rock,1000");
		assertEquals(allAlbums.get(1), "hello,world,punk,3000");
	}
	
	@Test
	void testGetAllSongs() {
		ArrayList<String> allSongs = testMusicStore.getAllSongs();
		assertEquals(allSongs.size(), 6);
		
		assertEquals(allSongs.get(0), "x, a, rock");
		assertEquals(allSongs.get(1), "y, a, rock");
		assertEquals(allSongs.get(2), "z, a, rock");
		assertEquals(allSongs.get(3), "this, world, punk");
		assertEquals(allSongs.get(4), "code, world, punk");
		assertEquals(allSongs.get(5), "stinks!, world, punk");
	}
	
	@Test
	void testSearchForAlbumByName() {
		ArrayList<String> test1Oracle = new ArrayList<String>();
		test1Oracle.add("hello,world,punk,3000\n    this, world, punk\n    code, world, punk\n    stinks!, world, punk\n");
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
		test1Oracle.add("code, world, punk");
		assertEquals(testMusicStore.searchForSongsByName("code", false), test1Oracle);
		assertEquals(testMusicStore.searchForSongsByName("od", false), test1Oracle);
		assertEquals(testMusicStore.searchForSongsByName("c", false), test1Oracle);
		
		assertEquals(testMusicStore.searchForSongsByName("fjeqo", false), new ArrayList<String>());
	}
	
	@Test
	void testSearchForSongByNameExtraAlbumInfo() {
		ArrayList<String> test1Oracle = new ArrayList<String>();
		test1Oracle.add("code, world, punk, Album:hello,world,punk,3000");
		assertEquals(testMusicStore.searchForSongsByName("code", true), test1Oracle);
		assertEquals(testMusicStore.searchForSongsByName("od", true), test1Oracle);
		assertEquals(testMusicStore.searchForSongsByName("c", true), test1Oracle);
		
		assertEquals(testMusicStore.searchForSongsByName("fjeqo", false), new ArrayList<String>());
	}
	
	@Test 
	void testSearchForAlbumByAuthor() {
		ArrayList<String> test1Oracle = new ArrayList<String>();
		test1Oracle.add("hello,world,punk,3000\n    this, world, punk\n    code, world, punk\n    stinks!, world, punk\n");
		assertEquals(testMusicStore.searchForAlbumByAuthor("world"), test1Oracle);
		assertEquals(testMusicStore.searchForAlbumByAuthor("w"), test1Oracle);
		assertEquals(testMusicStore.searchForAlbumByAuthor("rl"), test1Oracle);
	
		assertEquals(testMusicStore.searchForAlbumByAuthor("oiefqj"), new ArrayList<String>());
	}
	
	@Test
	void testSearchForSongByAuthor() {
		ArrayList<String> test1Oracle = new ArrayList<String>();
		test1Oracle.add("this, world, punk");
		test1Oracle.add("code, world, punk");
		test1Oracle.add("stinks!, world, punk");
		assertEquals(testMusicStore.searchForSongsByAuthor("world", false), test1Oracle);
		assertEquals(testMusicStore.searchForSongsByAuthor("o", false), test1Oracle);
		assertEquals(testMusicStore.searchForSongsByAuthor("ld", false), test1Oracle);
		
		assertEquals(testMusicStore.searchForSongsByAuthor("fjeqo", false), new ArrayList<String>());
	}
	
	@Test
	void testSearchForAlbumByAuthorExtraAlbumInfo() {
		ArrayList<String> test1Oracle = new ArrayList<String>();
		test1Oracle.add("this, world, punk, Album:hello,world,punk,3000");
		test1Oracle.add("code, world, punk, Album:hello,world,punk,3000");
		test1Oracle.add("stinks!, world, punk, Album:hello,world,punk,3000");
		assertEquals(testMusicStore.searchForSongsByAuthor("world", true), test1Oracle);
		assertEquals(testMusicStore.searchForSongsByAuthor("o", true), test1Oracle);
		assertEquals(testMusicStore.searchForSongsByAuthor("ld", true), test1Oracle);
		
		assertEquals(testMusicStore.searchForSongsByAuthor("fjeqo", false), new ArrayList<String>());
	}
	
	@Test
	void testGetSongAlbumDataOne() {
		String albumData = testMusicStore.getSongAlbumData("x", "a");
		Album loadedAlbum = Album.albumFromAlbumData(albumData);
		Album correctAlbum = new Album("data/1_a.txt");
		assertTrue(correctAlbum.equals(loadedAlbum));
	}
	
	@Test
	void testGetSongAlbumDataTwo() {
		String albumData = testMusicStore.getSongAlbumData("code", "world");
		Album loadedAlbum = Album.albumFromAlbumData(albumData);
		Album correctAlbum = new Album("data/hello_world.txt");
		assertTrue(correctAlbum.equals(loadedAlbum));
	}
	
	@Test
	void testGetSongAlbumDataNotFound() {
		String nullAlbumData = testMusicStore.getSongAlbumData("not", "here");
		assertEquals(nullAlbumData, null);
	}
}
