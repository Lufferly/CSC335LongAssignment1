package view;

import java.io.File;
//import java.io.FileNotFoundException;
import java.util.ArrayList;

import MusicStore.Album;
import MusicStore.MusicStore;
import userLibrary.LibraryModel;

public class Main {
    public static void main(String args[]) {
        // Create all of the albums
        MusicStore musicStore = new MusicStore("data/albums.txt");
        LibraryModel userLibrary = new LibraryModel("user");
        // Create the view
        View view = new View();

        // Show the user how to use the tool on startup
        printHelp();

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
            // Library functionality
            //  centralized command for simple viewing of the usersLibrary (songs, playlists, etc..)
            //  and searching inside the user's library
            //  should be in format
            //  >library otherArgs...
            else if (userInput.get(0).equals("library")) {
                // Check that we have enough info to decide where we need to go (searching or viewing)
                //  more userInput checking is done in the respective classes
                if (userInput.size() < 2) {
                    System.out.println("[!] Error! Invalid library command!");
                    System.out.println("[!] The format for the library command is >library [album or song or playlist or favorite]");
                    System.out.println("[!] OR >library search [album(s) or song(s)] [name or author] searchQuery!");
                }
                if (userInput.get(1).equals("search")) {  // Searching inside the user's library
                    view.searchLibrary(userInput, userLibrary);
                } else {  // Viewing the user's library
                    view.viewLibrary(userInput, userLibrary);
                }
            }
            // Buy an album functionality
            // should be in format:
            //  >buy [album or song] thing to buy
            else if (userInput.get(0).equals("buy")) {
                System.out.println("buying...");
                view.buy(userInput, musicStore, userLibrary);
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
                printHelp();
            }
            else {  // Command not recognized
                System.out.println("[!] Error! Command not recognized!");
                System.out.println("[!] Please use the \"help\" command for a list of commands!");
            }
        }
    }

    public static void printHelp() {        // Command menu, to tell user how to use the program
        System.out.println("\n======== Welcome to S&M Music Store! ========");
        System.out.println("Here you can search, buy, and manage your own music library! Use the following commands:\\n");
    
        // Search Commands
        System.out.println("SEARCH COMMANDS:");
        System.out.println("  > search album name [query]     - Search for an album by its name.");
        System.out.println("  > search album author [query]   - Search for albums by a specific artist.");
        System.out.println("  > search song name [query]      - Search for a song by its title.");
        System.out.println("  > search song author [query]    - Search for songs by a specific artist.");
        System.out.println("  (If multiple results exist, all matching items will be displayed.)\n");
    
        // Buy Commands
        System.out.println("BUY COMMANDS:");
        System.out.println("  > buy album [album name or artist] - Purchase an album (adds all its songs) to your library.");
        System.out.println("  > buy song [song name or artist]   - Purchase an individual song.\n");
        System.out.println("    If multiple options exist, a list to choose from will be given");
    
        // View Commands
        System.out.println("VIEW COMMANDS:");
        System.out.println("  > view musicstore album(s)       - View all albums in the Music Store.");
        System.out.println("  > view musicstore song(s)        - View all songs available in the Music Store.");
        System.out.println("  > library album(s)          - View all albums in your personal library.");
        System.out.println("  > library song(s)           - View all songs in your personal library.");
        System.out.println("  > library favorite(s)       - View all favorite songs in your library.");
        System.out.println("  > library playlist(s)       - View all playlists in your library.\n");
    
        // Playlist Commands
        System.out.println("PLAYLIST COMMANDS:");
        System.out.println("  > playlist create [playlist name]             - Create a new playlist.");
        System.out.println("  > playlist add [playlist name] [song name]    - Add a song to a playlist.");
        System.out.println("  > playlist remove [playlist name] [song name] - Remove a song from a playlist.\n");
    
        // Song Management Commands
        System.out.println("SONG MANAGEMENT COMMANDS:");
        System.out.println("  > favorite [song name] [artist]   - Mark a song as a favorite.");
        System.out.println("  > rate [song name] [artist] [1-5] - Rate a song between 1 and 5.");
        System.out.println("  (Songs rated 5 are automatically marked as favorites.)\n");
    
        // General Commands
        System.out.println("GENERAL COMMANDS:");
        System.out.println("  > help                 - Display this help menu.");
        System.out.println("  > exit                 - Quit the application.");
        System.out.println("  > user [username]      - Set your username to a new username.");
        System.out.println("  > user                 - Get your username.");
        System.out.println("====================================================\n");
    }
}
