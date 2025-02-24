package view;

import java.io.File;
//import java.io.FileNotFoundException;
import java.util.ArrayList;

import MusicStore.Album;
import MusicStore.MusicStore;

public class Main {
    public static void main(String args[]) {
        // Create all of the albums
        MusicStore musicStore = new MusicStore("data/albums.txt");
        musicStore.printAlbums();
    }
}
