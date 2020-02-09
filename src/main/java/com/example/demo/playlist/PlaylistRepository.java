package com.example.demo.playlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findAllByUserNoAndFlag(Long userNo, Integer flag);

    Optional<Playlist> findByUserNoAndPlaylistNoAndFlag(Long userNo, Long playlistNo, Integer flag);
}
