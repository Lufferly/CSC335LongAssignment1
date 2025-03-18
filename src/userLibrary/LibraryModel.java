package userLibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import MusicStore.Album;
import MusicStore.Song;

public class LibraryModel {
    private ArrayList<Song> userSongs;        // SongList
    private ArrayList<Playlist> userPlaylists;  // Array of playlists
    private String username;
    private ArrayList<Album> userAlbums;     // Use toString() of albums for this
    private Playlist mostPlayed;        // Most played songs playlist
    private Playlist recentlyPlayed;        // Most played songs playlist
    private String userDataFilePath;        // The path where we store this users data

    // Constructor & initialize class instance variables
    public LibraryModel (String userName) {
        this.username = userName;
        userPlaylists = new ArrayList<Playlist>();
        userAlbums = new ArrayList<Album>();
        userSongs = new ArrayList<Song>();
        mostPlayed = new Playlist("mostPlayed");
        recentlyPlayed = new Playlist("recentlyPlayed");
        userPlaylists.add(mostPlayed);
        userPlaylists.add(recentlyPlayed);
    }

    // Constructor for reading a user's data file
    public LibraryModel (String username, String dataFilePath) {
        this.username = username;
        userDataFilePath = dataFilePath;

        // TODO: All of this needs to be read from a save file:
        userPlaylists = new ArrayList<Playlist>();
        userAlbums = new ArrayList<Album>();
        userSongs = new ArrayList<Song>();
        mostPlayed = new Playlist("mostPlayed");
        recentlyPlayed = new Playlist("recentlyPlayed");
        userPlaylists.add(mostPlayed);
        userPlaylists.add(recentlyPlayed);

        // Read the datafile
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(dataFilePath));
            String thisLine = reader.readLine();
            while (thisLine != null) {
                // Clean the line
                thisLine = thisLine.trim();
                // Find the tag of this line (the first data segment)
                String thisLineTag = thisLine.substring(0, thisLine.indexOf(';'));
                // Find the data of this line (everything but the tag)
                String thisLineData = thisLine.substring(thisLine.indexOf(';') + 1); // Get rid of the semi-colon

                // Use the tag to find out what kind of data this is
                if (thisLineTag.equals("[song]")) {
                    // This line represents data for a song, add that song to the library
                    userSongs.add(Song.songFromSongData(thisLineData));
                }

                thisLine = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("[!] Error! Could not find file " + dataFilePath);
            System.out.println("[!] Library Model cannot create a user library!");
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.out.println("[!] Library Model encountered an exception!");
            System.out.println("[!] Library Model cannot create a user library!");
            e.printStackTrace();
            System.exit(1);
        }

    }

    // Method for saving all of our data into a data file
    public void saveData() {
        if (userDataFilePath == null) {
            System.out.println("[!] Error! " + username + "'s LibraryModel does not have anywhere to store its data! Cannot save data!");
            System.exit(1);
        }

        try {
            // We assume the save file has already been created for us by the view
            FileWriter writer = new FileWriter(userDataFilePath);

            // Save every song that we own
            for (Song song : userSongs) {
                writer.write("[song];"); // add the tag;
                writer.write(song.songData());
                writer.write("\n");
            }

            writer.close();
        } catch (Exception e) {
            System.out.println("[!] Error! Encountered an error when attempting to save " + username + "'s data!");
            e.printStackTrace();
            System.exit(1);
        }   
    }

    /* Return all of the albums we have in the form of an array of strings
    this means we wont get any song information from in the albums for this */
    public ArrayList<String> getAllAlbums() {
        ArrayList<String> allAlbums = new ArrayList<String>();
        for (Album album : userAlbums) {
            // We make all of its face data comma seperated so its easy to manipulate later
            allAlbums.add(album.getName() + "," + album.getAuthor() + "," + album.getGenre() + "," + album.getYear());
        }
        return allAlbums;
    }

    // Return all of the songs owned by the user in the form of an array of strings
    public ArrayList<String> getAllSongs() {
        ArrayList<String> allSongs = new ArrayList<String>();
        for (Song song : userSongs) {
            // Comma seperate the data so its easy to manipulate
            allSongs.add(song.toString());
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

    public ArrayList<String> getFavourites() {      // Get the list of favorite songs as Strings
        ArrayList<String> favStrings = new ArrayList<String>();
        for (Song song: userSongs) {
            if (song.isFavourite()) {
                favStrings.add(song.toString());
            }
        }
        return favStrings;
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

    // Method to play a song and update the most played playlist & recently played playlist
    public void playSong(String songName, String songAuthor) {
        Song songToPlay = getSongFromLibrary(songName, songAuthor);
        if (songToPlay == null) {
            System.out.println("[!] Error! Song does not exist in library.");
            return;
        }
        songToPlay.playsong();              // Play the song
        updateMostPlayed(songToPlay);       // Update the most played list
        updateRecentlyPlayed(songToPlay);   // Update the recently played list
    }

    // Update the mostPlayed playlist with the latest play count
    private void updateMostPlayed(Song song) {
        for (Song s : mostPlayed.getSongObjects()) {
            if (s.equals(song)) {  
                s.setPlays(song.getPlays());  // Update play count with the latest value
                mostPlayed.sortByPlays();  // Sort after updating
                return;
            }
        }
        mostPlayed.addSongs(song);            // Add the song to the list
        mostPlayed.sortByPlays();             // Sort list by plays (descending)
        mostPlayed.maxLength(10);         // Make sure there are no more than 10 songs in list
    }

    // Get the top 10 most played songs as a list of Strings
    public ArrayList<String> getMostPlayedSongs() {
        ArrayList<String> topSongs = new ArrayList<>();
        for (Song song : mostPlayed.getSongObjects()) {
            topSongs.add(song.toString() + "; (Plays: " + song.getPlays() + ")");
        }
        return topSongs;
    }

    // Update the recently played list with the lastest played song
    private void updateRecentlyPlayed(Song song) {
        if(recentlyPlayed.contains(song)) {
            recentlyPlayed.removeSong(song.getName(), song.getAuthor());
        }
        recentlyPlayed.addSongs(song, 0);
        recentlyPlayed.maxLength(10);
    }

    // Get the top 10 most recently played songs as a list of Strings
    public ArrayList<String> getRecentlyPlayed() {
        ArrayList<String> recentSongs = new ArrayList<>();
        for (Song song : recentlyPlayed.getSongObjects()) {
            recentSongs.add(song.toString());
        }
        return recentSongs;
    }

    // Get all the plays of all songs (for fun)
    public int getAllPlays() {
        int total = 0;
        for (Song song: userSongs) {
            total += song.getPlays();
        }
        return total;
    }
}