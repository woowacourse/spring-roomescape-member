-- 외래키 제약조건 무효화
SET REFERENTIAL_INTEGRITY FALSE;

-- 기존 테이블이 있다면 깔끔하게 날림
DROP TABLE IF EXISTS reservation CASCADE;
DROP TABLE IF EXISTS theme CASCADE;
DROP TABLE IF EXISTS time_slot CASCADE;

-- 메인 스키마와 동일한 형태(제약조건 포함)로 확정적 생성
CREATE TABLE theme (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(250) NOT NULL,
                       description VARCHAR(250) NOT NULL,
                       thumbnail_url VARCHAR(250) NOT NULL
);

CREATE TABLE time_slot (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           start_at TIME NOT NULL
);

CREATE TABLE reservation (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             date DATE NOT NULL,
                             time_id BIGINT NOT NULL,
                             theme_id BIGINT NOT NULL,
                             FOREIGN KEY (time_id) REFERENCES time_slot (id),
                             FOREIGN KEY (theme_id) REFERENCES theme (id),
                             CONSTRAINT uk_reservation_date_time_theme UNIQUE (date, time_id, theme_id)
);

-- 매 테스트마다 식별자가 1부터 시작하도록 격리
TRUNCATE TABLE reservation RESTART IDENTITY;
TRUNCATE TABLE theme RESTART IDENTITY;
TRUNCATE TABLE time_slot RESTART IDENTITY;

-- 외래키 제약조건 복구
SET REFERENTIAL_INTEGRITY TRUE;
