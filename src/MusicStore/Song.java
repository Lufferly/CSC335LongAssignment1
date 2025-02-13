package MusicStore;

public class Song {
	/// The name of the song
	private String name;
	
	/// Whether the song has been favorited or not
	private boolean favorite;
	
	public Song(String songName) {
		name = songName;
		favorite = false;
	}
	
	// Returns the name of the song
	public String getName() {
		return name;
	}
}
