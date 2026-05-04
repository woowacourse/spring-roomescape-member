package roomescape.time.service;

import org.junit.jupiter.api.Test;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationTimeServiceImplTest {

    private final FakeReservationTimeRepository fakeRepository = new FakeReservationTimeRepository();
    private final ReservationTimeServiceImpl reservationTimeService =
            new ReservationTimeServiceImpl(fakeRepository);

    @Test
    void 시간_목록을_조회하면_Repository_findAll_결과를_반환한다() {
        List<ReservationTime> times = List.of(new ReservationTime(1L, LocalTime.of(10, 0)));
        fakeRepository.toReturnAll = times;

        List<ReservationTime> result = reservationTimeService.getTimes();

        assertThat(result).isSameAs(times);
    }

    @Test
    void 시간_생성을_요청하면_시작_시간을_Repository에_전달하고_save_결과를_반환한다() {
        ReservationTime saved = new ReservationTime(1L, LocalTime.of(10, 0));
        fakeRepository.toReturnSaved = saved;

        ReservationTime result = reservationTimeService.createTime(LocalTime.of(10, 0));

        assertThat(result).isSameAs(saved);
        assertThat(fakeRepository.savedStartAt).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 시간_삭제를_요청하면_id를_Repository_remove에_전달한다() {
        reservationTimeService.removeTime(3L);

        assertThat(fakeRepository.removedId).isEqualTo(3L);
    }

    static class FakeReservationTimeRepository extends ReservationTimeRepository {

        List<ReservationTime> toReturnAll = List.of();
        ReservationTime toReturnSaved;
        LocalTime savedStartAt;
        Long removedId;

        FakeReservationTimeRepository() {
            super(null);
        }

        @Override
        public List<ReservationTime> findAll() {
            return toReturnAll;
        }

        @Override
        public ReservationTime save(LocalTime startAt) {
            this.savedStartAt = startAt;
            return toReturnSaved;
        }

        @Override
        public void remove(Long id) {
            this.removedId = id;
        }
    }
}
