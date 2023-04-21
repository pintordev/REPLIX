# 현재 세션에서 `ONLY_FULL_GROUP_BY` 모드 끄기
## 영구적으로 설정되는 것은 아닙니다.
SET SESSION sql_mode = (SELECT REPLACE(@@sql_mode, 'ONLY_FULL_GROUP_BY', ''));
SET sql_mode = (SELECT REPLACE(@@sql_mode, 'ONLY_FULL_GROUP_BY', ''));

# REPLIX DB 생성
DROP DATABASE IF EXISTS REPLIX;
CREATE DATABASE REPLIX;

# REPLIX DB 사용
USE REPLIX;

# 사용자 테이블 생성
CREATE TABLE `user` (
`id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
`loginId` VARCHAR(100) NOT NULL UNIQUE,
`loginPw` VARCHAR(100) NOT NULL,
`name` VARCHAR(100) NOT NULL,
`birthDate` VARCHAR(100) NOT NULL,
`gender` VARCHAR(100) NOT NULL,
`email` VARCHAR(100) NOT NULL,
regDate DATETIME NOT NULL,
updateDate DATETIME NOT NULL
);

# 컨텐츠 테이블 생성
CREATE TABLE `content` (
`id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(100) NOT NULL,
`releaseDate` VARCHAR(100) NOT NULL,
`productionCompany` VARCHAR(100) NOT NULL,
`director` VARCHAR(100) NOT NULL,
`plot` TEXT NOT NULL
);

# 리뷰 테이블 생성
CREATE TABLE `review` (
`id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
`comment` TEXT NOT NULL,
`score` INT UNSIGNED NOT NULL,
`replayFlag` TINYINT(1) NOT NULL,
regDate DATETIME NOT NULL,
updateDate DATETIME NOT NULL,
`userId` INT UNSIGNED NOT NULL,
`contentId` INT UNSIGNED NOT NULL,
FOREIGN KEY (`userId`) REFERENCES `user`(`id`),
FOREIGN KEY (`contentId`) REFERENCES `content`(`id`)
);

# 리뷰 평가 테이블 생성
CREATE TABLE `reviewEvaluation` (
`id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
`likeDislike` TINYINT(1) NOT NULL,
regDate DATETIME NOT NULL,
updateDate DATETIME NOT NULL,
`userId` INT UNSIGNED NOT NULL,
`reviewId` INT UNSIGNED NOT NULL,
FOREIGN KEY (`userId`) REFERENCES `user`(`id`),
FOREIGN KEY (`reviewId`) REFERENCES `review`(`id`)
);

# 장르 테이블 생성
CREATE TABLE `genre` (
`id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(100) NOT NULL
);

# 컨텐츠 장르 테이블 생성
CREATE TABLE `contentGenre` (
`id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
`contentId` INT UNSIGNED NOT NULL,
`genreId` INT UNSIGNED NOT NULL,
FOREIGN KEY (`contentId`) REFERENCES `content`(`id`),
FOREIGN KEY (`genreId`) REFERENCES `genre`(`id`)
);

# 선호 장르 테이블 생성
CREATE TABLE `favoriteGenre` (
`id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
`userId` INT UNSIGNED NOT NULL,
`genreId` INT UNSIGNED NOT NULL,
FOREIGN KEY (`userId`) REFERENCES `user`(`id`),
FOREIGN KEY (`genreId`) REFERENCES `genre`(`id`)
);

# 좋아요한 컨텐츠 테이블 생성
CREATE TABLE `likeContent` (
`id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
`userId` INT UNSIGNED NOT NULL,
`contentId` INT UNSIGNED NOT NULL,
FOREIGN KEY (`userId`) REFERENCES `user`(`id`),
FOREIGN KEY (`contentId`) REFERENCES `content`(`id`)
);

# 찜한 컨텐츠 테이블 생성
CREATE TABLE `dibsContent` (
`id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
`userId` INT UNSIGNED NOT NULL,
`contentId` INT UNSIGNED NOT NULL,
FOREIGN KEY (`userId`) REFERENCES `user`(`id`),
FOREIGN KEY (`contentId`) REFERENCES `content`(`id`)
);

# OTT 테이블 생성
CREATE TABLE `ott` (
`id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(100) NOT NULL
);

# 서비스 OTT 테이블 생성
CREATE TABLE `serviceOtt` (
`id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
`contentId` INT UNSIGNED NOT NULL,
`ottId` INT UNSIGNED NOT NULL,
FOREIGN KEY (`contentId`) REFERENCES `content`(`id`),
FOREIGN KEY (`ottId`) REFERENCES `ott`(`id`)
);

# 배우 테이블 생성
CREATE TABLE `actor` (
`id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
`name` VARCHAR(100) NOT NULL
);

