package roomescape.theme.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.custom.InvalidInputException;

class ThemeTest {

    private static final long ID = 1L;
    private static final String NAME = "우테코";
    private static final String DESCRIPTION = "미션 2는 방탈출 사용자 예약";
    private static final String THUMBNAIL = "url";

    @DisplayName("방 테마 생성시, 테마 명이 빈 값이면 예외를 던진다")
    @Test
    void createThemeTest_WhenNameIsNull() {
        // given // when // then
        assertThatThrownBy(() -> new Theme(ID, null, DESCRIPTION, THUMBNAIL))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("테마 명은 빈 값이 입력될 수 없습니다");
    }

    @DisplayName("방 테마 생성시, 테마 상세 설명이 빈 값이면 예외를 던진다")
    @Test
    void createThemeTest_WhenDescriptionIsNull() {
        // given // when // then
        assertThatThrownBy(() -> new Theme(ID, NAME, null, THUMBNAIL))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("테마 상세 설명은 빈 값이 입력될 수 없습니다");
    }

    @DisplayName("방 테마 생성시, 테마 상세 설명이 빈 값이면 예외를 던진다")
    @Test
    void createThemeTest_WhenThumbnailIsNull() {
        // given // when // then
        assertThatThrownBy(() -> new Theme(ID, NAME, DESCRIPTION, null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("썸네일 주소는 빈 값이 입력될 수 없습니다");
    }
}
