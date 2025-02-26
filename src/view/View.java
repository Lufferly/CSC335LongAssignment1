package view;
import java.util.ArrayList;
import java.util.Scanner;

import MusicStore.MusicStore;


public class View {

    // We use a scanner to get user input
    private Scanner scanner;

    public View() {
        // Read from standard in to get user input
        scanner = new Scanner(System.in);
    }


    // Get an input string from the user
    public String getInput() {
        System.out.print("Please enter your input:\n>");
        String userString = scanner.nextLine();

        return userString;
    }

    // Search in a given music store, returns a list of results
    //  The second index controls what we search for, "album" or "song"
    public ArrayList<String> search(ArrayList<String> userInput, MusicStore musicStore) {
        if (userInput.size() < 4) {
            System.out.println("[!] Error! Search request did not have enough inputs!");
            return null;
        }

        // Search for albums
        if (userInput.get(1).equals("album")) {
            return albumSearch(userInput, musicStore);
        }
        // Search for songs
        if (userInput.get(1).equals("song")) {
            return songSearch(userInput, musicStore);
        }

        // If we reach here, the search statement was entered in incorrectly
        System.out.println("[!] Error! Malformed search statement! Please put \"album\" or \"song\" after \"search\" to indicate what you want to search for!");
        return null;
    }

    // Search for albums in a music store
    //  The third element in the userInput determines if we search by name or author, an the fourth
    //  element is the query we are searching for
    private ArrayList<String> albumSearch(ArrayList<String> userInput, MusicStore musicStore) {
        // Search for albums by name
        if (userInput.get(2).equals("name")) {
            return musicStore.searchForAlbumByName(userInput.get(3));
        }
        // Search for albums by author
        if (userInput.get(2).equals("author")) {
            return musicStore.searchForAlbumByAuthor(userInput.get(3));
        }

        // If we reach here, the search statement was entered in incorrectly
        System.out.println("[!] Error! Malformed search statement! Please put \"name\" or \"author\" after \"search album\" to indicate what you want to search for!");
        return null;
    }

    // Search for songs in a music store
    //  The third element in the userInput determines if we search by name or author, an the fourth
    //  element is the query we are searching for
    private ArrayList<String> songSearch(ArrayList<String> userInput, MusicStore musicStore) {
        // Search for albums by name
        if (userInput.get(2).equals("name")) {
            return musicStore.searchForSongsByName(userInput.get(3));
        }
        // Search for albums by author
        if (userInput.get(2).equals("author")) {
            return musicStore.searchForSongsByAuthor(userInput.get(3));
        }

        // If we reach here, the search statement was entered in incorrectly
        System.out.println("[!] Error! Malformed search statement! Please put \"name\" or \"author\" after \"search name\" to indicate what you want to search for!");
        return null;
    }
}
