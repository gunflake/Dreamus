package com.example.demo.song;

import com.example.demo.album.Album;
import com.example.demo.playlist.PlaylistItem;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Song {
    @Id
    @GeneratedValue
    private Long songNo;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "albumNo")
    private Album album;

    @Column(nullable = false)
    private Integer length;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer track;

    @JsonBackReference
    @OneToMany(mappedBy = "song")
    private List<PlaylistItem> playlistItems = new ArrayList<>();

    public void setAlbum(Album album) {
        if(this.album != null){
            this.album.getSongs().remove(this);
        }
        this.album = album;
        this.album.getSongs().add(this);
    }
}
