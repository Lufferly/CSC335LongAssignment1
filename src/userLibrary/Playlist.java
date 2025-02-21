package userLibrary;

import java.util.ArrayList;
import MusicStore.Song;

public class Playlist {
    private String name;                // Playlist name
    private ArrayList<Song> songList;   // List of songs in Playlist

    // Constructor for Playlist class
    public Playlist (String name) {
        this.name = name;
        this.songList = new ArrayList<> ();
    }

    public String getName() { return name; }     // Name getter
    
    // Add songs into the playlist
    public void addSongs(Song song) {   
        Song newSong = new Song(song);
        songList.add(newSong);
    }

    // Remove song by passing name and author
    public void removeSong(String songName, String author) {
        for (int i = songList.size() - 1; i >= 0; i--) {
            if (songList.get(i).getName().equals(songName) && songList.get(i).getAuthor().equals(author)) {
                this.songList.remove(i);
                break;
            }
        }
    }

    // Get Song list as toString() representations
    public ArrayList<String> getSongs() {
        ArrayList<String> songListCopy = new ArrayList<String>();
        for (Song song: songList) {
            songListCopy.add(song.toString());
        }
        return songListCopy;
    }

    // Print the complete playlist (song strings)
    public void printPlaylist() {
        if (songList.isEmpty()) {
            System.out.println("Playlist is empty" +'\n');
        }
        ArrayList<String> stringsPrint = new ArrayList<String> ();
        stringsPrint = getSongs();
        for (String string: stringsPrint) {
            System.out.println(string + '\n');
        }
    }
}
