package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeRepository;

@Transactional
@SpringBootTest
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;


    @Test
    @DisplayName("예약을 추가하면 테마id가 부여된다.")
    void normalThemeTest() {
        Theme theme = Theme.builder()
                .name("포비")
                .description("포비가 나와요")
                .durationTime(LocalTime.of(1, 0))
                .thumbnailImageUrl("http://~~~")
                .build();

        Theme savedTheme = themeRepository.save(theme);

        ReservationTime reservationTime = ReservationTime.builder()
                .startAt(LocalTime.of(1, 0))
                .build();

        ReservationTime savedTime = reservationTimeRepository.save(reservationTime);

        Reservation reservation = Reservation.builder()
                .name("포비")
                .date(LocalDate.now())
                .time(savedTime)
                .theme(savedTheme)
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
