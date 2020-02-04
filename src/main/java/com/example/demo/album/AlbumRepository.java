package com.example.demo.album;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findAllByTitleLike(String title);
}
