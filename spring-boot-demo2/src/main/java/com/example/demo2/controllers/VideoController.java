package com.example.demo2.controllers;

import com.example.demo2.model.Video;
import com.example.demo2.model.Movie;
import com.example.demo2.model.Series;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    // In-memory list for the lab (no DB)
    private final List<Video> videos = new ArrayList<>();

    public VideoController() {
        // Seed data (similar to your console app)
        videos.add(new Movie("Inception", "Sci-Fi"));
        videos.add(new Series("Stranger Things", "Sci-Fi"));
    }

    /* -------------- #4: GET endpoints -------------- */

    /** GET /api/videos -> return all videos */
    @GetMapping
    public List<Video> getAll() {
        return videos;
    }

    /** GET /api/videos/available -> return only available videos */
    @GetMapping("/available")
    public List<Video> getAvailable() {
        return videos.stream()
                     .filter(Video::isAvailable)
                     .toList();
    }

    /* -------------- #5: POST endpoint -------------- */

    /**
     * POST /api/videos/add/movie -> add a new Movie
     * Request body JSON: { "title": "Shrek", "genre": "Animation" }
     */
    @PostMapping("/add/movie")
    public ResponseEntity<?> addMovie(@RequestBody AddMovieRequest body) {
        if (body == null || isBlank(body.getTitle()) || isBlank(body.getGenre())) {
            return ResponseEntity.badRequest().body("Both 'title' and 'genre' are required.");
        }

        // Enforce unique title (case-insensitive) for simplicity
        if (findByTitle(body.getTitle()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("A video with title '" + body.getTitle() + "' already exists.");
        }

        Movie m = new Movie(body.getTitle().trim(), body.getGenre().trim());
        videos.add(m);

        // Location header for the new resource
        String location = "/api/videos/" + URLEncoder.encode(m.getTitle(), StandardCharsets.UTF_8);
        return ResponseEntity.created(URI.create(location)).body(m);
    }

    /* -------------- #6: PUT endpoints -------------- */

    /** PUT /api/videos/{title}/rent -> mark as unavailable */
    @PutMapping("/{title}/rent")
    public ResponseEntity<?> rent(@PathVariable String title) {
        Optional<Video> maybe = findByTitle(title);
        if (maybe.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Video not found: '" + title + "'");
        }
        Video v = maybe.get();
        if (!v.isAvailable()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Already rented: '" + v.getTitle() + "'");
        }
        v.rentVideo();
        return ResponseEntity.ok(v);
    }

    /** PUT /api/videos/{title}/return -> mark as available */
    @PutMapping("/{title}/return")
    public ResponseEntity<?> giveBack(@PathVariable String title) {
        Optional<Video> maybe = findByTitle(title);
        if (maybe.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Video not found: '" + title + "'");
        }
        Video v = maybe.get();
        if (v.isAvailable()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Already available: '" + v.getTitle() + "'");
        }
        v.returnVideo();
        return ResponseEntity.ok(v);
    }

    /* -------------- Helpers -------------- */

    private Optional<Video> findByTitle(String title) {
        if (title == null) return Optional.empty();
        String t = title.trim().toLowerCase();
        return videos.stream()
                .filter(v -> v.getTitle() != null && v.getTitle().trim().toLowerCase().equals(t))
                .findFirst();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /** Request body for adding a movie (simple DTO) */
    public static class AddMovieRequest {
        private String title;
        private String genre;

        public AddMovieRequest() {}
        public AddMovieRequest(String title, String genre) {
            this.title = title; this.genre = genre;
        }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getGenre() { return genre; }
        public void setGenre(String genre) { this.genre = genre; }
    }
}