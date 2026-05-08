package roomescape.domain.reservation.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.dto.request.ReservationCreateRequestDto;
import roomescape.domain.reservation.dto.response.ReservationCreateResponseDto;
import roomescape.domain.reservation.dto.response.ReservationResponseDto;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.repository.FakeReservationRepository;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.dto.response.ThemeResponseDto;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.FakeThemeRepository;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.dto.response.TimeResponseDto;
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
        this.reservationService = new ReservationService(reservationRepository, timeRepository,
            themeRepository);
    }

    @Nested
    @DisplayName("getReservation 테스트")
    class GetReservationsTest {

        @Test
        @DisplayName("모든 예약을 조회한다.")
        void 성공() {

            // given
            LocalDate date = LocalDate.of(2026, 4, 30);
            Time time = Time.reconstruct(1L, LocalTime.of(10, 0));
            Theme theme = Theme.reconstruct(1L, "테마 이름", "테마 설명",
                "https://roomescape.com/images/themes/ring-banner.png");

            reservationRepository.save(Reservation.create("제이콥", date, time, theme));
            reservationRepository.save(
                Reservation.create("라이", date.plusDays(1),
                    Time.reconstruct(2L, LocalTime.of(11, 0)), theme));
            reservationRepository.save(
                Reservation.create("티모", date.plusDays(2),
                    Time.reconstruct(3L, LocalTime.of(12, 0)), theme));

            // when
            List<ReservationResponseDto> actual = reservationService.getReservations();

            // then
            assertAll(
                () -> assertEquals(3, actual.size()),
                () -> assertEquals(
                    new ReservationResponseDto(1L, "제이콥", date, TimeResponseDto.from(time),
                        ThemeResponseDto.from(theme)),
                    actual.get(0)),
                () -> assertEquals(
                    new ReservationResponseDto(2L, "라이", date.plusDays(1),
                        TimeResponseDto.from(Time.reconstruct(2L, LocalTime.of(11, 0))),
                        ThemeResponseDto.from(theme)),
                    actual.get(1)
                ),
                () -> assertEquals(
                    new ReservationResponseDto(3L, "티모", date.plusDays(2),
                        TimeResponseDto.from(Time.reconstruct(3L, LocalTime.of(12, 0))),
                        ThemeResponseDto.from(theme)),
                    actual.get(2)
                )
            );
        }
    }

    @Nested
    @DisplayName("saveReservation 테스트")
    class SaveReservationTest {

        @Test
        @DisplayName("예약을 생성하고, 생성된 예약을 반환한다.")
        void 성공() {

            // given
            ReservationCreateRequestDto request = new ReservationCreateRequestDto(
                "보예",
                LocalDate.of(2026, 5, 1),
                1L,
                1L
            );

            themeRepository.save(Theme.create("피온", "테마 설명",
                "https://roomescape.com/images/themes/ring-banner.png"));
            timeRepository.save(Time.create(LocalTime.of(15, 30)));

            // when
            ReservationCreateResponseDto actual = reservationService.saveReservation(request);

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
    @DisplayName("deleteReservationById 테스트")
    class DeleteReservationByIdTest {

        @Test
        @DisplayName("주어진 아이디를 가진 예약을 삭제한다.")
        void 성공() {

            // given
            Reservation savedReservation = reservationRepository.save(
                Reservation.create("제이슨", LocalDate.of(2026, 5, 2),
                    Time.reconstruct(1L, LocalTime.of(12, 0)),
                    Theme.reconstruct(1L, "테마 이름", "테마 설명",
                        "https://roomescape.com/images/themes/ring-banner.png")));
            reservationRepository.save(
                Reservation.create("시오", LocalDate.of(2026, 5, 3),
                    Time.reconstruct(2L, LocalTime.of(13, 0)),
                    Theme.reconstruct(1L, "테마 이름", "테마 설명",
                        "https://roomescape.com/images/themes/ring-banner.png")));

            // when
            reservationService.deleteReservationById(savedReservation.getId());

            // then
            List<ReservationResponseDto> actual = reservationService.getReservations();
            assertAll(
                () -> assertEquals(1, actual.size()),
                () -> assertEquals("시오", actual.getFirst().name())
            );

        }
    }
}
