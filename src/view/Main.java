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
        // Create the view
        View view = new View();

        // The user input broken into a list of words
        ArrayList<String> userInput = new ArrayList<>();
        // The main loop
        while (true) {
            // Get the command from the user
            userInput.clear();
            for (String word : view.getInput().split(" ")) {
                userInput.add(word.toLowerCase()); // Make everything lowercase, for simplification
            }

            // Search functionality
            if (userInput.get(0).equals("search")) {
                System.out.println("searching...");
                System.out.println(view.search(userInput, musicStore));
            }
            if (userInput.get(0).equals("buy")) {
                System.out.println("buying...");
            }
        }
    }
}
