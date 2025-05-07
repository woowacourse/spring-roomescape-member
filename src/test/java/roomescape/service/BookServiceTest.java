package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.dto.ReservationCreateRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.exception.DuplicateContentException;
import roomescape.exception.InvalidRequestException;
import roomescape.exception.NotFoundException;
import roomescape.fixture.FakeReservationRepositoryFixture;
import roomescape.fixture.FakeReservationTimeRepositoryFixture;
import roomescape.fixture.FakeThemeRepositoryFixture;
import roomescape.repository.FakeReservationRepository;
import roomescape.repository.FakeReservationTimeRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookServiceTest {

    BookService bookService;

    @Nested
    @DisplayName("예약 생성")
    class CreateReservation {

        @DisplayName("요청에 따라 Reservation을 생성 할 수 있다")
        @Test
        void createReservationTest() {
            LocalTime startTime = LocalTime.of(10, 0);

            ReservationRepository reservationRepository = new FakeReservationRepository(new ArrayList<>());
            ReservationTimeRepository reservationTimeRepository = FakeReservationTimeRepositoryFixture.create();
            ThemeRepository themeRepository = FakeThemeRepositoryFixture.create();
            bookService = new BookService(reservationRepository, reservationTimeRepository, themeRepository);

            ReservationCreateRequestDto requestDto = new ReservationCreateRequestDto("가이온", LocalDate.now().plusDays(7), 1L, 1L);
            ReservationResponseDto responseDto = bookService.createReservation(requestDto);

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
        void invalidReservationTimeIdTest() {
            ReservationRepository reservationRepository = new FakeReservationRepository(new ArrayList<>());
            ReservationTimeRepository reservationTimeRepository = FakeReservationTimeRepositoryFixture.create();
            ThemeRepository themeRepository = FakeThemeRepositoryFixture.create();
            bookService = new BookService(reservationRepository, reservationTimeRepository, themeRepository);

            ReservationCreateRequestDto requestDto = new ReservationCreateRequestDto("가이온", LocalDate.now().plusDays(7), 10L, 1L);

            assertThatThrownBy(() -> bookService.createReservation(requestDto)).isInstanceOf(NotFoundException.class);
        }

        @DisplayName("요청한 Theme의 id가 존재하지 않으면 Reservation을 생성할 수 없다")
        @Test
        void invalidThemeIdTest() {
            ReservationRepository reservationRepository = new FakeReservationRepository(new ArrayList<>());
            ReservationTimeRepository reservationTimeRepository = FakeReservationTimeRepositoryFixture.create();
            ThemeRepository themeRepository = FakeThemeRepositoryFixture.create();
            bookService = new BookService(reservationRepository, reservationTimeRepository, themeRepository);

            ReservationCreateRequestDto requestDto = new ReservationCreateRequestDto("가이온", LocalDate.now().plusDays(7), 1L, 10L);

            assertThatThrownBy(() -> bookService.createReservation(requestDto)).isInstanceOf(NotFoundException.class);
        }

        @DisplayName("이름이 공백이거나 존재하지 않으면 Reservation을 생성할 수 없다")
        @ParameterizedTest
        @MethodSource("invalidNames")
        void createInvalidNameTest(String name) {
            ReservationRepository reservationRepository = new FakeReservationRepository(new ArrayList<>());
            ReservationTimeRepository reservationTimeRepository = FakeReservationTimeRepositoryFixture.create();
            ThemeRepository themeRepository = FakeThemeRepositoryFixture.create();
            bookService = new BookService(reservationRepository, reservationTimeRepository, themeRepository);

            ReservationCreateRequestDto requestDto = new ReservationCreateRequestDto(name, LocalDate.now().plusDays(7), 1L, 1L);

            assertThatThrownBy(() -> bookService.createReservation(requestDto)).isInstanceOf(IllegalArgumentException.class);
        }

        static Stream<Arguments> invalidNames() {
            return Stream.of(
                    Arguments.of(" "),
                    Arguments.of(""),
                    Arguments.of((String) null)
            );
        }

        @DisplayName("이미 동일한 날짜와 시간에 예약이 있으면 생성할 수 없다")
        @Test
        void createDuplicateReservationTest() {
            ReservationRepository reservationRepository = FakeReservationRepositoryFixture.create();
            FakeReservationTimeRepository reservationTimeRepository = FakeReservationTimeRepositoryFixture.create();
            ThemeRepository themeRepository = FakeThemeRepositoryFixture.create();
            bookService = new BookService(reservationRepository, reservationTimeRepository, themeRepository);

            LocalDate date = LocalDate.now().plusDays(7);
            ReservationCreateRequestDto requestDto = new ReservationCreateRequestDto("가이온", date, 1L, 1L);

            assertThatThrownBy(() -> bookService.createReservation(requestDto)).isInstanceOf(DuplicateContentException.class);
        }

        @DisplayName("이미 지난 날짜의 경우 예약 생성이 불가능 하다")
        @Test
        void createInvalidDateTest() {
            ReservationRepository reservationRepository = FakeReservationRepositoryFixture.create();
            FakeReservationTimeRepository reservationTimeRepository = FakeReservationTimeRepositoryFixture.create();
            ThemeRepository themeRepository = FakeThemeRepositoryFixture.create();
            bookService = new BookService(reservationRepository, reservationTimeRepository, themeRepository);

            LocalDate date = LocalDate.now().minusDays(7);
            ReservationCreateRequestDto requestDto = new ReservationCreateRequestDto("가이온", date, 1L, 1L);

            assertThatThrownBy(() -> bookService.createReservation(requestDto)).isInstanceOf(InvalidRequestException.class);
        }
    }

    @Nested
    @DisplayName("예약 삭제")
    class DeleteReservation {

        @DisplayName("Reservation을 삭제할 수 있다")
        @Test
        void deleteReservationTest() {
            ReservationRepository reservationRepository = FakeReservationRepositoryFixture.create();
            ReservationTimeRepository reservationTimeRepository = FakeReservationTimeRepositoryFixture.create();
            ThemeRepository themeRepository = FakeThemeRepositoryFixture.create();
            bookService = new BookService(reservationRepository, reservationTimeRepository, themeRepository);
            ReservationService reservationService = new ReservationService(reservationRepository);

            bookService.deleteReservation(1L);

            List<ReservationResponseDto> responses = reservationService.findAllReservationResponses();

            assertThat(responses).isEmpty();
        }

        @DisplayName("존재하지 않는 Id의 Reservation을 삭제할 수 없다")
        @Test
        void deleteInvalidReservationIdTest() {
            ReservationRepository reservationRepository = FakeReservationRepositoryFixture.create();
            ReservationTimeRepository reservationTimeRepository = FakeReservationTimeRepositoryFixture.create();
            ThemeRepository themeRepository = FakeThemeRepositoryFixture.create();
            bookService = new BookService(reservationRepository, reservationTimeRepository, themeRepository);

            assertThatThrownBy(() -> bookService.deleteReservation(10L)).isInstanceOf(NotFoundException.class);
        }
    }
}
