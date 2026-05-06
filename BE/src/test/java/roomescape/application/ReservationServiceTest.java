package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.customException.ReservationException;
import roomescape.global.exception.customException.ReservationTimeException;
import roomescape.FakeReservationRepository;
import roomescape.FakeReservationTimeRepository;
import roomescape.entity.ReservationTime;
import roomescape.entity.ReservationRepository;
import roomescape.entity.ReservationTimeRepository;
import roomescape.presentation.dto.ReservationRequest;
import roomescape.presentation.dto.ReservationResponse;

class ReservationServiceTest {

    private static final LocalDate TODAY = LocalDate.now();

    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository);
    }

    private ReservationTime createReservationTime() {
        return reservationTimeRepository.save(
                ReservationTime.of(null, LocalTime.of(10, 0))
        );
    }

    private ReservationRequest createReservationRequest(ReservationTime time) {
        return new ReservationRequest(
                "브라운",
                TODAY,
                time.getId()
        );
    }

    private ReservationResponse saveReservation(String name, LocalDate date, ReservationTime time) {
        ReservationRequest request = new ReservationRequest(name, date, time.getId());
        return reservationService.saveReservation(request);
    }

    @Test
    @DisplayName("예약을 저장한다")
    void saveReservation() {
        // given
        ReservationTime time = createReservationTime();
        ReservationRequest request = createReservationRequest(time);

        // when
        ReservationResponse savedReservation = reservationService.saveReservation(request);

        // then
        assertThat(savedReservation.time().id()).isEqualTo(time.getId());
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간으로 예약하면 예외가 발생한다")
    void saveReservationWithNotFoundTime() {
        // given
        ReservationRequest request = new ReservationRequest(
                "브라운",
                TODAY,
                999L
        );

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(ReservationTimeException.class);
    }

    @Test
    @DisplayName("예약 요청이 null이면 예외가 발생한다")
    void saveReservationWithNullRequest() {
        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(null))
                .isInstanceOf(ReservationException.class);
    }

    @Test
    @DisplayName("예약자 이름이 null이면 예외가 발생한다")
    void saveReservationWithNullName() {
        // given
        ReservationTime time = createReservationTime();
        ReservationRequest request = new ReservationRequest(
                null,
                TODAY,
                time.getId()
        );

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(ReservationException.class);
    }

    @Test
    @DisplayName("예약자 이름이 비어 있으면 예외가 발생한다")
    void saveReservationWithBlankName() {
        // given
        ReservationTime time = createReservationTime();
        ReservationRequest request = new ReservationRequest(
                " ",
                TODAY,
                time.getId()
        );

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(ReservationException.class);
    }

    @Test
    @DisplayName("예약 날짜가 null이면 예외가 발생한다")
    void saveReservationWithNullDate() {
        // given
        ReservationTime time = createReservationTime();
        ReservationRequest request = new ReservationRequest(
                "브라운",
                null,
                time.getId()
        );

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(ReservationException.class);
    }

    @Test
    @DisplayName("예약 시간 id가 null이면 예외가 발생한다")
    void saveReservationWithNullTimeId() {
        // given
        ReservationRequest request = new ReservationRequest(
                "브라운",
                TODAY,
                null
        );

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(ReservationTimeException.class);
    }

    @Test
    @DisplayName("예약 목록을 조회한다")
    void getReservations() {
        // given
        ReservationTime time = createReservationTime();
        ReservationResponse savedReservation = saveReservation(
                "브라운",
                TODAY,
                time
        );

        // when
        List<ReservationResponse> reservations = reservationService.getReservations();

        // then
        assertThat(reservations).hasSize(1);
        assertThat(reservations.get(0).id()).isEqualTo(savedReservation.id());
    }

    @Test
    @DisplayName("예약이 없으면 빈 목록을 조회한다")
    void getReservationsWhenEmpty() {
        // given & when
        List<ReservationResponse> reservations = reservationService.getReservations();

        // then
        assertThat(reservations).isEmpty();
    }

    @Test
    @DisplayName("예약을 삭제한다")
    void deleteReservation() {
        // given
        ReservationTime time = createReservationTime();
        ReservationResponse savedReservation = saveReservation(
                "브라운",
                TODAY,
                time
        );

        // when
        reservationService.deleteReservation(savedReservation.id());

        // then
        assertThat(reservationService.getReservations()).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제해도 예외가 발생하지 않는다")
    void deleteNotFoundReservation() {
        // when
        reservationService.deleteReservation(999L);

        // then
        assertThat(reservationService.getReservations()).isEmpty();
    }

    @Test
    @DisplayName("예약 id가 null이면 삭제할 때 예외가 발생한다")
    void deleteReservationWithNullId() {
        // when & then
        assertThatThrownBy(() -> reservationService.deleteReservation(null))
                .isInstanceOf(ReservationException.class);
    }
}
