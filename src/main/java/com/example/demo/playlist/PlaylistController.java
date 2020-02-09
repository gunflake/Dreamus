package com.example.demo.playlist;

import com.example.demo.album.Album;
import com.example.demo.album.AlbumRepository;
import com.example.demo.album.AlbumService;
import com.example.demo.exception.NotFoundPlaylist;
import com.example.demo.exception.ResponseError;
import com.example.demo.model.ResponsePlaylist;
import com.example.demo.model.SavePlaylist;
import com.example.demo.model.SearchSong;
import com.example.demo.song.Song;
import com.example.demo.song.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistRepository playlistRepository;
    private final PlaylistItemRepository playlistItemRepository;
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final AlbumService albumService;
    private final PlaylistService playlistService;

    @PostMapping("/playlists")
    public ResponseEntity createPlaylist(@RequestBody Playlist playlist, HttpServletRequest request) {
        Playlist save = playlistRepository.save(playlist);

        return created(ServletUriComponentsBuilder
                .fromContextPath(request)
                .path("/playlists/{playlistNo}")
                .buildAndExpand(save.getPlaylistNo())
                .toUri())
                .build();
    }

    @PostMapping("/playlists/{playlistNo}")
    public ResponseEntity addItemInPlayList(@PathVariable("playlistNo") long playlistNo, @RequestBody SavePlaylist playlist, HttpServletRequest request) {

        Playlist findPlaylist = playlistRepository.findById(playlistNo).orElseThrow(NotFoundPlaylist::new);

        if (!findPlaylist.getUserNo().equals(playlist.getUserNo())) {
            return badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST, "플레이리스트 유저 정보랑 요청한 유저 정보가 같지 않습니다. 플레이리스트 번호를 확인해주세요"));
        }

        if (playlist.getAlbums().size() == 0 && playlist.getSongs().size() == 0) {
            return badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST, "플레이리스트에 추가해야할 앨범 혹은 곡이 하나 이상 이어야합니다."));
        }

        List<Song> songs = new ArrayList<>();

        if (playlist.getSongs().size() > 0)
            songs = songRepository.findAllById(playlist.getSongs());


        if (playlist.getAlbums().size() > 0) {
            List<Album> albums = albumRepository.findAllById(playlist.getAlbums());

            for (Album album : albums) {
                songs.addAll(album.getSongs());
            }
        }

        List<PlaylistItem> playlistItems = new ArrayList<>();

        for (Song song : songs) {
            PlaylistItem playlistItem = new PlaylistItem();
            playlistItem.setPlaylist(findPlaylist);
            playlistItem.setSong(song);
            playlistItems.add(playlistItem);
        }

        playlistItemRepository.saveAll(playlistItems);

        return created(ServletUriComponentsBuilder
                .fromContextPath(request)
                .path("/playlists/{playlistNo}")
                .buildAndExpand(playlistNo)
                .toUri())
                .body(playlistService.findPlaylist(playlist.getUserNo(), playlistNo));
    }

    @GetMapping("/playlists")
    public ResponseEntity<List<ResponsePlaylist>> findAllPlaylist(@RequestHeader("userNo") Long userNo) {

        List<ResponsePlaylist> responsePlaylists = new ArrayList<>();
        List<Playlist> playlists = playlistRepository.findAllByUserNoAndFlag(userNo, 1);

        for (Playlist playlist : playlists) {
            List<Song> songs = new ArrayList<>();

            for (PlaylistItem playlistItem : playlist.getPlaylistItems()) {
                songs.add(playlistItem.getSong());
            }

            List<SearchSong> searchSongs = albumService.setSearchSong(songs);
            ResponsePlaylist responsePlaylist = new ResponsePlaylist();
            responsePlaylist.setPlaylistNo(playlist.getPlaylistNo());
            responsePlaylist.setTitle(playlist.getTitle());
            responsePlaylist.setUserNo(playlist.getUserNo());
            responsePlaylist.setCount(searchSongs.size());
            responsePlaylist.setSongs(searchSongs);

            responsePlaylists.add(responsePlaylist);
        }

        return ok(responsePlaylists);
    }

    @GetMapping("/playlists/{playlistNo}")
    public ResponseEntity<ResponsePlaylist> findPlaylist(@PathVariable("playlistNo") Long playlistNo, @RequestHeader("userNo") Long userNo) {

        ResponsePlaylist responsePlaylist = playlistService.findPlaylist(userNo, playlistNo);

        return ok(responsePlaylist);
    }

    @DeleteMapping("/playlists/{playlistNo}")
    public ResponseEntity deletePlaylist(@PathVariable("playlistNo") Long playlistNo, @RequestHeader("userNo") Long userNo) {

        Playlist playlist = playlistRepository.findById(playlistNo).orElseThrow(NotFoundPlaylist::new);

        if (!playlist.getUserNo().equals(userNo)) {
            return badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST, "플레이리스트 유저 정보랑 요청한 유저 정보가 같지 않습니다. 플레이리스트 번호를 확인해주세요"));
        }

        playlist.setFlag(2);

        List<PlaylistItem> playlistItems = playlist.getPlaylistItems();
        for(PlaylistItem playlistItem : playlistItems){
            playlistItem.setFlag(2);
        }

        playlistRepository.save(playlist);
        playlistItemRepository.saveAll(playlistItems);

        return noContent().build();
    }

}
