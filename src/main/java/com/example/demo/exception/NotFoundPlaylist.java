package com.example.demo.exception;

public class NotFoundPlaylist extends RuntimeException {
    public NotFoundPlaylist() {
        super("플레이리스트 정보를 찾을 수 없습니다.");
    }
}
