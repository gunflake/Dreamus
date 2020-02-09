package com.example.demo.playlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findAllByUserNo(Long userNo);

    Optional<Playlist> findByUserNoAndPlaylistNo(Long userNo, Long playlistNo);
}
