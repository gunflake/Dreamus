package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseAlbums {
    private int statusCode;

    private SearchPage pages;

    private List<SearchAlbum> albums;

}
