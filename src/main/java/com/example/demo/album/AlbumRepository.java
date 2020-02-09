package com.example.demo.album;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findAllByTitleIgnoreCaseContaining(String title);

    @Query("select a from Album a inner join a.albumLocales l where upper(a.title) like upper(concat('%',:title,'%')) and upper(l.name) = upper(:localeName)")
    List<Album> findAllByTitleIgnoreCaseContainingAndAlbumLocalesContaining(String title, String localeName);

    @Query("select a from Album a inner join a.albumLocales l where upper(l.name) = upper(:localeName)")
    Page<Album> findAllByAlbumLocalesContaining(String localeName, Pageable pageable);
}
