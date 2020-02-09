package com.example.demo.exception;

public class NotFoundPlaylist extends RuntimeException {
    public NotFoundPlaylist() {
        super("해당 플레이리스트 번호를 찾을 수 없습니다.");
    }
}
