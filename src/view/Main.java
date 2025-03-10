package view;

import java.util.ArrayList;
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
                view.search(userInput, musicStore);
            }
            // view is used for viewing the albums and songs in the music store
            else if (userInput.get(0).equals("musicstore")) {
                view.viewMusicStore(userInput, musicStore);
            }
            // Library functionality
            //  centralized command for simple viewing of the usersLibrary (songs, playlists, etc..)
            //  and searching inside the user's library
            //  should be in format
            //  >library otherArgs...
            else if (userInput.get(0).equals("library")) {
                // Check that we have enough info to decide where we need to go
                //  more userInput checking is done in the respective classes
                if (userInput.size() < 2) {
                    System.out.println("[!] Error! Invalid library command!");
                    System.out.println("[!] The format for the library command is >library [album or song or artist or playlist or favorite]");
                    System.out.println("[!] OR >library search [album(s) or song(s)] [name or author] searchQuery!");
                    continue;
                }
                if (userInput.get(1).equals("search")) {  // Searching inside the user's library
                    view.searchLibrary(userInput, userLibrary);
                } else if (userInput.get(1).equals("view")) {
                    view.viewLibrary(userInput, userLibrary);
                } else if (userInput.get(1).contains("plays")) {
                    view.libraryPlays(userLibrary);
                } else if (userInput.get(1).contains("recent")) {
                    view.libraryRecents(userLibrary);
                }
            }
            // Username functionality: display or change username
            // Format: user || user [newUsername]
            else if (userInput.get(0).equals("user")) {
                if (userInput.size() == 1) {
                    System.out.println("Your username is: " + userLibrary.getUsername());
                } else {
                    userLibrary.setUsername(userInput.get(1));
                    System.out.println("Username changed to " + userInput.get(1));
                }
            }
            // Favorite songs: mark a song as favourite
            else if (userInput.get(0).equals("favorite")) {
                if (userInput.size() < 2) {
                    System.out.println("[!] Format error. Should be: favorite [song name]");
                }
                else {
                    view.favoriteSong(userInput, userLibrary);
                }
            }
            // Buy an album functionality
            // should be in format:
            //  >buy [album or song] thing to buy
            else if (userInput.get(0).equals("buy")) {
                System.out.println("buying...");
                view.buy(userInput, musicStore, userLibrary);
            }
            // Playlist functionality: create and add sings to the playlist
            // should be in in the format
            //  >playlist create playlist_name  # for creating a playlist
            //  >playlist add playlist_name song_name   # for adding songs to a playlist
            else if (userInput.get(0).equals("playlist")) {
                // Check that there is enough input data to know if we want to create a playlist or add to a playlist
                if (userInput.size() < 2) {
                    System.out.println("[!] Error! Invalid playlist command!");
                    System.out.println("[!] The format is: >playlist create playlist_name # for creating a playlist");
                    System.out.println("OR >playlist playlist_name   # for viewing songs in a playlist");
                    System.out.println("OR >playlist remove playlist_name song_name   # for removing songs from a playlist");
                    System.out.println("OR >playlist add playlist_name song_name   # for adding songs to a playlist");
                    continue;
                }

                if (userInput.get(1).equals("create")) {    // For creating a playlist
                    System.out.println("creating...");
                    view.createPlaylist(userInput, userLibrary);
                }
                else if (userInput.get(1).equals("add")) {   // For adding a song in your library to a playlist
                    System.out.println("adding...");
                    view.addSongToPlaylist(userInput, userLibrary);
                }
                else if (userInput.get(1).equals("remove")) {
                    System.out.println("removing...");
                    view.removeSongFromPlaylist(userInput, userLibrary);
                } else {
                    // Try and view the given playlist
                    view.viewPlaylist(userInput, userLibrary);
                }
            }
            /* Song plays functionality: play a song and get song plays
             * Format: "play [songname]", to play a song. */
            else if (userInput.get(0).equals("play")) {
                view.playLibrarySong(userInput, userLibrary);
                System.out.println("Playing...");
            }
            // rate functionality
            //  rates a song from 1-5 in the users library
            //  if the user rates the song 5 it is set to a favorite
            //  should be in the format:
            //  >rate song_name
            else if (userInput.get(0).equals("rate")) {
                view.rateSong(userInput, userLibrary);
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
        System.out.println("  > musicstore album(s)       - View all albums in the Music Store.");
        System.out.println("  > musicstore song(s)        - View all songs available in the Music Store.");
        System.out.println("  > library album(s)          - View all albums in your personal library.");
        System.out.println("  > library song(s)           - View all songs in your personal library.");
        System.out.println("  > library artists(s)        - View all artists in your personal library.");
        System.out.println("  > library favorite(s)       - View all favorite songs in your library.");
        System.out.println("  > library playlist(s)       - View all playlists in your library.");
        System.out.println("  > library search [song(s) or album(s)] [name or author] searchQuery       -Search for songs or albums in your library");
        System.out.println("  > library recent(s)         - View the most recent played songs in the library.");
        System.out.println("  > library plays             - View the total amount of songplays and the 10 most played songs in the library.\n");
    
        // Playlist Commands
        System.out.println("PLAYLIST COMMANDS:");
        System.out.println("  > playlist [playlist name]                    - View a playlist's songs.");
        System.out.println("  > playlist create [playlist name]             - Create a new playlist.");
        System.out.println("  > playlist add [playlist name] [song name]    - Add a song to a playlist.");
        System.out.println("  > playlist remove [playlist name] [song name] - Remove a song from a playlist.\n");
    
        // Song Management Commands
        System.out.println("SONG MANAGEMENT COMMANDS:");
        System.out.println("  > favorite [song name] [artist]   - Mark a song as a favorite.");
        System.out.println("  > rate [song name] [0-5]          - Rate a song between 1 and 5.");
        System.out.println("  > play [song name]                - Rate a song between 1 and 5.");
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
