package com.example.demo.album;

import com.example.demo.model.SaveAlbum;
import com.example.demo.model.SaveSong;
import com.example.demo.song.Song;
import com.example.demo.song.SongRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
public class AlbumRepositoryTest {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private AlbumLocaleRepository albumLocaleRepository;

    @Autowired
    private SongRepository songRepository;

    @Before
    public void saveAlbumAndSong(){
        ObjectMapper objectMapper = new ObjectMapper();
        List<SaveAlbum> saveAlbumList = null;
        File file = new File("./SaveDataJson.json");
        try {
            saveAlbumList = objectMapper.readValue(file, new TypeReference<List<SaveAlbum>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Album> albums = new ArrayList<>();
        for (SaveAlbum data : saveAlbumList) {

            //Album
            Album album = new Album();
            album.setTitle(data.getAlbum_title());

            //Song
            List<Song> songs = new ArrayList<>();
            for (SaveSong getSong : data.getSongs()) {
                Song newSong = new Song();
                newSong.setTitle(getSong.getTitle());
                newSong.setLength(getSong.getLength());
                newSong.setTrack(getSong.getTrack());
                newSong.setAlbum(album);
                songs.add(newSong);
            }

            //Locale
            List<AlbumLocale> locales = new ArrayList<>();
            for (String locale : data.getLocales()) {
                AlbumLocale newAlbumLocale = new AlbumLocale();
                newAlbumLocale.setName(locale);
                newAlbumLocale.setAlbum(album);
                locales.add(newAlbumLocale);
            }

            album.setSongs(songs);
            album.setAlbumLocales(locales);
            albums.add(album);
        }

        albumRepository.saveAll(albums);

        for (int i = 0; i < albums.size(); i++) {
            songRepository.saveAll(albums.get(i).getSongs());
            albumLocaleRepository.saveAll(albums.get(i).getAlbumLocales());
        }
    }

    @Test
    public void findAllByTitleContaining() {
        List<Album> getAlbums = albumRepository.findAllByTitleIgnoreCaseContaining("please");
        List<Song> songs = getAlbums.get(0).getSongs();
        assertThat(getAlbums.size()).isEqualTo(1);
        assertThat(songs.size()).isEqualTo(11);

    }

    @Test
    public void findAllByTitleContainingAndAlbumLocalesContaining() {
        List<Album> getAlbums = albumRepository.findAllByTitleIgnoreCaseContainingAndAlbumLocalesContaining("please", "JA");
        assertThat(getAlbums.size()).isEqualTo(1);
    }

    @Test
    public void findAllByAlbumLocalesContaining() {
        Page<Album> findAlbums = albumRepository.findAllByAlbumLocalesContaining("Ja", PageRequest.of(1, 10));
        List<Album> albums = findAlbums.get().collect(Collectors.toList());
        assertThat(albums.size()).isEqualTo(2);

        findAlbums = albumRepository.findAll(PageRequest.of(3, 10));
        albums = findAlbums.get().collect(Collectors.toList());
        assertThat(albums.size()).isEqualTo(1);
    }
}
