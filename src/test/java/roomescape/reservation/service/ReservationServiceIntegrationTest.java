package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.auth.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.SaveReservationRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = BEFORE_TEST_METHOD)
class ReservationServiceIntegrationTest {

  @Autowired
  private ReservationService reservationService;

  @DisplayName("전체 예약 정보를 조회한다.")
  @Test
  void getReservationsTest() {
    // When
    final List<Reservation> reservations = reservationService.getReservations();

    // Then
    assertThat(reservations).hasSize(16);
  }

  @DisplayName("예약 정보를 저장한다.")
  @Test
  void saveReservationTest() {
    // Given
    final LocalDate date = LocalDate.now().plusDays(10);
    final SaveReservationRequest saveReservationRequest = new SaveReservationRequest(date, 1L,
        1L);

    // When
    final Reservation reservation = reservationService.saveReservation(
        Member.createInstance(1L, "켈리", "kelly@example.com", "password123", "USER"),
        saveReservationRequest);

    // Then
    final List<Reservation> reservations = reservationService.getReservations();
    assertAll(
        () -> assertThat(reservations).hasSize(17),
        () -> assertThat(reservation.getId()).isEqualTo(17L),
        () -> assertThat(reservation.getMember().getName().getValue()).isEqualTo("켈리"),
        () -> assertThat(reservation.getDate().getValue()).isEqualTo(date),
        () -> assertThat(reservation.getTime().getStartAt()).isEqualTo(LocalTime.of(9, 30))
    );
  }

  @DisplayName("저장하려는 예약 시간이 존재하지 않는다면 예외를 발생시킨다.")
  @Test
  void throwExceptionWhenSaveReservationWithNotExistReservationTimeTest() {
    // Given
    final SaveReservationRequest saveReservationRequest = new SaveReservationRequest(
        LocalDate.now(), 9L, 1L);

    // When & Then
    assertThatThrownBy(
        () -> reservationService.saveReservation(Member.of("브라운", "brown@mail.com", "1234"),
            saveReservationRequest))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("해당 id의 예약 시간이 존재하지 않습니다.");
  }

  @DisplayName("예약 정보를 삭제한다.")
  @Test
  void deleteReservationTest() {
    // When
    reservationService.deleteReservation(1L);

    // When & Then
    final List<Reservation> reservations = reservationService.getReservations();
    assertThat(reservations).hasSize(15);
  }

  @DisplayName("존재하지 않는 예약 정보를 삭제하려고 하면 예외가 발생한다.")
  @Test
  void throwExceptionWhenDeleteNotExistReservationTest() {
    // When & Then
    assertThatThrownBy(() -> reservationService.deleteReservation(18L))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("해당 id의 예약이 존재하지 않습니다.");
  }

  @DisplayName("이미 존재하는 예약 날짜/시간/테마가 입력되면 예외가 발생한다.")
  @Test
  void throwExceptionWhenInputDuplicateReservationDate() {
    // Given
    final SaveReservationRequest saveReservationRequest = new SaveReservationRequest(
        LocalDate.now().plusDays(2),
        4L,
        9L
    );

    // When & Then
    assertThatThrownBy(
        () -> reservationService.saveReservation(Member.of("브라운", "brown@mail.com", "1234"),
            saveReservationRequest))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("이미 해당 날짜/시간의 테마 예약이 있습니다.");
  }
}
