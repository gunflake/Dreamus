package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SavePlaylist {
    private Long userNo;
    private List<Long> albums;
    private List<Long> songs;

}