# 출연진 테이블 생성
CREATE TABLE `cast` (
`id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
`contentId` INT UNSIGNED NOT NULL,
`actorId` INT UNSIGNED NOT NULL,
FOREIGN KEY (`contentId`) REFERENCES `content`(`id`),
FOREIGN KEY (`actorId`) REFERENCES `actor`(`id`)
);

# 각 테이블 구조 검사
DESC `user`;
DESC `content`;
DESC `review`;
DESC `reviewEvaluation`;
DESC `genre`;
DESC `contentGenre`;
DESC `favoriteGenre`;
DESC `likeContent`;
DESC `dibsContent`;
DESC `ott`;
DESC `serviceOtt`;
DESC `actor`;
DESC `cast`;

# 테이블 생성 여부 검사
SHOW TABLES;

# 테스트 데이터 삽입
# 유저 데이터 삽입
INSERT INTO `user`
SET `loginId` = 'daeun1214',
`loginPw` = 'qwerty',
`name` = '김다은',
`birthDate` = '1999-12-14',
`gender` = '여',
`email` = 'unknown@replix.io',
regDate = NOW(),
updateDate = NOW();

INSERT INTO `user`
SET `loginId` = 'pintor.dev',
`loginPw` = 'qwerty',
`name` = '김호현',
`birthDate` = '1993-11-11',
`gender` = '남',
`email` = 'unknown@replix.io',
regDate = NOW(),
updateDate = NOW();

INSERT INTO `user`
SET `loginId` = 'tjqls0929',
`loginPw` = 'qwerty',
`name` = '서빈',
`birthDate` = '1998-09-29',
`gender` = '남',
`email` = 'unknown@replix.io',
regDate = NOW(),
updateDate = NOW();

INSERT INTO `user`
SET `loginId` = 'sjh1501',
`loginPw` = 'qwerty',
`name` = '송주현',
`birthDate` = '1989-08-04',
`gender` = '여',
`email` = 'unknown@replix.io',
regDate = NOW(),
updateDate = NOW();

SELECT * FROM `user`;

# 컨텐츠 데이터
INSERT INTO `content`
SET `name` = '스즈메의 문단속',
`releaseDate` = '2023-03-08',
`productionCompany` = '코믹스 웨이브 필름',
`director` = '토쿠나가 토모히로',
`plot` = '문의 건너편에는, 모든 시간이 있었다';

INSERT INTO `content`
SET `name` = '가디언즈 오브 갤럭시: Volume 3',
`releaseDate` = '2023-05-03',
`productionCompany` = '마블 스튜디오',
`director` = '제임스 건',
`plot` = '이 느낌 그대로, 다시 한번 볼륨 업!';

INSERT INTO `content`
SET `name` = '비긴 어게인',
`releaseDate` = '2014-08-13',
`productionCompany` = '시카 픽처스',
`director` = '존 카니',
`plot` = '다시 시작해, 너를 빛나게 할 노래를!';

INSERT INTO `content`
SET `name` = '인터스텔라',
`releaseDate` = '2014-11-06',
`productionCompany` = '레전더리 픽처스',
`director` = '크리스토퍼 놀란',
`plot` = '우린 답을 찾을 것이다. 늘 그랬듯이.';

INSERT INTO `content`
SET `name` = '극한직업',
`releaseDate` = '2019-01-23',
`productionCompany` = '어바웃필름',
`director` = '이병헌',
`plot` = '낮에는 치킨장사! 밤에는 잠복근무! 지금까지 이런 수사는 없었다!';

SELECT * FROM `content`;

# 장르 데이터
INSERT INTO `genre`
SET `name` = '드라마';

INSERT INTO `genre`
SET `name` = '로맨스';

INSERT INTO `genre`
SET `name` = '멜로';

INSERT INTO `genre`
SET `name` = '공포';

INSERT INTO `genre`
SET `name` = '스릴러';

INSERT INTO `genre`
SET `name` = '코미디';

INSERT INTO `genre`
SET `name` = '애니메이션';

INSERT INTO `genre`
SET `name` = 'SF/판타지';

SELECT * FROM `genre`;

# 컨텐츠 장르 데이터
INSERT INTO `contentGenre`
SET `contentId` = 1,
`genreId` = 7;

INSERT INTO `contentGenre`
SET `contentId` = 2,
`genreId` = 1;

INSERT INTO `contentGenre`
SET `contentId` = 2,
`genreId` = 6;

INSERT INTO `contentGenre`
SET `contentId` = 2,
`genreId` = 8;

INSERT INTO `contentGenre`
SET `contentId` = 3,
`genreId` = 1;

INSERT INTO `contentGenre`
SET `contentId` = 3,
`genreId` = 3;

INSERT INTO `contentGenre`
SET `contentId` = 3,
`genreId` = 6;

INSERT INTO `contentGenre`
SET `contentId` = 4,
`genreId` = 1;

INSERT INTO `contentGenre`
SET `contentId` = 4,
`genreId` = 8;

INSERT INTO `contentGenre`
SET `contentId` = 5,
`genreId` = 5;

INSERT INTO `contentGenre`
SET `contentId` = 5,
`genreId` = 6;


SELECT * FROM `contentGenre`;

SELECT A.`id`, A.`name`, A.`releaseDate`, A.`productionCompany`, A.`director`, A.`plot`,
GROUP_CONCAT(DISTINCT C.`name` ORDER BY C.`id` DESC SEPARATOR ', ') AS 'genre'
FROM `content` AS A
INNER JOIN contentGenre AS B
ON A.`id` = B.`contentId`
INNER JOIN `genre` AS C
ON B.`genreId` = C.`id`
GROUP BY A.`id`;