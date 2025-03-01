package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import MusicStore.Album;
import MusicStore.Song;
import userLibrary.LibraryModel;

class libraryTests {

	private LibraryModel testLibrary;
	// Album added to the library automatically
	private Album libraryAlbum;
	// Album not yet added to the library for testing
	private Album testAlbum;
	// Song not yet added to the library for testing
	private Song testSong;
	
	public libraryTests() {
		testLibrary = new LibraryModel("sev");
		
		libraryAlbum = new Album("data/1_a.txt");
		testAlbum = new Album("data/hello_world.txt");
		testSong = new Song("Severian", "Adams");
		
		// Automatically add libraryAlbum to the library
		testLibrary.buyAlbum(libraryAlbum.getName(), libraryAlbum.getAuthor());
		
	}
	
	@Test
	void testGetUsername() {
		assertEquals(testLibrary.getUsername(), "sev");
	}
	
	@Test 
	void testSetUsername() {
		testLibrary.setUsername("Severian");
		assertEquals(testLibrary.getUsername(), "Severian");
	}
	
	@Test
	void testGetAllAlbums() {
		ArrayList<String> oracle = new ArrayList<String>();
		oracle.add(libraryAlbum.toString());
		
		assertEquals(testLibrary.getAllAlbums(), oracle);
		
		testLibrary.buyAlbum(testAlbum.getName(), testAlbum.getAuthor());
		oracle.add(testAlbum.toString());
		assertEquals(testLibrary.getAllAlbums(), oracle);
	}
	
	@Test
	void testGetAllSongs() {
		ArrayList<String> oracle = new ArrayList<String>();
		for (String song : libraryAlbum.getSongs()) {
			oracle.add(song);
		}
		
		assertEquals(testLibrary.getAllSongs(), oracle);
	}
	
	@Test
	void testGetAllSongsAfterAdd() {
		testLibrary.buyAlbum(testAlbum.getName(), testAlbum.getAuthor());
		testLibrary.buySong(testSong.getName(), testSong.getAuthor());
		
		ArrayList<String> oracle = new ArrayList<String>();
		for (String song : libraryAlbum.getSongs()) {
			oracle.add(song);
		}
		for (String song : testAlbum.getSongs()) {
			oracle.add(song);
		}
		oracle.add(testSong.toString());
		
		assertEquals(testLibrary.getAllSongs(), oracle);
	}
	
	@Test
	void testGetAllArtists() {
		ArrayList<String> oracle = new ArrayList<String>();
		oracle.add(libraryAlbum.getAuthor());
		
		assertEquals(testLibrary.getAllArtists(), oracle);
	}
	
	@Test
	void testGetAllArtistsAfterAdd() {
		testLibrary.buyAlbum(testAlbum.getName(), testAlbum.getAuthor());
		testLibrary.buySong(testSong.getName(), testSong.getAuthor());
		
		ArrayList<String> oracle = new ArrayList<String>();
		oracle.add(libraryAlbum.getAuthor());
		oracle.add(testAlbum.getAuthor());
		oracle.add(testSong.getAuthor());
		
		assertEquals(testLibrary.getAllArtists(), oracle);
	}
	
	@Test
	void testGetAllPlaylists() {
		ArrayList<String> oracle = new ArrayList<String>();
		assertEquals(testLibrary.getAllPlaylists(), oracle);
		
		testLibrary.createPlaylist("hello");
		testLibrary.createPlaylist("world");
		oracle.add("hello");
		oracle.add("world");
		
		assertEquals(testLibrary.getAllPlaylists(), oracle);
	}
	
	@Test
	void testCreatePlaylists() {
		ArrayList<String> oracle = new ArrayList<String>();
		
		testLibrary.createPlaylist("hello");
		testLibrary.createPlaylist("hello");
		testLibrary.createPlaylist("world");
		testLibrary.createPlaylist("hello");
		testLibrary.createPlaylist("world");
		oracle.add("hello");
		oracle.add("world");
		
		assertEquals(testLibrary.getAllPlaylists(), oracle);
	}
	
	@Test
	void testRemovePlaylist() {
		ArrayList<String> oracle = new ArrayList<String>();
		
		testLibrary.createPlaylist("hello");
		testLibrary.createPlaylist("world");
		testLibrary.removePlaylist("hello");
		oracle.add("world");
		
		assertEquals(testLibrary.getAllPlaylists(), oracle);
		
		testLibrary.removePlaylist("world");
		oracle.remove(0);
		assertEquals(testLibrary.getAllPlaylists(), oracle);
	}
	
