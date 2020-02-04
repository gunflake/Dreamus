package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SaveAlbum {
    private List<Song> songs;
    private List<String> locales;
    private String album_title;

    @Getter
    @Setter
    public static class Song {
        private int track;
        private String title;
        private int length;
    }
}
