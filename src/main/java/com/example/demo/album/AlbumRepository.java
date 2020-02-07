package com.example.demo.album;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findAllByTitleContaining(String title);

    @Query("select a from Album a inner join a.albumLocales l where a.title like :title and l.name = :localeName")
    List<Album> findAllByTitleContainingAndAlbumLocalesContaining(String title, String localeName);

    @Query("select a from Album a inner join a.albumLocales l where l.name = :localeName")
    Page<Album> findAllByAlbumLocalesContaining(String localeName, Pageable pageable);
}