	@Test
	void testAddToPlaylist() {
		ArrayList<String> oracle = new ArrayList<String>();
		
		testLibrary.createPlaylist("hello");
		testLibrary.createPlaylist("world");

		
		testLibrary.addSongToPlaylist("x", "a", "hello");
		testLibrary.addSongToPlaylist("y", "a", "hello");
		testLibrary.addSongToPlaylist("z", "a", "hello");
		
		oracle.add("x,a");
		oracle.add("y,a");
		oracle.add("z,a");
		
		assertEquals(testLibrary.getPlaylistSongs("hello"), oracle);
		
		testLibrary.addSongToPlaylist("x", "a", "hello");
		testLibrary.addSongToPlaylist("y", "a", "hello");
		testLibrary.addSongToPlaylist("z", "a", "hel");
		
		oracle.add("x,a");
		oracle.add("y,a");
		
		assertEquals(testLibrary.getPlaylistSongs("hello"), oracle);
	}
	
	
	@Test
	void testRemoveFromPlaylist() {
		testLibrary.createPlaylist("hello");
		testLibrary.createPlaylist("world");
		
		ArrayList<String> oracle = new ArrayList<String>();
		testLibrary.addSongToPlaylist("x", "a", "hello");
		testLibrary.addSongToPlaylist("y", "a", "hello");
		testLibrary.addSongToPlaylist("z", "a", "hello");
		oracle.add("x,a");
		oracle.add("y,a");
		oracle.add("z,a");
		assertEquals(testLibrary.getPlaylistSongs("hello"), oracle);
		
		testLibrary.removeSongFromPlaylist("x", "a", "hello");
		testLibrary.removeSongFromPlaylist("y", "a", "hel");
		oracle.remove(0);
		assertEquals(testLibrary.getPlaylistSongs("hello"), oracle);
		
		testLibrary.removeSongFromPlaylist("z", "a", "hello");
		testLibrary.removeSongFromPlaylist("y", "a", "hello");
		oracle.remove(0);
		oracle.remove(0);
		assertEquals(testLibrary.getPlaylistSongs("hello"), oracle);
	}
	
	@Test
	void testAddFavorites() {
		ArrayList<String> oracle = new ArrayList<String>();
		assertEquals(testLibrary.getFavourites(), oracle);
		
		testLibrary.favouriteSong("y", "a");
		testLibrary.favouriteSong("m", "a");
		testLibrary.favouriteSong("x", "b");
		oracle.add("y,a");
		
		assertEquals(testLibrary.getFavourites(), oracle);
	}
	
	@Test
	void testAddFavoritesAfterBuy() {
		ArrayList<String> oracle = new ArrayList<String>();
		testLibrary.favouriteSong("y", "a");
		oracle.add("y,a");
		
		testLibrary.buyAlbum(testAlbum.getName(), testAlbum.getAuthor());
		testLibrary.favouriteSong("code", "world");
		oracle.add("code,world");
		
		assertEquals(testLibrary.getFavourites(), oracle);
		
		testLibrary.buySong(testSong.getName(), testSong.getAuthor());
		testLibrary.favouriteSong(testSong.getName(), testSong.getAuthor());
		oracle.add(testSong.getName() + "," + testSong.getAuthor());
		
		assertEquals(testLibrary.getFavourites(), oracle);
	}
	
	@Test
	void testRateSongMakesFavorite() {
		ArrayList<String> oracle = new ArrayList<String>();
		assertEquals(testLibrary.getFavourites(), oracle);
		
		testLibrary.rateSong("z", "a", 1);
		testLibrary.rateSong("y", "a", 5);
		testLibrary.rateSong("x", "a", 3);
		oracle.add("y,a,*****");
		
		assertEquals(testLibrary.getFavourites(), oracle);
		
		testLibrary.rateSong("x", "a", 4);
		testLibrary.rateSong("y", "a", 3);
		testLibrary.rateSong("z", "a", 5);
		oracle.clear();
		oracle.add("y,a,***");
		oracle.add("z,a,*****");
		
		assertEquals(testLibrary.getFavourites(), oracle);
	}
	
}
