package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.exception.ExistedException;
import roomescape.member.Member;
import roomescape.reservation.Reservation;
import roomescape.reservation.dao.FakeReservationDao;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.dao.FakeReservationTimeDao;
import roomescape.reservationtime.dto.request.ReservationTimeRequest;
import roomescape.reservationtime.dto.response.AvailableTimeResponse;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;
import roomescape.theme.Theme;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationTimeServiceTest {

    private FakeReservationTimeDao fakeReservationTimeDao;
    private FakeReservationDao fakeReservationDao;
    private ReservationTimeService reservationTimeService;

    private final ReservationTime fakeReservationTime1 = new ReservationTime(1L, LocalTime.of(10, 0));
    private final ReservationTime fakeReservationTime2 = new ReservationTime(2L, LocalTime.of(11, 0));

    private final Theme theme = Theme.of(1L, "themeName1", "des", "th");

    private final Member member1 = Member.of(1L, "포라", "sy@gmail.com", "1234", "USER");
    private final Member member2 = Member.of(2L, "라리사", "lalisa@gmail.com", "1234", "USER");

    private final Reservation fakeReservation1 = Reservation.of(1L, member1, LocalDate.of(2025, 7, 25),
            fakeReservationTime1, theme);
    private final Reservation fakeReservation2 = Reservation.of(2L, member2, LocalDate.of(2025, 12, 25),
            fakeReservationTime1, theme);


    @BeforeEach
    void setUp() {
        fakeReservationTimeDao = new FakeReservationTimeDao(fakeReservationTime1, fakeReservationTime2);
        fakeReservationDao = new FakeReservationDao(fakeReservation1, fakeReservation2);
        reservationTimeService = new ReservationTimeService(fakeReservationTimeDao, fakeReservationDao);
    }

    @Test
    void 예약_시간을_조회할_수_있다() {
        // given & when
        List<ReservationTimeResponse> all = reservationTimeService.findAll();

        // then
        assertThat(all.size()).isEqualTo(2);
        assertThat(all.get(0).startAt()).isEqualTo(LocalTime.of(10, 0));
        assertThat(all.get(1).startAt()).isEqualTo(LocalTime.of(11, 0));
    }

    @Test
    void 예약_시간을_추가할_수_있다() {
        // given & when
        ReservationTimeRequest newTime = new ReservationTimeRequest(LocalTime.of(2, 0));
        reservationTimeService.create(newTime);
        List<ReservationTimeResponse> all = reservationTimeService.findAll();

        // then
        assertThat(all.size()).isEqualTo(3);
        assertThat(all.getLast().startAt()).isEqualTo(LocalTime.of(2, 0));
    }

    @Test
    void 예약_시간을_삭제할_수_있다() {
        // given & when
        reservationTimeService.delete(2L);
        List<ReservationTimeResponse> all = reservationTimeService.findAll();

        // then
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.getFirst().startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 특정_시간에_대한_예약이_존재하면_예약시간을_삭제할_수_없다() {
        // when & then
        assertThatThrownBy(() -> reservationTimeService.delete(1L))
                .isInstanceOf(ExistedException.class);
    }

    @Test
    void 예약_가능한_시간을_조회한다() {
        // given
        LocalDate date = LocalDate.of(2025, 12, 25);
        // when
        List<AvailableTimeResponse> availableTimes = reservationTimeService.findAvailableTimes(date, 1L);
        // then
        assertThat(availableTimes).hasSize(2);
        assertThat(availableTimes.getFirst().alreadyBooked()).isTrue();
        assertThat(availableTimes.get(1).alreadyBooked()).isFalse();
    }
}
