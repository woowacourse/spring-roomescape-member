package roomescape.domain.reservationdate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservationdate.dto.AdminReservationDateResponse;
import roomescape.domain.reservationdate.dto.ReservationDateCreationRequest;
import roomescape.domain.reservationdate.dto.ReservationDateCreationResponse;
import roomescape.support.exception.RoomescapeException;

class ReservationDateServiceTest {

    @Test
    void 예약_날짜를_생성한다() {
        // given
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        FakeReservationDateRepository reservationDateRepository = new FakeReservationDateRepository();
        ReservationDateService reservationDateService = new ReservationDateService(
            reservationRepository,
            reservationDateRepository
        );

        // when
        ReservationDateCreationResponse response = reservationDateService.createReservationDate(
            new ReservationDateCreationRequest(LocalDate.of(2026, 5, 4))
        );

        // then
        assertSoftly(
            softly -> {
                assertThat(response.id()).isEqualTo(1L);
                assertThat(response.playDay()).isEqualTo(LocalDate.of(2026, 5, 4));
                assertThat(reservationDateRepository.savedReservationDate.getPlayDay())
                    .isEqualTo(LocalDate.of(2026, 5, 4));
            }
        );
    }

    @Test
    void 예약_날짜_목록을_조회한다() {
        // given
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        FakeReservationDateRepository reservationDateRepository = new FakeReservationDateRepository();
        reservationDateRepository.findAllResult = List.of(
            ReservationDate.of(1L, LocalDate.of(2026, 5, 4))
        );
        ReservationDateService reservationDateService = new ReservationDateService(
            reservationRepository,
            reservationDateRepository
        );

        // when
        List<AdminReservationDateResponse> responses = reservationDateService.getAllReservationDateForAdmin();

        // then
        assertSoftly(softly -> {
            assertThat(responses.size()).isEqualTo(1);
            assertThat(responses.getFirst().id()).isEqualTo(1L);
            assertThat(responses.getFirst().playDay()).isEqualTo(LocalDate.of(2026, 5, 4));
        });
    }

    @Test
    void 이미_예약이_존재하는_날짜는_삭제할_수_없다() {
        // given
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        reservationRepository.countByReservationDateIdResult = 1;
        FakeReservationDateRepository reservationDateRepository = new FakeReservationDateRepository();
        ReservationDateService reservationDateService = new ReservationDateService(
            reservationRepository,
            reservationDateRepository
        );

        // when & then
        assertThatThrownBy(() -> reservationDateService.deleteReservationDate(1L))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("이미 예약이 존재하는 날짜는 삭제할 수 없습니다.");
    }

    @Test
    void 예약이_없는_날짜는_삭제한다() {
        // given
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        FakeReservationDateRepository reservationDateRepository = new FakeReservationDateRepository();
        ReservationDateService reservationDateService = new ReservationDateService(
            reservationRepository,
            reservationDateRepository
        );

        // when
        reservationDateService.deleteReservationDate(1L);

        // then
        assertThat(reservationDateRepository.deletedId).isEqualTo(1L);
    }

    private static class FakeReservationRepository implements ReservationRepository {

        private int countByReservationDateIdResult;

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
            return 0;
        }

        @Override
        public int countByReservationDateId(Long dateId) {
            return countByReservationDateIdResult;
        }

        @Override
        public List<Long> findReservedTimes(Long themeId, Long dateId) {
            return List.of();
        }

        @Override
        public int countByThemeId(Long id) {
            return 0;
        }
    }

    private static class FakeReservationDateRepository implements ReservationDateRepository {

        private ReservationDate savedReservationDate;
        private List<ReservationDate> findAllResult = List.of();
        private Long deletedId;

        @Override
        public Optional<ReservationDate> findById(Long id) {
            return Optional.empty();
        }

        @Override
        public List<ReservationDate> findAll() {
            return findAllResult;
        }

        @Override
        public ReservationDate save(ReservationDate reservationDate) {
            savedReservationDate = reservationDate;
            return ReservationDate.of(1L, reservationDate.getPlayDay());
        }

        @Override
        public int deleteById(Long id) {
            deletedId = id;
            return 1;
        }
    }
}
