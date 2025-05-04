package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.custom.InvalidInputException;

class RoomThemeTest {

    private static final long ID = 1L;
    private static final String NAME = "우테코";
    private static final String DESCRIPTION = "미션 2는 방탈출 사용자 예약";
    private static final String THUMBNAIL = "url";

    @DisplayName("방 테마 생성시, 테마 명이 빈 값이면 예외를 던진다")
    @Test
    void createRoomThemeTest1() {
        // when // then
        assertThatThrownBy(() -> new RoomTheme(ID, null, DESCRIPTION, THUMBNAIL))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("테마 명은 빈 값이 입력될 수 없습니다");
    }

    @DisplayName("방 테마 생성시, 테마 상세 설명이 빈 값이면 예외를 던진다")
    @Test
    void createRoomThemeTest2() {
        // when // then
        assertThatThrownBy(() -> new RoomTheme(ID, NAME, null, THUMBNAIL))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("테마 상세 설명은 빈 값이 입력될 수 없습니다");
    }

    @DisplayName("방 테마 생성시, 테마 상세 설명이 빈 값이면 예외를 던진다")
    @Test
    void createRoomThemeTest3() {
        // when // then
        assertThatThrownBy(() -> new RoomTheme(ID, NAME, DESCRIPTION, null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("썸네일 주소는 빈 값이 입력될 수 없습니다");
    }
}
