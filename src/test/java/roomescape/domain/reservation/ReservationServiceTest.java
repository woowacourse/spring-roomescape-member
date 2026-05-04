package roomescape.domain.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.dto.CreateReservationRequest;
import roomescape.domain.reservation.dto.CreateReservationResponse;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeRepository;
import roomescape.support.exception.RoomescapeException;

class ReservationServiceTest {

    @Test
    void 존재하는_예약_시간으로_예약을_생성한다() {
        // given
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        ReservationTime reservationTime = ReservationTime.of(1L, LocalTime.of(10, 0));
        reservationTimeRepository.reservationTime = reservationTime;
        ReservationService reservationService = new ReservationService(
            reservationRepository,
            reservationTimeRepository
        );
        CreateReservationRequest request = new CreateReservationRequest(
            "보예",
            LocalDate.of(2026, 5, 4),
            1L
        );

        // when
        CreateReservationResponse response = reservationService.createReservation(request);

        // then
        assertSoftly(softly -> {
            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.name()).isEqualTo("보예");
            assertThat(response.date()).isEqualTo(LocalDate.of(2026, 5, 4));
            assertThat(response.time()).isEqualTo(LocalTime.of(10, 0));
            assertThat(reservationRepository.savedReservation.getTime()).isEqualTo(reservationTime);
        });
    }

    @Test
    void 존재하지_않는_예약_시간으로_예약을_생성하면_예외가_발생한다() {
        // given
        ReservationService reservationService = new ReservationService(
            new FakeReservationRepository(),
            new FakeReservationTimeRepository()
        );
        CreateReservationRequest request = new CreateReservationRequest(
            "보예",
            LocalDate.of(2026, 5, 4),
            1L
        );

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("존재하지 않는 예약 시간대 입니다.");
    }

    @Test
    void 예약_목록을_조회한다() {
        // given
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        reservationRepository.findAllResult = List.of(
            Reservation.of(
                1L,
                "보예",
                LocalDate.of(2026, 5, 4),
                ReservationTime.of(2L, LocalTime.of(10, 0))
            )
        );
        ReservationService reservationService = new ReservationService(
            reservationRepository,
            reservationTimeRepository
        );

        // when
        List<ReservationResponse> responses = reservationService.getAllReservations();

        // then
        assertSoftly(softly -> {
            assertThat(responses).hasSize(1);
            assertThat(responses.getFirst().id()).isEqualTo(1L);
            assertThat(responses.getFirst().name()).isEqualTo("보예");
            assertThat(responses.getFirst().time().id()).isEqualTo(2L);
            assertThat(responses.getFirst().time().startAt()).isEqualTo(LocalTime.of(10, 0));
        });
    }

    private static class FakeReservationRepository implements ReservationRepository {

        private Reservation savedReservation;
        private List<Reservation> findAllResult = List.of();

        @Override
        public Reservation save(Reservation reservation) {
            savedReservation = reservation;
            return Reservation.of(1L, reservation.getName(), reservation.getDate(), reservation.getTime());
        }

        @Override
        public List<Reservation> findAll() {
            return findAllResult;
        }

        @Override
        public int deleteById(Long id) {
            return 0;
        }

        @Override
        public int countByTimeId(Long timeId) {
            return 0;
        }
    }

    private static class FakeReservationTimeRepository implements ReservationTimeRepository {

        private ReservationTime reservationTime;

        @Override
        public Optional<ReservationTime> findById(Long id) {
            return Optional.ofNullable(reservationTime);
        }

        @Override
        public ReservationTime save(ReservationTime reservationTime) {
            return null;
        }

        @Override
        public List<ReservationTime> findAll() {
            return List.of();
        }

        @Override
        public int deleteById(Long id) {
            return 0;
        }
    }
}
