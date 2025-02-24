package userLibrary;

import java.util.ArrayList; 
import MusicStore.MusicStore;

public class LibraryModel {
    private ArrayList<Playlist> userLibrary;
    private String username;
    private ArrayList<String> boughtAlbums;     // Use toString() of albums for this

    // Constructor & initialize class instance variables
    public LibraryModel (String userName) {
        this.username = userName;
        userLibrary = new ArrayList<Playlist>();
        boughtAlbums = new ArrayList<String>();
    }

    public void buyAlbum (String albumName, String albumAuthor) {       // Mark an album as bought
        for (String album: MusicStore.getAllAlbums()) {
            String name = album.split(",") [0];
            String author = album.split(",") [1];
            if (name.equals(albumName) && author.equals(albumAuthor)) {
                boughtAlbums.add(album);
            }
        }
    }

    public ArrayList<String> getBoughtAlbums() { return new ArrayList<String>(boughtAlbums); }     // Return copy of boughtAlbums list

    public String getUsername() { return this.username; }    // Get username
    public void setUsername(String newUsername) { this.username = newUsername; }      // give option to change username

    public void createPlaylist(String playlistName) {       // Create playlist and add to library
        for (Playlist playlist: userLibrary) {
            if (playlist.getName().equals(playlistName)){       // Make sure name is unique for each playlist
                System.out.println("The name " + playlistName + " has already been used. Choose another one.");
                return;
            }
        }
        userLibrary.add(new Playlist(playlistName)); 
    }    

    public void removePlaylist(String playlistName) {       // Removes playlist given a PLaylist Name
        for (Playlist playlist: userLibrary) {
            if (playlist.getName().equals(playlistName)){
                userLibrary.remove(playlist);
                break;
            }
        }
    }

    public ArrayList<String> getLibrary() {     // Get the user library which as a list of playlist strings
        ArrayList<String> playlistStrings  =  new ArrayList<String>();
        for (Playlist playlist: userLibrary) {
            playlistStrings.add(playlist.getName());
        }
        return playlistStrings;
    }
}