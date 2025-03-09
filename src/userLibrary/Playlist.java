package userLibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import MusicStore.Song;

public class Playlist {
    private String name;                // Playlist name
    private ArrayList<Song> songList;   // List of songs in Playlist

    // Constructor for Playlist class
    public Playlist(String name) {
        this.name = name;
        this.songList = new ArrayList<Song>();
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

    /* Deep copy of the songList */
    public ArrayList<Song> getSongObjects() {   
        ArrayList<Song> objects = new ArrayList<Song>();
        for(Song song: songList) {
            Song newSong = new Song(song);
            objects.add(newSong);
        }
        return objects;
    }

    // Sort songs alphabetically by song name
    public void sortAlphabeticName() {
        Collections.sort(songList, Comparator.comparing(Song::getName, String.CASE_INSENSITIVE_ORDER));
    }

    // Sort songs alphabetically by author name
    public void sortAlphabeticAuthor() {
        Collections.sort(songList, Comparator.comparing(Song::getAuthor, String.CASE_INSENSITIVE_ORDER));
    }

    // Sort songs by play count in descending order
    public void sortByPlays() {
        Collections.sort(songList, Comparator.comparingInt(Song::getPlays).reversed());
    }

    public boolean contains(Song newSong) {
        for (Song song: songList) {
            if (song.equals(newSong)) return true;
        }
        return false;
    }

    public int size() { return songList.size(); }       // Get length of playlist no. of songs

    public void maxLength(int max) {
        if (songList.size() <= max) {
            return;
        }
        for (int i = songList.size() - 1; i >= max; i--) {      // Remove elements from the last index to max
            songList.remove(i);
        }
    }
}
