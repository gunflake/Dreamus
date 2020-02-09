package com.example.demo.album;

import com.example.demo.song.Song;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference
    @OneToMany(mappedBy = "album", fetch = FetchType.EAGER)
    private List<Song> songs = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "album")
    private List<AlbumLocale> albumLocales = new ArrayList<>();
}
