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
        // Main.printHelp();
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
            System.out.println("[!] OR >library search [album(s) or song(s)] [name or author] searchQuery!");
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

    // Search the user library for songs or albums, should be formatted like:
    //  >library search [album(s) or song(s)] query
    //  where the user wants to search for an album or song by author or name
    public void searchLibrary(ArrayList<String> userInput, LibraryModel userLibrary) {
        if (userInput.size() < 5) {
            System.out.println("[!] Error! Invalid library command!");
            System.out.println("[!] The format for the library command is >library [album or song or playlist or favorite]");
            System.out.println("[!] OR >library search [album(s) or song(s)] [name or author] searchQuery!");
            return;
        }    

        // What the user wants to see, albums or songs
        String userInterest = userInput.get(2);
        // If the user is searching for names or authors
        String searchType = userInput.get(3);
        // What the user is searching for
        String searchQuery = userInput.get(4);

        // Matches to the search request
        ArrayList<String> foundMatches;

        if (userInterest.contains("album")) {  // Searching for albums
            foundMatches = searchLibraryAlbums(searchType, searchQuery, userLibrary);
        } else if (userInterest.contains("song")) {  // Searching for songs
            foundMatches = searchLibrarySongs(searchType, searchQuery, userLibrary);
        } else {  // Unrecognized input
            System.out.println("[!] Error! Invalid library search command!");
            System.out.println("[!] You must put \"album\" or \"song\" after \"library search\" in order to search for albums or songs!");
            return;
        }

        // Check that we actually got results
        if (foundMatches == null) {
            return; // This happens if there was an error, and it will have already been handled
        }
        if (foundMatches.size() == 0) {
            System.out.println("[!] Your search came up with no results!");
            return;
        }

        // Print out all of the albums the search found
        System.out.println("Your search found the following matches in your library:");
        for (String match : foundMatches) {
            System.out.println(match);
        }
    }

    // Search for albums inside of the user's library based on the searchQuery
    //  searchType is the type we are searching by "name" or "author"
    //  searchQuery is what we are searching for
    //  userLibrary is the LibraryModel we are looking in
    public ArrayList<String> searchLibraryAlbums(String searchType, String searchQuery, LibraryModel userLibrary) {
        ArrayList<String> foundAlbums = new ArrayList<String>();

        // Decide what index of a split album string we are comparing to the searchQuery
        int searchIndex;
        if (searchType.equals("name")) {  // Searching by name
            searchIndex = 0;
        } else if (searchType.equals("author")) {  // Searching by author
            searchIndex = 1;
        } else {  // Unrecognized input
            System.out.println("[!] Error! Invalid library search command!");
            System.out.println("[!] You must choose to search by name or author!");
            System.out.println("[!] Proper format is >library search [album(s) or song(s)] [name or author] searchQuery!");
            return null;
        }

        for (String album : userLibrary.getAllAlbums()) {
            // Split the album into its data
            String[] albumData = album.split(",");
            // Check if the wanted data type contains the search query
            if (albumData[searchIndex].toLowerCase().contains(searchQuery)) {
                // Add this album to the found albums
                foundAlbums.add(album);
            }
        }

        return foundAlbums;
    }

    // Search for songs insie of the user's library based on the searchQuery
    public ArrayList<String> searchLibrarySongs(String searchType, String searchQuery, LibraryModel userLibrary) {
        ArrayList<String> foundSongs = new ArrayList<String>();

        // Decide what index of a split album string we are comparing to the searchQuery
        int searchIndex;
        if (searchType.equals("name")) {  // Searching by name
            searchIndex = 0;
        } else if (searchType.equals("author")) {  // Searching by author
            searchIndex = 1;
        } else {  // Unrecognized input
            System.out.println("[!] Error! Invalid library search command!");
            System.out.println("[!] You must choose to search by name or author!");
            System.out.println("[!] Proper format is >library search [album(s) or song(s)] [name or author] searchQuery!");
            return null;
        }

        for (String song : userLibrary.getAllSongs()) {
            // Split the album into its data
            String[] albumData = song.split(",");
            // Check if the wanted data type contains the search query
            if (albumData[searchIndex].toLowerCase().contains(searchQuery)) {
                // Add this album to the found albums
                foundSongs.add(song);
            }
        }

        return foundSongs;
    }

    public void favoriteSong(ArrayList<String> userInput, LibraryModel userLibrary) {
        String songQuery = userInput.get(1);
        
        // Get the song that the user wants to favorite
        String songToFavorite = getChoiceFromLibrarySongs(songQuery, userLibrary);

        // Check we got a song back
        if (songToFavorite == null) {
            return;
        }

        // Split the song into its data
        String[] songData = songToFavorite.split(",");
        String songName = songData[0];
        String songAuthor = songData[1];

        userLibrary.favouriteSong(songName, songAuthor);
    }

    // Given a songQuery, try to find a song from the user's library to return (as a string)
    //  if we find multiple matches give the user the option to choose from a list of them
    private String getChoiceFromLibrarySongs(String songQuery, LibraryModel userLibrary) {
        ArrayList<String> possibleSongs = new ArrayList<String>();
        for (String song : userLibrary.getAllSongs()) {
            if (song.toLowerCase().contains(songQuery)) {
                possibleSongs.add(song);
            }
        }
        if (possibleSongs.size() > 1) {  // If we got multiple matches
            // Give the user an enumerated list of options and let them choose
            System.out.println("[!] That query came up with multiple results!");
            System.out.println("Please enter the NUMBER of the song that you meant:");
            // Print out all of the possible options
            int i = 1;  // Used for enumerating the options
            for (String song : possibleSongs) {
                System.out.print(i);
                System.out.println(": " + song);
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
                return null;
            }
            // Check the bounds is good
            if (((userChoice) >= possibleSongs.size()) || ((userChoice) < 0)) {
                System.out.println("[!] Error! That number is out of bounds!");
                return null;
            }
            
            return possibleSongs.get(userChoice);
        }
        else if (possibleSongs.size() == 1) {       // If only one match
            return possibleSongs.get(0);
        } else {        // If no matches
            System.out.println("[!] Couldn't find any songs by the given name.");
            return null;
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
            buySong(buyQuery, musicStore, userLibrary);
        }
        else {  // Invalid buy command
            System.out.println("[!] Error! Malformed search statement! Please put \"album\" or \"song\" after \"buy\" to indicate what you want to buy!");
        }
    }

    // Helper function of buy() to buy an album
    private void buyAlbum(String buyQuery, MusicStore musicStore, LibraryModel userLibrary) {
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

    // Helper function for buy(), tries to put a new song into the userLibrary
    private void buySong(String buyQuery, MusicStore musicStore, LibraryModel userLibrary) {
        // Get all the possible matches for the song
        ArrayList<String> possibleSongs = new ArrayList<String>();
        for (String song : musicStore.getAllSongs()) {
            if (song.toLowerCase().contains(buyQuery)) {
                possibleSongs.add(song);
            }
        }

        if (possibleSongs.size() > 1) {  // If we got multiple matches
            // Give the user an enumerated list of options and let them choose
            System.out.println("[!] That query came up with multiple results!");
            System.out.println("Please enter the NUMBER of the song that you meant:");
            // Print out all of the possible options
            int i = 1;  // Used for enumerating the options
            for (String song : possibleSongs) {
                System.out.print(i);
                System.out.println(": " + song);
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
            if (((userChoice) >= possibleSongs.size()) || ((userChoice) < 0)) {
                System.out.println("[!] Error! That number is out of bounds!");
                return;
            }

            // Add the chosen album to the user's library
            String chosenName = possibleSongs.get(userChoice).split(",")[0];
            String chosenAuthor = possibleSongs.get(userChoice).split(",")[1];
            userLibrary.buySong(chosenName, chosenAuthor);
        } else if (possibleSongs.size() == 1) {  // Only one result
            // Only one result found, just buy it
            String chosenName = possibleSongs.get(0).split(",")[0];
            String chosenAuthor = possibleSongs.get(0).split(",")[1];
            userLibrary.buySong(chosenName, chosenAuthor);
        } else {
            System.out.println("[!] Could not find a song by that name!");
        }
    }

    // Search in a given music store, returns a list of results
    //  The second index controls what we search for, "album" or "song"
    public ArrayList<String> search(ArrayList<String> userInput, MusicStore musicStore) {
        if (userInput.size() < 4) {
            System.out.println("[!] Error! Search request did not have enough inputs!");
            System.out.println("[!] format for search is \"search [album or song] [name or author] query\"");
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

    // Create a playlist in a userlibrary
    public void createPlaylist(ArrayList<String> userInput, LibraryModel userLibrary) {
        // Check that the userInput is correct
        if (userInput.size() < 3) {
            System.out.println("[!] Error! Invalid playlist create command! Not enough arguments!");
            System.out.println("[!] The format is: >playlist create playlist_name");
            return;
        }

        String playlistName = userInput.get(2);

        // Create the playlist
        System.out.println(userLibrary.createPlaylist(playlistName));
    }

    // View all the songs in a playlist
    public void viewPlaylist(ArrayList<String> userInput, LibraryModel userLibrary) {
        if (userInput.size() < 2) {
            System.out.println("[!] Error! Invalid playlist command! Not enough arguments!");
            System.out.println("[!] The format is: >playlist playlist_name");
            return;
        }

        String playlistName = userInput.get(1);
        ArrayList<String> playlistSongs = userLibrary.getPlaylistSongs(playlistName);

        // Check the playlist exists
        if (playlistSongs == null) {
            System.out.println("[!] Error! That playlist does not exist! Cannot view it!");
            return;
        }
        // Check if there are any songs in the playlist
        if (playlistSongs.size() == 0) {
            System.out.println("[!] No songs in the " + playlistName + " playlist!");
            return;
        }

        // Print all the songs
        System.out.println("All the songs in the " + playlistName + " playlist:");
        for (String song : playlistSongs) {
            System.out.println(song);
        }
    }

    // Given a playlist and a song, attempt to add that song from the user's library to the playlist
    public void addSongToPlaylist(ArrayList<String> userInput, LibraryModel userLibrary) {
        if (userInput.size() < 4) {
            System.out.println("[!] Error! Invalid playlist create command! Not enough arguments!");
            System.out.println("[!] The format is: >playlist add playlist_name song_name");
            return;
        }

        String playlistName = userInput.get(2);
        String songQuery = userInput.get(3);

        // Try and find the song to add
        String songToAdd = getChoiceFromLibrarySongs(songQuery, userLibrary);

        // Check we could find a song
        if (songToAdd == null) {
            return;
        }

        // Get the strings data, and pass it to the userLibrary to be added to a playlist (if it can)
        String[] songData = songToAdd.split(",");
        userLibrary.addSongToPlaylist(songData[0], songData[1], playlistName);
    }

    // Given a playlist and a song, attempt to remove that song from the playlist
    // This is kinda bad cause we dont consider only whats in the playlist,
    //  but doing so would require some ugly backpassing between the library and view, maybe ill do it later
    public void removeSongFromPlaylist(ArrayList<String> userInput, LibraryModel userLibrary) {
        if (userInput.size() < 4) {
            System.out.println("[!] Error! Invalid playlist create command! Not enough arguments!");
            System.out.println("[!] The format is: >playlist remove playlist_name song_name");
            return;
        }

        String playlistName = userInput.get(2);
        String songQuery = userInput.get(3);

        // Try and find the song to remove
        String songToAdd = getChoiceFromLibrarySongs(songQuery, userLibrary);

        // Check we could find a song
        if (songToAdd == null) {
            return;
        }

        // Get the strings data, and pass it to the userLibrary to be added to a playlist (if it can)
        String[] songData = songToAdd.split(",");
        userLibrary.removeSongFromPlaylist(songData[0], songData[1], playlistName);
    }
}
