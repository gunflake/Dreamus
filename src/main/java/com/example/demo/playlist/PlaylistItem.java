package com.example.demo.playlist;

import com.example.demo.song.Song;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class PlaylistItem {

    @Id @GeneratedValue
    private Long playlistItemNo;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "playlistNo")
    private Playlist playlist;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "songNo")
    private Song song;

    @Column(nullable = false)
    private Integer flag; // 삭제 여부 (1: 정상, 2: 삭제)

    public void setPlaylist(Playlist playlist) {
        if(this.playlist != null){
            this.playlist.getPlaylistItems().remove(this);
        }
        this.playlist = playlist;
        this.playlist.getPlaylistItems().add(this);
    }

    public void setSong(Song song) {
        if(this.song != null){
            this.song.getPlaylistItems().remove(this);
        }
        this.song = song;
        this.song.getPlaylistItems().add(this);
    }

    public PlaylistItem() {
        this.flag = 1;
    }
}
