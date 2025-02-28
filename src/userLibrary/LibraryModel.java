package userLibrary;

import java.util.ArrayList;

import MusicStore.Album;
import MusicStore.MusicStore;
import MusicStore.Song;

public class LibraryModel {
    private ArrayList<Song> userSongs;        // SongList
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
                ArrayList<Song> albumSongs = new ArrayList<Song>(album.getSongObjects());
                for (Song song: albumSongs){          // Iterate over each song in album
                    if (!userSongs.contains(song)){        // If user hasnt bought song before
                        userSongs.add(song);        // Add each song in album to songlist
                    }
                }
                boughtAlbums.add(album.toString());     // Mark album as bought
                return "Album bought!";
            }
        }
        return "[!] Error, this album doesn't exist in Music Library";      // Couldn't find album
    }

    public String buySong (String songName, String songAuthor) {       // Mark individual song as bought (ugly)
        for (Album album: MusicStore.getAlbumObjects()) {       // Iterate over album objects (deep copy of actual list)
            for (Song song: album.getSongObjects()) {           // Iterate over each song in each album
                if (userSongs.contains(song)) {         // If song is in userSongs, user already owns it
                    return "You already own this song!";
                } else if (songName.equals(song.getName()) && songAuthor.equals(song.getAuthor())) {    // If song matches name and author
                    userSongs.add(song);        // Buy song
                    return "Song Bought!";
                }
            }
        }
        return "[!] Error, this song doesn't exist in Music Library";       // Couldn't find song
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

    public String rateSong(String songName, String songAuthor, String newRating) {
        for (Song song: userSongs) {
            if (songName.equals(song.getName()) && songAuthor.equals(song.getAuthor())) {
                song.setRating(newRating);
                if (newRating.equals("5")){
                    song.makeFavorite();
                }
                return "Song rated!";
            }   
        }
        return "You don't own this song, or the song doesn't exist";
    }

    public ArrayList<String> getBoughtAlbums() { return new ArrayList<String>(boughtAlbums); }     // Return copy of boughtAlbums list

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

    public ArrayList<String> getLibrary() {     // Get list of playlist strings
        ArrayList<String> playlistStrings = new ArrayList<String>();
        for (Playlist playlist: userPlaylists) {
            playlistStrings.add(playlist.getName());
        }
        return playlistStrings;
    }
}