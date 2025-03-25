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
	// Playlists that are included in every single library
	private ArrayList<String> defaultPlaylists = new ArrayList<String>();
	
	public libraryTests() {
		testLibrary = new LibraryModel("sev");
		
		libraryAlbum = new Album("data/1_a.txt");
		testAlbum = new Album("data/hello_world.txt");
		testSong = new Song("Severian", "Adams");
		
		// Automatically add libraryAlbum to the library
		testLibrary.buyAlbum(libraryAlbum.getName(), libraryAlbum.getAuthor());
		
		// The default playlists in every library
		defaultPlaylists.add("mostplayed");
		defaultPlaylists.add("recentlyplayed");
		defaultPlaylists.add("favourites");
		defaultPlaylists.add("toprated");
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
		testLibrary.buySong(testSong.getName(), testSong.getAuthor(), testSong.getGenre());
		
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
		testLibrary.buySong(testSong.getName(), testSong.getAuthor(), testSong.getGenre());
		
		ArrayList<String> oracle = new ArrayList<String>();
		oracle.add(libraryAlbum.getAuthor());
		oracle.add(testAlbum.getAuthor());
		oracle.add(testSong.getAuthor());
		
		assertEquals(testLibrary.getAllArtists(), oracle);
	}
	
	@Test
	void testGetAllPlaylists() {
		ArrayList<String> oracle = new ArrayList<String>();
		
		for (String playlist : defaultPlaylists) {
			oracle.add(playlist);
		}
		
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
		
		for (String playlist : defaultPlaylists) {
			oracle.add(playlist);
		}
		
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
		for (String playlist : defaultPlaylists) {
			oracle.add(playlist);
		}
		
		testLibrary.createPlaylist("hello");
		testLibrary.createPlaylist("world");
		testLibrary.removePlaylist("hello");
		oracle.add("world");
		
		assertEquals(testLibrary.getAllPlaylists(), oracle);
		
		testLibrary.removePlaylist("world");
		oracle.remove(defaultPlaylists.size());
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
		
		oracle.add("x, a, rock");
		oracle.add("y, a, rock");
		oracle.add("z, a, rock");
		
		assertEquals(testLibrary.getPlaylistSongs("hello"), oracle);
		
		testLibrary.addSongToPlaylist("x", "a", "hello");
		testLibrary.addSongToPlaylist("y", "a", "hello");
		testLibrary.addSongToPlaylist("z", "a", "hel");
		
		oracle.add("x, a, rock");
		oracle.add("y, a, rock");
		
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
		oracle.add("x, a, rock");
		oracle.add("y, a, rock");
		oracle.add("z, a, rock");
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
		oracle.add("y, a, rock");
		
		assertEquals(testLibrary.getFavourites(), oracle);
	}
	
	@Test
	void testAddFavoritesAfterBuy() {
		ArrayList<String> oracle = new ArrayList<String>();
		testLibrary.favouriteSong("y", "a");
		oracle.add("y, a, rock");
		
		testLibrary.buyAlbum(testAlbum.getName(), testAlbum.getAuthor());
		testLibrary.favouriteSong("code", "world");
		oracle.add("code, world, punk");
		
		assertEquals(testLibrary.getFavourites(), oracle);
		
		testLibrary.buySong(testSong.getName(), testSong.getAuthor(), testSong.getGenre());
		testLibrary.favouriteSong(testSong.getName(), testSong.getAuthor());
		oracle.add(testSong.getName() + ", " + testSong.getAuthor() + ", " + testSong.getGenre());
		
		assertEquals(testLibrary.getFavourites(), oracle);
	}
	
	@Test
	void testRateSongMakesFavorite() {
		ArrayList<String> oracle = new ArrayList<String>();
		assertEquals(testLibrary.getFavourites(), oracle);
		
		testLibrary.rateSong("z", "a", 1);
		testLibrary.rateSong("y", "a", 5);
		testLibrary.rateSong("x", "a", 3);
		oracle.add("y, a, *****, rock");
		
		assertEquals(testLibrary.getFavourites(), oracle);
		
		testLibrary.rateSong("x", "a", 4);
		testLibrary.rateSong("y", "a", 3);
		testLibrary.rateSong("z", "a", 5);
		oracle.clear();
		oracle.add("y, a, ***, rock");
		oracle.add("z, a, *****, rock");
		
		assertEquals(testLibrary.getFavourites(), oracle);
	}
	
	@Test
	void testPlaySong() {
		// 1_a album is automatically added to our test
		testLibrary.playSong("x", "a");
		testLibrary.playSong("y", "a");
		testLibrary.playSong("z", "a");
		
		// Roundabout way
		ArrayList<String> playedSongs = testLibrary.getMostPlayedSongs();
		assertEquals(playedSongs.size(), 3);
		assertTrue(playedSongs.get(0).equals("x, a, rock; (Plays: 1)"));
		assertTrue(playedSongs.get(1).equals("y, a, rock; (Plays: 1)"));
		assertTrue(playedSongs.get(2).equals("z, a, rock; (Plays: 1)"));
		
		
	}
	
	@Test
	void testPlaySongMultiple() {
		// 1_a album is automatically added to our test
		testLibrary.playSong("x", "a");
		testLibrary.playSong("x", "a");
		testLibrary.playSong("y", "a");
		testLibrary.playSong("y", "a");
		testLibrary.playSong("y", "a");
		testLibrary.playSong("z", "a");
		
		// Roundabout way
		ArrayList<String> playedSongs = testLibrary.getMostPlayedSongs();
		assertEquals(playedSongs.size(), 3);
		System.out.println(playedSongs.get(0));
		assertTrue(playedSongs.get(0).equals("y, a, rock; (Plays: 3)"));
		assertTrue(playedSongs.get(1).equals("x, a, rock; (Plays: 2)"));
		assertTrue(playedSongs.get(2).equals("z, a, rock; (Plays: 1)"));
	}
	
	@Test
	void testMostPlayedNothing() {
		assertEquals(testLibrary.getMostPlayedSongs(), new ArrayList<String>());
	}
	
	@Test
	void testMostPlayedSongs() {
		// 1_a album is automatically added to our test
		testLibrary.playSong("x", "a");
		testLibrary.playSong("x", "a");
		testLibrary.playSong("y", "a");
		testLibrary.playSong("y", "a");
		testLibrary.playSong("y", "a");
		testLibrary.playSong("z", "a");
		testLibrary.playSong("x", "a");
		testLibrary.playSong("x", "a");
		testLibrary.playSong("x", "a");
		testLibrary.playSong("x", "a");
		testLibrary.playSong("x", "a");
		testLibrary.playSong("z", "a");
		testLibrary.playSong("z", "a");
		testLibrary.playSong("z", "a");
		testLibrary.playSong("z", "a");
		
		ArrayList<String> mostPlayedSongs = testLibrary.getMostPlayedSongs();
		assertEquals(mostPlayedSongs.size(), 3);
		assertTrue(mostPlayedSongs.get(0).equals("x, a, rock; (Plays: 7)"));
		assertTrue(mostPlayedSongs.get(1).equals("z, a, rock; (Plays: 5)"));
		assertTrue(mostPlayedSongs.get(2).equals("y, a, rock; (Plays: 3)"));
	}
	
	@Test
	void testRecentlyPlayedNothing() {
		assertEquals(testLibrary.getRecentlyPlayed(), new ArrayList<String>());
	}
	
	@Test
	void testRecenltyPlayedSimple() {
		// 1_a album is automatically added to our test
		testLibrary.playSong("y", "a");
		testLibrary.playSong("z", "a");
		testLibrary.playSong("x", "a");
		
		ArrayList<String> recentlyPlayedSongs = testLibrary.getRecentlyPlayed();
		assertEquals(recentlyPlayedSongs.size(), 3);
		assertTrue(recentlyPlayedSongs.get(0).equals("x, a, rock"));
		assertTrue(recentlyPlayedSongs.get(1).equals("z, a, rock"));
		assertTrue(recentlyPlayedSongs.get(2).equals("y, a, rock"));
	}
	
	@Test
	void testRecentlyPlayedComplicated() {
		testLibrary.playSong("x", "a");
		testLibrary.playSong("x", "a");
		testLibrary.playSong("y", "a");
		testLibrary.playSong("y", "a");
		testLibrary.playSong("y", "a");
		testLibrary.playSong("z", "a");
		testLibrary.playSong("x", "a");
		testLibrary.playSong("x", "a");
		testLibrary.playSong("x", "a");
		testLibrary.playSong("x", "a");
		testLibrary.playSong("x", "a");
		testLibrary.playSong("z", "a");
		testLibrary.playSong("x", "a");
		testLibrary.playSong("z", "a");
		testLibrary.playSong("z", "a");
		testLibrary.playSong("z", "a");
		
		ArrayList<String> recentlyPlayedSongs = testLibrary.getRecentlyPlayed();
		assertEquals(recentlyPlayedSongs.size(), 3);
		assertTrue(recentlyPlayedSongs.get(0).equals("z, a, rock"));
		assertTrue(recentlyPlayedSongs.get(1).equals("x, a, rock"));
		assertTrue(recentlyPlayedSongs.get(2).equals("y, a, rock"));
	}
	
	@Test
	void testGetAllPlaysNone() {
		assertEquals(testLibrary.getAllPlays(), 0);
	}
	
	@Test
	void testGetAllPlays() {
		testLibrary.playSong("y", "a");
		testLibrary.playSong("z", "a");
		testLibrary.playSong("x", "a");
		testLibrary.playSong("y", "a");
		testLibrary.playSong("z", "a");
		testLibrary.playSong("x", "a");
		testLibrary.playSong("y", "a");
		
		assertEquals(testLibrary.getAllPlays(), 7);
	}
	
	@Test
	void testDeleteSong() {
		testLibrary.deleteSong("x", "a");
		
		for (String song : testLibrary.getAllSongs()) {
			assertFalse(song.equals("x, a, rock"));
		}
	}
	
	@Test
	void testDeleteManySongs() {
		// So we know that we deleted songs
		testLibrary.addSongToAlbum(testSong, libraryAlbum);
		
		testLibrary.deleteSong("x", "a");
		testLibrary.deleteSong("y", "a");
		
		for (String song : testLibrary.getAllSongs()) {
			assertFalse(song.equals("x, a, rock"));
			assertFalse(song.equals("y, a, rock"));
		}
	}
	
	@Test
	void testAddAlbum() {
		assertEquals(testLibrary.getAllAlbums().size(), 1);
		testLibrary.addAlbum(testAlbum);
		assertEquals(testLibrary.getAllAlbums().size(), 2);
	}
	
	@Test
	void testDeleteAlbumOnly() {
		testLibrary.deleteAlbum("1", "a");
		assertEquals(testLibrary.getAllAlbums().size(), 0);
	}
	
	@Test
	void testDeleteAlbum() {
		testLibrary.addAlbum(testAlbum);
		testLibrary.deleteAlbum("1", "a");
		
		assertEquals(testLibrary.getAllAlbums().size(), 1);
		assertTrue(testLibrary.getAllAlbums().get(0).equals("hello,world,punk,3000"));
	}
	
	// There really isn't a good way to test loading, but this is an attempt
	//@Test
	//void testLoadUserLibrary() {
		// Set the user up
	//	testLibrary.addSongToAlbum(testSong, libraryAlbum);
	//	testLibrary.createPlaylist("testPlaylist");
	//	testLibrary.addSongToPlaylist(testSong.getName(), testSong.getAuthor(), "testPlaylist");
		
		// Simulate a save
		
	//}
}
