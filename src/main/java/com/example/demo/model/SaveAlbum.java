package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SaveAlbum {
    private List<SaveSong> songs;
    private List<String> locales;
    private String album_title;
}
