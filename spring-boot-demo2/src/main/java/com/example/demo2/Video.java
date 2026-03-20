package com.example.demo2.model;
import jakarta.persistence.*;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)

public abstract class Video {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String genre;
    private boolean available;


    // JPA requires a no-args constructor
    protected Video() {
    }

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
