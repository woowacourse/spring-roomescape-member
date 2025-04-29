package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.repository.ReservationRepository;


class ReservationServiceTest {

    private final FakeReservationRepository fakeReservationRepository = new FakeReservationRepository();
    private final ReservationService reservationService = new ReservationService(fakeReservationRepository);

    @AfterEach
    void tearDown() {
        fakeReservationRepository.deleteAll();
    }

    @Test
    @DisplayName("날짜와 시간이 모두 중복되면 예외가 발생한다.")
    void whenDuplicateDateAndTimeThrowException() {
        // given
        ReservationRequest existedRequest = new ReservationRequest("lemon", LocalDate.of(2025, 4, 18),
                1L);
        ReservationRequest newRequest = new ReservationRequest("lemon", LocalDate.of(2025, 4, 18),
                1L);
        // when
        reservationService.createReservation(existedRequest);
        // then
        Assertions.assertThatThrownBy(() -> reservationService.createReservation(newRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 예약 목록을 가져온다.")
    void findAllReservations() {
        // given
        fakeReservationRepository.save(
                new Reservation(1L, "Lemon", LocalDate.of(2025, 4, 22),
                        new ReservationTime(1L, LocalTime.of(10, 0)))
        );
        fakeReservationRepository.save(
                new Reservation(2L, "DDingHwa", LocalDate.of(2025, 4, 22),
                        new ReservationTime(2L, LocalTime.of(12, 0)))
        );

        // when
        List<ReservationResponse> reservations = reservationService.getReservations();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(reservations).hasSize(2);
            softAssertions.assertThat(reservations.getFirst().id()).isEqualTo(1);
            softAssertions.assertThat(reservations.getFirst().name()).isEqualTo("Lemon");
            softAssertions.assertThat(reservations.getFirst().date()).isEqualTo(LocalDate.of(2025, 4, 22));
            softAssertions.assertThat(reservations.getFirst().time().startAt()).isEqualTo(LocalTime.of(10, 0));
        });
    }

    @Test
    @DisplayName("아이디를 통해 예약을 삭제한다.")
    void deleteReservationById() {
        // given
        fakeReservationRepository.save(
                new Reservation(1L, "Lemon", LocalDate.of(2025, 4, 22),
                        new ReservationTime(1L, LocalTime.of(10, 0)))
        );
        fakeReservationRepository.save(
                new Reservation(2L, "DDingHwa", LocalDate.of(2025, 4, 22),
                        new ReservationTime(2L, LocalTime.of(12, 0)))
        );
        long validId = 1;
        // when
        reservationService.delete(validId);

        // then
        Reservation deletedReservation = fakeReservationRepository.findById(validId);
        Assertions.assertThat(deletedReservation).isNull();
    }

    @Test
    @DisplayName("존재하지 않는 아이디를 삭제시 예외 발생")
    void deleteException() {
        // given
        Long invalidId = 1231L;

        // when & then
        assertThatThrownBy(() -> reservationService.delete(invalidId)).
                isInstanceOf(NoSuchElementException.class);
    }

    //todo: @ActiveProfiles(value = "test") 로 테스트시 의존성을 부여하는 방법에 대해 찾아보기
    static class FakeReservationRepository implements ReservationRepository {

        List<Reservation> reservations = new ArrayList<>();
        private final AtomicLong atomicLong = new AtomicLong(1);

        public FakeReservationRepository() {

        }

        @Override
        public Reservation findById(Long id) {
            return reservations.stream()
                    .filter(reservation -> reservation.getId() == id)
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public List<Reservation> findAll() {
            return reservations;
        }

        @Override
        public Reservation save(Reservation reservation) {
            reservations.add(reservation);
            return reservation;
        }

        @Override
        public int deleteById(Long id) {
            boolean existingId = reservations.stream().anyMatch(reservation -> reservation.getId() == id);
            if (existingId) {
                reservations.removeIf(reservation -> reservation.getId() == id);
                return 1;
            }
            return 0;
        }

        @Override
        public boolean isDuplicateDateAndTime(LocalDate date, Long timeId) {
            return reservations.stream().anyMatch(
                    reservation -> reservation.getDate().equals(date) && reservation.getTime().getId().equals(timeId)
            );
        }

        @Override
        public boolean existByTimeId(Long id) {
            return reservations.stream()
                    .anyMatch(reservation -> reservation.getTime().getId().equals(id));
        }

        public void deleteAll() {
            reservations.clear();
        }
    }
}