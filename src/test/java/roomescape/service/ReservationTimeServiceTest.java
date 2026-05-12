package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;
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
        // given: 새로운 예약 시간 정보가 주어짐
        LocalTime startAt = LocalTime.of(10, 0);
        ReservationTimeRequest request = new ReservationTimeRequest(startAt);

        // when: 시간을 등록함
        ReservationTimeResponse response = reservationTimeService.register(request);

        // then: 새로운 ID를 발급해서 반환
        assertThat(response)
                .extracting(ReservationTimeResponse::id, ReservationTimeResponse::startAt)
                .containsExactly(1L, startAt);
    }

    @Test
    void 이미_등록된_시간으로_등록을_시도하면_예외가_발생한다() {
        // given: 10시 예약 시간이 이미 등록되어 있음
        LocalTime startAt = LocalTime.of(10, 0);
        reservationTimeRepository.save(new ReservationTime(startAt));

        ReservationTimeRequest request = new ReservationTimeRequest(startAt);

        // when & then: 중복된 시간 등록 시 DuplicateEntityException 발생
        assertThatThrownBy(() -> reservationTimeService.register(request))
                .isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("이미 등록된 예약 시간 입니다.");
    }

    @Test
    void 식별자를_이용해_예약_시간을_삭제한다() {
        // given: 삭제할 예약 시간이 저장되어 있음
        ReservationTime saved = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        Long id = saved.getId();

        // when: 해당 ID로 삭제를 요청함
        reservationTimeService.remove(id);

        // then: 전체 조회 시 해당 데이터가 존재하지 않음
        assertThat(reservationTimeRepository.findAllByPaging(0, 10)).isEmpty();
    }

    @Test
    void 모든_예약_시간_목록을_조회한다() {
        // given: 10시와 11시 예약 시간이 저장되어 있음
        reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        reservationTimeRepository.save(new ReservationTime(LocalTime.of(11, 0)));

        // when: 전체 조회를 요청함
        List<ReservationTimeResponse> responses = reservationTimeService.getAllReservationTimesByPaging(0, 10);

        // then: 저장된 2개의 시간 정보가 반환됨
        assertThat(responses).hasSize(2);
    }
}
