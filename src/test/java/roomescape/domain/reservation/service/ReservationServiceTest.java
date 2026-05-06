package roomescape.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.request.ReservationCreateRequest;
import roomescape.domain.reservation.response.ReservationResponse;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeRepository;

class ReservationServiceTest {

    private ReservationService reservationService;
    private FakeReservationTimeRepository fakeReservationTimeRepository;

    @BeforeEach
    void setUp() {
        fakeReservationTimeRepository = new FakeReservationTimeRepository();
        reservationService = new ReservationService(new FakeReservationRepository(), fakeReservationTimeRepository);
    }

    @Test
    @DisplayName("예약을 성공적으로 생성한다.")
    void saveReservation() {
        // given
        ReservationTime savedTime = fakeReservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        ReservationCreateRequest request = new ReservationCreateRequest(
                "브라운",
                LocalDate.of(2026, 4, 30),
                savedTime.getId()
        );

        // when
        ReservationResponse response = reservationService.saveReservation(request);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("브라운");
        assertThat(response.date()).isEqualTo(LocalDate.of(2026, 4, 30));
        assertThat(response.time().id()).isEqualTo(savedTime.getId());
    }

    @Test
    @DisplayName("존재하지 않는 시간으로 예약 시 예외가 발생한다.")
    void saveReservation_throwsException_whenTimeNotFound() {
        // given
        ReservationCreateRequest request = new ReservationCreateRequest(
                "브라운",
                LocalDate.of(2026, 4, 30),
                999L
        );

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 id의 ReservationTime이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("모든 예약을 조회한다.")
    void findAllReservations() {
        // given
        ReservationTime savedTime1 = fakeReservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        ReservationTime savedTime2 = fakeReservationTimeRepository.save(new ReservationTime(LocalTime.of(11, 0)));

        reservationService.saveReservation(
                new ReservationCreateRequest("브라운", LocalDate.of(2026, 4, 30), savedTime1.getId())
        );
        reservationService.saveReservation(
                new ReservationCreateRequest("크루", LocalDate.of(2026, 4, 30), savedTime2.getId())
        );

        // when
        List<ReservationResponse> responses = reservationService.findAllReservations();

        // then
        assertThat(responses).hasSize(2)
                .extracting("name", "date")
                .containsExactly(
                        tuple("브라운", LocalDate.of(2026, 4, 30)),
                        tuple("크루", LocalDate.of(2026, 4, 30))
                );
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void deleteReservationBy() {
        // given
        ReservationTime savedTime = fakeReservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        ReservationResponse savedReservation = reservationService.saveReservation(
                new ReservationCreateRequest("브라운", LocalDate.of(2026, 4, 30), savedTime.getId())
        );

        // when
        reservationService.deleteReservationBy(savedReservation.id());

        // then
        List<ReservationResponse> responses = reservationService.findAllReservations();
        assertThat(responses).isEmpty();
    }
}
