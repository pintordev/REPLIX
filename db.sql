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
`score` DOUBLE UNSIGNED NOT NULL,
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

INSERT INTO `likeContent`
SET `userId` = 1,
`contentId` = 1;

INSERT INTO `likeContent`
SET `userId` = 2,
`contentId` = 1;

INSERT INTO `likeContent`
SET `userId` = 3,
`contentId` = 1;

SELECT * FROM `likeContent`;

INSERT INTO `ott`
SET `name` = '넷플릭스';

INSERT INTO `ott`
SET `name` = '디즈니플러스';

INSERT INTO `ott`
SET `name` = '티빙';

INSERT INTO `ott`
SET `name` = '웨이브';

INSERT INTO `ott`
SET `name` = '왓챠';

INSERT INTO `ott`
SET `name` = '쿠팡플레이';

SELECT * FROM `ott`;

INSERT INTO `actor`
SET `name` = '하라 나노카';

INSERT INTO `actor`
SET `name` = '마츠무라 호쿠토';

INSERT INTO `actor`
SET `name` = '크리스 프랫';

INSERT INTO `actor`
SET `name` = '조 샐다나';

INSERT INTO `actor`
SET `name` = '키이라 나이틀리';

INSERT INTO `actor`
SET `name` = '마크 러팔로';

INSERT INTO `actor`
SET `name` = '매튜 매커너히';

INSERT INTO `actor`
SET `name` = '앤 해서웨이';

INSERT INTO `actor`
SET `name` = '류승룡';

INSERT INTO `actor`
SET `name` = '이하늬';

SELECT * FROM `actor`;

SELECT * FROM `content`;

INSERT INTO `cast`
SET `contentId` = 1,
`actorId` = 1;

INSERT INTO `cast`
SET `contentId` = 1,
`actorId` = 2;

INSERT INTO `cast`
SET `contentId` = 2,
`actorId` = 3;

INSERT INTO `cast`
SET `contentId` = 2,
`actorId` = 4;

INSERT INTO `cast`
SET `contentId` = 3,
`actorId` = 5;

INSERT INTO `cast`
SET `contentId` = 3,
`actorId` = 6;

INSERT INTO `cast`
SET `contentId` = 4,
`actorId` = 7;

INSERT INTO `cast`
SET `contentId` = 4,
`actorId` = 8;

INSERT INTO `cast`
SET `contentId` = 5,
`actorId` = 9;

INSERT INTO `cast`
SET `contentId` = 5,
`actorId` = 10;

SELECT A.`id`, A.`name`, A.`releaseDate`, A.`productionCompany`, A.`director`, A.`plot`,
GROUP_CONCAT(DISTINCT C.`name` ORDER BY C.`id` DESC SEPARATOR ', ') AS 'cast',
GROUP_CONCAT(DISTINCT E.`name` ORDER BY E.`id` DESC SEPARATOR ', ') AS 'genre',
IFNULL(GROUP_CONCAT(DISTINCT G.`name` ORDER BY G.`id` DESC SEPARATOR ', '), '없음') AS 'ott'
FROM `content` AS A
INNER JOIN `cast` as B
ON A.`id` = B.`contentId`
INNER JOIN `actor` as C
ON B.`actorId` = C.`id`
INNER JOIN `contentGenre` AS D
ON A.`id` = D.`contentId`
INNER JOIN `genre` AS E
ON D.`genreId` = E.`id`
LEFT JOIN `serviceOtt` as F
ON A.`id` = F.`contentId`
LEFT JOIN ott as G
ON F.`ottId` = G.`id`
WHERE A.`name` Like '%가디%'
GROUP BY A.`id`;

SELECT COUNT(B.`id`) as 'like'
FROM `content` AS A
LEFT JOIN `likeContent` as B
ON A.`id` = B.`contentId`
WHERE A.`name` Like '%가%'
GROUP BY A.`id`;


SELECT COUNT(C.`id`) as 'dibs'
FROM `content` AS A
LEFT JOIN `dibsContent` as C
ON A.`id` = C.`contentId`
WHERE A.`name` Like '%가디%'
GROUP BY A.`id`;

SELECT COUNT(D.`id`) as 'review'
FROM `content` AS A
LEFT JOIN `review` as D
ON A.`id` = D.`contentId`
WHERE A.`name` Like '%가%'
GROUP BY A.`id`;

SELECT * from likeContent;

SELECT COUNT(*)
FROM `likeContent` as A
WHERE A.`userId` = 1 && A.`contentId` = 2;

SELECT COUNT(*)
FROM `dibsContent` as A
INNER JOIN `user` as B
ON A.`userId` = B.`id`
INNER JOIN `content` as C
ON A.`contentId` = C.`id`
WHERE B.`id` = 1 & C.`id` = 2;

DELETE FROM `likeContent`
WHERE `userId` = 1 & `contentId` = 1;

SELECT * FROM `likeContent`;
SELECT * FROM `dibsContent`;

SELECT A.`id`, A.`comment`, A.`score`, A.`replayFlag`, A.regDate, A.updateDate, B.`name` AS 'userName', C.`name` AS 'contentName'
FROM `review` as A
INNER JOIN `user` as B
ON A.`userId` = B.`id`
INNER JOIN `content` as C
ON A.`contentId` = C.`id`
WHERE A.`userId` = 1 && A.`contentId` = 1
ORDER BY regDate DESC
LIMIT 1, 5;

SELECT IFNULL(AVG(B.`score`), 0) AS 'score'
FROM `content` AS A
LEFT JOIN `review` AS B
ON A.`id` = B.`contentId`
WHERE A.`name` LIKE CONCAT('%', '스', '%')
GROUP BY A.`id`;

desc `review`;
desc `likeContent`;
desc `content`;

SELECT * FROM `likeContent`;

SELECT B.`name`
FROM `likeContent` AS A
INNER JOIN `content` AS B
ON A.`contentId` = B.`id`
WHERE A.`userId` = 1;