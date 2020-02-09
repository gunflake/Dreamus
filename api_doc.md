# Playlist API

## [3.1] Playlist 생성 API
```
POST /playlists
```
사용자가 Playlist를 생성하는 API 입니다.

**Parameter(JSON)**

Parameter | Type | Description
--------- | ---- | -----------
userNo    | long  | (필수) PlayList를 생성할 사용자번호
title     | string | (필수) 생성할 PlayList 제목 (예: 재즈모음)

**Response Header**
```
Status : 201
Header : Location = "https://SERVER_URL/playlists/{playlistNo}  (생성된 playlistNo)
```

**Response Body**
```
응답 값 없음
```



## [3.2] Playlist 노래, 앨범 추가 API
```
POST /playlists/{playlistNo}
```
사용자가 Playlist에 노래 혹은 앨범을 추가하는 API 입니다.

**Parameter(JSON)**

Parameter | Type | Description
--------- | ---- | -----------
userNo    | long  | (필수) Playlist에 노래를 추가할 사용자번호
albums    | long | (선택) Playlist에 추가할 앨범 id 번호
Songs     | long | (선택) Playlist에 추가할 노래 id 번호

albums 혹은 Songs에 둘 중 하나의 값은 필수여야 합니다.  
albums, Songs은 배열안에 해당 앨범or노래 id 번호를 입력해야합니다.(예시: [2,3])

**요청예시**
```
{
	"userNo" : 1,
	"albums": [
		2
		],
	"songs": [
		    32, 33
		]
}
```


**Response Header(Success)**
```
Status : 201
Location = "https://SERVER_URL/playlists/{playlistNo}  (요청한 playlistNo)
```

**Response Body(Success)**
```
{
    "playlistNo": 416,
    "title": "test playlist",
    "count": 11,
    "userNo": 1,
    "songs": [
        {
            "title": "Please Please Me (1963.3) song-1",
            "id": 32,
            "track": 1,
            "length": 224
        },
        {
            "title": "Please Please Me (1963.3) song-2",
            "id": 33,
            "track": 2,
            "length": 454
        },
        ...
    ]
}
```

**Response(fail)**
```
플레이리스트에 담을 곡이 하나도 없을 경우
{
    "status": "BAD_REQUEST",
    "message": "플레이리스트에 추가해야할 앨범 혹은 곡이 하나 이상 이어야합니다."
}

잘못된 플레이리스트 번호를 요청했을 경우
{
    "status": "BAD_REQUEST",
    "message": "해당 플레이리스트 번호를 찾을 수 없습니다."
}

플레이리스트 유저 정보랑 요청 유저 정보가 다를 경우
{
    "status": "BAD_REQUEST",
    "message": "플레이리스트에 추가해야할 앨범 혹은 곡이 하나 이상 이어야합니다."
}
```

## [3.3] Playlist 목록 API
```
GET /playlists
GET /playlists/{playlistNo}
```

**Parameter(JSON)**

Parameter   | Type | Description | 비고
----------- | ---- | ----------- | ---
playlistNo  | long | (선택) 조회할 playlist 번호 | 특정 번호 플레이리스트를 조회하고 싶을 때 입
userNo      | long | (필수) user 번호 | Request Header에 담아서 전송

**Response(Success)**
```
{
    "playlistNo": 416,
    "title": "test playlist",
    "count": 11,
    "userNo": 3,
    "songs": [
        {
            "title": "With The Beatles (1963.11) song-1",
            "id": 45,
            "track": 1,
            "length": 114
        },
        {
            "title": "With The Beatles (1963.11) song-2",
            "id": 46,
            "track": 2,
            "length": 494
        },
        ...
    ]
}
```

**Response(Fail)**
```
플레이리스트 정보가 없을 경우
{
    "status": "BAD_REQUEST",
    "message": "플레이리스트 정보를 찾을 수 없습니다."
}
```

## [3.4] Playlist 삭제 API
```
DELETE /playlists/{playlistNo}
```

**Parameter(JSON)**

Parameter   | Type | Description | 비고 
----------- | ---- | ----------- | ---
playlistNo  | long | (필수) 삭제할 playlist 번호 | 
userNo       | long | (필수) 삭제할 playlist의 유저 정보와 일치 여부 확인 | 해더에 담아서 전송

**Response Header(Success)**
```
Status : 204
```

**Response Body(Success)**
```
응답 값 없음 
```

**Response(Fail)**
```
플레이리스트 정보가 없을 경우
{
    "status": "BAD_REQUEST",
    "message": "플레이리스트 정보를 찾을 수 없습니다."
}

플레이리스트 유저 정보랑 요청 유저 정보가 다를 경우
{
    "status": "BAD_REQUEST",
    "message": "플레이리스트 유저 정보랑 요청한 유저 정보가 같지 않습니다. 플레이리스트 번호를 확인해주세요."
}
```
