package roomescape.reservation.service;

import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.exception.InvalidReservationTimeException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationServiceImplTest {

    private final FakeReservationRepository fakeReservationRepository = new FakeReservationRepository();
    private final FakeReservationTimeRepository fakeReservationTimeRepository = new FakeReservationTimeRepository();
    private final ReservationServiceImpl reservationService =
            new ReservationServiceImpl(fakeReservationRepository, fakeReservationTimeRepository);

    @Test
    void 예약_목록을_조회하면_Repository_findAllWithTime_결과를_반환한다() {
        List<Reservation> reservations = List.of(
                new Reservation(1L, "브라운", LocalDate.of(2026, 5, 10),
                        new ReservationTime(1L, LocalTime.of(10, 0)))
        );
        fakeReservationRepository.toReturnAllWithTime = reservations;

        List<Reservation> result = reservationService.getReservations();

        assertThat(result).isSameAs(reservations);
    }

    @Test
    void 유효한_시간_id로_예약을_생성하면_시간을_조회하고_저장된_예약을_반환한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        fakeReservationTimeRepository.stubFindById.put(1L, Optional.of(time));
        fakeReservationRepository.toReturnSavedId = 99L;

        Reservation result = reservationService.createReservation("브라운", LocalDate.of(2026, 5, 10), 1L);

        assertThat(result.getId()).isEqualTo(99L);
        assertThat(result.getName()).isEqualTo("브라운");
        assertThat(result.getDate()).isEqualTo(LocalDate.of(2026, 5, 10));
        assertThat(result.getTime()).isSameAs(time);
        assertThat(fakeReservationRepository.savedName).isEqualTo("브라운");
        assertThat(fakeReservationRepository.savedDate).isEqualTo(LocalDate.of(2026, 5, 10));
        assertThat(fakeReservationRepository.savedTimeId).isEqualTo(1L);
    }

    @Test
    void 존재하지_않는_시간_id로_예약을_생성하면_예외가_발생하고_저장하지_않는다() {
        fakeReservationTimeRepository.stubFindById.put(999L, Optional.empty());

        assertThatThrownBy(() ->
                reservationService.createReservation("브라운", LocalDate.of(2026, 5, 10), 999L))
                .isInstanceOf(InvalidReservationTimeException.class);

        assertThat(fakeReservationRepository.savedName).isNull();
    }

    @Test
    void 예약_삭제를_요청하면_id를_Repository_deleteById에_전달한다() {
        fakeReservationRepository.toReturnDeletedRows = 1;

        reservationService.deleteReservation(7L);

        assertThat(fakeReservationRepository.deletedId).isEqualTo(7L);
    }

    @Test
    void 존재하지_않는_id로_예약을_삭제하면_예외가_발생한다() {
        fakeReservationRepository.toReturnDeletedRows = 0;

        assertThatThrownBy(() -> reservationService.deleteReservation(999L))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    static class FakeReservationRepository extends ReservationRepository {

        List<Reservation> toReturnAllWithTime = List.of();
        Long toReturnSavedId;
        String savedName;
        LocalDate savedDate;
        Long savedTimeId;
        Long deletedId;
        int toReturnDeletedRows;

        FakeReservationRepository() {
            super(null);
        }

        @Override
        public List<Reservation> findAllWithTime() {
            return toReturnAllWithTime;
        }

        @Override
        public Long save(String name, LocalDate date, Long timeId) {
            this.savedName = name;
            this.savedDate = date;
            this.savedTimeId = timeId;
            return toReturnSavedId;
        }

        @Override
        public int deleteById(Long id) {
            this.deletedId = id;
            return toReturnDeletedRows;
        }
    }

    static class FakeReservationTimeRepository extends ReservationTimeRepository {

        Map<Long, Optional<ReservationTime>> stubFindById = new HashMap<>();

        FakeReservationTimeRepository() {
            super(null);
        }

        @Override
        public Optional<ReservationTime> findById(Long timeId) {
            return stubFindById.getOrDefault(timeId, Optional.empty());
        }
    }
}
