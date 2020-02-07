package com.example.demo.album;

import com.example.demo.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
public class AlbumController {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AlbumLocaleRepository albumLocaleRepository;

    @Autowired
    private AlbumService albumService;

    @GetMapping("/search")
    public ResponseEntity<ResponseSearchData> searchAlbumAndSong(@RequestParam("title") String title, @RequestParam("locale") String locale) {

        List<Song> songs;
        List<Album> albums;

        // Album 검색
        if (locale.equals("all")) {
            log.info("all");
            albums = albumRepository.findAllByTitleContaining(title);
        }else{
            log.info("others");
            albums = albumRepository.findAllByTitleContainingAndAlbumLocalesContaining("%" + title + "%", locale);
        }

        // Song 검색
        songs = songRepository.findAllByTitleContaining(title);

        List<SearchAlbum> searchAlbums = albumService.setSearchAlbum(albums);
        List<SearchSong> searchSongs = albumService.setSearchSong(songs);

        // TODO: Song에서는 Album josn 데이터 삭제하고, Album에서는 Song 데이터 추가하기


        ResponseSearchData responseSearchData = new ResponseSearchData(searchAlbums, searchSongs);

        return ok(responseSearchData);
    }

    @GetMapping("/albums")
    public ResponseEntity<ResponseAlbums> getAlbums(@RequestParam("locale") String locale, @RequestParam("page") int page){

        page -= 1;

        Page<Album> findAlbums;
        ResponseAlbums responseAlbums = new ResponseAlbums();
        responseAlbums.setStatusCode(200);

        // Page Setting
        SearchPage searchPage = new SearchPage();
        String url = "https://SERVER_URL/api/albums?page=";

        if (locale.equals("all")) {
            findAlbums = albumRepository.findAll(PageRequest.of(page, 10));
        }else{
            findAlbums = albumRepository.findAllByAlbumLocalesContaining(locale, PageRequest.of(page, 10));
        }

        log.info("getSize: "+findAlbums.getTotalElements());

        searchPage.setFirst(page <= 0 || findAlbums.getTotalElements() == 0 ? null : url+1);
        searchPage.setPrev(page <= 0 || page > findAlbums.getTotalPages() ? null : url+page);
        searchPage.setLast(page >= findAlbums.getTotalPages()-1 ? null : url+findAlbums.getTotalPages());
        searchPage.setNext(page >= findAlbums.getTotalPages()-1 || page < 0 ? null : url+(page+2));

        responseAlbums.setPages(searchPage);

        // Album Setting
        List<Album> albums = findAlbums.get().collect(Collectors.toList());

        responseAlbums.setAlbums(albumService.setSearchAlbum(albums));

        return ok(responseAlbums);
    }

    @PostMapping("/albums")
    public ResponseEntity saveAlbumAndSong(@RequestBody List<SaveAlbum> saveAlbumList) {
        List<Album> albums = new ArrayList<>();
        for (SaveAlbum data : saveAlbumList) {

            //Album
            Album album = new Album();
            album.setTitle(data.getAlbum_title());

            //Song
            List<Song> songs = new ArrayList<>();
            for (SaveAlbum.Song getSong : data.getSongs()) {
                Song newSong = new Song();
                newSong.setTitle(getSong.getTitle());
                newSong.setLength((short) getSong.getLength());
                newSong.setTrack((short) getSong.getTrack());
                newSong.setAlbum(album);
                songs.add(newSong);
            }

            //Locale
            List<AlbumLocale> locales = new ArrayList<>();
            for (String locale : data.getLocales()) {
                AlbumLocale newAlbumLocale = new AlbumLocale();
                newAlbumLocale.setName(locale);
                newAlbumLocale.setAlbum(album);
                locales.add(newAlbumLocale);
            }

            album.setSongs(songs);
            album.setAlbumLocales(locales);
            albums.add(album);
        }

        albumRepository.saveAll(albums);

        for (int i = 0; i < albums.size(); i++) {
            songRepository.saveAll(albums.get(i).getSongs());
            albumLocaleRepository.saveAll(albums.get(i).getAlbumLocales());
        }


        return created(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("")
                        .buildAndExpand()
                        .toUri())
                .build();
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Happy Coding!";
    }
}
