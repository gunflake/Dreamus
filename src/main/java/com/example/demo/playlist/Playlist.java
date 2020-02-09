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
    private Long playlistNo; //playlist 번호

    @Column(nullable = false)
    private Long userNo; // 유저 번호

    @Column(nullable = false)
    private String title; // 플레이리스트 제목

    @Column(nullable = false)
    private Integer flag; // 삭제 여부 (1:정상, 2:삭제)

    @JsonBackReference
    @OneToMany(mappedBy = "playlist", fetch = FetchType.EAGER)
    private List<PlaylistItem> playlistItems = new ArrayList<>();

    public Playlist() {
        this.flag = 1;
    }
}
