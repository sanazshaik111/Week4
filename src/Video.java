public abstract class Video {
    private String title;
    private String genre;
    private boolean available;

    // Constructor
    public Video(String title, String genre) {
        this.title = title;
        this.genre = genre;
        this.available = true;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Abstract method
    public abstract void play();

    // Method to rent video (marks as unavailable)
    public void rentVideo() {
        this.available = false;
    }

    // Method to return video (marks as available)
    public void returnVideo() {
        this.available = true;
    }
}
