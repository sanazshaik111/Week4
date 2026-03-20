package com.example.demo2.controllers;

import com.example.demo2.model.Movie;
import com.example.demo2.model.Video;
import com.example.demo2.service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(VideoController.class)
class VideoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    VideoService videoService;

    // Checks if VideoController endpoint exists and responds by simulating HTTP calls with Mock
    @Test
    void testGetAll() throws Exception {
        List<Video> videos = List.of(new Movie("Inception", "Sci-Fi")); //puts in sample movies
        when(videoService.findAll()).thenReturn(videos); // to be  returned

        mockMvc.perform(get("/api/videos") // sends GET request
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    // tests getting available videos
    @Test
    void testGetAvailable() throws Exception {
        when(videoService.findAvailable()).thenReturn(List.of(new Movie("Shrek", "Animation")));

        mockMvc.perform(get("/api/videos/available").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    // tests adding a movie with tile and body and checks if it is created successfully with correct response and content
    @Test
    void testAddMovie() throws Exception {
        Video v = new Movie("Shrek", "Animation");
        when(videoService.addMovie("Shrek", "Animation")).thenReturn(v);

        mockMvc.perform(post("/api/videos/add/movie")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Shrek\",\"genre\":\"Animation\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/videos/Shrek"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Shrek"))
                .andExpect(jsonPath("$.genre").value("Animation"));
    }

    // tests JSON status and body when a movie is rented 
    @Test
    void testRent() throws Exception {
        when(videoService.rent("Inception")).thenReturn(new Movie("Inception", "Sci-Fi"));

        mockMvc.perform(put("/api/videos/Inception/rent"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    // tests JSON status and body when a movie is returned
    @Test
    void testReturn() throws Exception {
        when(videoService.giveBack("Inception")).thenReturn(new Movie("Inception", "Sci-Fi"));

        mockMvc.perform(put("/api/videos/Inception/return"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
