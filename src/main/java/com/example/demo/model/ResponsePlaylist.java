package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponsePlaylist {
    private Long playlistNo;
    private String title;
    private Integer count;
    private Long userNo;
    private List<SearchSong> songs;
}
