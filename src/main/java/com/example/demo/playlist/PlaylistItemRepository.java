package com.example.demo.playlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistItemRepository extends JpaRepository<PlaylistItem, Long> {
    List<PlaylistItem> findAllByPlaylist(Playlist playlist);
}
