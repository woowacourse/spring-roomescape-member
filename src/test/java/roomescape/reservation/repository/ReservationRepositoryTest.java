package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.auth.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repositoy.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@SpringBootTest
@Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = BEFORE_TEST_METHOD)
class ReservationRepositoryTest {

  @Autowired
  private ReservationRepository reservationRepository;

  @DisplayName("모든 예약 정보를 조회한다.")
  @Test
  void findAllTest() {
    // When
    final List<Reservation> reservations = reservationRepository.findAll(null, null, null, null);

    // Then
    assertThat(reservations).hasSize(16);
  }

  @DisplayName("멤버ID로 예약을 검색한다.")
  @Test
  void findAllWithMemberIdTest() {
    // When
    final List<Reservation> reservations = reservationRepository.findAll(null, null, null, 1L);

    // Then
    assertThat(reservations).hasSize(1);
  }

  @DisplayName("특정 기간 이후 예약을 검색한다.")
  @Test
  void findAllWithDateFrom() {
    // When
    final List<Reservation> reservations = reservationRepository.findAll(LocalDate.now(),
        null, null, null);

    // Then
    assertThat(reservations).hasSize(4);
  }

  @DisplayName("특정 기간 이전 예약을 검색한다.")
  @Test
  void findAllWithDateTo() {
    // When
    final List<Reservation> reservations = reservationRepository.findAll(null,
        LocalDate.now(), null, null);

    // Then
    assertThat(reservations).hasSize(12);
  }

  @DisplayName("주어진 기간으로 예약을 검색한다.")
  @Test
  void findAllWithDateFromAndDateTo() {
    // When
    final List<Reservation> reservations = reservationRepository.findAll(LocalDate.now(),
        LocalDate.now().plusDays(3), null, null);

    // Then
    assertThat(reservations).hasSize(2);
  }

  @DisplayName("주어진 기간과 멤버ID로 예약을 검색한다.")
  @Test
  void findAllWithDateFromAndDateToAndMemberId() {
    // When
    final List<Reservation> reservations = reservationRepository.findAll(LocalDate.now(),
        LocalDate.now().plusDays(3), null, 13L);

    // Then
    assertThat(reservations).hasSize(1);
  }

  @DisplayName("주어진 기간과 멤버ID로 예약을 검색한다.")
  @Test
  void findAllWithDateFromAndDateToAndThemeId() {
    // When
    final List<Reservation> reservations = reservationRepository.findAll(
        LocalDate.now().minusDays(4),
        LocalDate.now().plusDays(3), 10L, null);

    // Then
    assertThat(reservations).hasSize(2);
  }

  @DisplayName("주어진 테마ID로 예약을 검색한다.")
  @Test
  void findAllWithThemeId() {
    // When
    final List<Reservation> reservations = reservationRepository.findAll(
        null, null, 1L, null);

    // Then
    assertThat(reservations).hasSize(4);
  }

  @DisplayName("특정 예약 정보를 조회한다.")
  @Test
  void findByIdTest() {
    // Given
    final Long reservationId = 1L;

    // When
    final Optional<Reservation> reservation = reservationRepository.findById(reservationId);

    // Then
    assertThat(reservation.isPresent()).isTrue();
  }

  @DisplayName("예약 정보를 저장한다.")
  @Test
  void saveTest() {
    // Given
    final String clientName = "브라운";
    final LocalDate reservationDate = LocalDate.now().plusDays(10);
    final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 10));
    final Theme theme = Theme.of(1L, "테바의 비밀친구", "테바의 은밀한 비밀친구", "대충 테바 사진 링크");
    final Reservation reservation = Reservation.createInstanceWithoutId(
        Member.createInstance(1L, "브라운", "kelly@example.com", "password123", "ADMIN"),
        reservationDate,
        reservationTime, theme);

    // When
    final Reservation savedReservation = reservationRepository.save(reservation);

    // Then
    final List<Reservation> reservations = reservationRepository.findAll(null, null, null, null);
    assertAll(
        () -> assertThat(reservations).hasSize(17),
        () -> assertThat(savedReservation.getId()).isEqualTo(17L),
        () -> assertThat(savedReservation.getMember().getName()).isEqualTo(
            reservation.getMember().getName()),
        () -> assertThat(savedReservation.getDate()).isEqualTo(reservation.getDate()),
        () -> assertThat(savedReservation.getTime()).isEqualTo(reservation.getTime())
    );
  }

  @DisplayName("예약 정보를 삭제한다.")
  @Test
  void deleteByIdTest() {
    // When
    reservationRepository.deleteById(1L);

    // Then
    final List<Reservation> reservations = reservationRepository.findAll(null, null, null, null);
    assertThat(reservations).hasSize(15);
  }

  @DisplayName("특정 날짜와 시간 아이디를 가진 예약이 존재하는지 조회한다.")
  @Test
  void existByDateAndTimeIdTest() {
    // Given
    final LocalDate reservationDate = LocalDate.now().plusDays(2);
    final Long timeId = 4L;
    final Long themeId = 9L;

    // When
    final boolean isExist = reservationRepository.existByDateAndTimeIdAndThemeId(reservationDate,
        timeId, themeId);

    // Then
    assertThat(isExist).isTrue();
  }

  @DisplayName("특정 시간 아이디를 가진 예약이 존재하는지 조회한다.")
  @Test
  void existByTimeIdTest() {
    // Given
    final Long timeId = 1L;

    // When
    final boolean isExist = reservationRepository.existByTimeId(timeId);

    // Then
    assertThat(isExist).isTrue();
  }

  @DisplayName("특정 날짜와 테마 아이디를 가진 예약 정보를 모두 조회한다.")
  @Test
  void findAllByDateAndThemeIdTest() {
    // Given
    final LocalDate reservationDate = LocalDate.now().minusDays(3);
    final Long themeId = 1L;

    // When
    final List<Reservation> allByDateAndThemeId = reservationRepository.findAllByDateAndThemeId(
        reservationDate, themeId);

    // Then
    assertThat(allByDateAndThemeId).hasSize(2);
  }
}
