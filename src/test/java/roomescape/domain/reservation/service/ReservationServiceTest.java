package roomescape.domain.reservation.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.dto.request.ReservationCreateRequestDTO;
import roomescape.domain.reservation.dto.response.ReservationCreateResponseDTO;
import roomescape.domain.reservation.dto.response.ReservationResponseDTO;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.repository.FakeReservationRepository;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.FakeThemeRepository;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.FakeTimeRepository;
import roomescape.domain.time.repository.TimeRepository;

class ReservationServiceTest {

    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    ReservationServiceTest() {
        this.reservationRepository = new FakeReservationRepository();
        this.timeRepository = new FakeTimeRepository();
        this.themeRepository = new FakeThemeRepository();
        this.reservationService = new ReservationService(reservationRepository, timeRepository, themeRepository);
    }

    @Nested
    class GetReservationTest {

        @Test
        void 성공() {

            // given
            LocalDate date = LocalDate.of(2026, 4, 30);
            Time time = Time.reconstruct(1L, LocalTime.of(10, 0));
            Theme theme = Theme.reconstruct(1L, "테마 이름", "테마 설명",
                "https://roomescape.com/images/themes/ring-banner.png");

            reservationRepository.save(Reservation.create("제이콥", date, time, theme));
            reservationRepository.save(
                Reservation.create("라이", date.plusDays(1), Time.reconstruct(2L, LocalTime.of(11, 0)), theme));
            reservationRepository.save(
                Reservation.create("티모", date.plusDays(2), Time.reconstruct(3L, LocalTime.of(12, 0)), theme));

            // when
            List<ReservationResponseDTO> actual = reservationService.getReservations();

            // then
            assertAll(
                () -> assertEquals(3, actual.size()),
                () -> assertEquals(
                    new ReservationResponseDTO(1L, "제이콥", date, time.toResponseDTO(), theme.toResponseDTO()),
                    actual.get(0)),
                () -> assertEquals(
                    new ReservationResponseDTO(2L, "라이", date.plusDays(1),
                        Time.reconstruct(2L, LocalTime.of(11, 0)).toResponseDTO(), theme.toResponseDTO()),
                    actual.get(1)
                ),
                () -> assertEquals(
                    new ReservationResponseDTO(3L, "티모", date.plusDays(2),
                        Time.reconstruct(3L, LocalTime.of(12, 0)).toResponseDTO(), theme.toResponseDTO()),
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
                1L,
                1L
            );

            themeRepository.save(Theme.create("피온", "테마 설명", "https://roomescape.com/images/themes/ring-banner.png"));
            timeRepository.save(Time.create(LocalTime.of(15, 30)));

            // when
            ReservationCreateResponseDTO actual = reservationService.saveReservation(request);

            // then
            assertAll(
                () -> assertEquals(1L, actual.id()),
                () -> assertEquals("보예", actual.name()),
                () -> assertEquals(LocalDate.of(2026, 5, 1), actual.date()),
                () -> assertEquals(1L, actual.timeId()),
                () -> assertEquals(1L, actual.themeId()),
                () -> assertEquals(1, reservationRepository.findAllReservations().size())
            );
        }
    }

    @Nested
    class DeleteReservationByIdTest {

        @Test
        void 성공() {

            // given
            Reservation savedReservation = reservationRepository.save(
                Reservation.create("제이슨", LocalDate.of(2026, 5, 2), Time.reconstruct(1L, LocalTime.of(12, 0)),
                    Theme.reconstruct(1L, "테마 이름", "테마 설명", "https://roomescape.com/images/themes/ring-banner.png")));
            reservationRepository.save(
                Reservation.create("시오", LocalDate.of(2026, 5, 3), Time.reconstruct(2L, LocalTime.of(13, 0)),
                    Theme.reconstruct(1L, "테마 이름", "테마 설명", "https://roomescape.com/images/themes/ring-banner.png")));

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
