package roomescape.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;

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
import roomescape.domain.theme.mapper.ThemeMapper;
import roomescape.domain.theme.repository.FakeThemeRepository;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.mapper.TimeMapper;
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
            assertThat(actual).containsExactly(
                new ReservationResponseDTO(1L, "제이콥", date, TimeMapper.toResponseDTO(time),
                    ThemeMapper.toResponseDTO(theme)),
                new ReservationResponseDTO(2L, "라이", date.plusDays(1),
                    TimeMapper.toResponseDTO(Time.reconstruct(2L, LocalTime.of(11, 0))),
                    ThemeMapper.toResponseDTO(theme)),
                new ReservationResponseDTO(3L, "티모", date.plusDays(2),
                    TimeMapper.toResponseDTO(Time.reconstruct(3L, LocalTime.of(12, 0))),
                    ThemeMapper.toResponseDTO(theme))
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
            assertThat(actual).isEqualTo(new ReservationCreateResponseDTO(1L, "보예", LocalDate.of(2026, 5, 1), 1L, 1L));
            assertThat(reservationRepository.findAllReservations()).hasSize(1);
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
            assertThat(actual)
                .hasSize(1)
                .extracting(ReservationResponseDTO::name)
                .containsExactly("시오");

        }
    }
}
