package roomescape.domain.reservationtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservationtime.dto.CreateTimeRequest;
import roomescape.domain.reservationtime.dto.CreateTimeResponse;
import roomescape.domain.reservationtime.dto.ReservationTimeResponse;
import roomescape.support.exception.RoomescapeException;

class ReservationTimeServiceTest {

    @Test
    void 예약_시간을_생성한다() {
        // given
        FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        ReservationTimeService reservationTimeService = new ReservationTimeService(
            reservationTimeRepository,
            reservationRepository
        );

        // when
        CreateTimeResponse response = reservationTimeService.createReservationTime(
            new CreateTimeRequest(LocalTime.of(10, 0))
        );

        // then
        assertSoftly(softly -> {
            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.startAt()).isEqualTo(LocalTime.of(10, 0));
            assertThat(reservationTimeRepository.savedReservationTime.getStartAt()).isEqualTo(LocalTime.of(10, 0));
        });
    }

    @Test
    void 예약_시간_목록을_조회한다() {
        // given
        FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        reservationTimeRepository.findAllResult = List.of(
            ReservationTime.of(1L, LocalTime.of(10, 0))
        );
        ReservationTimeService reservationTimeService = new ReservationTimeService(
            reservationTimeRepository,
            new FakeReservationRepository()
        );

        // when
        List<ReservationTimeResponse> responses = reservationTimeService.getAllReservationTime();

        // then
        assertSoftly(softly -> {
            assertThat(responses).hasSize(1);
            assertThat(responses.getFirst().id()).isEqualTo(1L);
            assertThat(responses.getFirst().startAt()).isEqualTo(LocalTime.of(10, 0));
        });
    }

    @Test
    void 이미_예약이_존재하는_시간은_삭제할_수_없다() {
        // given
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        reservationRepository.countByTimeIdResult = 1;
        FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        ReservationTimeService reservationTimeService = new ReservationTimeService(
            reservationTimeRepository,
            reservationRepository
        );

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("이미 예약이 존재하는 시간대는 삭제할 수 없습니다.");
    }

    @Test
    void 예약이_없는_시간은_삭제한다() {
        // given
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        ReservationTimeService reservationTimeService = new ReservationTimeService(
            reservationTimeRepository,
            reservationRepository
        );

        // when
        reservationTimeService.deleteReservationTime(1L);

        // then
        assertThat(reservationTimeRepository.deletedId).isEqualTo(1L);
    }

    private static class FakeReservationTimeRepository implements ReservationTimeRepository {

        private ReservationTime savedReservationTime;
        private List<ReservationTime> findAllResult = List.of();
        private Long deletedId;

        @Override
        public ReservationTime save(ReservationTime reservationTime) {
            savedReservationTime = reservationTime;
            return ReservationTime.of(1L, reservationTime.getStartAt());
        }

        @Override
        public List<ReservationTime> findAll() {
            return findAllResult;
        }

        @Override
        public int deleteById(Long id) {
            deletedId = id;
            return 1;
        }

        @Override
        public Optional<ReservationTime> findById(Long id) {
            return Optional.empty();
        }
    }

    private static class FakeReservationRepository implements ReservationRepository {

        private int countByTimeIdResult;

        @Override
        public Reservation save(Reservation reservation) {
            return null;
        }

        @Override
        public List<Reservation> findAll() {
            return List.of();
        }

        @Override
        public int deleteById(Long id) {
            return 0;
        }

        @Override
        public int countByTimeId(Long timeId) {
            return countByTimeIdResult;
        }

        @Override
        public int countByReservationDateId(Long dateId) {
            return 0;
        }

        @Override
        public List<Long> findReservedTimes(Long themeId, Long dateId) {
            return List.of();
        }
    }
}
