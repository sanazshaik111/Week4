package com.example.demo2.repository;

import com.example.demo2.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {

    Optional<Video> findByTitleIgnoreCase(String title);

    List<Video> findByAvailableTrue();
}
