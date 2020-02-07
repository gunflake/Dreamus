package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseSearchData {

    private List<SearchAlbum> albums;

    private List<SearchSong> songs;

    public ResponseSearchData(List<SearchAlbum> albums, List<SearchSong> songs) {
        this.albums = albums;
        this.songs = songs;
    }
}
