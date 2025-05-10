package roomescape.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.reservation.ReservationResponseDto;
import roomescape.dto.time.ReservationTimeResponseDto;
import roomescape.exception.DuplicateContentException;
import roomescape.exception.InvalidRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.*;
import roomescape.service.dto.ReservationCreateDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ReservationServiceTest {

    ReservationService reservationService;

    @Nested
    @DisplayName("예약 생성")
    class CreateReservation {

        static Stream<Arguments> invalidNames() {
            return Stream.of(
                    Arguments.of(" "),
                    Arguments.of(""),
                    Arguments.of((String) null)
            );
        }

        @DisplayName("요청에 따라 Reservation을 생성 할 수 있다")
        @Test
        void createReservationTest() {
            LocalTime startTime = LocalTime.now();

            ReservationRepository reservationRepository = new FakeReservationRepository(new ArrayList<>());
            ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository(List.of(new ReservationTime(1L, startTime)));
            ThemeRepository themeRepository = new FakeThemeRepository(List.of(new Theme(1L, "우테코", "방탈출", ".png")));
            reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);

            ReservationCreateDto requestDto = new ReservationCreateDto("가이온", LocalDate.now().plusDays(7), 1L, 1L);
            ReservationResponseDto responseDto = reservationService.createReservation(requestDto);

            Long id = responseDto.id();
            LocalDate date = responseDto.date();
            String name = requestDto.name();
            ReservationTimeResponseDto time = responseDto.time();
            Long timeId = time.id();
            LocalTime localTime = time.startAt();

            Assertions.assertAll(
                    () -> assertThat(id).isEqualTo(1L),
                    () -> assertThat(date).isEqualTo(requestDto.date()),
                    () -> assertThat(name).isEqualTo("가이온"),
                    () -> assertThat(timeId).isEqualTo(1L),
                    () -> assertThat(localTime).isEqualTo(startTime)
            );
        }

        @DisplayName("요청한 ReservationTime의 id가 존재하지 않으면 Reservation을 생성할 수 없다")
        @Test
        void createInvalidReservationIdTest() {
            ReservationRepository reservationRepository = new FakeReservationRepository(new ArrayList<>());
            ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository(new ArrayList<>());
            ThemeRepository themeRepository = new FakeThemeRepository(List.of(new Theme(1L, "우테코", "방탈출", ".png")));
            reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);

            ReservationCreateDto requestDto = new ReservationCreateDto("가이온", LocalDate.now(), 1L, 1L);

            assertThatThrownBy(() -> reservationService.createReservation(requestDto)).isInstanceOf(NotFoundException.class);
        }

        @DisplayName("이름이 공백이거나 존재하지 않으면 Reservation을 생성할 수 없다")
        @ParameterizedTest
        @MethodSource("invalidNames")
        void createInvalidNameTest(String name) {
            ReservationRepository reservationRepository = new FakeReservationRepository(new ArrayList<>());
            ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository(List.of(new ReservationTime(1L, LocalTime.now())));
            ThemeRepository themeRepository = new FakeThemeRepository(List.of(new Theme(1L, "우테코", "방탈출", ".png")));
            reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);

            ReservationCreateDto requestDto = new ReservationCreateDto(name, LocalDate.now().plusDays(7), 1L, 1L);

            assertThatThrownBy(() -> reservationService.createReservation(requestDto)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("이미 동일한 날짜와 시간에 예약이 있으면 생성할 수 없다")
        @Test
        void createDuplicateReservationTest() {
            LocalTime startTime = LocalTime.now();
            ArrayList<Reservation> reservations = new ArrayList<>();
            reservations.add(new Reservation(1L, "가이온", LocalDate.now().plusDays(7),
                    new ReservationTime(1L, startTime),
                    new Theme(1L, "우테코", "방탈출", ".png")));

            ReservationRepository reservationRepository = new FakeReservationRepository(reservations);
            FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository(List.of(new ReservationTime(1L, startTime)));
            ThemeRepository themeRepository = new FakeThemeRepository(List.of(new Theme(1L, "우테코", "방탈출", ".png")));
            reservationTimeRepository.addReservation(
                    new Reservation(1L, "가이온", LocalDate.now().plusDays(7),
                            new ReservationTime(1L, startTime),
                            new Theme(1L, "우테코", "방탈출", ".png")));
            reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);

            ReservationCreateDto requestDto = new ReservationCreateDto("가이온", LocalDate.now().plusDays(7), 1L, 1L);

            assertThatThrownBy(() -> reservationService.createReservation(requestDto)).isInstanceOf(DuplicateContentException.class);
        }

        @DisplayName("이미 지난 날짜의 경우 예약 생성이 불가능 하다")
        @Test
        void createInvalidDateTest() {
            ReservationRepository reservationRepository = new FakeReservationRepository(new ArrayList<>());
            FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository(List.of(new ReservationTime(1L, LocalTime.now())));
            ThemeRepository themeRepository = new FakeThemeRepository(List.of(new Theme(1L, "우테코", "방탈출", ".png")));
            reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);

            ReservationCreateDto requestDto = new ReservationCreateDto("가이온", LocalDate.of(2025, 1, 1), 1L, 1L);

            assertThatThrownBy(() -> reservationService.createReservation(requestDto)).isInstanceOf(InvalidRequestException.class);
        }

        @DisplayName("예약 시간에 같은 테마가 이미 예약 중이지 않다면 예약이 가능하다")
        @Test
        void createSameTimeButOtherTheme() {
            LocalTime startTime = LocalTime.now();
            ArrayList<Reservation> reservations = new ArrayList<>();
            reservations.add(new Reservation(1L, "가이온", LocalDate.now().plusDays(7),
                    new ReservationTime(1L, startTime),
                    new Theme(1L, "우테코1", "방탈출", ".png")));

            ReservationRepository reservationRepository = new FakeReservationRepository(reservations);
            FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository(List.of(new ReservationTime(1L, startTime)));
            ThemeRepository themeRepository = new FakeThemeRepository(List.of(new Theme(1L, "우테코", "방탈출", ".png"), new Theme(2L, "우테코", "방탈출", ".png")));
            reservationTimeRepository.addReservation(
                    new Reservation(1L, "가이온", LocalDate.now().plusDays(7),
                            new ReservationTime(1L, startTime),
                            new Theme(1L, "우테코", "방탈출", ".png")));

            reservationTimeRepository.addReservation(
                    new Reservation(1L, "가이온", LocalDate.now().plusDays(7),
                            new ReservationTime(1L, startTime),
                            new Theme(1L, "우테코", "방탈출", ".png")));
            reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);

            ReservationCreateDto requestDto = new ReservationCreateDto("가이온", LocalDate.now().plusDays(7), 1L, 2L);

            assertDoesNotThrow(() -> reservationService.createReservation(requestDto));
        }
    }

    @Nested
    @DisplayName("예약 조회")
    class FindReservation {

        @DisplayName("모든 Reservation을 조회할 수 있다")
        @Test
        void findAllReservationResponsesTest() {
            LocalTime startTime = LocalTime.of(10, 0);
            ReservationTime reservationTime = new ReservationTime(1L, startTime);
            Theme theme = new Theme(1L, "우테코", "방탈출", ".png");

            Reservation reservation1 = new Reservation(1L, "가이온", LocalDate.of(2025, 4, 24), reservationTime, theme);
            Reservation reservation2 = new Reservation(2L, "홍길동", LocalDate.of(2025, 4, 25), reservationTime, theme);

            ReservationRepository reservationRepository = new FakeReservationRepository(new ArrayList<>(List.of(reservation1, reservation2)));
            ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository(List.of(reservationTime));
            ThemeRepository themeRepository = new FakeThemeRepository(List.of(theme));
            ReservationService reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);

            List<ReservationResponseDto> responses = reservationService.findAllReservationResponses();

            assertThat(responses).hasSize(2);
            assertThat(responses).extracting("name").containsExactly("가이온", "홍길동");
        }
    }

    @Nested
    @DisplayName("예약 삭제")
    class DeleteReservation {

        @DisplayName("Reservation을 삭제할 수 있다")
        @Test
        void deleteReservationTest() {
            LocalTime startTime = LocalTime.of(10, 0);
            ReservationTime reservationTime = new ReservationTime(1L, startTime);
            Theme theme = new Theme(1L, "우테코", "방탈출", ".png");
            Reservation reservation = new Reservation(1L, "가이온", LocalDate.of(2025, 4, 24), reservationTime, theme);

            ReservationRepository reservationRepository = new FakeReservationRepository(new ArrayList<>(List.of(reservation)));
            ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository(List.of(reservationTime));
            ThemeRepository themeRepository = new FakeThemeRepository(List.of(theme));
            ReservationService reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);

            reservationService.deleteReservation(1L);

            List<ReservationResponseDto> responses = reservationService.findAllReservationResponses();
            assertThat(responses).isEmpty();
        }

        @DisplayName("존재하지 않는 Id의 Reservation을 삭제할 수 없다")
        @Test
        void deleteInvalidReservationIdTest() {
            LocalTime startTime = LocalTime.of(10, 0);
            ReservationTime reservationTime = new ReservationTime(1L, startTime);
            Theme theme = new Theme(1L, "우테코", "방탈출", ".png");
            Reservation reservation = new Reservation(1L, "가이온", LocalDate.of(2025, 4, 24), reservationTime, theme);

            ReservationRepository reservationRepository = new FakeReservationRepository(new ArrayList<>(List.of(reservation)));
            ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository(List.of(reservationTime));
            ThemeRepository themeRepository = new FakeThemeRepository(List.of(theme));
            ReservationService reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);

            assertThatThrownBy(() -> reservationService.deleteReservation(2L)).isInstanceOf(NotFoundException.class);
        }
    }
}
