package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.DatabaseCleaner;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.exception.code.ThemeErrorCode;
import roomescape.exception.domain.ThemeException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        databaseCleaner.clean();
    }

    @Test
    void 테마를_생성할_수_있다() {
        // given
        ThemeRequest request = new ThemeRequest(
                "테마1",
                "설명",
                "https://dsf.sdaf"
        );

        // when
        ThemeResponse response = themeService.create(request);

        // then
        assertThat(response)
                .extracting(
                        ThemeResponse::name,
                        ThemeResponse::description,
                        ThemeResponse::thumbnail
                )
                .containsExactly(
                        request.name(),
                        request.description(),
                        request.thumbnail()
                );
    }

    @Test
    void 이름이_같은_테마_생성시_예외가_발생한다() {
        // given
        ThemeRequest request = new ThemeRequest(
                "테마1",
                "설명",
                "https://dsf.sdaf"
        );
        themeService.create(request);

        // when & then
        assertThatThrownBy(() -> themeService.create(request))
                .isInstanceOf(ThemeException.class)
                .hasMessage(ThemeErrorCode.THEME_ALREADY_EXISTS.getMessage());

    }


    @Test
    void 테마를_삭제할_수_있다() {
        // given
        ThemeResponse response = themeService.create(new ThemeRequest(
                "테마1",
                "설명",
                "https://dsf.sdaf"
        ));
        int beforeSize = themeService.getThemes().size();

        // when
        themeService.delete(response.id());

        // then
        List<ThemeResponse> themes = themeService.getThemes();
        assertAll(
                () -> assertThat(themes).hasSize(beforeSize - 1),
                () -> assertThat(themes)
                        .extracting(ThemeResponse::id)
                        .doesNotContain(response.id())
        );
    }

    @Test
    void 존재하지_않는_테마_삭제시_예외가_발생한다() {
        // given
        long invalidThemeId = 0L;

        // when & then
        assertThatThrownBy(() -> themeService.delete(invalidThemeId))
                .isInstanceOf(ThemeException.class)
                .hasMessage(ThemeErrorCode.THEME_NOT_FOUND.getMessage());
    }

    @Test
    void 테마_삭제시_관련_예약이_존재하면_예외가_발생한다() {
        // given
        Theme theme = saveTheme("테마1");
        ReservationTime reservationTime = saveReservationTime(LocalTime.of(10, 0));

        Reservation reservation = Reservation.createWithoutId(
                "예약1",
                LocalDate.of(2026, 5, 8),
                reservationTime,
                theme
        );
        reservationDao.save(reservation);

        // when & then
        assertThatThrownBy(() -> themeService.delete(theme.getId()))
                .isInstanceOf(ThemeException.class)
                .hasMessage(ThemeErrorCode.THEME_HAS_RESERVATION.getMessage());
    }

    private Theme saveTheme(String name) {
        Theme theme = Theme.createWithoutId(
                name,
                "설명",
                "https://dsf.sdaf"
        );
        return themeDao.save(theme);
    }

    private ReservationTime saveReservationTime(LocalTime startAt) {
        ReservationTime reservationTime = ReservationTime.createWithoutId(startAt);
        return reservationTimeDao.save(reservationTime);
    }
}
