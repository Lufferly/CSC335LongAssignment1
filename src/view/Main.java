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
            // should be in format:
            // >search [album or song] [name or author] query
            if (userInput.get(0).equals("search")) {
                System.out.println("searching...");
                System.out.println(view.search(userInput, musicStore));
            }
            // Buy an album functionality
            // should be in format:
            //  >buy [album or song] thing to buy
            else if (userInput.get(0).equals("buy")) {
                System.out.println("buying...");
                System.out.println("[!] Not implemented!");
            }
            // View functionality
            // should be in the format:
            //  >view [musicstore or library] [song(s)/artist(s)/album(s)/playlist(s)/favorite(s)]
            else if (userInput.get(0).equals("view")) {
                System.out.println("viewing...");
                System.out.println("[!] Not implemented!");
            }
            // playlist functionality
            // should be in in the format
            //  >playlist create playlist_name  # for creating
            //  >playlist add playlist_name song_name   # for adding songs to a playlist
            else if (userInput.get(0).equals("playlist")) {
                if (userInput.get(1).equals("create")) {    // For creating a playlist
                    System.out.println("creating...");
                    System.out.println("[!] Not implemented!");
                }
                if (userInput.get(1).equals("add")) {   // For adding a song in your library to a playlist
                    System.out.println("adding...");
                    System.out.println("[!] Not implemented!");
                }
            }
            // exit functionality
            // Exits the program.
            // should be in the format:
            //  >exit
            else if (userInput.get(0).equals("exit")) {
                break;
            }
            // help functionality
            // Prints out a help method with all the commands
            // should be in the format:
            //  >help
            else if (userInput.get(0).equals("help")) {
                print_help();
            }
            else {  // Command not recognized
                System.out.println("[!] Error! Command not recognized!");
                System.out.println("[!] Please use the \"help\" command for a list of commands!");
            }
        }
    }

    private static void print_help() {
        System.out.println("Here is how to use the music store:");
    }
}
