package view;

import java.io.File;
//import java.io.FileNotFoundException;
import java.util.ArrayList;

import MusicStore.Album;

public class Main {
    public static void main(String args[]) {
        // Create all of the albums
        /* String test = new File("src").getAbsolutePath();
        System.out.print(test); */

        File dataFile = new File("data");
        File[] allDataFiles = dataFile.listFiles();
        ArrayList<Album> allAlbums = new ArrayList<Album>();
        for (File file : allDataFiles) {
            if (file.getName().equals("albums.txt")) {
                continue; // This is different than the rest...
            }
            // Create an album based on the file
            Album thisAlbum = new Album(file.getPath());
            thisAlbum.print();
            allAlbums.add(thisAlbum);
        }
    }
}
