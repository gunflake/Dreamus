package com.example.demo.album;

import com.example.demo.model.SaveAlbum;
import com.example.demo.model.SaveSong;
import com.example.demo.song.Song;
import com.example.demo.song.SongRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AlbumController.class)
@RunWith(SpringRunner.class)
@RequiredArgsConstructor
public class AlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private final AlbumRepository albumRepository;
    private final AlbumLocaleRepository albumLocaleRepository;
    private final SongRepository songRepository;
    private final AlbumService albumService;

    @Before
    public void saveAlbum(){
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
    public void searchAlbumAndSong() throws Exception{
        mockMvc.perform(get("/search?title=please&locale=ALL"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getAlbums() {
    }

    @Test
    public void saveAlbumAndSong() {
    }
}
