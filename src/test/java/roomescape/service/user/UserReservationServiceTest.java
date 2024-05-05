package roomescape.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.service.fixture.LocalDateFixture.AFTER_ONE_DAYS_DATE;
import static roomescape.service.fixture.LocalTimeFixture.TEN_HOUR;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.BookableTimeResponse;
import roomescape.dto.BookableTimesRequest;
import roomescape.service.fakeDao.FakeReservationDao;
import roomescape.service.fakeDao.FakeReservationTimeDao;

class UserReservationServiceTest {

    UserReservationService userReservationService;
    FakeReservationDao fakeReservationDao;
    FakeReservationTimeDao fakeReservationTimeDao;

    @BeforeEach
    void setUp() {
        fakeReservationDao = new FakeReservationDao();
        fakeReservationTimeDao = new FakeReservationTimeDao();
        userReservationService = new UserReservationService(fakeReservationDao, fakeReservationTimeDao);
    }

    @DisplayName("예약 가능 시각을 알 수 있습니다.")
    @Test
    void should_know_bookable_times() {
        ReservationTime reservationTime = new ReservationTime(null, TEN_HOUR);
        Theme theme = new Theme(null, "테마1", "설명", "썸네일");
        fakeReservationTimeDao.insert(reservationTime);
        fakeReservationDao.insert(
                new Reservation(null, "dodo", AFTER_ONE_DAYS_DATE, reservationTime, theme));

        List<BookableTimeResponse> bookableTimes = userReservationService.findBookableTimes(
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
        fakeReservationDao.insert(
                new Reservation(null, "dodo", AFTER_ONE_DAYS_DATE, reservationTime, theme));

        List<BookableTimeResponse> bookableTimes = userReservationService.findBookableTimes(
                new BookableTimesRequest(AFTER_ONE_DAYS_DATE, 1L));

        assertThat(bookableTimes.get(1).alreadyBooked()).isFalse();
    }
}
