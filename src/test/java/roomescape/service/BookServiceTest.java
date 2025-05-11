package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.exception.DuplicateContentException;
import roomescape.exception.InvalidRequestException;
import roomescape.exception.NotFoundException;
import roomescape.fixture.FakeMemberRepositoryFixture;
import roomescape.fixture.FakeReservationRepositoryFixture;
import roomescape.fixture.FakeReservationTimeRepositoryFixture;
import roomescape.fixture.FakeThemeRepositoryFixture;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookServiceTest {

    private final ReservationRepository reservationRepository = FakeReservationRepositoryFixture.create();
    private final ReservationTimeRepository reservationTimeRepository = FakeReservationTimeRepositoryFixture.create();
    private final ThemeRepository themeRepository = FakeThemeRepositoryFixture.create();
    private final MemberRepository memberRepository = FakeMemberRepositoryFixture.create();
    private final BookService bookService = new BookService(reservationRepository, reservationTimeRepository, themeRepository, memberRepository);

    @Nested
    @DisplayName("예약 생성")
    class CreateReservation {

        @DisplayName("요청에 따라 Reservation을 생성 할 수 있다")
        @Test
        void createReservationTest() {
            // given
            LocalDate date = LocalDate.now().plusDays(10);
            ReservationRequest requestDto = new ReservationRequest(date, 1L, 1L, 1L);

            // when
            ReservationResponse responseDto = bookService.createAdminReservation(requestDto);

            // then
            Assertions.assertAll(
                    () -> assertThat(responseDto.id()).isEqualTo(2L),
                    () -> assertThat(responseDto.date()).isEqualTo(date),
                    () -> assertThat(responseDto.member().name()).isEqualTo("어드민"),
                    () -> assertThat(responseDto.time().startAt()).isEqualTo(LocalTime.of(10, 0)),
                    () -> assertThat(responseDto.theme().name()).isEqualTo("우테코")
            );
        }

        @DisplayName("요청한 ReservationTime의 id가 존재하지 않으면 Reservation을 생성할 수 없다")
        @Test
        void invalidReservationTimeIdTest() {
            // given
            ReservationRequest requestDto = new ReservationRequest(LocalDate.now().plusDays(10), 10L,
                    1L, 1L);

            // when & then
            assertThatThrownBy(() -> bookService.createAdminReservation(requestDto))
                    .isInstanceOf(NotFoundException.class);
        }

        @DisplayName("요청한 Theme의 id가 존재하지 않으면 Reservation을 생성할 수 없다")
        @Test
        void invalidThemeIdTest() {
            // given
            ReservationRequest requestDto = new ReservationRequest(LocalDate.now().plusDays(10), 1L,
                    10L, 1L);

            // when & then
            assertThatThrownBy(() -> bookService.createAdminReservation(requestDto))
                    .isInstanceOf(NotFoundException.class);
        }

        @DisplayName("유저가 존재하지 않으면 Reservation을 생성할 수 없다")
        @Test
        void createInvalidNameTest() {
            // given
            ReservationRequest requestDto = new ReservationRequest(LocalDate.now().plusDays(10), 1L,
                    1L, 10L);

            // when & then
            assertThatThrownBy(() -> bookService.createAdminReservation(requestDto))
                    .isInstanceOf(NotFoundException.class);
        }

        @DisplayName("이미 동일한 날짜와 시간에 예약이 있으면 생성할 수 없다")
        @Test
        void createDuplicateReservationTest() {
            // given
            ReservationRequest requestDto = new ReservationRequest(LocalDate.now().plusDays(7), 1L,
                    1L, 1L);

            // when & then
            assertThatThrownBy(() -> bookService.createAdminReservation(requestDto))
                    .isInstanceOf(DuplicateContentException.class);
        }

        @DisplayName("이미 지난 날짜의 경우 예약 생성이 불가능 하다")
        @Test
        void createInvalidDateTest() {
            // given
            ReservationRequest requestDto = new ReservationRequest(LocalDate.now().minusDays(7), 1L,
                    1L, 1L);

            // when & then
            assertThatThrownBy(() -> bookService.createAdminReservation(requestDto))
                    .isInstanceOf(InvalidRequestException.class);
        }
    }

    @Nested
    @DisplayName("예약 삭제")
    class DeleteReservation {

        @DisplayName("Reservation을 삭제할 수 있다")
        @Test
        void deleteReservationTest() {
            // given
            ReservationService reservationService = new ReservationService(reservationRepository);

            // when
            bookService.deleteReservation(1L);

            // then
            assertThat(reservationService.findAllReservationResponses()).isEmpty();
        }

        @DisplayName("존재하지 않는 Id의 Reservation을 삭제할 수 없다")
        @Test
        void deleteInvalidReservationIdTest() {
            // given
            long targetId = 10L;

            // when & then
            assertThatThrownBy(() -> bookService.deleteReservation(targetId)).isInstanceOf(NotFoundException.class);
        }
    }
}
