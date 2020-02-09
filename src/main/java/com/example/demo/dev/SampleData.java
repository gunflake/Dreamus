package com.example.demo.dev;

import com.example.demo.album.AlbumService;
import com.example.demo.model.SaveAlbum;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class SampleData implements ApplicationRunner {
    private final AlbumService albumService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        List<SaveAlbum> saveAlbumList = null;
        File file = new File("./SaveDataJson.json");
        try {
            saveAlbumList = objectMapper.readValue(file, new TypeReference<List<SaveAlbum>>(){});
            albumService.saveAlbumAndSong(saveAlbumList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
