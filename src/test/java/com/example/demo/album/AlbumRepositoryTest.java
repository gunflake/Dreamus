package com.example.demo.album;

import com.example.demo.model.SaveAlbum;
import com.example.demo.song.Song;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AlbumRepositoryTest {

    @Autowired
    private AlbumRepository albumRepository;

    @Test
    public void saveAlbumAndSong(){

//        List<String> locales = new ArrayList<>();
//        locales.add("en");
//        locales.add("ja");
//
//        List<SaveAlbum> saveAlbumList = new ArrayList<>();
//
//        List<SaveAlbum.Song> saveSongs = new ArrayList<>();
//
//        SaveAlbum saveAlbum = new SaveAlbum();
//        saveAlbum.setAlbum_title("Please Please Me (1963.3)");
//        saveAlbum.setLocales(locales);
//
//
//
//        List<Album> albums = new ArrayList<>();
//        for (SaveAlbum data : saveAlbumList) {
//
//            //Album
//            Album album = new Album();
//            album.setTitle(data.getAlbum_title());
//
//            //Song
//            List<Song> songs = new ArrayList<>();
//            for (SaveAlbum.Song getSong : data.getSongs()) {
//                Song newSong = new Song();
//                newSong.setTitle(getSong.getTitle());
//                newSong.setLength((short) getSong.getLength());
//                newSong.setTrack((short) getSong.getTrack());
//                newSong.setAlbum(album);
//                songs.add(newSong);
//            }
//
//            //Locale
//            List<AlbumLocale> locales = new ArrayList<>();
//            for (String locale : data.getLocales()) {
//                AlbumLocale newAlbumLocale = new AlbumLocale();
//                newAlbumLocale.setName(locale);
//                newAlbumLocale.setAlbum(album);
//                locales.add(newAlbumLocale);
//            }
//
//            album.setSongs(songs);
//            album.setAlbumLocales(locales);
//            albums.add(album);
//        }
//
//        albumRepository.saveAll(albums);

//        for (int i = 0; i < albums.size(); i++) {
//            songRepository.saveAll(albums.get(i).getSongs());
//            albumLocaleRepository.saveAll(albums.get(i).getAlbumLocales());
//        }

    }

    @Test
    public void findAllByTitleContaining() {
    }

    @Test
    public void findAllByTitleContainingAndAlbumLocalesContaining() {
    }

    @Test
    public void findAllByAlbumLocalesContaining() {
    }
}
