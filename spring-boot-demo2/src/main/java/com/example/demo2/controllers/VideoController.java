package com.example.demo2.controllers;

import com.example.demo2.model.Video;
// If your models are in com.example.demo2 (not .model), use:
// import com.example.demo2.Video;

import com.example.demo2.service.VideoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    // Use the service instead of a local list
    private final VideoService service;

    // Constructor injection
    public VideoController(VideoService service) {
        this.service = service;
    }

    /* ---------- #4: GET endpoints ---------- */

    /** GET /api/videos -> return all videos */
    @GetMapping
    public List<Video> getAll() {
        return service.findAll();
    }

    /** GET /api/videos/available -> return only available videos */
    @GetMapping("/available")
    public List<Video> getAvailable() {
        return service.findAvailable();
    }

    /* ---------- #5: POST add movie ---------- */

    /**
     * Body: { "title": "Shrek", "genre": "Animation" }
     */
    @PostMapping("/add/movie")
    public ResponseEntity<?> addMovie(@RequestBody AddMovieRequest body) {
        try {
            if (body == null) {
                return ResponseEntity.badRequest().body("Request body is required.");
            }
            Video created = service.addMovie(body.title, body.genre);
            String location = "/api/videos/" + URLEncoder.encode(created.getTitle(), StandardCharsets.UTF_8);
            return ResponseEntity.created(URI.create(location)).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /* ---------- #6: PUT rent/return ---------- */

    @PutMapping("/{title}/rent")
    public ResponseEntity<?> rent(@PathVariable String title) {
        try {
            return ResponseEntity.ok(service.rent(title));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{title}/return")
    public ResponseEntity<?> giveBack(@PathVariable String title) {
        try {
            return ResponseEntity.ok(service.giveBack(title));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /* ---------- Simple request DTO ---------- */
    public static class AddMovieRequest {
        public String title;
        public String genre;
    }
}