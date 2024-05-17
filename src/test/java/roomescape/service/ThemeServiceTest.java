package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.theme.dto.ThemeCreateRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.global.exception.exceptions.NotExistingEntryException;
import roomescape.theme.application.ThemeService;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Test
    @DisplayName("테마 생성 기능을 확인한다.")
    void checkThemeCreate() {
        //given
        ThemeCreateRequest themeCreateRequest = new ThemeCreateRequest(
                "테마명",
                "테마 설명",
                "테마 이미지"
        );

        //when
        ThemeResponse themeResponse = themeService.create(themeCreateRequest);

        //then
        assertAll(
                () -> assertThat(themeResponse.id()).isEqualTo(1L),
                () -> assertThat(themeResponse.name()).isEqualTo("테마명"),
                () -> assertThat(themeResponse.description()).isEqualTo("테마 설명"),
                () -> assertThat(themeResponse.thumbnail()).isEqualTo("테마 이미지")
        );
    }

    @Test
    @DisplayName("예약 시간 삭제 기능을 확인한다.")
    void checkReservationTimeDelete() {
        //given
        ThemeCreateRequest themeCreateRequest = new ThemeCreateRequest(
                "테마명",
                "테마 설명",
                "테마 이미지"
        );
        ThemeResponse themeResponse = themeService.create(themeCreateRequest);

        //when & then
        assertDoesNotThrow(() -> themeService.delete(themeResponse.id()));
    }

    @Test
    @DisplayName("예약 시간 삭제 기능의 실패를을 확인한다.")
    void checkReservationTimeDeleteFail() {
        //given & when & then
        assertThatThrownBy(() -> themeService.delete(0L))
                .isInstanceOf(NotExistingEntryException.class)
                .hasMessage("삭제할 테마가 존재하지 않습니다");
    }
}
