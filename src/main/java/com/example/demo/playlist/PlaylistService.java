package com.example.demo.playlist;

import com.example.demo.album.AlbumService;
import com.example.demo.exception.NotFoundPlaylist;
import com.example.demo.model.ResponsePlaylist;
import com.example.demo.model.SearchSong;
import com.example.demo.song.Song;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final AlbumService albumService;

    public ResponsePlaylist findPlaylist(Long userNo, Long playlistNo){
        List<Song> songs = new ArrayList<>();

        Playlist playlist = playlistRepository.findByUserNoAndPlaylistNoAndFlag(userNo, playlistNo, 1).orElseThrow(NotFoundPlaylist::new);

        for (PlaylistItem playlistItem : playlist.getPlaylistItems()) {
            songs.add(playlistItem.getSong());
        }

        List<SearchSong> searchSongs = albumService.setSearchSong(songs);
        ResponsePlaylist responsePlaylist = new ResponsePlaylist();
        responsePlaylist.setPlaylistNo(playlistNo);
        responsePlaylist.setTitle(playlist.getTitle());
        responsePlaylist.setUserNo(playlist.getUserNo());
        responsePlaylist.setCount(searchSongs.size());
        responsePlaylist.setSongs(searchSongs);

        return responsePlaylist;
    }
}
