package com.example.demo.album;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Song {
    @Id
    @GeneratedValue
    private Long id;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "albumNo")
    private Album album;

    private Short length;
    private String title;
    private Short track;

    public void setAlbum(Album album) {
        if(this.album != null){
            this.album.getSongs().remove(this);
        }
        this.album = album;
        this.album.getSongs().add(this);
    }
}
