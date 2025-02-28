package view;
import java.util.ArrayList;
import java.util.Scanner;

import MusicStore.MusicStore;
import userLibrary.LibraryModel;


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

    // View some data from the usersLibrary
    //  The userInput will be formatted
    //  >library query
    //  Where query is what the user wants to view, and can be any of the following:
    //  album(s), song(s), playlist(s), favorite(s) 
    public void viewLibrary(ArrayList<String> userInput, LibraryModel userLibrary) {
        if (userInput.size() < 2) {
            System.out.println("[!] Error! Invalid library command!");
            System.out.println("[!] The format for the library command is >library [album or song or playlist or favorite]");
            return;
        }

        String userQuery = userInput.get(1);

        if (userQuery.contains("album")) {
            ArrayList<String> allAlbums = userLibrary.getAllAlbums();
            for (String album : allAlbums) {
                System.out.println(album);
            }
        } else if (userQuery.contains("song")) {
            ArrayList<String> allSongs = userLibrary.getAllSongs();
            for (String song : allSongs) {
                System.out.println(song);
            }
        } else if (userQuery.contains("playlist")) {
            ArrayList<String> allPlaylists = userLibrary.getAllPlaylists();
            for (String playlist : allPlaylists) {
                System.out.println(playlist);
            }
        } else if (userQuery.contains("favorite")) {
            ArrayList<String> allFavorites = userLibrary.getFavourites();
            for (String favorite : allFavorites) {
                System.out.println(favorite);
            }
        } else {
            System.out.println("[!] Error! Invalid library command!");
            System.out.println("[!] The format for the library command is >library [album or song or playlist or favorite]");
        }
    }

    // Given a userInput containing info on what they want to buy, buy the album/song in the uerLibrary
    //  the userInput will be in format: (each word is a seperate element)
    //  >buy [album or song] buyQuery
    //      buyQuery should be a name of the album or song, if we encounter multiple we will
    //      give the user multiple options
    public void buy(ArrayList<String> userInput, MusicStore musicStore, LibraryModel userLibrary) {
        // Check that the length of the userInput is correct
        if (userInput.size() < 3) {
            System.out.println("[!] Error! Invalid buy command! Format should be \"buy [album or song] buyQuery\" ");
            return;
        }
        
        String buyQuery = userInput.get(2);

        if (userInput.get(1).equals("album")) {  // Buying an album
            buyAlbum(buyQuery, musicStore, userLibrary);

        }
        else if (userInput.get(1).equals("song")) { // Buying a song

        }
        else {  // Invalid buy command
            System.out.println("[!] Error! Malformed search statement! Please put \"album\" or \"song\" after \"buy\" to indicate what you want to buy!");
        }
    }

    // Helper function of buy() to buy an album
    public void buyAlbum(String buyQuery, MusicStore musicStore, LibraryModel userLibrary) {
        // Get all the albums that match the buyQuery
        ArrayList<String> possibleMatches = new ArrayList<String>();
        for (String album : musicStore.getAllAlbums()) {
            if (album.toLowerCase().contains(buyQuery)) {
                possibleMatches.add(album);
            }
        }

        if (possibleMatches.size() > 1) {  // If we got multiple matches
            // Give the user an enumerated list of options and let them choose
            System.out.println("[!] That query came up with multiple results!");
            System.out.println("Please enter the NUMBER of the album that you meant:");
            // Print out all of the possible options
            int i = 1;  // Used for enumerating the options
            for (String album : possibleMatches) {
                System.out.print(i);
                System.out.println(": " + album);
                i += 1;
            }
            System.out.print(">");

            // Check that the user gave valid input
            int userChoice = -1;
            try {
                // Turn the user's choice into an integer, and subtract one
                userChoice = Integer.parseInt(scanner.nextLine()) - 1;
            } catch (Exception NumberFormatException) {
                System.out.println("[!] Error! Could not convert your input to a number!");
                return;
            }
            // Check the bounds is good
            if (((userChoice) >= possibleMatches.size()) || ((userChoice) < 0)) {
                System.out.println("[!] Error! That number is out of bounds!");
                return;
            }

            // Add the chosen album to the user's library
            String chosenName = possibleMatches.get(userChoice).split(",")[0];
            String chosenAuthor = possibleMatches.get(userChoice).split(",")[1];
            userLibrary.buyAlbum(chosenName, chosenAuthor);
        } else if (possibleMatches.size() == 1) {  // Only one result
            // Only one result found, just buy it
            String chosenName = possibleMatches.get(0).split(",")[0];
            String chosenAuthor = possibleMatches.get(0).split(",")[1];
            userLibrary.buyAlbum(chosenName, chosenAuthor);
        } else {
            System.out.println("[!] Could not find an album by that name!");
        }
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
        // Search for songs by name
        if (userInput.get(2).equals("name")) {
            return musicStore.searchForSongsByName(userInput.get(3));
        }
        // Search for songs by author
        if (userInput.get(2).equals("author")) {
            return musicStore.searchForSongsByAuthor(userInput.get(3));
        }

        // If we reach here, the search statement was entered in incorrectly
        System.out.println("[!] Error! Malformed search statement! Please put \"name\" or \"author\" after \"search name\" to indicate what you want to search for!");
        return null;
    }
}
