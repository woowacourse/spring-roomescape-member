package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.common.globalexception.NotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.fixture.ReservationFixture;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.fixture.ReservationTimeFixture;
import roomescape.reservationtime.repository.ReservationTimeRepositoryImpl;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepositoryImpl;

@JdbcTest
@Import({ReservationRepositoryImpl.class, ReservationTimeRepositoryImpl.class, ThemeRepositoryImpl.class})
class ReservationRepositoryImplTest {

    @Autowired
    private ReservationRepositoryImpl reservationRepository;
    @Autowired
    private ReservationTimeRepositoryImpl reservationTimeRepository;
    @Autowired
    private ThemeRepositoryImpl themeRepository;

    @DisplayName("존재하지 않는 예약 ID로 조회하면 예외가 발생한다.")
    @Test
    void findById_throwsExceptionByNonExistentId() {
        // given
        String dummyName1 = "kali";
        LocalDateTime future1 = LocalDateTime.now().plusDays(1);
        LocalDate dummyDate = future1.toLocalDate();
        LocalTime dummyTime = LocalTime.of(10, 11);

        ReservationTime reservationTime1 = ReservationTimeFixture.create(dummyTime);
        ReservationTime savedReservationTime1 = reservationTimeRepository.add(reservationTime1);

        Theme theme = new Theme("name1", "dd", "tt");
        Theme savedTheme = themeRepository.add(theme);

        Reservation reservation1 = ReservationFixture.create(dummyName1, dummyDate, savedReservationTime1, savedTheme);

        String dummyName2 = "kali";
        LocalDateTime future2 = LocalDateTime.now().plusDays(2);
        LocalDate dummyDate2 = future2.toLocalDate();
        LocalTime dummyTime2 = LocalTime.of(10, 22);

        ReservationTime reservationTime2 = ReservationTimeFixture.create(dummyTime2);
        ReservationTime savedReservationTime2 = reservationTimeRepository.add(reservationTime2);

        Reservation reservation2 = ReservationFixture.create(dummyName2, dummyDate2, savedReservationTime2, savedTheme);

        List<Reservation> reservations = List.of(reservation1, reservation2);

        for (Reservation reservation : reservations) {
            reservationRepository.add(reservation);
        }

        // when & then
        Assertions.assertThatCode(
            () -> reservationTimeRepository.findByIdOrThrow(Long.MAX_VALUE)
        ).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("예약 시간에 해당하는 예약의 존재 여부를 알 수 있다.")
    @Test
    void existsByReservationTime() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.add(new ReservationTime(LocalTime.now()));

        Theme theme = new Theme("name1", "dd", "tt");
        Theme savedTheme = themeRepository.add(theme);

        Reservation reservation = ReservationFixture.create("r1", LocalDate.now().plusMonths(2), reservationTime,
            savedTheme);
        reservationRepository.add(reservation);

        // when
        boolean actual = reservationRepository.existsByReservationTime(reservationTime);

        // then
        assertThat(actual).isTrue();
    }
}
