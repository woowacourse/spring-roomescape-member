package roomescape.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.auth.Role;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exception.ExistedReservationException;
import roomescape.service.ReservationTimeService;
import roomescape.unit.fake.FakeReservationRepository;
import roomescape.unit.fake.FakeReservationTimeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationTimeServiceTest {

    private FakeReservationTimeRepository fakeReservationTimeDao;
    private FakeReservationRepository fakeReservationDao;
    private ReservationTimeService reservationTimeService;

    private final ReservationTime fakeReservationTime1 = new ReservationTime(1L, LocalTime.of(10, 0));
    private final ReservationTime fakeReservationTime2 = new ReservationTime(2L, LocalTime.of(11, 0));
    private final Theme theme = new Theme(1L, "themeName1", "des", "th");
    private final Member member1 = new Member(1L, "name1", "email1@domain.com", "password1", Role.MEMBER);
    private final Member member2 = new Member(1L, "name1", "email1@domain.com", "password1", Role.MEMBER);
    private final Reservation fakeReservation1 = Reservation.of(1L, member1, LocalDate.of(2025, 7, 25),
            fakeReservationTime1, theme);
    private final Reservation fakeReservation2 = Reservation.of(2L, member2, LocalDate.of(2025, 12, 25),
            fakeReservationTime1, theme);


    @BeforeEach
    void setUp() {
        fakeReservationTimeDao = new FakeReservationTimeRepository(fakeReservationTime1, fakeReservationTime2);
        fakeReservationDao = new FakeReservationRepository(fakeReservation1, fakeReservation2);
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
                .isInstanceOf(ExistedReservationException.class);
    }
}
