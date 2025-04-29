package roomescape.reservation.repository;

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
import roomescape.globalException.CustomException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.fixture.ReservationFixture;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.fixture.ReservationTimeFixture;
import roomescape.reservationTime.repository.ReservationTimeRepositoryImpl;

@JdbcTest
@Import({ReservationRepositoryImpl.class, ReservationTimeRepositoryImpl.class})
class ReservationRepositoryImplTest {

    @Autowired
    private ReservationRepositoryImpl reservationRepository;
    @Autowired
    private ReservationTimeRepositoryImpl reservationTimeRepository;

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

        Reservation reservation1 = ReservationFixture.create(dummyName1, dummyDate, savedReservationTime1);

        String dummyName2 = "kali";
        LocalDateTime future2 = LocalDateTime.now().plusDays(2);
        LocalDate dummyDate2 = future2.toLocalDate();
        LocalTime dummyTime2 = LocalTime.of(10, 22);

        ReservationTime reservationTime2 = ReservationTimeFixture.create(dummyTime2);
        ReservationTime savedReservationTime2 = reservationTimeRepository.add(reservationTime2);

        Reservation reservation2 = ReservationFixture.create(dummyName2, dummyDate2, savedReservationTime2);

        List<Reservation> reservations = List.of(reservation1, reservation2);

        for (Reservation reservation : reservations) {
            reservationRepository.add(reservation);
        }

        // when & then
        Assertions.assertThatCode(
            () -> reservationTimeRepository.findByIdOrThrow(Long.MAX_VALUE)
        ).isInstanceOf(CustomException.class);
    }
}
