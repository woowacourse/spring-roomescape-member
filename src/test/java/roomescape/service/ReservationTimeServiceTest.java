package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;
import roomescape.domain.fixture.ReservationTimeFixture;
import roomescape.global.exception.DuplicateEntityException;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.fake.FakeReservationTimeRepository;
import roomescape.web.dto.reservationTime.ReservationTimeRequest;
import roomescape.web.dto.reservationTime.ReservationTimeResponse;

class ReservationTimeServiceTest {

    private ReservationTimeRepository reservationTimeRepository;
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        this.reservationTimeRepository = new FakeReservationTimeRepository();
        this.reservationTimeService = new ReservationTimeService(this.reservationTimeRepository);
    }

    @Test
    void 새로운_예약_시간을_정상적으로_등록한다() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);
        ReservationTimeRequest request = new ReservationTimeRequest(startAt);

        // when
        ReservationTimeResponse response = reservationTimeService.register(request);

        // then
        assertThat(response)
                .extracting(ReservationTimeResponse::id, ReservationTimeResponse::startAt)
                .containsExactly(1L, startAt);
    }

    @Test
    void 이미_등록된_시간으로_등록을_시도하면_예외가_발생한다() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);
        reservationTimeRepository.save(ReservationTimeFixture.createReservationTime(startAt));

        ReservationTimeRequest request = new ReservationTimeRequest(startAt);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.register(request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("이미 등록된 예약 시간 입니다.");
    }

    @Test
    void 식별자를_이용해_예약_시간을_삭제한다() {
        // given
        ReservationTime saved = reservationTimeRepository.save(ReservationTimeFixture.createDefaultReservationTime());
        Long id = saved.getId();

        // when
        reservationTimeService.remove(id);

        // then
        assertThat(reservationTimeRepository.findAllByPaging(0, 10)).isEmpty();
    }

    @Test
    void 모든_예약_시간_목록을_조회한다() {
        // given
        reservationTimeRepository.save(ReservationTimeFixture.createDefaultReservationTime());
        reservationTimeRepository.save(ReservationTimeFixture.createReservationTime(LocalTime.of(11, 0)));

        // when
        List<ReservationTimeResponse> responses = reservationTimeService.getAllReservationTimesByPaging(0, 10);

        // then
        assertThat(responses).hasSize(2);
    }
}
