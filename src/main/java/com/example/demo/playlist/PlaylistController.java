package com.example.demo.playlist;

import com.example.demo.album.*;
import com.example.demo.exception.NotFoundPlaylist;
import com.example.demo.exception.ResponseError;
import com.example.demo.model.ResponsePlaylist;
import com.example.demo.model.SavePlaylist;
import com.example.demo.model.SearchSong;
import com.example.demo.song.Song;
import com.example.demo.song.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
public class PlaylistController {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistItemRepository playlistItemRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private AlbumService albumService;

    @PostMapping("/playlists")
    public ResponseEntity createPlaylist(@RequestBody Playlist playlist, HttpServletRequest request){
        Playlist save = playlistRepository.save(playlist);

        return created(ServletUriComponentsBuilder
                .fromContextPath(request)
                .path("/playlists/{playlistNo}")
                .buildAndExpand(save.getPlaylistNo())
                .toUri())
                .build();
    }

    @PostMapping("/playlists/{playlistNo}")
    public ResponseEntity addItemInPlayList(@PathVariable("playlistNo") long playlistNo, @RequestBody SavePlaylist playlist, HttpServletRequest request){

        Playlist findPlaylist = playlistRepository.findById(playlistNo).orElseThrow(NotFoundPlaylist::new);

        if(!findPlaylist.getUserNo().equals(playlist.getUserNo())){
            return badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST, "플레이리스트 유저 정보랑 요청한 유저 정보가 같지 않습니다. 플레이리스트 번호를 확인해주세요"));
        }

        if(playlist.getAlbums().size() == 0 && playlist.getSongs().size() == 0){
            return badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST, "플레이리스트에 추가해야할 앨범 혹은 곡이 하나 이상 이어야합니다."));
        }

        List<Song> songs = new ArrayList<>();

        if(playlist.getSongs().size() > 0)
            songs = songRepository.findAllById(playlist.getSongs());


        if(playlist.getAlbums().size() > 0){
            List<Album> albums = albumRepository.findAllById(playlist.getAlbums());

            for(Album album : albums){
                songs.addAll(album.getSongs());
            }
        }

        List<PlaylistItem> playlistItems = new ArrayList<>();

        for(Song song : songs){
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
                .build();
    }

    @GetMapping("/playlists/{playlistNo}")
    public ResponseEntity<ResponsePlaylist> findPlaylist(@PathVariable("playlistNo") Long playlistNo){
        List<Song> songs = new ArrayList<>();

        Playlist playlist = playlistRepository.findById(playlistNo).orElseThrow(NotFoundPlaylist::new);

        List<PlaylistItem> findPlaylists = playlistItemRepository.findAllByPlaylist(playlist);

        for(PlaylistItem playlistItem : findPlaylists){
            songs.add(playlistItem.getSong());
        }

        List<SearchSong> searchSongs = albumService.setSearchSong(songs);

        ResponsePlaylist responsePlaylist = new ResponsePlaylist();
        responsePlaylist.setTitle(playlist.getTitle());
        responsePlaylist.setUserNo(playlist.getUserNo());
        responsePlaylist.setCount(searchSongs.size());
        responsePlaylist.setSongs(searchSongs);

        return ok(responsePlaylist);
    }

    @DeleteMapping("/playlists/{playlistNo}")
    public ResponseEntity deletePlaylist(@PathVariable("playlistNo") Long playlistNo, @RequestParam("userNo") Long userNo){

        Playlist playlist = playlistRepository.findById(playlistNo).orElseThrow(NotFoundPlaylist::new);

        // todo: UserNo랑 PlayListNo의 UserNo 비교
        if(!playlist.getUserNo().equals(userNo)){
            return badRequest().body(new ResponseError(HttpStatus.BAD_REQUEST, "플레이리스트 유저 정보랑 요청한 유저 정보가 같지 않습니다. 플레이리스트 번호를 확인해주세요"));
        }

        playlistRepository.delete(playlist);

        return noContent().build();
    }

}
