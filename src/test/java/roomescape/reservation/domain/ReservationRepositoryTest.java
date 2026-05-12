package roomescape.reservation.domain;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.config.TestTimeConfig;
import roomescape.reservation.infra.JdbcReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.infra.JdbcThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeRepository;
import roomescape.time.infra.JdbcReservationTimeRepository;

@JdbcTest
@Import({
        TestTimeConfig.class,
        JdbcReservationRepository.class,
        JdbcThemeRepository.class,
        JdbcReservationTimeRepository.class
})
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private Clock clock;

    @Test
    @DisplayName("예약을 추가하면 테마id가 부여된다.")
    void normalThemeTest() {
        Theme theme = Theme.builder()
                .name("포비")
                .description("포비가 나와요")
                .durationTime(LocalTime.now(clock))
                .thumbnailImageUrl("http://~~~")
                .build();

        Theme savedTheme = themeRepository.save(theme);

        ReservationTime reservationTime = ReservationTime.builder()
                .startAt(LocalTime.now(clock))
                .build();

        ReservationTime savedTime = reservationTimeRepository.save(reservationTime);

        Reservation reservation = Reservation.builder()
                .name("포비")
                .date(LocalDate.now(clock))
                .time(savedTime)
                .theme(savedTheme)
                .status(Status.ACTIVE)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        Assertions.assertThat(savedReservation.getId())
                .isNotNull();
        Assertions.assertThat(savedReservation.getTheme().getId())
                .isEqualTo(savedTheme.getId());
        Assertions.assertThat(savedReservation.getTime().getId())
                .isEqualTo(savedTime.getId());
    }
}
