package userLibrary;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import MusicStore.Album;
import MusicStore.MusicStore;
import MusicStore.Song;

public class LibraryModel {
    private ArrayList<Song> userSongs;        // SongList
    private ArrayList<Playlist> userPlaylists;  // Array of playlists
    private String username;
    private ArrayList<Album> userAlbums;     // Use toString() of albums for this

    // Constructor & initialize class instance variables
    public LibraryModel (String userName) {
        this.username = userName;
        userPlaylists = new ArrayList<Playlist>();
        userAlbums = new ArrayList<Album>();
        userSongs = new ArrayList<Song>();
    }

    // Return all of the albums we have in the form of an array of strings
    //  this means we wont get any song information from in the albums for this
    public ArrayList<String> getAllAlbums() {
        ArrayList<String> allAlbums = new ArrayList<String>();
        for (Album album : userAlbums) {
            // We make all of its face data comma seperated so its easy to manipulate later
            allAlbums.add(album.getName() + "," + album.getAuthor() + "," + album.getYear() + "," + album.getGenre());
        }

        return allAlbums;
    }

    // Return all of the songs owned by the user in the form of an array of strings
    public ArrayList<String> getAllSongs() {
        ArrayList<String> allSongs = new ArrayList<String>();
        for (Song song : userSongs) {
            // Comma seperate the data so its easy to manipulate
            allSongs.add(song.getName() + "," + song.getAuthor());
        }

        return allSongs;
    }

    // Return an array of all the artists represented by the user's library
    public ArrayList<String> getAllArtists() {
        ArrayList<String> allArtists = new ArrayList<String>();
        for (Song song : userSongs) {
            if (! allArtists.contains(song.getAuthor())) {
                allArtists.add(song.getAuthor());
            }
        }

        return allArtists;
    }

    // Add an album to the user's library
    //  also add all of the album's songs to the library
    public void buyAlbum (String albumName, String albumAuthor) {
        // Check that the album is not already in the list
        for (Album album : userAlbums) {
            if (album.getName().equals(albumName) && album.getAuthor().equals(albumAuthor)) {
                // Album already exists in the library
                System.out.println("[!] Error! Album is already owned by user!");
                return;
            }
        }

        // Construct the filepath from the albumName and albumAuthor
        String albumFilePath = "data/" + albumName + "_" + albumAuthor + ".txt";
        // Create the album and add it to the library
        Album newAlbum = new Album(albumFilePath);
        userAlbums.add(newAlbum);
        
        // Add all of the album's songs to the library
        for (Song song : newAlbum.getSongObjects()) {
            // This is dumb that we have to copy another copy, but encapsulation...
            Song thisSong = new Song(song);
            // Add the song if we dont already own it
            if (!songInLibrary(thisSong)) {
                userSongs.add(thisSong);
            }
        }
    }

    // Add a song to the user's library
    public void buySong (String songName, String songAuthor) {       // Mark individual song as bought (ugly)
        // Create the song
        Song newSong = new Song(songName, songAuthor);

        // Check the song is not already in the library
        if (!songInLibrary(newSong)) {
            // Add the song to the library
            userSongs.add(newSong);
        } else {
            // Print an error
            System.out.println("[!] Error! Song already exists in that userLibrary!");
        }
    }

    public String favouriteSong(String songName, String songAuthor) {
        for (Song song: userSongs) {
            if (songName.equals(song.getName()) && songAuthor.equals(song.getAuthor())) {
                song.makeFavorite();
                return "Song favourited!";
            }
        }
        return "You don't have this song or it doesn't exist";
    }

    // Rate the song, if the rating is 5, set it to the favorite
    public void rateSong(String songName, String songAuthor, int newRating) {
        Song songToRate = getSongFromLibrary(songName, songAuthor);
        if (songToRate == null) {
            System.out.print("[!] Error! Song does not exist in library!");
            return;
        }

        if (newRating == 5) {
            songToRate.makeFavorite();
        }

        songToRate.setRating(newRating);
    }

    public ArrayList<String> getBoughtSongs() {     // Return deep copy of userSongs list (bought songs)
        ArrayList<String> songStrings = new ArrayList<String>();
        for (Song song: userSongs) {
            songStrings.add(song.toString());
        }
        return songStrings;
    }

