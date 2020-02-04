package com.example.demo.album;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Album {

    @Id @GeneratedValue
    private Long albumNo;

    @Column
    private String title;

    @OneToMany(mappedBy = "album")
    private List<Song> songs = new ArrayList<>();

    @OneToMany(mappedBy = "album", fetch = FetchType.EAGER)
    private List<AlbumLocale> albumLocales = new ArrayList<>();
}
