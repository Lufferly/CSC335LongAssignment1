package MusicStore;

public class Song {
	/// The name of the song
	private String name;
	
	// The author of the song
	private String author;
	
	// Whether the song is favorite or not
	private boolean favorite;
	
	// Make rating a String to not deal with doubles when possible (simpler than enum)
	private String rating;
	
	
	public Song(String songName, String authorName) {
		name = songName;
		author = authorName;
		favorite = false;
	}
	
	public String getName() { return name; } // Returns name of the song
	
	// Returns the name of the author
	public String getAuthor() { return author; }	// Returns name of the author
	
	public void makeFavorite() { this.favorite = true; }	// Makes a song favorite	
	
	public void unfavorite() { this.favorite = false; }		// Makes song not favorite
	
	public boolean getFavorite() { return favorite; }	// Returns if song is favorite
	
	public String getRating() { return rating; }	// Returns rating as a String
	
	// Sets new rating and makes song favorite automatically if rating is 5
	public void setRating (String newRating) {
		this.rating = newRating;
		if (rating.equals("5")) { 
			favorite = true;
		}
	}
	
	// Return a String representation of Song to refer to it
	@Override
	public String toString() {
		return name + ", " + author; 
	}
}
