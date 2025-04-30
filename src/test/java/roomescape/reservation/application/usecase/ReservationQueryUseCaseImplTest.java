package roomescape.reservation.application.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.domain.ReserverName;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeDescription;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.domain.ThemeThumbnail;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationTimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class ReservationQueryUseCaseImplTest {

    @Autowired
    private ReservationQueryUseCaseImpl reservationQueryUseCase;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("예약을 조회할 수 있다")
    void createAndFindReservation() {
        // given
        final ReservationTime reservationTime = reservationTimeRepository.save(
                ReservationTime.withoutId(
                        LocalTime.of(10, 0)));

        final Theme theme = themeRepository.save(
                Theme.withoutId(ThemeName.from("공포"),
                        ThemeDescription.from("지구별 방탈출 최고"),
                        ThemeThumbnail.from("www.making.com")));

        final Reservation given1 = Reservation.withoutId(
                ReserverName.from("강산"),
                ReservationDate.from(LocalDate.now().plusDays(1)),
                reservationTime,
                theme);

        final Reservation given2 = Reservation.withoutId(
                ReserverName.from("강산2"),
                ReservationDate.from(LocalDate.now().plusDays(1)),
                reservationTime,
                theme);

        final Reservation saved1 = reservationRepository.save(given1);
        final Reservation saved2 = reservationRepository.save(given2);

        // when
        final List<Reservation> reservations = reservationQueryUseCase.getAll();

        // then
        assertThat(reservations).hasSize(2);
        final Reservation found1 = reservations.getFirst();
        final Reservation found2 = reservations.get(1);

        assertAll(() -> {
            assertThat(found1).isEqualTo(saved1);
            assertThat(found2).isEqualTo(saved2);
        });
    }
}
