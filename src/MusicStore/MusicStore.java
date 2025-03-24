package MusicStore;

import java.io.*;
import java.util.*;

// The music store class. A repository for all the albums (and therefore songs) a user can get.
//  Esentially an information getting class for the view.
public class MusicStore {
    private static ArrayList<Album> albums; 

    // The constructor, given the location for the albums.txt file, reads it and creates all the albums
    // from it.
    public MusicStore(String albumFilePath) {
        // Create all of the albums
        BufferedReader albumDataFile;
        try {
            albumDataFile = new BufferedReader(new FileReader(albumFilePath));
        } catch (Exception FileNotFoundException) {
            System.out.println("[!] ERROR! FILE NOT FOUND!");
            System.out.println(albumFilePath);
            System.exit(1);
            return;
        }
        ArrayList<Album> allAlbums = new ArrayList<Album>();


        // Split each line by comma. The order will be albumName, authorName
        String[] thisLine;
        try {
            thisLine = albumDataFile.readLine().split(",");
        } catch (Exception IOException) {
            System.out.println("[!] ERROR! FILE NOT FOUND!");
            System.exit(1);
            return;
        }
        // For each line, create the file path where the album file will exist, and feed that into the album constructor
        while (true) {
            // Album file name will be albumName_authorName
            // Create the album and add it to our array of albums
            String filePath = "data/" + thisLine[0] + "_" + thisLine[1] + ".txt";
            allAlbums.add(new Album(filePath));

            try {
                thisLine = albumDataFile.readLine().split(",");
            } catch (Exception IOException) {
                break;
            }
        }
        albums = allAlbums;     // Make list of all albums static
    } 

    // Given an album name (or part of one), returns an array of strings containing all of the found
    //  albums whose name has the given query.
    // Ignores case
    public ArrayList<String> searchForAlbumByName(String albumName) {
        ArrayList<String> foundAlbums = new ArrayList<String>();
        for (Album album: albums) {
            if (album.getName().toLowerCase().contains(albumName)) {
            	String thisAlbum = album.toString() + "\n";
            	for (Song song : album.getSongObjects()) {
            		thisAlbum += "    " + song.toString() + "\n";
            	}
                foundAlbums.add(thisAlbum);
            }
        }
        return foundAlbums;
    }

    // Given an album name (or part of one), returns an array of strings containing all of the found
    //  albums whose name has the given query.
    // Ignores case
    public ArrayList<String> searchForAlbumByAuthor(String authorName) {
        ArrayList<String> foundAlbums = new ArrayList<String>();
        for (Album album: albums) {
            if (album.getAuthor().toLowerCase().contains(authorName)) {
            	String thisAlbum = album.toString() + "\n";
            	for (Song song : album.getSongObjects()) {
            		thisAlbum += "    " + song.toString() + "\n";
            	}
                foundAlbums.add(thisAlbum);
            }
        }
        return foundAlbums;
    }

    // Given a query for an author, search for and return every song (in the form of an array of strings)
    //  whose author name contains the author query
    // Ignores case
    // If b_SongAlbumInfo is true, then the user is requesting extra information about the song's album
    public ArrayList<String> searchForSongsByAuthor(String authorQuery, boolean b_songAlbumInfo) {
        ArrayList<String> foundSongs = new ArrayList<String>();
        // Search through every Album's author, if the author has the author query, add
        //  all of their songs to the foundSongs list
        for (Album album : albums) {
            if (album.getAuthor().toLowerCase().contains(authorQuery)) {
                // We found an author that matches

                for (String song : album.getSongs()) {
                    if (b_songAlbumInfo == true) {
                        foundSongs.add(song + ", Album:" + album.toString());
                    } else {
                        foundSongs.add(song);
                    }
                }
            }
        }

        return foundSongs;
    }

    // Given a query for an song, search for and return every song (in the form of an array of strings)
    //  whose name contains the name query
    // Ignores case
    //  if b_songAlbumInfo is true, also append information about the album
    public ArrayList<String> searchForSongsByName(String songQuery, boolean b_songAlbumInfo) {
        ArrayList<String> foundSongs = new ArrayList<String>();
        // Search through every Album's songs, if that song's name contains the song query, add
        //  it to the foundSongs list
        for (Album album : albums) {
            for (String song : album.getSongs()) {
                if (song.toLowerCase().split(",")[0].contains(songQuery)) {
                    if (b_songAlbumInfo == true) {
                        foundSongs.add(song + ", Album:" + album.toString());
                    } else {
                        foundSongs.add(song);
                    }
                }
            }
        }
        return foundSongs;
    }

    public ArrayList<String> getAllAlbums() {        // Get all albums as Strings. The toString() of music store class
        ArrayList<String> albumStrings = new ArrayList<String>();
        for (Album album: albums) {
            albumStrings.add(album.toString());
        }
        return albumStrings;
    }

    // Get all the songs in the music store, represented as strings
    public ArrayList<String> getAllSongs() {
        ArrayList<String> songStrings = new ArrayList<String>();
        for (Album album : albums) {
            for (Song song : album.getSongObjects()) {
                songStrings.add(song.toString());
            }
        }

        return songStrings;
    }

    // Print all the albums in the music store, a debugging function
    public void printAlbums() {
        for (Album album : albums) {
            album.print();
        }
    }

    // Given a song name and album, return the album data string of an album that has the given song
    //  returns null if it could not find an album
    //  The album data will not contain the album's songs
    public String getSongAlbumData(String songName, String songAuthor) {
        // Create a dummy song for comparing songs
        Song querySong = new Song(songName, songAuthor, null);
        // Loop through all the songs in an album, if you find a matching song return the string version
        //  of that album
        for (Album thisAlbum : albums) {
            for (Song thisSong : thisAlbum.getSongObjects()) {
                if (thisSong.equals(querySong)) {
                    return thisAlbum.albumData(false); // False tells us
                }
            }
        }

        // nada found
        return null;
    }
}