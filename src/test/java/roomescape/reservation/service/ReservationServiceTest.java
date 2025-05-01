package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.exception.DataExistException;
import roomescape.exception.DataNotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.H2ReservationRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.service.dto.AvailableReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.H2ThemeRepository;
import roomescape.theme.repository.ThemeRepository;
import roomescape.theme.service.ThemeService;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.H2ReservationTimeRepository;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.time.service.ReservationTimeService;


@JdbcTest
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void 예약_정보_목록을_조회한다() {
        // given
        final String themeName = "공포";
        final String description = "무섭다";
        final String thumbnail = "귀신사진";
        final Long themeId = themeRepository.save(new Theme(themeName, description, thumbnail));

        final String name = "헤일러";
        final LocalDate date = LocalDate.parse("2025-08-01");
        final LocalTime time = LocalTime.parse("10:00");
        final Long timeId1 = reservationTimeRepository.save(new ReservationTime(time));

        final String name2 = "머피";
        final LocalDate date2 = LocalDate.parse("2025-08-01");
        final LocalTime time2 = LocalTime.parse("18:00");
        final Long timeId2 = reservationTimeRepository.save(new ReservationTime(time2));

        reservationRepository.save(
                new Reservation(
                        name,
                        date,
                        new ReservationTime(timeId1, time),
                        new Theme(themeId, themeName, description, thumbnail)
                )
        );

        reservationRepository.save(
                new Reservation(
                        name2,
                        date2,
                        new ReservationTime(timeId2, time2),
                        new Theme(themeId, themeName, description, thumbnail)
                )
        );

        // when
        final List<Reservation> reservations = reservationService.findAll();

        // then
        assertThat(reservations.size()).isEqualTo(2);
    }

    @Test
    void 예약_정보를_저장한다() {
        // given
        final String name = "헤일러";
        final LocalTime time = LocalTime.parse("10:00");
        final LocalDate date = LocalDate.parse("2023-08-05");
        final Long timeId = reservationTimeRepository.save(new ReservationTime(time));

        final String themeName = "공포";
        final String description = "무섭다";
        final String thumbnail = "귀신사진";
        final Long themeId = themeRepository.save(new Theme(themeName, description, thumbnail));

        // when & then
        Assertions.assertThatCode(() -> reservationService.save(name, date, timeId, themeId))
                .doesNotThrowAnyException();
    }

    @Test
    void 예약_정보를_id로_조회한다() {
        // given
        final String name = "헤일러";
        final LocalTime time = LocalTime.parse("10:00");
        final LocalDate date = LocalDate.parse("2023-08-05");
        final Long timeId = reservationTimeRepository.save(new ReservationTime(time));

        final String themeName = "공포";
        final String description = "무섭다";
        final String thumbnail = "귀신사진";
        final Long themeId = themeRepository.save(new Theme(themeName, description, thumbnail));

        final Long savedId = reservationService.save(name, date, timeId, themeId);

        // when
        final Reservation found = reservationService.getById(savedId);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(savedId).isEqualTo(found.getId());
            softly.assertThat(name).isEqualTo(found.getName());
            softly.assertThat(date).isEqualTo(found.getDate());
            softly.assertThat(timeId).isEqualTo(found.getTime().getId());
            softly.assertThat(time).isEqualTo(found.getTime().getStartAt());
        });
    }

    @Test
    void 예약_정보를_삭제한다() {
        // given
        final String name = "헤일러";
        final LocalTime time = LocalTime.parse("10:00");
        final LocalDate date = LocalDate.parse("2023-08-05");
        final Long timeId = reservationTimeRepository.save(new ReservationTime(time));

        final String themeName = "공포";
        final String description = "무섭다";
        final String thumbnail = "귀신사진";
        final Long themeId = themeRepository.save(new Theme(themeName, description, thumbnail));

        Long savedId = reservationRepository.save(new Reservation(
                name,
                date,
                new ReservationTime(timeId, time),
                new Theme(themeId, themeName, description, thumbnail)
        ));

        // when & then
        Assertions.assertThatCode(() -> reservationService.deleteById(savedId)).doesNotThrowAnyException();
    }

    @Test
    void 이용가능한_예약_시간을_조회한다() {
        // given
        final String name = "헤일러";
        final LocalTime time = LocalTime.parse("10:00");
        final LocalDate date = LocalDate.parse("2023-08-05");
        final Long timeId = reservationTimeRepository.save(new ReservationTime(time));

        final String themeName = "공포";
        final String description = "무섭다";
        final String thumbnail = "귀신사진";
        final Long themeId = themeRepository.save(new Theme(themeName, description, thumbnail));

        reservationRepository.save(new Reservation(
                name,
                date,
                new ReservationTime(timeId, time),
                new Theme(themeId, themeName, description, thumbnail)
        ));

        final LocalTime time2 = LocalTime.parse("12:00");
        reservationTimeRepository.save(new ReservationTime(time2));

        // when
        final long count =
                reservationService.findAvailableReservationTimes(date, themeId)
                        .stream()
                        .filter(AvailableReservationTime::alreadyBooked)
                        .count();

        // then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void 예약_정보를_저장할_때_이미_예약된_시간이면_예외가_발생한다() {
        // given
        final String name = "헤일러";
        final LocalTime time = LocalTime.parse("10:00");
        final LocalDate date = LocalDate.parse("2023-08-05");
        final Long timeId = reservationTimeRepository.save(new ReservationTime(time));

        final String themeName = "공포";
        final String description = "무섭다";
        final String thumbnail = "귀신사진";
        final Long themeId = themeRepository.save(new Theme(themeName, description, thumbnail));

        reservationRepository.save(
                new Reservation(
                        name,
                        date,
                        new ReservationTime(timeId, time),
                        new Theme(themeId, themeName, description, thumbnail)
                )
        );

        // when & then
        Assertions.assertThatThrownBy(() -> reservationService.save(name, date, timeId, themeId))
                .isInstanceOf(DataExistException.class);
    }

    @Test
    void 예약_정보를_저장할_때_예약시간이_존재하지않으면_예외가_발생한다() {
        // given
        final String name = "헤일러";
        final LocalDate date = LocalDate.parse("2023-08-05");
        final Long timeId = Long.MAX_VALUE;

        final String themeName = "공포";
        final String description = "무섭다";
        final String thumbnail = "귀신사진";
        final Long themeId = themeRepository.save(new Theme(themeName, description, thumbnail));

        // when & then
        Assertions.assertThatThrownBy(() -> reservationService.save(name, date, timeId, themeId))
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    void 한_테마의_날짜와_시간이_중복_될_수_없다() {
        // given
        final String name = "헤일러";
        final LocalTime time = LocalTime.parse("10:00");
        final LocalDate date = LocalDate.parse("2023-08-05");
        final Long timeId = reservationTimeRepository.save(new ReservationTime(time));

        final String themeName = "공포";
        final String description = "무섭다";
        final String thumbnail = "귀신사진";
        final Long themeId = themeRepository.save(new Theme(themeName, description, thumbnail));

        reservationRepository.save(
                new Reservation(
                        name,
                        date,
                        new ReservationTime(timeId, time),
                        new Theme(themeId, themeName, description, thumbnail)
                )
        );

        // when & then
        Assertions.assertThatThrownBy(() -> reservationService.save("우가", date, timeId, themeId))
                .isInstanceOf(DataExistException.class);
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ReservationTimeRepository reservationTimeRepository(
                final JdbcTemplate jdbcTemplate
        ) {
            return new H2ReservationTimeRepository(jdbcTemplate);
        }

        @Bean
        public ReservationRepository reservationRepository(
                final JdbcTemplate jdbcTemplate
        ) {
            return new H2ReservationRepository(jdbcTemplate);
        }

        @Bean
        public ReservationTimeService reservationTimeService(
                final ReservationTimeRepository reservationTimeRepository
        ) {
            return new ReservationTimeService(reservationTimeRepository);
        }

        @Bean
        public ThemeRepository themeRepository(
                final JdbcTemplate jdbcTemplate
        ) {
            return new H2ThemeRepository(jdbcTemplate);
        }

        @Bean
        public ThemeService themeService(
                final ThemeRepository themeRepository
        ) {
            return new ThemeService(themeRepository);
        }

        @Bean
        public ReservationService reservationService(
                final ReservationRepository reservationRepository,
                final ReservationTimeRepository reservationTimeRepository,
                final ThemeRepository themeRepository
        ) {
            return new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);
        }
    }
}
