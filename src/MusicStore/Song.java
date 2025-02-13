package MusicStore;

public class Song {
	/// The name of the song
	private String name;
	
	// The author of the song
	private String author;
	
	/// Whether the song has been favorited or not
	private boolean favorite;
	
	public Song(String songName, String authorName) {
		name = songName;
		author = authorName;
		favorite = false;
	}
	
	// Returns the name of the song
	public String getName() {
		return name;
	}
	
	// Returns the name of the author
	public String getAuthor() {
		return author;
	}
}
