package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.application.service.ReservationTimeService;
import roomescape.domain.exception.ReservationExistException;
import roomescape.domain.exception.ReservationTimeDuplicatedException;
import roomescape.domain.exception.ResourceNotExistException;
import roomescape.domain.model.Reservation;
import roomescape.domain.model.ReservationTime;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeRepository;
import roomescape.presentation.dto.request.ReservationTimeRequest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static roomescape.fixture.TestFixture.*;

public class ReservationTimeServiceTest {

    ReservationRepository reservationRepository;
    ReservationTimeRepository reservationTimeRepository;
    ReservationTimeService reservationTimeService;

    public ReservationTimeServiceTest() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        this.reservationTimeService = new ReservationTimeService(reservationRepository, reservationTimeRepository);
    }

    @BeforeEach
    void setUp() {
        ((FakeReservationTimeRepository) reservationTimeRepository).clear();
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
                .isInstanceOf(ReservationTimeDuplicatedException.class);
    }

    @Test
    void 특정_시간에_대한_예약이_존재하는_경우_시간을_삭제할_수_없다() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(DEFAULT_TIME);
        reservationRepository.save(new Reservation(1L, TOMORROW, savedTime, DEFAULT_THEME));

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(savedTime.getId()))
                .isInstanceOf(ReservationExistException.class);
    }

    @Test
    void 존재하지_않는_예약_시간을_삭제하려는_예외를_발생시킨다() {
        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(-1L))
                .isInstanceOf(ResourceNotExistException.class);
    }
}
