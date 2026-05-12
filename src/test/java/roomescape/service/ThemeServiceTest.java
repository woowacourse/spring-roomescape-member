package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Theme;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.TimeAvailabilityDto;

@JdbcTest
class ThemeServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ThemeService themeService;

    @BeforeEach
    void setup() {
        ThemeRepository themeRepository = new ThemeRepository(jdbcTemplate);
        ReservationRepository reservationRepository = new ReservationRepository(jdbcTemplate);
        ReservationTimeRepository reservationTimeRepository = new ReservationTimeRepository(jdbcTemplate);
        this.themeService = new ThemeService(themeRepository, reservationRepository, reservationTimeRepository);
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
    }

    @Test
    void 테마_생성_테스트() {
        // when
        Theme result = themeService.create("테스트테마", "테스트용 테마입니다.", "/썸네일");

        // then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getName()).isEqualTo("테스트테마")
        );
    }

    @Test
    void 전체_테마_조회_테스트() {
        // given
        themeService.create("테스트테마1", "테스트용 테마1입니다.", "/썸네일1");
        themeService.create("테스트테마2", "테스트용 테마2입니다.", "/썸네일2");

        // when
        List<Theme> result = themeService.findAll();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void 테마_삭제_테스트() {
        // given
        Theme created = themeService.create("테스트테마", "테스트용 테마입니다.", "/썸네일");

        // when
        themeService.delete(created.getId());

        // then
        assertThat(themeService.findAll()).isEmpty();
    }

    @Test
    void 존재하지않는_theme_id_삭제_시_예외_발생() {
        // when & then
        assertThatThrownBy(() -> themeService.delete(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 테마입니다.");
    }

    @Test
    void 예약된_테마_삭제_시_예외_발생() {
        LocalDate date = LocalDate.parse("2026-05-05");
        Long timeId = createReservationTime();
        Long themeId = createTheme();
        createReservation(date, timeId, themeId);

        // when & then
        assertThatThrownBy(() -> themeService.delete(themeId))
                .isInstanceOf(ConflictException.class)
                .hasMessage("해당 테마의 예약이 존재하여 삭제할 수 없습니다.");
    }


    @Test
    void 예약_가능한_시간_조회_테스트() {
        // given
        LocalDate date = LocalDate.parse("2026-05-05");
        Long timeId = createReservationTime();
        Long themeId = createTheme();
        createReservation(date, timeId, themeId);

        // when
        List<TimeAvailabilityDto> result = themeService.findAvailableTime(themeId, date);

        // then
        assertThat(result).extracting(TimeAvailabilityDto::available)
                .containsOnlyOnce(false);
    }

    private Long createTheme() {
        jdbcTemplate.update(
                "INSERT INTO theme(name, description, thumbnail) VALUES (?, ?, ?)",
                "테스트 테마", "테스트 테마 설명", "테스트 테마 url"
        );
        return jdbcTemplate.queryForObject(
                "SELECT id FROM theme ORDER BY id DESC LIMIT 1",
                Long.class
        );
    }

    private Long createReservationTime() {
        jdbcTemplate.update(
                "INSERT INTO reservation_time(start_at) VALUES (?)",
                "10:00"
        );
        return jdbcTemplate.queryForObject(
                "SELECT id FROM reservation_time ORDER BY id DESC LIMIT 1",
                Long.class
        );
    }

    private void createReservation(LocalDate date, Long timeId, Long themeId) {
        jdbcTemplate.update(
                "INSERT INTO reservation(name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "브라운", date, timeId, themeId
        );
    }
}
