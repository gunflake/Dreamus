package com.example.demo.album;

import com.example.demo.model.SaveAlbum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.created;

@Slf4j
@RestController
public class AlbumController {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AlbumLocaleRepository albumLocaleRepository;

    @GetMapping("/search")
    public ResponseEntity<Object> searchAlbumAndSong(@RequestParam("title") String title, @RequestParam("locale") String locale){
        List<Album> allByTitleLike = albumRepository.findAllByTitleLike("%" + title + "%");


        return null;
    }

    @PostMapping("/albums")
    public ResponseEntity saveAlbumAndSong(@RequestBody List<SaveAlbum> saveAlbumList){
        List<Album> albums = new ArrayList<>();
        for (SaveAlbum data: saveAlbumList) {

            //Album
            Album album = new Album();
            album.setTitle(data.getAlbum_title());

            //Song
            List<Song> songs = new ArrayList<>();
            for(SaveAlbum.Song getSong: data.getSongs()){
                Song newSong = new Song();
                newSong.setTitle(getSong.getTitle());
                newSong.setLength((short) getSong.getLength());
                newSong.setTrack((short) getSong.getTrack());
                newSong.setAlbum(album);
                songs.add(newSong);
            }

            //Locale
            List<AlbumLocale> locales = new ArrayList<>();
            for(String locale:data.getLocales()){
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


        return created(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("")
                        .buildAndExpand()
                        .toUri())
                .build();
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Happy Coding!";
    }
}
