package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.reservation.Reservation;
import roomescape.reservation.dao.FakeReservationDao;
import roomescape.reservation.dto.request.ReservationCreateRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.dao.FakeReservationTimeDao;

class ReservationServiceTest {

    private FakeReservationDao fakeReservationDao;
    private FakeReservationTimeDao fakeReservationTimeDao;
    private ReservationService reservationService;
    private Clock clock;

    private final ReservationTime fakeReservationTime1 = new ReservationTime(1L, LocalTime.of(10, 0));
    private final ReservationTime fakeReservationTime2 = new ReservationTime(2L, LocalTime.of(11, 0));
    private final Reservation fakeReservation1 = new Reservation(1L, "포라", LocalDate.of(2025, 7, 25),
            fakeReservationTime1);
    private final Reservation fakeReservation2 = new Reservation(2L, "널안보면내마음에멍", LocalDate.of(2025, 12, 25),
            fakeReservationTime2);

    @BeforeEach
    void setUp() {
        LocalDateTime fixedDateTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        clock = Clock.fixed(fixedDateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        fakeReservationTimeDao = new FakeReservationTimeDao(fakeReservationTime1, fakeReservationTime2);
        fakeReservationDao = new FakeReservationDao(fakeReservation1, fakeReservation2);
        reservationService = new ReservationService(fakeReservationDao, fakeReservationTimeDao, clock);
    }

    @Test
    void 예약을_조회할_수_있다() {
        // given & when
        List<ReservationResponse> all = reservationService.findAll();

        // then
        assertThat(all.size()).isEqualTo(2);
        assertThat(all.get(0).name()).isEqualTo("포라");
        assertThat(all.get(1).name()).isEqualTo("널안보면내마음에멍");
    }

    @Test
    void 예약을_추가할_수_있다() {
        // given & when
        ReservationCreateRequest 포비 = new ReservationCreateRequest("포비", LocalDate.now().plusDays(1), 1L);
        reservationService.create(포비);
        List<ReservationResponse> all = reservationService.findAll();

        // then
        assertThat(all.size()).isEqualTo(3);
        assertThat(all.getLast().name()).isEqualTo("포비");
    }

    @Test
    void 예약을_삭제할_수_있다() {
        // given & when
        reservationService.delete(fakeReservation1.getId());
        List<ReservationResponse> all = reservationService.findAll();

        // then
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.getFirst().name()).isEqualTo("널안보면내마음에멍");
    }

    @Test
    void id에_대한_예약이_없을_경우_예외가_발생한다() {
        // when & then
        Assertions.assertThatThrownBy(() -> reservationService.delete(10L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
