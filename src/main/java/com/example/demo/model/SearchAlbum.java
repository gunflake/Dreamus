package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchAlbum {
    private String title;
    private Long id;
    private List<SearchSong> songs;
}
