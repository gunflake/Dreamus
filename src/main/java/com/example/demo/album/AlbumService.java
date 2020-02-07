package com.example.demo.album;

import com.example.demo.model.SearchAlbum;
import com.example.demo.model.SearchSong;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlbumService {

    public List<SearchAlbum> setSearchAlbum(List<Album> albums){
        List<SearchAlbum> resultAlbums = new ArrayList<>();
        for(Album album: albums){
            SearchAlbum searchAlbum = new SearchAlbum();
            searchAlbum.setTitle(album.getTitle());
            searchAlbum.setId(album.getId());
            searchAlbum.setSongs(setSearchSong(album.getSongs()));
            resultAlbums.add(searchAlbum);
        }
        return resultAlbums;
    }

    public List<SearchSong> setSearchSong(List<Song> songs){
        List<SearchSong> resultSongs = new ArrayList<>();
        for(Song song: songs){
            SearchSong searchSong = new SearchSong();
            searchSong.setId(song.getId());
            searchSong.setLength(song.getLength());
            searchSong.setTrack(song.getTrack());
            searchSong.setTitle(song.getTitle());
            resultSongs.add(searchSong);
        }
        return resultSongs;
    }
}
