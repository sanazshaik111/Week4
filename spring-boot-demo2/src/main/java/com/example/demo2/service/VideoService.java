package com.example.demo2.service;

import com.example.demo2.model.Movie;
import com.example.demo2.model.Series;
import com.example.demo2.model.Video;
import com.example.demo2.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class VideoService {

    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    /* ---------- Queries ---------- */

    public List<Video> findAll() {
        return videoRepository.findAll();
    }

    public List<Video> findAvailable() {
        return videoRepository.findByAvailableTrue();
    }

    public Optional<Video> findByTitle(String title) {
        if (title == null) return Optional.empty();
        String t = title.trim();
        if (t.isEmpty()) return Optional.empty();
        return videoRepository.findByTitleIgnoreCase(t);
    }

    /* ---------- Commands ---------- */

    public Video addMovie(String title, String genre) {
        requireText(title, "title");
        requireText(genre, "genre");

        if (findByTitle(title).isPresent()) {
            throw new IllegalStateException("A video with title '" + title + "' already exists.");
        }

        Movie m = new Movie(title.trim(), genre.trim());
        return videoRepository.save(m);
    }

    // Optional helper if you also want to add series through the service
    public Video addSeries(String title, String genre) {
        requireText(title, "title");
        requireText(genre, "genre");

        if (findByTitle(title).isPresent()) {
            throw new IllegalStateException("A video with title '" + title + "' already exists.");
        }

        Series s = new Series(title.trim(), genre.trim());
        return videoRepository.save(s);
    }

    @Transactional
    public Video rent(String title) {
        Video v = findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException("Video not found: '" + title + "'"));

        if (!v.isAvailable()) {
            throw new IllegalStateException("Already rented: '" + v.getTitle() + "'");
        }

        v.rentVideo(); // flips available=false
        // With @Transactional, Hibernate will persist the change automatically.
        // videoRepository.save(v); // not required, but okay if you prefer explicit saves
        return v;
    }

    @Transactional
    public Video giveBack(String title) {
        Video v = findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException("Video not found: '" + title + "'"));

        if (v.isAvailable()) {
            throw new IllegalStateException("Already available: '" + v.getTitle() + "'");
        }

        v.returnVideo(); // flips available=true
        return v;
    }

    private static void requireText(String s, String field) {
        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException("Field '" + field + "' is required.");
        }
    }
}