    public ArrayList<String> getFavourites() {      // Get the list of favorite songs as Strings
        ArrayList<String> favStrings = new ArrayList<String>();
        for (Song song: userSongs) {
            if (song.isFavourite()) {
                favStrings.add(song.toString());
            }
        }
        return favStrings;
    }

    public ArrayList<String> getAuthorsInLibrary() {    // Return list of authors of all songs in the list
        ArrayList<String> authors = new ArrayList<String>();
        for (Song song: userSongs) {
            String author = song.getAuthor();
            if (!authors.contains(author)) {     // Avoid duplicates
                authors.add(author.trim());
            }
        }
        return authors;
    }

    // @Override from object class, helper to compare song objects in userSongs
    public boolean contains(Song newSong) {
        for (Song song: userSongs){
            if (song.equals(newSong)) return true;
        }
        return false;
    }

    public String getUsername() { return this.username; }    // Get username
    public void setUsername(String newUsername) { this.username = newUsername; }      // Give option to change username

    // Get all the songs from a playlist in the form of an array of strings
    public ArrayList<String> getPlaylistSongs(String playlistName) {
        // Try and get the playlist
        Playlist playlist = getPlaylistFromLibrary(playlistName);
        // Check that the playlist exists
        if (playlist == null) {
            return null;
        }

        // return all the songs from the playlist
        return playlist.getSongs();
    }

    public String createPlaylist(String playlistName) {       // Create playlist and add to library
        for (Playlist playlist: userPlaylists) {
            if (playlist.getName().equals(playlistName)){       // Make sure name is unique for each playlist
                return "The name " + playlistName + " has already been used. Choose another one.";
            }
        }
        userPlaylists.add(new Playlist(playlistName)); 
        return "Playlist '" + playlistName + "' created";
    }    

    public void removePlaylist(String playlistName) {       // Removes playlist given a PLaylist Name
        for (Playlist playlist: userPlaylists) {
            if (playlist.getName().equals(playlistName)){
                userPlaylists.remove(playlist);
                break;
            }
        }
    }

    // Given a playlist name and a song name and author, try to add the song to that playlist
    public void addSongToPlaylist(String songName, String songAuthor, String playlistName) {
        // Get the song in the library
        Song playlistSong = getSongFromLibrary(songName, songAuthor);
        // Check we actually got something (theoretically dont need to do this)
        if (playlistSong == null) {
            System.out.println("[!] Error! Tried to add a song to playlist, but song did not exist in user library!");
            return;
        }

        // Try and get the playlist
        Playlist playlist = getPlaylistFromLibrary(playlistName);
        // Check that the playlist exists
        if (playlist == null) {
            System.out.println("[!] Error! Cannot add song to playlist that does not exit!");
            return;
        }

        // Add the song to the playlist
        playlist.addSongs(playlistSong);
    }

    public void removeSongFromPlaylist(String songName, String songAuthor, String playlistName) {
        // Try and get the playlist
        Playlist playlist = getPlaylistFromLibrary(playlistName);
        // Check that the playlist exists
        if (playlist == null) {
            System.out.println("[!] Error! Cannot remove song from a playlist that does not exit!");
            return;
        }

        // Try and remove the song from the playlist
        playlist.removeSong(songName, songAuthor);
    }

    public ArrayList<String> getAllPlaylists() {     // Get list of playlist strings
        ArrayList<String> playlistStrings = new ArrayList<String>();
        for (Playlist playlist: userPlaylists) {
            playlistStrings.add(playlist.getName());
        }
        return playlistStrings;
    }


    // Internal checker to see if a song is already in the library
    private boolean songInLibrary(Song checkingSong) {
        for (Song song : userSongs) {
            // If a song matches, we already have it
            if (song.equals(checkingSong)) {
                return true;
            }
        }
        // If we reach here the song is not already in the library
        return false;
    }

    // Internal helper to get a song that we own based on name and author
    private Song getSongFromLibrary(String songName, String songAuthor) {
        for (Song song : userSongs) {
            if (song.getName().equals(songName) && song.getAuthor().equals(songAuthor)) {
                return song;
            }
        }

        // No song of that found
        return null;
    }

    // Internal helper to get a playlist from the library
    private Playlist getPlaylistFromLibrary(String playlistName) {
        for (Playlist playlist : userPlaylists) {
            if (playlist.getName().equals(playlistName)) {  // All playlist names will be lowercase
                return playlist;
            }
        }

        return null; // Found no playlist
    }
}