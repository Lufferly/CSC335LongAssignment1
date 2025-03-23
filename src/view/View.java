package view;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

import MusicStore.*;
import userLibrary.LibraryModel;


public class View {
    
    private Scanner scanner;        // We use a scanner to get user input

    public View() {
        scanner = new Scanner(System.in);        // Read from standard in to get user input
    }

    // Get an input string from the user
    public String getInput() {
        System.out.print(" > ");
        String userString = scanner.nextLine().trim();

        return userString;
    }

    // Get a password input string from the user, properly encrypted
    public String getPasswordInput(String salt) {
        String userInput = getInput();
        // Check the input is the right size
        while (userInput.length() < 7 || userInput.length() > 26) {
            System.out.println("Password must be between 7 and 26 characters! Please try again:");
            userInput = getInput();
        }
        // Salt the input
        userInput += salt;
        // Encrypt the input
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encryptedBytes = digest.digest(userInput.getBytes(StandardCharsets.UTF_8));
            // Change the user input into a string version of the encrypted Bytes
            userInput = bytesToHexstring(encryptedBytes);
        } catch (Exception e) {
            System.out.println("[!] Error! Encountered an error while attempting to encrypt!");
            e.printStackTrace();
            System.exit(1);
        }

        return userInput;
    }

    // Turn an array of bytes into a hex string
    private String bytesToHexstring(byte[] bytes) {
        String returnString = "";
        for (int i = 0; i < bytes.length; i++) {
            String thisHex = Integer.toHexString(0xff & bytes[i]);
            if (thisHex.length() == 1) { // We want the hex like 03 not 3 (for example)
                returnString += "0";
            }
            returnString += thisHex;
        }

        return returnString;
    }

    // Prompt the user for log in, and fetch/create their save file
    //  returns a userLibrary corresponding to the user
    public LibraryModel login() {
        // Prompt for the user's username
        System.out.println("What is your username?");
        // The username can only be one word, so just get the first word from the input
        String usernameInput = getInput().split(" ")[0];

        // Check if the user has an account
        //  if they do check their password
        //  otherwise create their account
        String userPasswordsLocation = "userdata/users/userPasswords.txt";
        try {
            // Create the passwords file if we need to
            new File(userPasswordsLocation).createNewFile();
            // Read through the passwords file and try to find the user
            BufferedReader reader = new BufferedReader(new FileReader(userPasswordsLocation));
            // The password file is a list of key value value (seperated by :) triples where the key is the username
            //  , the first value is the salt, and the third value is the password
            String thisLine = reader.readLine();
            String userEncryptedPassword = null;
            String userSalt = null;
            while (thisLine != null) {
                // Split the line into the key value salt triple
                String[] keyValueSalt = thisLine.split(":", 3);
                if (keyValueSalt[0].equals(usernameInput)) {
                    // This is the user's password
                    userEncryptedPassword = keyValueSalt[1];
                    // This is the user's salt
                    userSalt = keyValueSalt[2];
                }

                thisLine = reader.readLine();
            }
            reader.close();

            // If the found password is null, we need to set up a new user. Otherwise we verify the user's password
            String passwordInput;
            if (userEncryptedPassword == null) {
                // Generate a new salt for the user
                Random random = new Random();
                userSalt = Integer.toString(random.nextInt());
                // Get the user's encrypted input
                System.out.println("Welcome new user!\nPlease create an unique password:");
                passwordInput = getPasswordInput(userSalt);
                // Append to the user password list and let the user through
                FileWriter writer = new FileWriter(userPasswordsLocation, true);
                writer.write(usernameInput + ":" + passwordInput + ":" + userSalt + "\n");
                writer.close();

                System.out.println("Password accepted! Welcome new user " + usernameInput);
            } else { // We found this user, verify their password
                System.out.println("Welcome back " + usernameInput + "!");
                System.out.println("Enter your password:");
                // Get the user's encrypted password and verify it
                passwordInput = getPasswordInput(userSalt);
                while (passwordInput.equals(userEncryptedPassword) == false) {
                    System.out.println("Wrong Password! Please try again");
                    passwordInput = getPasswordInput(userSalt);
                }
                System.out.println("Password Accepted!\nWelcome back " + usernameInput);
            }
        } catch (FileNotFoundException e) {
            System.out.println("[!] Error! Experienced a FileNotFoundException while attempting to get the user's password!");
            System.out.println("[!] Could not find file " + userPasswordsLocation);
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            System.out.println("[!] Error! Experienced an IO exception while attempting to get the user's password!");
            System.out.println("[!] Failing file: " + userPasswordsLocation);
            e.printStackTrace();
            System.exit(1);
        }

        // Find the location of the user's savefile
        // Check that the file for storing where user's save files are is actually created
        String userSaveFileLocationsPath = "userdata/users/userSaveFileLocations.txt";
        try {
            new File(userSaveFileLocationsPath).createNewFile();
        } catch (IOException e) {
            System.out.print("[!] Error! Experienced an IO exception, could not get " + userSaveFileLocationsPath);
            e.printStackTrace();
            System.exit(1);
        }
        // Create the buffered reader to read the file
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(userSaveFileLocationsPath));
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file " + userSaveFileLocationsPath);
            System.exit(1);
            return null;
        }

        // Try and find where the user's data is stored
        String userDataPath = null;
        try {
            String thisLine = reader.readLine();
            while (thisLine != null) {
                // Trim the line of whitespace
                thisLine = thisLine.trim();
                // Split the line into its individual data segments
                String[] lineData = thisLine.split(";");
                if (lineData[0].equals(usernameInput)) { // Check if this is the user's data
                    // Found the line that contains the user's data location
                    userDataPath = lineData[1];
                    break;
                }
    
                thisLine = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println("[!] Error! Encountered an error while reading " + userSaveFileLocationsPath);
            e.printStackTrace();
            System.exit(1);
        }

        // Create the LibraryModel using the path we found, if we did not find a path create a file
        //  and give that path
        LibraryModel userLibrary;
        // Create the file if we found no matching file, and add to the userSaveFileLocations file
        if (userDataPath == null) {
            // No save file found, create a new one
            try {
                userDataPath = "userdata/savedata/"; 
                userDataPath += usernameInput + Integer.toString(usernameInput.hashCode()); // This could be anything technically (so long as we dont get duplicates)
                userDataPath += ".txt";
                new File(userDataPath).createNewFile();
                // Append this save file's location to the end of userSaveFileLocations
                FileWriter writer = new FileWriter(userSaveFileLocationsPath, true); // The true lets us append to the file
                writer.write(usernameInput + ";" + userDataPath + "\n");
                writer.close();
                System.out.println("Created a save file for user: " + usernameInput);
            } catch (Exception e) {
                System.out.println("[!] Error! Encountered an error while trying to create a new save file for user " + usernameInput);
                System.exit(1);
            }
        }
        // Now we can be sure a save file will exist for the user, create the LibraryModel
        userLibrary = new LibraryModel(usernameInput, userDataPath);

        // close the buffered reader
        try {
            reader.close();
        } catch (IOException e) {
            // Fuck you
            e.printStackTrace();
            System.exit(1);
        }

        return userLibrary;
    }

    // View the songs or albums in a musicstore
    public void viewMusicStore(ArrayList<String> userInput, MusicStore musicStore) {
        if (userInput.size() < 2) {
            System.out.println("[!] Error! Bad musicstore command! Not Enough arguments!");
            System.out.println("Format should be view musicstore [album(s) or song(s)]");
            return;
        }
        String userInterest = userInput.get(1);     // What the user wants to see
        ArrayList<String> returnedData;         // All the things we need to print

        if (userInterest.contains("album")) {  // View all the albums in the musicstore
            returnedData = musicStore.getAllAlbums();
        } else if (userInterest.contains("song")) {  // View all the songs in the musicstore
            returnedData = musicStore.getAllSongs();
        } else {  // Invalid input
            System.out.println("[!] Error! Bad musicstore command!");
            System.out.println("Format should be view musicstore [album(s) or song(s)]");
            return;
        }

        // print all the data
        for (String string : returnedData) {
            System.out.println(string);
        }
    }

    /* View some data from the userLibrary
    The userInput will be formatted:
    >library query
    Where query is what the user wants to view, and can be any of the following:
    album(s), song(s), playlist(s), favorite(s) */
    public void viewLibrary(ArrayList<String> userInput, LibraryModel userLibrary) {
        if (userInput.size() < 2) {
            System.out.println("[!] Error! Invalid library command!");
            System.out.println("[!] The format for the library command is >library [album or song or artist or playlist or favorite]");
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
            if (userInput.size() == 2) {            // Just view all songs
                ArrayList<String> allSongs = userLibrary.getAllSongs();
                for (String song : allSongs) {
                    System.out.println(song);
                }
            } else if (userInput.size() > 3 && userInput.get(2).equals("genre")) {
                ArrayList<String> allSongs = userLibrary.getSongsbyGenre(userInput.get(3));     // Get songs by genre
                for (String song : allSongs) {
                    System.out.println(song);
                }
            }
        } else if (userQuery.contains("artist")) {
            ArrayList<String> allArtists = userLibrary.getAllArtists();
            for (String artist : allArtists) {
                System.out.println(artist);
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

    // Set a song in the userLibrary to a favorite
    public void favoriteSong(ArrayList<String> userInput, LibraryModel userLibrary) {
        String songQuery = userInput.get(1);
        
        String songToFavorite = getFromList(userLibrary.getAllSongs(), songQuery);      // Get the song that the user wants to favorite

        if (songToFavorite == null) {           // Check we got a song back
            return;
        }
        String songName = songToFavorite.split(",")[0].trim();
        String songAuthor = songToFavorite.split(",")[1].trim();
        userLibrary.favouriteSong(songName, songAuthor);
    }

    // rate a song in the user library from 1-5
    //  if it is rated 5, add it to the user's favorites
    public void rateSong(ArrayList<String> userInput, LibraryModel userLibrary) {
        // Check the input is formatted correctly
        if (userInput.size() < 3) {
            System.out.println("[!] Error! Invalid rate command! Not enough arguments!");
            System.out.println("[!] proper rate format is >rate song_name rating(0-5)");
            return;
        }

        // Get the rating
        int rating = -1;
        try {
            rating = Integer.parseInt(userInput.get(2));
        } catch (Exception NumberFormatException) {
            System.out.println("[!] Error! Rating must be an integer from 1-5!");
            return;
        }
        if (rating < 0 || rating > 5) {
            System.out.println("[!] Error! Rating must be an integer from 1-5!");
            return;
        }
        String songQuery = userInput.get(1);        // Get the song
        String songToRate = getFromList(userLibrary.getAllSongs(), songQuery);
        if (songToRate == null) {
            return;
        }
        String[] songData = songToRate.split(",");            // Split the songToRate into its data (dumb)
        userLibrary.rateSong(songData[0], songData[1], rating);     // Rate the song
    }

    // Given a userInput containing info on what they want to buy, buy the album/song in the uerLibrary
    //  the userInput will be in format: (each word is a seperate element)
    //  >buy [album or song] buyQuery
    //      buyQuery should be a name of the album or song, if we encounter multiple we will
    //      give the user multiple options
    public void buy(ArrayList<String> userInput, MusicStore musicStore, LibraryModel userLibrary) {
        if (userInput.size() < 3) {     // Check that the length of the userInput is correct
            System.out.println("[!] Error! Invalid buy command! Format should be \"buy [album or song] buyQuery\" ");
            return;
        }

        String buyQuery = userInput.get(2);
        if (userInput.get(1).equals("album")) {  // Buying an album
            buyAlbum(buyQuery, musicStore, userLibrary);
        } else if (userInput.get(1).equals("song")) { // Buying a song
            buySong(buyQuery, musicStore, userLibrary);
        } else {  // Invalid buy command
            System.out.println("[!] Error! Malformed search statement! Please put \"album\" or \"song\" after \"buy\" to indicate what you want to buy!");
        }
    }

    // Helper function of buy() to buy an album
    private void buyAlbum(String buyQuery, MusicStore musicStore, LibraryModel userLibrary) {
        String chosenAlbum = getFromList(musicStore.getAllAlbums(), buyQuery);
        if (chosenAlbum != null) {
            String chosenName = chosenAlbum.split(",")[0].trim();
            String chosenAuthor = chosenAlbum.split(",")[1].trim();
            userLibrary.buyAlbum(chosenName, chosenAuthor);
        } else {
            System.out.println("[!] Could not find an album by that name!");
        }
    }

    // Helper function for buy(), tries to put a new song into the userLibrary
    private void buySong(String buyQuery, MusicStore musicStore, LibraryModel userLibrary) {
        String chosenSong = getFromList(musicStore.getAllSongs(), buyQuery);    // Get element from music store songlist
        
        if (chosenSong == null) {       // If we didn't find anything
            System.out.println("[!] Could not find an song by that name!");
            return;
        }

        // If we found a song
        String chosenName = chosenSong.split(",")[0].trim();
        String chosenAuthor = chosenSong.split(",")[1].trim();
        String chosenGenre = chosenSong.split(",")[2].trim();     
        userLibrary.buySong(chosenName, chosenAuthor, chosenGenre);      // Buy song by name & author

        // Get the album data for the chosen song
        String chosenSongAlbumData = musicStore.getSongAlbumData(chosenName, chosenAuthor);   

        // Construct the album
        Album newAlbum = Album.albumFromAlbumData(chosenSongAlbumData);

        // Add the album to the library, this will check for copies
        userLibrary.addAlbum(newAlbum);
        // Add the song to the album in the library
        userLibrary.addSongToAlbum(new Song(chosenName, chosenAuthor), newAlbum);

    }

    // Search in a given music store, returns a list of results
    //  The second index controls what we search for, "album" or "song"
    public void search(ArrayList<String> userInput, MusicStore musicStore) {
        if (userInput.size() < 4) {
            System.out.println("[!] Error! Search request did not have enough inputs!");
            System.out.println("[!] format for search is \"search [album or song] [name or author] query\"");
            return;
        }

        ArrayList<String> returnedData;     // The data we got
        if (userInput.get(1).equals("album")) returnedData = albumSearch(userInput, musicStore);    // Search for albums
        else if (userInput.get(1).equals("song")) returnedData = songSearch(userInput, musicStore); // Search for songs
        else {      // If we reach here, the search statement was entered in incorrectly
            System.out.println("[!] Error! Malformed search statement! Please put \"album\" or \"song\" after \"search\" to indicate what you want to search for!");
            return;
        }

        if (returnedData.size() == 0) {     // If nothing was found
            System.out.println("[!] Search query came up with NO results!");
            return;
        }
        for (String string : returnedData) {    // Iterate over every returned element & print
            System.out.println(string);
        }
    }

    // Search for albums in a music store
    //  The third element in the userInput determines if we search by name or author, an the fourth
    //  element is the query we are searching for
    private ArrayList<String> albumSearch(ArrayList<String> userInput, MusicStore musicStore) {
        if (userInput.get(2).equals("name")) {      // Search for albums by name
            return musicStore.searchForAlbumByName(userInput.get(3));
        } else if (userInput.get(2).equals("author")) {    // Search for albums by author
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
        if (userInput.get(2).equals("name")) {          // Search for songs by name
            return musicStore.searchForSongsByName(userInput.get(3));
        }
        if (userInput.get(2).equals("author")) {        // Search for songs by author
            return musicStore.searchForSongsByAuthor(userInput.get(3));
        }

        // If we reach here, the search statement was entered in incorrectly
        System.out.println("[!] Error! Malformed search statement! Please put \"name\" or \"author\" after \"search name\" to indicate what you want to search for!");
        return null;
    }

    // Create a playlist in a userlibrary
    public void createPlaylist(ArrayList<String> userInput, LibraryModel userLibrary) {
        if (userInput.size() < 3) {     // Check that the userInput is correct
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

        String songToAdd = getFromList(userLibrary.getAllSongs(), songQuery);   // Try and find the song to add

        if (songToAdd == null) {     // Check we could find a song
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
        String songToAdd = getFromList(userLibrary.getAllSongs(), songQuery);   // Try and find the song to remove
        if (songToAdd == null) {        // Check we could find a song
            return;
        }

        // Get the strings data, and pass it to the userLibrary to be added to a playlist (if it can)
        String[] songData = songToAdd.split(",");
        userLibrary.removeSongFromPlaylist(songData[0].trim(), songData[1].trim(), playlistName);
    }

    // Get the 10 most played songs in the user library
    public void libraryPlays(LibraryModel userLibrary) {
        ArrayList<String> songs = userLibrary.getMostPlayedSongs();
        System.out.println("You have a total of " + userLibrary.getAllPlays() + " song plays in your library!");
        System.out.println("Here are your most played songs:");
        for (String song: songs) {
            System.out.println(song);
        }
    }

    public void libraryRecents(LibraryModel userLibrary) {
        ArrayList<String> songs = userLibrary.getRecentlyPlayed();
        System.out.println("Here are your most recently played songs:");
        for (String song: songs) {
            System.out.println(" - " + song);
        }
    }

    public void playLibrarySong(ArrayList<String> userInput, LibraryModel userLibrary) {
        if (userInput.size() < 2) {
            System.out.println("[!] Error: Please specify the name of the song you want to play");
            return;
        }
        String songName = userInput.get(1);
        ArrayList<String> matches = new ArrayList<String>();
        for (String song : userLibrary.getAllSongs()) {
            if (song.toLowerCase().contains(songName)) {
                matches.add(song);
            }
        }

        if (matches.size() > 1) {  // If we got multiple matches
            System.out.println("[!] That came up with multiple results!");
            System.out.println("Please enter the NUMBER of the song that you want to play:");
            // Print out all of the possible options
            int i = 1;  // Used for enumerating the options
            for (String song : matches) {
                System.out.print(i);
                System.out.println(": " + song);
                i += 1;
            }
            System.out.print("> ");
            int userChoice =- 1;
            try {       // Turn the user's choice into an integer, and subtract one
                userChoice = Integer.parseInt(scanner.nextLine()) - 1;
            } catch (Exception NumberFormatException) {
                System.out.println("[!] Error! Could not convert your input to a number!");
                return;
            }
            if (((userChoice) >= matches.size()) || ((userChoice) < 0)) {       // Check the bounds is good
                System.out.println("[!] Error! That number is out of bounds!");
                return;
            }
            String chosenName = matches.get(userChoice).split(",")[0];
            String chosenAuthor = matches.get(userChoice).split(",")[1];
            userLibrary.playSong(chosenName, chosenAuthor);         // Play the chosen song
        } else if (matches.size() == 1) {                   // If only one result just play it
            String chosenName = matches.get(0).split(",")[0];
            String chosenAuthor = matches.get(0).split(",")[1];
            userLibrary.playSong(chosenName, chosenAuthor);
        } else {        // No results 
            System.out.println("[!] Could not find a song on your library by that name!");
        }
    }

    public void librarySorting(LibraryModel userLibrary, ArrayList<String> userInput) {
        if (userInput.size() < 3) {
            System.out.println("[!] Invalid sort command. Format should be: 'library sort [title || artist || rating]'");
        } else {
            if (userInput.get(2).contains("rating")) userLibrary.sortByRating();
            else if (userInput.get(2).contains("artist")) userLibrary.sortByArtist();
            else if (userInput.get(2).contains("title")) userLibrary.sortByTitle();
            for (String song: userLibrary.getAllSongs()) {
                System.out.println(song);
            }
        }
    }

    public void libraryDelete(LibraryModel userLibrary, ArrayList<String> userInput) {
        if (userInput.size() < 4) {
            System.out.println("[!] Invalid sort command. Format should be: 'library delete [song || album] title'");
        } else {
            if (userInput.get(2).contains("song")) {
                String songString = getFromList(userLibrary.getAllSongs(), userInput.get(3));
                if (songString != null) {
                    String title = songString.split(",")[0].trim();
                    String author = songString.split(",")[1].trim();
                    userLibrary.deleteSong(title, author);
                }
            } else if (userInput.get(2).contains("album")) {
                String albumString = getFromList(userLibrary.getAllAlbums(), userInput.get(3));
                if (albumString != null) {
                    String title = albumString.split(",")[0].trim();
                    String author = albumString.split(",")[1].trim();
                    userLibrary.deleteAlbum(title, author);
                }
            }
        }
    }

    // Finds a matching or containing string based on the query in list
    //  if multiple possibilities show up, ask the user to chose.
    private String getFromList(ArrayList<String> list, String query) {
        // Get all the possible matches for the song
        ArrayList<String> matches = new ArrayList<String>();
        for (String element: list) {
            if (element.toLowerCase().contains(query)) {
                matches.add(element);
            }
        }
        if (matches.size() > 1) {  // If we got multiple matches
            // Give the user an enumerated list of options and let them choose
            System.out.println("[!] That query came up with multiple results!");
            System.out.println("Please enter the NUMBER of the song that you meant:");
            // Print out all of the possible options
            int i = 1;  // Used for enumerating the options
            for (String element : matches) {
                System.out.print(i);
                System.out.println(": " + element);
                i += 1;
            }
            System.out.print(" > ");

            // Check that the user gave valid input
            int userChoice = -1;
            try {
                userChoice = Integer.parseInt(scanner.nextLine()) - 1;    // Turn the user choice into an integer, and -1
            } catch (Exception NumberFormatException) {
                System.out.println("[!] Error! Could not convert your input to a number!");
                return null;
            }
            if (((userChoice) >= matches.size()) || ((userChoice) < 0)) {     // Check the bounds is good
                System.out.println("[!] Error! That number is out of bounds!");
                return null;
            }
            String chosenElement = matches.get(userChoice);  
            return chosenElement;      // return chosen element
        } else if (matches.size() == 1) { // We only found one match
            return matches.get(0);
        } else {
            System.out.println("[!] Could not find a song by that name!");
            return null;
        }
    }

    public void libraryShuffle(LibraryModel userLibrary) {
        userLibrary.shuffleSongs();
        System.out.println("Songs shuffled successfully!");
    }

    public void shufflePlaylist(ArrayList<String> userInput, LibraryModel userLibrary) {
        if (userInput.size() < 3) {
            System.err.println("[!] Invalid playlist shuffle command. Format should be: 'playlist shuffle [name]'");
        } else {
            String chosenName = getFromList(userLibrary.getAllPlaylists(), userInput.get(2));
            if (chosenName != null) userLibrary.shufflePlaylist(chosenName); // If name matches, shuffle
            else System.err.println("[!] Couldn't find any playlists by that name");
        }
    }
}