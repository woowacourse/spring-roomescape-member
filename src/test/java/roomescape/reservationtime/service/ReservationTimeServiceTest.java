package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.stub.StubReservationTimeRepository;

class ReservationTimeServiceTest {

    private final LocalTime time1 = LocalTime.of(10, 0);
    private final LocalTime time2 = LocalTime.of(11, 0);

    private final StubReservationTimeRepository stubRepo =
            new StubReservationTimeRepository(
                    new ReservationTime(1L, time1),
                    new ReservationTime(2L, time2)
            );

    private final ReservationTimeService service = new ReservationTimeService(stubRepo);


    @Test
    void 예약_시간이_저장된다() {
        // given
        LocalTime newTime = LocalTime.of(12, 30);

        // when
        ReservationTimeResponse response = service.saveTime(newTime);

        // then
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(response.startAt()).isEqualTo(newTime);
            soft.assertThat(stubRepo.findAll())
                    .extracting(ReservationTime::getStartAt)
                    .contains(newTime);
        });
    }

    @Test
    void 모든_예약_시간을_조회한다() {
        // when
        List<ReservationTimeResponse> all = service.findAll();

        // then
        assertThat(all)
                .hasSize(2)
                .extracting(ReservationTimeResponse::startAt)
                .containsExactlyInAnyOrder(time1, time2);
    }

    @Test
    void 예약_시간이_삭제된다() {
        // given
        assertThat(service.findAll()).hasSize(2);

        // when
        service.delete(1L);

        // then
        List<ReservationTimeResponse> afterDelete = service.findAll();
        assertThat(afterDelete)
                .hasSize(1)
                .extracting(ReservationTimeResponse::id)
                .doesNotContain(1L);
    }

    @Test
    void 아이디로_예약시간을_찾는다() {
        // given
        Optional<ReservationTime> found = stubRepo.findById(1L);

        // when
        // then
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(found).isPresent();
            soft.assertThat(found.get().getStartAt()).isEqualTo(LocalTime.of(10, 0));
        });

    }

    @Test
    void 존재하지_않는_아이디면_빈_옵셔널을_반환한다() {
        // given
        Optional<ReservationTime> found = stubRepo.findById(999L);

        // when
        // then
        assertThat(found).isEmpty();
    }
}
