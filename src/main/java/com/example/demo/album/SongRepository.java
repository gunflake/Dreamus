package com.example.demo.album;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findAllByTitleContaining(String title);
}
