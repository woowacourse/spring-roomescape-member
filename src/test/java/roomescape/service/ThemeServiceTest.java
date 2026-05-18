package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.RestApiException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationQueryDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.row.ReservationRow;
import roomescape.dao.row.ThemeRow;
import roomescape.dao.row.TimeRow;
import roomescape.dto.request.ThemeRequestDto;
import roomescape.dto.response.ThemeResponseDto;
import roomescape.fixture.FakeDatabase;
import roomescape.fixture.FakeReservationDao;
import roomescape.fixture.FakeReservationQueryDao;
import roomescape.fixture.FakeThemeDao;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ThemeServiceTest {

    private static final LocalDate FIXED_DATE = LocalDate.of(2026, 5, 10);
    private static final Long NOT_EXISTS_ID = Long.MAX_VALUE;

    private ThemeService themeService;
    private ThemeDao themeDao;
    private ReservationDao reservationDao;
    private ReservationQueryDao reservationQueryDao;


    @BeforeEach
    void setUp() {
        FakeDatabase.clearAll();
        themeDao = new FakeThemeDao();
        reservationDao = new FakeReservationDao();
        reservationQueryDao = new FakeReservationQueryDao();
        themeService = new ThemeService(themeDao, reservationDao, reservationQueryDao);
    }

    private ThemeRequestDto requestOf(String name) {
        return new ThemeRequestDto(name, "https://test.com", "테스트용 설명입니다");
    }

    private ThemeResponseDto givenTheme(String name) {
        return themeService.create(requestOf(name));
    }

    private void givenReservationOn(ThemeResponseDto theme, int hour) {
        reservationDao.create(new ReservationRow(
                "달수",
                FIXED_DATE,
                new TimeRow(LocalTime.of(hour, 0)),
                new ThemeRow(theme.id(), theme.name(), theme.thumbnailUrl(), theme.description())));
    }


    @Test
    void 조회하려는_id가_존재하지_않으면_예외_처리한다() {
        assertThatThrownBy(() -> themeService.findById(NOT_EXISTS_ID))
                .isInstanceOf(RestApiException.class);
    }

    @Nested
    @DisplayName("테마를 생성할 때: ")
    class Created {

        @Test
        void 정상_요청이면_테마가_생성된다() {
            ThemeRequestDto request = requestOf("방탈출");
            ThemeResponseDto response = themeService.create(request);

            assertThat(response.id()).isNotNull();
            assertThat(response.name()).isEqualTo("방탈출");
        }

        @Test
        void 동일한_이름의_테마가_이미_있으면_예외_처리한다() {
            String name = "방탈출";
            givenTheme(name);
            ThemeRequestDto request = requestOf(name);

            assertThatThrownBy(() -> themeService.create(request))
                    .isInstanceOf(RestApiException.class);
        }
    }

    @Nested
    @DisplayName("테마를 삭제할 때: ")
    class Delete {

        @Test
        void 정상_요청이면_삭제된다() {
            ThemeResponseDto theme = givenTheme("방탈출");

            themeService.delete(theme.id());

            assertThatThrownBy(() -> themeService.findById(theme.id()))
                    .isInstanceOf(RestApiException.class);
        }

        @Test
        void 삭제하려는_id가_존재하지_않으면_예외_처리한다() {
            assertThatThrownBy(() -> themeService.delete(NOT_EXISTS_ID))
                    .isInstanceOf(RestApiException.class);
        }

        @Test
        void 예약_id가_존재하면_예외를_반환한다() {
            ThemeResponseDto theme = givenTheme("방탈출");
            givenReservationOn(theme, 10);

            assertThatThrownBy(() -> themeService.delete(theme.id()))
                    .isInstanceOf(RestApiException.class);
        }
    }
}
