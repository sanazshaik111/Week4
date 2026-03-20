package com.example.demo2.model;
import jakarta.persistence.Entity;

@Entity
public class Movie extends Video {

    
    // JPA requires a no-args constructor
    protected Movie() {
        super();
    }


    // Constructor
    public Movie(String title, String genre) {
        super(title, genre);
    }

    // Implementation of abstract play() method
    @Override
    public void play() {
        System.out.println("Playing movie: " + getTitle() + " (" + getGenre() + ")");
    }

    // Overloaded play() method with quality parameter
    public void play(String quality) {
        System.out.println("Playing movie: " + getTitle() + " (" + getGenre() + ") in " + quality);
    }
}
