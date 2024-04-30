package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationResponse;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static roomescape.TestFixture.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("예약을 생성한다.")
    void create() {
        // given
        Reservation reservation = MIA_RESERVATION();

        BDDMockito.given(reservationTimeRepository.findById(any()))
                .willReturn(Optional.of(new ReservationTime(MIA_RESERVATION_TIME)));
        BDDMockito.given(reservationRepository.save(reservation))
                .willReturn(new Reservation(1L, reservation));

        // when
        ReservationResponse response = reservationService.create(reservation);

        // then
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("동일한 시간에 같은 사용자가 예약할 수 없다.")
    void createSameReservation() {
        // given
        Reservation miaReservation = MIA_RESERVATION();

        BDDMockito.given(reservationTimeRepository.findById(any()))
                .willReturn(Optional.of(new ReservationTime(MIA_RESERVATION_TIME)));
        BDDMockito.given(reservationRepository.findAllByDateAndTime(any(), any()))
                .willReturn(List.of(miaReservation));

        // when & then
        assertThatThrownBy(() -> reservationService.create(miaReservation))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("동일한 시간대에 최대 4팀이 예약할 수 있다. 초과되면 예외가 발생한다.")
    void createLimitedReservations() {
        // given
        Reservation miaReservation = new Reservation(USER_MIA, MIA_RESERVATION_DATE, new ReservationTime(MIA_RESERVATION_TIME));
        Reservation tommyReservation = new Reservation(USER_TOMMY, MIA_RESERVATION_DATE, new ReservationTime(MIA_RESERVATION_TIME));
        Reservation wonnyReservation = new Reservation("wonny", MIA_RESERVATION_DATE, new ReservationTime(MIA_RESERVATION_TIME));
        Reservation neoReservation = new Reservation("neo", MIA_RESERVATION_DATE, new ReservationTime(MIA_RESERVATION_TIME));

        BDDMockito.given(reservationTimeRepository.findById(any()))
                .willReturn(Optional.of(new ReservationTime(MIA_RESERVATION_TIME)));
        BDDMockito.given(reservationRepository.findAllByDateAndTime(any(), any()))
                .willReturn(List.of(miaReservation, tommyReservation, wonnyReservation, neoReservation));

        Reservation newReservation = new Reservation("new", MIA_RESERVATION_DATE, new ReservationTime(MIA_RESERVATION_TIME));

        // when & then
        assertThatThrownBy(() -> reservationService.create(newReservation))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("모든 예약 목록을 조회한다.")
    void getAll() {
        // given
        Reservation miaReservation = MIA_RESERVATION();
        Reservation tommyReservation = TOMMY_RESERVATION();

        BDDMockito.given(reservationRepository.findAll())
                .willReturn(List.of(miaReservation, tommyReservation));

        // when
        List<ReservationResponse> reservations = reservationService.findAll();

        // then
        assertThat(reservations).hasSize(2)
                .extracting(ReservationResponse::name)
                .containsExactly(USER_MIA, USER_TOMMY);
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void delete() {
        // given
        Reservation miaReservation = MIA_RESERVATION();

        BDDMockito.given(reservationRepository.findById(1L))
                .willReturn(Optional.of(miaReservation));

        // when & then
        assertThatCode(() -> reservationService.delete(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하지 않는 예약 Id로 삭제할 수 없다.")
    void deleteNotExistingId() {
        // given
        Long notExistingId = 1L;

        BDDMockito.given(reservationRepository.findById(notExistingId))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.delete(notExistingId))
                .isInstanceOf(NotFoundException.class);
    }
}
