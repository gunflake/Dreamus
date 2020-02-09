package com.example.demo.album;

import com.example.demo.model.SaveAlbum;
import com.example.demo.model.SaveSong;
import com.example.demo.model.SearchAlbum;
import com.example.demo.model.SearchSong;
import com.example.demo.song.Song;
import com.example.demo.song.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final SongRepository songRepository;
    private final AlbumLocaleRepository albumLocaleRepository;


    /**
     *
     * @param albums Entity Album 객체
     * @return SearchAlbum 객체로 응답
     */
    public List<SearchAlbum> setSearchAlbum(List<Album> albums){
        List<SearchAlbum> resultAlbums = new ArrayList<>();
        for(Album album: albums){
            SearchAlbum searchAlbum = new SearchAlbum();
            searchAlbum.setTitle(album.getTitle());
            searchAlbum.setId(album.getAlbumNo());
            searchAlbum.setSongs(setSearchSong(album.getSongs()));
            resultAlbums.add(searchAlbum);
        }
        return resultAlbums;
    }

    /**
     *
     * @param songs Entity Song 객체
     * @return SearchSong 객체로 응답
     */
    public List<SearchSong> setSearchSong(List<Song> songs){
        List<SearchSong> resultSongs = new ArrayList<>();
        for(Song song: songs){
            SearchSong searchSong = new SearchSong();
            searchSong.setId(song.getSongNo());
            searchSong.setLength(song.getLength());
            searchSong.setTrack(song.getTrack());
            searchSong.setTitle(song.getTitle());
            resultSongs.add(searchSong);
        }
        return resultSongs;
    }

    public void saveAlbumAndSong(List<SaveAlbum> saveAlbumList){
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
    }
}
