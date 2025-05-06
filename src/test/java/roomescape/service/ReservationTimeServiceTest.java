package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.exception.ResourceNotExistException;
import roomescape.fake.FakeReservationDao;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeDao;
import roomescape.fake.FakeThemeDao;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static roomescape.fixture.TestFixture.*;

public class ReservationTimeServiceTest {

    FakeReservationTimeDao timeDao;
    ReservationRepository repository;
    ReservationTimeService reservationTimeService;

    public ReservationTimeServiceTest() {
        timeDao = new FakeReservationTimeDao();
        repository = new FakeReservationRepository(new FakeReservationDao(), timeDao, new FakeThemeDao());
        this.reservationTimeService = new ReservationTimeService(repository);
    }

    @BeforeEach
    void setUp() {
        timeDao.clear();
    }

    @Test
    void 예약_시간을_생성할_수_있다() {
        // given
        ReservationTimeRequest time = new ReservationTimeRequest(ONE_PM);

        // when & then
        assertDoesNotThrow(() -> reservationTimeService.save(time));
    }

    @Test
    void 중복된_시간이_존재하는_경우_시간을_생성할_수_없다() {
        // given
        ReservationTimeRequest time = new ReservationTimeRequest(DEFAULT_TIME.getStartAt());

        // when & then
        assertDoesNotThrow(() -> reservationTimeService.save(time));
        assertThatThrownBy(() -> reservationTimeService.save(time))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 해당 시간이 이미 존재합니다.");
    }

    @Test
    void 특정_시간에_대한_예약이_존재하는_경우_시간을_삭제할_수_없다() {
        // given
        ReservationTime savedTime = repository.saveReservationTime(DEFAULT_TIME);
        Theme savedTheme = repository.saveTheme(DEFAULT_THEME);
        repository.saveReservation(new Reservation("예약", TOMORROW, savedTime, savedTheme));

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(savedTime.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 해당 시간에 대한 예약이 존재하기 때문에 삭제할 수 없습니다.");
    }

    @Test
    void 존재하지_않는_예약_시간을_삭제하려는_예외를_발생시킨다() {
        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(-1L))
                .isInstanceOf(ResourceNotExistException.class);
    }
}
