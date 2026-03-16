public class GoldenVideoStoreApp {
    public static void main(String[] args) {
        // Create an array of Video objects (mix of Movie and Series)
        Video[] videos = new Video[2];
        
        // Add videos to the array
        videos[0] = new Movie("Inception", "Sci-Fi");
        videos[1] = new Series("Stranger Things", "Sci-Fi");
        
        // Play Inception movie
        videos[0].play();
        
        // Play Inception in HD quality using overloaded method
        Movie inception = (Movie) videos[0];
        inception.play("HD quality");
        
        // Play Stranger Things series
        videos[1].play();
        
        // Rent Inception
        System.out.println("Renting " + videos[0].getTitle() + "...");
        videos[0].rentVideo();
        System.out.println("Available: " + videos[0].isAvailable());
        
        // Return Inception
        System.out.println("Returning " + videos[0].getTitle() + "...");
        videos[0].returnVideo();
        System.out.println("Available: " + videos[0].isAvailable());
        
        // Print available videos
        printAvailableVideos(videos);
    }
    
    // Helper method: print available videos using array traversal
    public static void printAvailableVideos(Video[] videos) {
        System.out.print("Available Videos: ");
        boolean first = true;
        for (Video video : videos) {
            if (video.isAvailable()) {
                if (!first) {
                    System.out.print(", ");
                }
                System.out.print(video.getTitle());
                first = false;
            }
        }
        System.out.println();
    }
}
