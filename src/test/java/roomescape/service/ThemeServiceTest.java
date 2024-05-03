package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.dto.app.ThemeAppRequest;
import roomescape.exception.DuplicatedThemeException;
import roomescape.exception.ReservationExistsException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String name = "themeName";
    private final String description = "themeDesc";
    private final String thumbnail = "https://";

    @AfterEach
    void clear() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.update("TRUNCATE TABLE reservation_time");
        jdbcTemplate.update("TRUNCATE TABLE reservation");
        jdbcTemplate.update("TRUNCATE TABLE theme");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");

        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
    }

    @DisplayName("실패: 이름이 null 또는 빈 값이면 예외 발생")
    @ParameterizedTest
    @NullAndEmptySource
    void save_IllegalName(String invalidName) {
        assertThatThrownBy(
            () -> themeService.save(new ThemeAppRequest(invalidName, description, thumbnail))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("실패: description이 null 또는 빈 값이면 예외 발생")
    @ParameterizedTest
    @NullAndEmptySource
    void save_IllegalDescription(String invalidDescription) {
        assertThatThrownBy(
            () -> themeService.save(new ThemeAppRequest(name, invalidDescription, thumbnail))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("thumbnail 형식이 잘못된 경우 예외 발생")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"ftp://hello.jpg"})
    void save_IllegalThumbnail(String invalidThumbnail) {
        assertThatThrownBy(
            () -> themeService.save(new ThemeAppRequest(name, description, invalidThumbnail))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("실패: 이름이 동일한 방탈출 테마를 저장하면 예외 발생")
    @Test
    void save_DuplicatedName() {
        themeService.save(new ThemeAppRequest(name, description, thumbnail));

        assertThatThrownBy(
            () -> themeService.save(new ThemeAppRequest(name, "d", "https://d"))
        ).isInstanceOf(DuplicatedThemeException.class);
    }

    @DisplayName("실패: 예약에 사용되는 테마 삭제 시도 시 예외 발생")
    @Test
    void delete_ReservationExists() {
        Theme savedTheme = themeService.save(new ThemeAppRequest(name, description, thumbnail));

        ReservationTime savedTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));

        reservationRepository.save(
            new Reservation("name", LocalDate.parse("2060-01-01"), savedTime, savedTheme));

        assertThatThrownBy(() -> themeService.delete(savedTheme.getId()))
            .isInstanceOf(ReservationExistsException.class);
    }
}
