package roomescape.reservation.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.time.dao.TimeJdbcDao;
import roomescape.time.domain.Time;
import roomescape.reservation.dao.ReservationJdbcDao;
import roomescape.reservation.domain.Reservation;

@ExtendWith(MockitoExtension.class)
class ReservationRepositoryTest {
    private final Reservation reservation = new Reservation(1L, "polla", LocalDate.now(),
            new Time(1L, LocalTime.now()));
    private final Time time = new Time(1L, LocalTime.of(17, 3));

    @InjectMocks
    private ReservationRepository reservationRepository;
    @Mock
    private TimeJdbcDao timeJdbcDao;
    @Mock
    private ReservationJdbcDao reservationJdbcDao;

    @Test
    @DisplayName("예약을 정상적으로 저장한다.")
    void saveReservation() {
        Mockito.when(timeJdbcDao.findById(reservation.getReservationTime().getId()))
                .thenReturn(time);
        Mockito.when(reservationJdbcDao.save(reservation))
                .thenReturn(reservation);

        Reservation savedReservation = reservationRepository.saveReservation(reservation);

        Assertions.assertThat(savedReservation.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("찾으려는 시간이 존재하지 않는 경우 에러가 발생한다.")
    void saveNotExistTimeReservation() {
        Mockito.when(timeJdbcDao.findById(2))
                .thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> reservationRepository.saveReservation(reservation));
    }
}
