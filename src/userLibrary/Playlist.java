package userLibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import MusicStore.Album;
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

    // Overload to add a song to a specific index
    public void addSongs(Song song, int index) {
        Song newSong = new Song(song);
        songList.add(index, newSong);
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
        if (songList.size() <= max) return;
        for (int i = songList.size() - 1; i >= max; i--) {      // Remove elements from the last index to max
            songList.remove(i);
        }
    }

    // Returns a string representing all of this playlist's data in key value pairs,
    //   which is used to reconstruct it
    public String playlistData() {
        String dataString = "";

        dataString += "name:" + name + ";";
        // Add all the songs
		// We have the song tag, and then the data for all of the songs, but the first piece of 
		//	data for the songs is how many characters long the next song's data lasts for. So its like:
		//		songs:125;[a song data string that is 125 characters long];32;[and so on...];
		//	a length of zero lets us know we have no more songs
        dataString += "songs:";
        for (Song song : songList) {
			String thisSongData = song.songData();
			dataString += Integer.toString(thisSongData.length()) + ";"; // find the length
			dataString += thisSongData; // Song data puts a semicolon at the end for us
		}
		dataString += "0;";  // No more songs in the album

        return dataString;
    }

    // Reconstructs a new playlist based on data derived from playlistData()
    public static Playlist playlistFromPlaylistData(String playlistData) {
        Playlist newPlaylist = new Playlist(null);

        // Split the data into it segments, which are key value pairs, Dont split the song list
        int totalDataSegments = 2;  // How many data segments the album has, this must be updated as more is added to the playlist class
		ArrayList<String[]> albumDataList = new ArrayList<String[]>();
		for (String data : playlistData.split(";", totalDataSegments)) {
			// break the data into its key value pairs and add it to the list
			String[] keyValuePair = new String[] {data.split(":", 2)[0], data.split(":", 2)[1]};
			albumDataList.add(keyValuePair);
		}

        // Read the data and use it to fill in the playlist
        for (String[] keyValue : albumDataList) {
            String key = keyValue[0];
            String value = keyValue[1];

            // We know what data the value is based on the key
            if (key.equals("name")) {
                newPlaylist.name = value;
            } else if (key.equals("songs")) {
                // We use the same method as album for representing our array of songs
                newPlaylist.songList = Album.songArrayFromAlbumData(value);
            }
        }
        return newPlaylist;
    }

    public void playInsidePlaylist(Song songToPlay) {
        for (Song s: songList) {
            if (s.equals(songToPlay)) s.playsong();
        }
    }
}
