package userLibrary;

import java.util.ArrayList;

import MusicStore.Album;
import MusicStore.MusicStore;

public class LibraryModel {
    private ArrayList<String> userSongs;        // SongList
    private ArrayList<Playlist> userPlaylists;  // Array of playlists
    private String username;
    private ArrayList<String> boughtAlbums;     // Use toString() of albums for this

    // Constructor & initialize class instance variables
    public LibraryModel (String userName) {
        this.username = userName;
        userPlaylists = new ArrayList<Playlist>();
        boughtAlbums = new ArrayList<String>();
    }

    public String buyAlbum (String albumName, String albumAuthor) {       // Mark an album as bought
        for (Album album: MusicStore.getAlbumObjects()) {       // Iterate over album objects (deep copy of actual list)
            String name = album.toString().split(",") [0];      // Name of current album object
            String author = album.toString().split(",") [1];    // Author of current album object
            if (boughtAlbums.contains(album.toString())) {
                return "You already own this album";
            }
            else if (name.trim().equals(albumName) && author.trim().equals(albumAuthor)) {
                ArrayList<String> albumSongs = new ArrayList<String>(album.getSongs());
                for (String song: albumSongs){          // Iterate over each song in album
                    if (!userSongs.contains(song)){        // If user hasnt bought song before
                        userSongs.add(song);        // Add each song in album to songlist
                    }
                }
                boughtAlbums.add(album.toString());     // Mark album as bought
                return "Album bought!";
            }
        }
        return "[!] Error, this album doesn't exist in Music Library";
    }

    public String buySong (String songName, String songAuthor) {       // Mark individual song as bought
        ArrayList<String> foundSongs = new ArrayList<String>(MusicStore.searchForSongsByName(songName));
        for (String song: foundSongs) {
            String author = song.split(",")[1];
            if (userSongs.contains(song)) {        // Check if user alredy owns song
                return "You already own this song";
            }
            else if (author.trim().equals(songAuthor)) {    // Check if user alredy owns song
                userSongs.add(song);
                return "Song bought!";
            }
        }
        return "[!] Error, this song doesn't exist in Music Library";
    }

    public ArrayList<String> getBoughtAlbums() { return new ArrayList<String>(boughtAlbums); }     // Return copy of boughtAlbums list
    public ArrayList<String> getBoughtSongs() { return new ArrayList<String>(userSongs); }     // Return copy of userSongs list (bought songs)

    public ArrayList<String> getAuthorsInLibrary() {    // Return list of authors of all songs in the list
        ArrayList<String> authors = new ArrayList<String>();
        for (String song: userSongs) {
            String author = song.split(",")[1];
            if (!authors.contains(author.trim())) {     // Avoid duplicates
                authors.add(author.trim());
            }
        }
        return authors;
    }

    public String getUsername() { return this.username; }    // Get username
    public void setUsername(String newUsername) { this.username = newUsername; }      // Give option to change username

    public void createPlaylist(String playlistName) {       // Create playlist and add to library
        for (Playlist playlist: userPlaylists) {
            if (playlist.getName().equals(playlistName)){       // Make sure name is unique for each playlist
                System.out.println("The name " + playlistName + " has already been used. Choose another one.");
                return;
            }
        }
        userPlaylists.add(new Playlist(playlistName)); 
    }    

    public void removePlaylist(String playlistName) {       // Removes playlist given a PLaylist Name
        for (Playlist playlist: userPlaylists) {
            if (playlist.getName().equals(playlistName)){
                userPlaylists.remove(playlist);
                break;
            }
        }
    }

    public ArrayList<String> getLibrary() {     // Get list of playlist strings
        ArrayList<String> playlistStrings = new ArrayList<String>();
        for (Playlist playlist: userPlaylists) {
            playlistStrings.add(playlist.getName());
        }
        return playlistStrings;
    }
}