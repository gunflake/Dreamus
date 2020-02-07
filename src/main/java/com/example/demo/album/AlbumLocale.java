package com.example.demo.album;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class AlbumLocale {
    @Id
    @GeneratedValue
    private Long no;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "albumNo")
    private Album album;

    @Column
    private String name;

    public void setAlbum(Album album) {
        if(this.album != null){
            this.album.getAlbumLocales().remove(this);
        }
        this.album = album;
        this.album.getAlbumLocales().add(this);
    }
}
