package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReserveName;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@Sql(scripts = {"/drop.sql", "/schema.sql", "/data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Import(H2ReservationRepository.class)
@JdbcTest
class ReservationRepositoryTest {

    final long LAST_ID = 12;
    final Reservation exampleFirstReservation = new Reservation(
            1L,
            new ReserveName("Seyang"),
            LocalDate.now().minusDays(8),
            new ReservationTime(1L, LocalTime.of(10, 15)),
            new Theme(1L,
                    "Spring",
                    "A time of renewal and growth, as nature awakens from its slumber and bursts forth with vibrant colors and fragrant blooms.",
                    "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")
    );
    final Reservation unassignedReservation = new Reservation(
            null,
            new ReserveName("Seyang"),
            LocalDate.now().plusMonths(1),
            new ReservationTime(2L),
            new Theme(2L, null, null, null)
    );

    @Autowired
    ReservationRepository reservationRepository;

    @Test
    @DisplayName("모든 예약 목록을 조회한다.")
    void findAll() {
        // given & when
        final var actual = reservationRepository.findAll();

        // then
        assertThat(actual).hasSize((int) LAST_ID);
        assertThat(actual.get(0)).isEqualTo(exampleFirstReservation);
    }

    @Test
    @DisplayName("특정 id를 통해 예약을 조회한다.")
    void findByIdPresent() {
        // given
        final Long id = exampleFirstReservation.getId();
        final Reservation expected = exampleFirstReservation
                .assignTime(new ReservationTime(exampleFirstReservation.getTime().getId(), LocalTime.MIN))
                .assignTheme(new Theme(exampleFirstReservation.getTheme().getId(), "", "", ""));

        // when
        final Optional<Reservation> actual = reservationRepository.findById(id);

        // then
        assertThat(actual).hasValue(expected);
    }

    @Test
    @DisplayName("존재하지 않는 예약을 조회할 경우 빈 값을 반환한다.")
    void findByIdNotPresent() {
        // given & when
        final Optional<Reservation> actual = reservationRepository.findById(LAST_ID + 1);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("등록된 시간 아이디로 예약 존재 여부를 확인한다.")
    void existsByTimeIdPresent() {
        // given
        final Long timeId = 2L;
        final boolean expected = true;

        // when
        final boolean actual = reservationRepository.existsByTimeId(timeId);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("등록되지 않은 시간 아이디로 예약 존재 여부를 확인한다,")
    void existsByTimeIdNotPresent() {
        // given
        final Long timeId = 4L;
        final boolean expected = false;

        // when
        final boolean actual = reservationRepository.existsByTimeId(timeId);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("예약 정보를 저장하면 새로운 아이디가 부여된다.")
    void save() {
        // given & when
        final Reservation saved = reservationRepository.save(unassignedReservation);

        // then
        assertThat(saved.getId()).isPositive();
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간으로 예약을 저장하면 예외가 발생한다.")
    void exceptionOnSavingWithNotPresentTime() {
        // given
        final Reservation reservation = unassignedReservation.assignTime(
                new ReservationTime(5L, LocalTime.MIN)
        );

        // when & then
        assertThatCode(() -> reservationRepository.save(reservation))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("존재하지 않는 테마 정보로 예약을 저장하면 예외가 발생한다.")
    void exceptionOnSavingWithNotPresentTheme() {
        // given
        final Reservation reservation = unassignedReservation.assignTheme(
                new Theme(5L, null, null, null)
        );

        // when & then
        assertThatCode(() -> reservationRepository.save(reservation))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("등록된 예약 번호로 삭제한다.")
    void deleteAssignedId() {
        // given & when & then
        assertThat(reservationRepository.findById(LAST_ID)).isPresent();
        assertThat(reservationRepository.deleteById(LAST_ID)).isNotZero();
    }

    @Test
    @DisplayName("없는 예약 번호로 삭제할 경우 아무런 영향이 없다.")
    void deleteNotExistId() {
        // given
        final long nonExistId = LAST_ID + 1;

        // when & then
        assertThat(reservationRepository.findById(nonExistId)).isEmpty();
        assertThat(reservationRepository.deleteById(nonExistId)).isZero();
    }
}
