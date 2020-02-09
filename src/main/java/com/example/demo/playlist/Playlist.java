package com.example.demo.playlist;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@JsonIgnoreProperties("playlistItems")
public class Playlist {
    @Id @GeneratedValue
    private Long playlistNo;

    private Long userNo;

    private String title;

    @JsonBackReference
    @OneToMany(mappedBy = "playlist", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<PlaylistItem> playlistItems = new ArrayList<>();

}
