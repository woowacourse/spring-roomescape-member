package roomescape.time.domain;

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
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.domain.Status;
import roomescape.reservation.infra.JdbcReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.infra.JdbcThemeRepository;
import roomescape.time.application.exception.ReservationTimeNotFoundException;
import roomescape.time.infra.JdbcReservationTimeRepository;

@JdbcTest
@Import({
        TestTimeConfig.class,
        JdbcReservationTimeRepository.class,
        JdbcThemeRepository.class,
        JdbcReservationRepository.class
})
class ReservationTimeRepositoryTest {

    @Autowired
    private ReservationTimeRepository timeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private Clock clock;

    @Test
    @DisplayName("오늘 날짜로 등록된 테마가 있으면 조회된다.")
    void normalTest() {
        ReservationTime time = timeRepository.save(ReservationTime.builder()
                .startAt(LocalTime.now(clock).withNano(0))
                .build());
        Theme theme = themeRepository.save(Theme.builder()
                .name("포비")
                .description("포비가 나와요")
                .thumbnailImageUrl("https://~~~~")
                .durationTime(LocalTime.now(clock))
                .build());

        Reservation reservation = reservationRepository.save(Reservation.builder()
                        .name("포비")
                        .date(LocalDate.now(clock))
                        .time(time)
                        .theme(theme)
                        .status(Status.ACTIVE)
                .build());

        Assertions.assertThat(reservation.getId()).isNotNull();
        Assertions.assertThat(reservation.getTime()).isEqualTo(time);
        Assertions.assertThat(reservation.getTheme()).isEqualTo(theme);
    }

    @Test
    @DisplayName("저장된 예약 시간을 ID로 조회할 수 있다.")
    void findByIdTest() {
        ReservationTime time = timeRepository.save(ReservationTime.builder()
                .startAt(LocalTime.now(clock).withNano(0))
                .build());
        Assertions.assertThat(timeRepository.getById(time.getId())).isNotNull();
    }

    @Test
    @DisplayName("전체 예약 시간 목록을 조회할 수 있다.")
    void findAllTest() {
        timeRepository.save(ReservationTime.builder().startAt(LocalTime.now(clock)).build());
        timeRepository.save(ReservationTime.builder().startAt(LocalTime.now(clock).plusHours(1)).build());

        Assertions.assertThat(timeRepository.findAll()).hasSize(2);
    }

    @Test
    @DisplayName("예약 시간을 삭제 할 수 있다.")
    void deleteTest() {
        ReservationTime time = timeRepository.save(ReservationTime.builder().startAt(LocalTime.now(clock)).build());
        timeRepository.deleteById(time.getId());
        Assertions.assertThatThrownBy(() -> timeRepository.getById(time.getId()))
                .isInstanceOf(ReservationTimeNotFoundException.class);
    }
}
