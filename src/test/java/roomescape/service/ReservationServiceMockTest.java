package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationAddRequest;
import roomescape.repository.ReservationDao;


@ExtendWith(MockitoExtension.class)
class ReservationServiceMockTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationDao reservationDao;

    private Reservation reservation = new Reservation(1L, "dodo", LocalDate.of(2020, 12, 12),
            new ReservationTime(1L, LocalTime.of(12, 12)));

    @DisplayName("모든 예약 목록을 찾습니다.")
    @Test
    void should_find_all_reservation() {
        given(reservationDao.findAll())
                .willReturn(List.of(reservation));
        int expectedSize = 1;

        int actualSize = reservationService.findAllReservation().size();

        assertThat(actualSize).isEqualTo(expectedSize);
    }

    @DisplayName("예약을 추가하고 저장된 예약을 반환합니다.")
    @Test
    void should_add_reservation() {
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(LocalDate.of(2020, 12, 12), "dodo", 1L);
        Reservation expectedReservation = reservation;
        given(reservationDao.insert(reservationAddRequest)).willReturn(expectedReservation);

        Reservation actualReservation = reservationService.addReservation(reservationAddRequest);

        assertThat(actualReservation).isEqualTo(expectedReservation);
    }

    @DisplayName("원하는 id의 예약을 삭제합니다.")
    @Test
    void should_true_when_remove_reservation_with_exist_id() {
        given(reservationDao.findById(1L)).willReturn(Optional.of(reservation));
        doNothing().when(reservationDao).deleteById(1L);

        reservationService.removeReservation(1L);

        verify(reservationDao, times(1)).deleteById(1L);
    }

    @DisplayName("없는 id의 예약을 삭제하면 예외를 발생합니다.")
    @Test
    void should_false_when_remove_reservation_with_non_exist_id() {
        given(reservationDao.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.removeReservation(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 id를 가진 예약이 존재하지 않습니다.");
    }
}
