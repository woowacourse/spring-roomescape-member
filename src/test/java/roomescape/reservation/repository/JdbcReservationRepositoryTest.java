package roomescape.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.infra.JdbcReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.domain.repository.ReservationTimeRepository;
import roomescape.reservationtime.infra.JdbcReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.repository.ThemeRepository;
import roomescape.theme.infra.JdbcThemeRepository;

@JdbcTest
class JdbcReservationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    ReservationRepository reservationRepository;
    ReservationTimeRepository timeRepository;
    ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new JdbcReservationRepository(jdbcTemplate);
        timeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
        themeRepository = new JdbcThemeRepository(jdbcTemplate);
    }

    @DisplayName("사용자의 방탈출 예약 시간 추가를 테스트합니다.")
    @Test
    void save_user_reservation() {
        ReservationTime savedTime = timeRepository.save(ReservationTime.builder()
                .startAt(LocalTime.of(9, 0))
                .build());
        Theme savedTheme = themeRepository.save(Theme.builder()
                .name("theme name")
                .description("theme description")
                .thumbnailImgUrl("theme img url")
                .build());

        Reservation reservation = Reservation.builder()
                .name("name")
                .date(LocalDate.of(2026, 5, 4))
                .themeId(savedTheme.getId())
                .timeId(savedTime.getId())
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        SoftAssertions.assertSoftly(assertSoftly -> {
            assertSoftly.assertThat(savedReservation.getName()).isEqualTo("name");
            assertSoftly.assertThat(savedReservation.getDate()).isEqualTo(LocalDate.of(2026, 5, 4));
            assertSoftly.assertThat(savedReservation.getThemeId()).isEqualTo(savedTheme.getId());
            assertSoftly.assertThat(savedReservation.getTimeId()).isEqualTo(savedTime.getId());
        });
    }
}
