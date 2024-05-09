package roomescape.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.fixture.LocalDateFixture.AFTER_ONE_DAYS_DATE;
import static roomescape.fixture.LocalTimeFixture.TEN_HOUR;
import static roomescape.fixture.MemberFixture.ADMIN_MEMBER;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.reservation.dto.BookableTimeResponse;
import roomescape.domain.reservation.dto.BookableTimesRequest;
import roomescape.domain.reservationTime.domain.ReservationTime;
import roomescape.domain.reservationTime.service.FakeReservationTimeRepository;
import roomescape.domain.theme.domain.Theme;

class ReservationServiceTest {

    ReservationService reservationService;
    FakeReservationRepository fakeReservationRepository;
    FakeReservationTimeRepository fakeReservationTimeDao;

    @BeforeEach
    void setUp() {
        fakeReservationRepository = new FakeReservationRepository();
        fakeReservationTimeDao = new FakeReservationTimeRepository();
        reservationService = new ReservationService(fakeReservationRepository, fakeReservationTimeDao);
    }

    @DisplayName("예약 가능 시각을 알 수 있습니다.")
    @Test
    void should_know_bookable_times() {
        ReservationTime reservationTime = new ReservationTime(null, TEN_HOUR);
        Theme theme = new Theme(null, "테마1", "설명", "썸네일");
        fakeReservationTimeDao.insert(reservationTime);
        fakeReservationRepository.insert(
                new Reservation(null, AFTER_ONE_DAYS_DATE, reservationTime, theme, ADMIN_MEMBER));

        List<BookableTimeResponse> bookableTimes = reservationService.findBookableTimes(
                new BookableTimesRequest(AFTER_ONE_DAYS_DATE, 1L));

        assertThat(bookableTimes.get(0).alreadyBooked()).isTrue();
    }

    @DisplayName("예약 불가능 시각을 알 수 있습니다.")
    @Test
    void should_know_not_bookable_times() {
        ReservationTime reservationTime = new ReservationTime(null, TEN_HOUR);
        Theme theme = new Theme(null, "테마1", "설명", "썸네일");
        fakeReservationTimeDao.insert(reservationTime);
        fakeReservationTimeDao.insert(new ReservationTime(1L, LocalTime.of(11, 0)));
        fakeReservationRepository.insert(
                new Reservation(null, AFTER_ONE_DAYS_DATE, reservationTime, theme, ADMIN_MEMBER));

        List<BookableTimeResponse> bookableTimes = reservationService.findBookableTimes(
                new BookableTimesRequest(AFTER_ONE_DAYS_DATE, 1L));

        assertThat(bookableTimes.get(1).alreadyBooked()).isFalse();
    }
}
