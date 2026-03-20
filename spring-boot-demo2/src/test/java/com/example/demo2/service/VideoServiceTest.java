package com.example.demo2.service;

import com.example.demo2.model.Movie;
import com.example.demo2.model.Video;
import com.example.demo2.repository.VideoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    VideoRepository videoRepository;

    @InjectMocks
    VideoService videoService;

    // service returns everything from the video catalog
    @Test
    void testFindAll() {
        when(videoRepository.findAll()).thenReturn(List.of(new Movie("Inception", "Sci-Fi")));

        List<Video> result = videoService.findAll();

        assertThat(result).hasSize(1);
        verify(videoRepository).findAll();
    }

    // service returns the repository results unchanged
    @Test
    void testFindAvailable() {
        when(videoRepository.findByAvailableTrue())
                .thenReturn(List.of(new Movie("Inception", "Sci-Fi")));

        List<Video> result = videoService.findAvailable();

        assertThat(result).hasSize(1);
        verify(videoRepository).findByAvailableTrue();
    }


    // service returns object of movie title saved
    @Test
    void testAddMovie() {
        when(videoRepository.findByTitleIgnoreCase("Inception")).thenReturn(Optional.empty());
        when(videoRepository.save(any(Video.class))).thenAnswer(inv -> inv.getArgument(0));

        Video saved = videoService.addMovie("Inception", "Sci-Fi");

        ArgumentCaptor<Video> captor = ArgumentCaptor.forClass(Video.class);
        verify(videoRepository).save(captor.capture());

        assertThat(captor.getValue()).isInstanceOf(Movie.class);
        assertThat(saved.getTitle()).isEqualTo("Inception");
        assertThat(saved.getGenre()).isEqualTo("Sci-Fi");
    }

    // service returns false when movie is rented and not available
    @Test
    void testRentAvail() {
        Movie m = new Movie("Inception", "Sci-Fi");
        m.setAvailable(true);

        when(videoRepository.findByTitleIgnoreCase("Inception")).thenReturn(Optional.of(m));

        Video rented = videoService.rent("Inception");

        assertThat(rented.isAvailable()).isFalse();
    }

    // service throws exception when movie is rented but not available
    @Test
    void testRentNotAvail() {
        Movie m = new Movie("Inception", "Sci-Fi");
        m.setAvailable(false);

        when(videoRepository.findByTitleIgnoreCase("Inception")).thenReturn(Optional.of(m));

        assertThatThrownBy(() -> videoService.rent("Inception"))
                .isInstanceOf(IllegalStateException.class);
    }

    // tests the return when the object in the repository is not available
    @Test
    void testReturn() {
        Movie m = new Movie("Inception", "Sci-Fi");
        m.setAvailable(false); // currently rented

        when(videoRepository.findByTitleIgnoreCase("Inception"))
                .thenReturn(Optional.of(m));

        Video returned = videoService.giveBack("Inception");

        assertThat(returned.isAvailable()).isTrue();
    }

    // tests the return when the object in the repository is already available
    @Test
    void testReturnWhenAlreadyAvailable() {
        Movie m = new Movie("Inception", "Sci-Fi");
        m.setAvailable(true); // already returned

        when(videoRepository.findByTitleIgnoreCase("Inception"))
                .thenReturn(Optional.of(m));

        assertThatThrownBy(() -> videoService.giveBack("Inception"))
                .isInstanceOf(IllegalStateException.class);
    }

}