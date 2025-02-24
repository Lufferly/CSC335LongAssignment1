package MusicStore;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

// The music store class. A repository for all the albums (and therefore songs) a user can get.
//  Esentially an information getting class for the view.
public class MusicStore {
    private static ArrayList<Album> albums; 

    // The constructor, given the location for the albums.txt file, reads it and creates all the albums
    // from it.
    public MusicStore(String albumFilePath) {
        // Create all of the albums
        File albumDataFile = new File(albumFilePath);
        ArrayList<Album> allAlbums = new ArrayList<Album>();
        Scanner scanner;
        try {
			scanner = new Scanner(albumDataFile);
		} catch (Exception FileNotFoundException) {
			System.out.print("Error! MusicStore Constructor was given a file path that does not exist! : ");
			System.out.println(albumDataFile);
            System.out.println("[!] Exiting...");
			System.exit(1); // Probably a better way to do this, better ask teach
			return; // So that the ide does not complain
		}

        // Split each line by comma. The order will be albumName, authorName
        String[] thisLine;
        // For each line, create the file path where the album file will exist, and feed that into the album constructor
        while (scanner.hasNext()) {
            thisLine = scanner.nextLine().split(",");
            // Album file name will be albumName_authorName
            // Create the album and add it to our array of albums
            String filePath = "data/" + thisLine[0] + "_" + thisLine[1] + ".txt";
            allAlbums.add(new Album(filePath));
        }
        albums = allAlbums;     // Make list of all albums static
        scanner.close();
    } 

    // Given an album name (or part of one), returns an array of strings containing all of the found
    //  albums whose name match the given query.
    public ArrayList<String> searchForAlbumByName(String albumName) {
        ArrayList<String> foundAlbums = new ArrayList<String>();
        for (Album album: albums) {
            if (album.getName().equals(albumName)) {
                foundAlbums.add(album.toString());
            }
        }
        return foundAlbums;
    }

    public ArrayList<String> searchForAlbumByAuthor(String authorName) {
        ArrayList<String> foundAlbums = new ArrayList<String>();
        for (Album album: albums) {
            if (album.getAuthor().equals(authorName)) {
                foundAlbums.add(album.toString());
            }
        }
        return foundAlbums;
    }

    public static ArrayList<String> getAllAlbums() {        // Get all albums as Strings
        ArrayList<String> albumStrings = new ArrayList<String>();
        for (Album album: albums) {
            albumStrings.add(album.toString());
        }
        return albumStrings;
    }

    // Print all the albums in the music store, a debugging function
    public void printAlbums() {
        for (Album album : albums) {
            album.print();
        }
    }
}