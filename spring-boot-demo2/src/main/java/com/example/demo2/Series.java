package com.example.demo2.model;
import jakarta.persistence.Entity;

@Entity
public class Series extends Video {

    // JPA requires a no-args constructor
    protected Series() {
        super();
    }


    // Constructor
    public Series(String title, String genre) {
        super(title, genre);
    }

    // Implementation of abstract play() method
    @Override
    public void play() {
        System.out.println("Playing episode of series: " + getTitle() + " (" + getGenre() + ")");
    }
}
