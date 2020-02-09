package com.example.demo.song;

import com.example.demo.album.Album;
import com.example.demo.album.AlbumLocale;
import com.example.demo.album.AlbumLocaleRepository;
import com.example.demo.album.AlbumRepository;
import com.example.demo.model.SaveAlbum;
import com.example.demo.model.SaveSong;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
public class SongRepositoryTest {

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
                newSong.setLength((short) getSong.getLength());
                newSong.setTrack((short) getSong.getTrack());
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
        List<Song> please = songRepository.findAllByTitleIgnoreCaseContaining("PLEASE");
        log.info(please.size()+"  size");
        assertThat(please.size()).isEqualTo(11);

    }
}
