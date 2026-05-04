package roomescape.domain.reservation.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.dto.request.ReservationCreateRequestDTO;
import roomescape.domain.reservation.dto.response.ReservationResponseDTO;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.repository.FakeReservationRepository;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.FakeTimeRepository;
import roomescape.domain.time.repository.TimeRepository;

class ReservationServiceTest {

    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;

    ReservationServiceTest() {
        this.reservationRepository = new FakeReservationRepository();
        this.timeRepository = new FakeTimeRepository();
        this.reservationService = new ReservationService(reservationRepository, timeRepository);
    }

    @Nested
    class GetReservationTest {

        @Test
        void 성공() {

            // given
            LocalDate date = LocalDate.of(2026, 4, 30);
            Time time = Time.reconstruct(1L, LocalTime.of(10, 0));

            reservationRepository.save(Reservation.create("제이콥", date, time));
            reservationRepository.save(
                Reservation.create("라이", date.plusDays(1), Time.reconstruct(2L, LocalTime.of(11, 0))));
            reservationRepository.save(
                Reservation.create("티모", date.plusDays(2), Time.reconstruct(3L, LocalTime.of(12, 0))));

            // when
            List<ReservationResponseDTO> actual = reservationService.getReservations();

            // then
            assertAll(
                () -> assertEquals(3, actual.size()),
                () -> assertEquals(new ReservationResponseDTO(1L, "제이콥", date, time.toResponseDTO()), actual.get(0)),
                () -> assertEquals(
                    new ReservationResponseDTO(2L, "라이", date.plusDays(1),
                        Time.reconstruct(2L, LocalTime.of(11, 0)).toResponseDTO()),
                    actual.get(1)
                ),
                () -> assertEquals(
                    new ReservationResponseDTO(3L, "티모", date.plusDays(2),
                        Time.reconstruct(3L, LocalTime.of(12, 0)).toResponseDTO()),
                    actual.get(2)
                )
            );
        }
    }

    @Nested
    class SaveReservationTest {

        @Test
        void 성공() {

            // given
            ReservationCreateRequestDTO request = new ReservationCreateRequestDTO(
                "보예",
                LocalDate.of(2026, 5, 1),
                1L
            );
            timeRepository.save(Time.create(LocalTime.of(15, 30)));

            // when
            ReservationResponseDTO actual = reservationService.saveReservation(request);

            // then
            assertAll(
                () -> assertEquals(1L, actual.id()),
                () -> assertEquals("보예", actual.name()),
                () -> assertEquals(LocalDate.of(2026, 5, 1), actual.date()),
                () -> assertEquals(Time.reconstruct(1L, LocalTime.of(15, 30)).toResponseDTO(), actual.time()),
                () -> assertEquals(List.of(actual), reservationService.getReservations())
            );
        }
    }

    @Nested
    class DeleteReservationByIdTest {

        @Test
        void 성공() {

            // given
            Reservation savedReservation = reservationRepository.save(
                Reservation.create("제이슨", LocalDate.of(2026, 5, 2), Time.reconstruct(1L, LocalTime.of(12, 0)))
            );
            reservationRepository.save(
                Reservation.create("시오", LocalDate.of(2026, 5, 3), Time.reconstruct(2L, LocalTime.of(13, 0))));

            // when
            reservationService.deleteReservationById(savedReservation.getId());

            // then
            List<ReservationResponseDTO> actual = reservationService.getReservations();
            assertAll(
                () -> assertEquals(1, actual.size()),
                () -> assertEquals("시오", actual.getFirst().name())
            );

        }
    }
}
