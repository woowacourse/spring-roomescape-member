package roomescape.reservation.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

@JdbcTest
@Import(ReservationRepository.class)
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("예약을 추가할 수 있다")
    void test1() {
        // given
        Reservation reservation = new Reservation(
                1L,
                new Member(1L, "미미", "mimi@email.com", "password", Role.MEMBER),
                LocalDate.now(),
                new Time(1L, LocalTime.of(10, 0)),
                new Theme(1L, "테마1", "테마 설명", "테마 썸네일")
        );

        // when
        Reservation savedReservation = reservationRepository.add(reservation);

        // then
        assertThat(savedReservation).isNotNull();
        assertThat(savedReservation.id()).isNotNull();
        assertThat(savedReservation.member().name()).isEqualTo("미미");
        assertThat(savedReservation.date()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("ID로 예약을 조회할 수 있다")
    void test2() {
        // given
        Long reservationId = 1L;

        // when
        Optional<Reservation> result = reservationRepository.findById(reservationId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(reservationId);
        assertThat(result.get().member().name()).isEqualTo("미미");
    }

    @Test
    @DisplayName("존재하지 않는 예약 ID로 조회 시 빈 Optional을 반환한다")
    void test3() {
        // given
        Long nonExistentId = 999L;

        // when
        Optional<Reservation> result = reservationRepository.findById(nonExistentId);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("모든 예약을 조회할 수 있다")
    void test4() {
        // when
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        assertThat(reservations).hasSize(71);
    }

    @Test
    @DisplayName("검색 조건에 맞는 예약을 조회할 수 있다")
    void test5() {
        // given
        Long themeId = 1L;
        Long memberId = 1L;
        LocalDate dateFrom = LocalDate.of(2025, 5, 1);
        LocalDate dateTo = LocalDate.of(2025, 5, 31);

        // when
        List<Reservation> result = reservationRepository.findBySearchFilter(themeId, memberId, dateFrom, dateTo);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).member().name()).isEqualTo("미미");
    }

    @Test
    @DisplayName("특정 테마에 대한 예약 존재 여부를 확인할 수 있다")
    void test6() {
        // given
        Long themeId = 1L;

        // when
        boolean exists = reservationRepository.existsByThemeId(themeId);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("특정 시간에 대한 예약 존재 여부를 확인할 수 있다")
    void test7() {
        // given
        Long timeId = 1L;

        // when
        boolean exists = reservationRepository.existsByTimeId(timeId);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("특정 날짜, 시간, 테마에 대한 예약 존재 여부를 확인할 수 있다")
    void test8() {
        // given
        LocalDate date = LocalDate.of(2025, 4, 30);
        Long timeId = 1L;
        Long themeId = 1L;

        // when
        boolean exists = reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("예약을 삭제할 수 있다")
    void test9() {
        // given
        Long reservationId = 1L;

        // when
        reservationRepository.deleteById(reservationId);

        // then
        Optional<Reservation> result = reservationRepository.findById(reservationId);
        assertThat(result).isEmpty();
    }
}
