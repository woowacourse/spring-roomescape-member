package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.application.service.ReservationService;
import roomescape.domain.exception.ReservationDuplicatedException;
import roomescape.domain.exception.ResourceNotExistException;
import roomescape.domain.model.ReservationTime;
import roomescape.domain.model.Theme;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.domain.repository.ThemeRepository;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeRepository;
import roomescape.fake.FakeThemeRepository;
import roomescape.presentation.dto.request.ReservationRequest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static roomescape.fixture.TestFixture.*;

public class ReservationServiceTest {

    ReservationRepository reservationRepository;
    ReservationTimeRepository reservationTimeRepository;
    ThemeRepository themeRepository;
    ReservationService reservationService;

    public ReservationServiceTest() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        themeRepository = new FakeThemeRepository();
        this.reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);
    }

    @BeforeEach
    void setUp() {
        ((FakeReservationRepository) reservationRepository).clear();
    }

    @Test
    void 예약을_생성할_수_있다() {
        // given
        ReservationTime time = reservationTimeRepository.save(DEFAULT_TIME);
        Theme theme = themeRepository.save(DEFAULT_THEME);

        // when
        ReservationRequest reservation = new ReservationRequest("예약", TOMORROW, time.getId(), theme.getId());

        // when & then
        assertDoesNotThrow(() -> reservationService.save(reservation));
    }

    @Test
    void 중복_예약을_시도하는_경우_예외를_발생시킨다() {
        // given
        ReservationTime time = reservationTimeRepository.save(DEFAULT_TIME);
        Theme theme = themeRepository.save(DEFAULT_THEME);

        // when
        ReservationRequest reservation = new ReservationRequest("예약", TOMORROW, time.getId(), theme.getId());
        reservationService.save(reservation);

        // when & then
        assertThatThrownBy(() -> reservationService.save(reservation))
                .isInstanceOf(ReservationDuplicatedException.class);
    }

    @Test
    void 과거_시간대를_예약하려는_경우_예외를_발생시킨다() {
        // given
        ReservationTime time = reservationTimeRepository.save(DEFAULT_TIME);
        Theme theme = themeRepository.save(DEFAULT_THEME);

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
