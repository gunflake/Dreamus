package com.example.demo.album;

import com.example.demo.model.*;
import com.example.demo.song.Song;
import com.example.demo.song.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;
    private final AlbumLocaleRepository albumLocaleRepository;
    private final AlbumService albumService;

    /**
     *
     * @param title 검색어
     * @param locale 검색을 요청하는 사용자의 지역
     * @return 검색된 노래 및 앨범 정보
     */
    @GetMapping("/search")
    public ResponseEntity<ResponseSearchData> searchAlbumAndSong(@RequestParam("title") String title, @RequestParam("locale") String locale) {

        // Todo : parameter 없을 떄 에러 나오는거 처리하는거 추가

        List<Song> songs;
        List<Album> albums;

        // Album 검색
        if (locale.toLowerCase().equals("all")) {
            albums = albumRepository.findAllByTitleIgnoreCaseContaining(title);
        }else{
            albums = albumRepository.findAllByTitleIgnoreCaseContainingAndAlbumLocalesContaining(title, locale);
        }

        // Song 검색
        songs = songRepository.findAllByTitleIgnoreCaseContaining(title);

        List<SearchAlbum> searchAlbums = albumService.setSearchAlbum(albums);
        List<SearchSong> searchSongs = albumService.setSearchSong(songs);

        // TODO: Song에서는 Album josn 데이터 삭제하고, Album에서는 Song 데이터 추가하기


        ResponseSearchData responseSearchData = new ResponseSearchData(searchAlbums, searchSongs);

        return ok(responseSearchData);
    }

    /**
     *
     * @param locale 검색을 요청하는 사용자의 지역
     * @param page 요청할 page
     * @return 페이지에 따른 앨범 정보
     */
    @GetMapping("/albums")
    public ResponseEntity<ResponseAlbum> getAlbums(@RequestParam("locale") String locale, @RequestParam("page") int page){

        page -= 1;

        Page<Album> findAlbums;
        ResponseAlbum responseAlbum = new ResponseAlbum();
        responseAlbum.setStatusCode(200);

        // Page Setting
        SearchPage searchPage = new SearchPage();
        String url = "https://SERVER_URL/api/albums?page=";

        if (locale.equals("all")) {
            findAlbums = albumRepository.findAll(PageRequest.of(page, 10));
        }else{
            findAlbums = albumRepository.findAllByAlbumLocalesContaining(locale, PageRequest.of(page, 10));
        }

        searchPage.setFirst(page <= 0 || findAlbums.getTotalElements() == 0 ? null : url+1);
        searchPage.setPrev(page <= 0 || page > findAlbums.getTotalPages() ? null : url+page);
        searchPage.setLast(page >= findAlbums.getTotalPages()-1 ? null : url+findAlbums.getTotalPages());
        searchPage.setNext(page >= findAlbums.getTotalPages()-1 || page < 0 ? null : url+(page+2));

        responseAlbum.setPages(searchPage);

        // Album Setting
        List<Album> albums = findAlbums.get().collect(Collectors.toList());

        responseAlbum.setAlbums(albumService.setSearchAlbum(albums));

        return ok(responseAlbum);
    }

    /**
     *
     * @param saveAlbumList 저장할 노래 JSON 데이터(제공 데이터 예시 포맷으로 전송)
     * @return 201 상태값 응답 및 No content Body
     */
    @PostMapping("/albums")
    public ResponseEntity saveAlbumAndSong(@RequestBody List<SaveAlbum> saveAlbumList) {
        List<Album> albums = new ArrayList<>();
        for (SaveAlbum data : saveAlbumList) {

            //Album
            Album album = new Album();
            album.setTitle(data.getAlbum_title());

            //Song
            List<Song> songs = new ArrayList<>();
            for (SaveSong getSong : data.getSongs()) {
                Song newSong = new Song();
                newSong.setTitle(getSong.getTitle());
                newSong.setLength(getSong.getLength());
                newSong.setTrack(getSong.getTrack());
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
