package com.example.demo2.service;

import com.example.demo2.model.Movie;
import com.example.demo2.model.Series;
import com.example.demo2.model.Video;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class VideoService {

    // Thread-safe list for demo purposes; a real app would use a repository/DB.
    private final List<Video> videos = new CopyOnWriteArrayList<>();

    public VideoService() {
        // Seed initial data (you can remove or modify)
        videos.add(new Movie("Inception", "Sci-Fi"));
        videos.add(new Series("Stranger Things", "Sci-Fi"));
    }

    /* ---------- Queries ---------- */

    public List<Video> findAll() {
        // Return a copy to avoid accidental external modification
        return List.copyOf(videos);
    }

    public List<Video> findAvailable() {
        return videos.stream()
                .filter(Video::isAvailable)
                .toList();
    }

    public Optional<Video> findByTitle(String title) {
        if (title == null) return Optional.empty();
        String t = title.trim().toLowerCase();
        return videos.stream()
                .filter(v -> v.getTitle() != null && v.getTitle().trim().toLowerCase().equals(t))
                .findFirst();
    }

    /* ---------- Commands ---------- */

    public Video addMovie(String title, String genre) {
        requireText(title, "title");
        requireText(genre, "genre");

        if (findByTitle(title).isPresent()) {
            throw new IllegalStateException("A video with title '" + title + "' already exists.");
        }

        Movie m = new Movie(title.trim(), genre.trim());
        videos.add(m);
        return m;
    }

    public Video rent(String title) {
        Video v = findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException("Video not found: '" + title + "'"));
        if (!v.isAvailable()) {
            throw new IllegalStateException("Already rented: '" + v.getTitle() + "'");
        }
        v.rentVideo();
        return v;
    }

    public Video giveBack(String title) {
        Video v = findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException("Video not found: '" + title + "'"));
        if (v.isAvailable()) {
            throw new IllegalStateException("Already available: '" + v.getTitle() + "'");
        }
        v.returnVideo();
        return v;
    }

    private static void requireText(String s, String field) {
        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException("Field '" + field + "' is required.");
        }
    }
}