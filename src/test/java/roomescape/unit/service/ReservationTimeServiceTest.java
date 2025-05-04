package roomescape.unit.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.controller.request.AvailableReservationTimeRequest;
import roomescape.time.controller.request.CreateReservationTimeRequest;
import roomescape.time.controller.response.AvailableReservationTimeResponse;
import roomescape.time.controller.response.ReservationTimeResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.time.service.ReservationTimeService;

public class ReservationTimeServiceTest {

    private ReservationTimeRepository reservationTimeRepository;
    private ReservationRepository reservationRepository;
    private ReservationTimeService service;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = mock(ReservationTimeRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        service = new ReservationTimeService(reservationTimeRepository, reservationRepository);
    }

    @Test
    void 예약시간을_생성할_수_있다() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);
        CreateReservationTimeRequest request = new CreateReservationTimeRequest(startAt);
        ReservationTime created = new ReservationTime(1L, startAt);

        when(reservationTimeRepository.existByStartAt(startAt)).thenReturn(false);
        when(reservationTimeRepository.save(startAt)).thenReturn(created);

        // when
        ReservationTimeResponse response = service.createReservationTime(request);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.startAt()).isEqualTo(startAt);
    }

    @Test
    void 중복된_예약시간은_생성할_수_없다() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);
        CreateReservationTimeRequest request = new CreateReservationTimeRequest(startAt);
        when(reservationTimeRepository.existByStartAt(startAt)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> service.createReservationTime(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_예약시간을_조회할_수_있다() {
        // given
        List<ReservationTime> times = List.of(
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new ReservationTime(2L, LocalTime.of(11, 0))
        );
        when(reservationTimeRepository.findAll()).thenReturn(times);

        // when
        List<ReservationTimeResponse> result = service.getAllReservationTimes();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void 예약이_없는_시간은_삭제할_수_있다() {
        // given
        Long id = 1L;
        ReservationTime time = new ReservationTime(id, LocalTime.of(10, 0));
        when(reservationRepository.existReservationByTimeId(id)).thenReturn(false);
        when(reservationTimeRepository.findById(id)).thenReturn(Optional.of(time));

        // when
        service.deleteReservationTimeById(id);

        // then
        verify(reservationTimeRepository).deleteById(id);
    }

    @Test
    void 예약이_존재하는_시간은_삭제할_수_없다() {
        // given
        Long id = 1L;
        when(reservationRepository.existReservationByTimeId(id)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> service.deleteReservationTimeById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_예약시간은_삭제할_수_없다() {
        // given
        Long id = 999L;
        when(reservationRepository.existReservationByTimeId(id)).thenReturn(false);
        when(reservationTimeRepository.findById(id)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> service.deleteReservationTimeById(id))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 예약시간을_ID로_조회할_수_있다() {
        // given
        ReservationTime expected = new ReservationTime(1L, LocalTime.of(10, 0));
        when(reservationTimeRepository.findById(1L)).thenReturn(Optional.of(expected));

        // when
        ReservationTime found = service.getReservationTime(1L);

        // then
        assertThat(found.getId()).isEqualTo(1L);
    }

    @Test
    void 예약시간_ID로_조회시_없으면_예외() {
        // given
        when(reservationTimeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> service.getReservationTime(1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 예약가능한_시간들을_조회할_수_있다() {
        // given
        LocalDate date = LocalDate.of(2024, 5, 5);
        Long themeId = 1L;
        AvailableReservationTimeRequest request = new AvailableReservationTimeRequest(date, themeId);
        List<AvailableReservationTimeResponse> expected = List.of(
                new AvailableReservationTimeResponse(1L, LocalTime.of(10, 0), false)
        );
        when(reservationTimeRepository.findAllAvailableReservationTimes(date, themeId)).thenReturn(expected);

        // when
        List<AvailableReservationTimeResponse> result = service.findAvailableReservationTimes(request);

        // then
        assertThat(result).isEqualTo(expected);
    }
}
