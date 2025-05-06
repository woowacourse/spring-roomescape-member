package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationRequest;
import roomescape.exception.ResourceNotExistException;
import roomescape.fake.FakeReservationDao;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeDao;
import roomescape.fake.FakeThemeDao;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static roomescape.fixture.TestFixture.*;

public class ReservationServiceTest {

    FakeReservationDao reservationDao;
    ReservationRepository repository;
    ReservationService reservationService;

    public ReservationServiceTest() {
        reservationDao = new FakeReservationDao();
        repository = new FakeReservationRepository(reservationDao, new FakeReservationTimeDao(), new FakeThemeDao());
        this.reservationService = new ReservationService(repository);
    }

    @BeforeEach
    void setUp() {
        reservationDao.clear();
    }

    @Test
    void 예약을_생성할_수_있다() {
        // given
        ReservationTime time = repository.saveReservationTime(DEFAULT_TIME);
        Theme theme = repository.saveTheme(DEFAULT_THEME);

        // when
        ReservationRequest reservation = new ReservationRequest("예약", TOMORROW, time.getId(), theme.getId());

        // when & then
        assertDoesNotThrow(() -> reservationService.save(reservation));
    }

    @Test
    void 중복_예약을_시도하는_경우_예외를_발생시킨다() {
        // given
        ReservationTime time = repository.saveReservationTime(DEFAULT_TIME);
        Theme theme = repository.saveTheme(DEFAULT_THEME);

        // when
        ReservationRequest reservation = new ReservationRequest("예약", TOMORROW, time.getId(), theme.getId());
        reservationService.save(reservation);

        // when & then
        assertThatThrownBy(() -> reservationService.save(reservation))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 해당 날짜와 시간에 대한 예약이 이미 존재합니다.");
    }

    @Test
    void 과거_시간대를_예약하려는_경우_예외를_발생시킨다() {
        // given
        ReservationTime time = repository.saveReservationTime(DEFAULT_TIME);
        Theme theme = repository.saveTheme(DEFAULT_THEME);

        // when
        ReservationRequest reservation = new ReservationRequest("예약", YESTERDAY, time.getId(), theme.getId());

        // when & then
        assertThatThrownBy(() -> reservationService.save(reservation))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 현재보다 과거 시간에는 예약이 불가능합니다.");
    }

    @Test
    void 존재하지_않는_예약을_삭제하려는_예외를_발생시킨다() {
        // when & then
        assertThatThrownBy(() -> reservationService.deleteReservation(-1L))
                .isInstanceOf(ResourceNotExistException.class);
    }
}
