package roomescape.reservation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.reservation.repository.projection.ReservationDetailProjection;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@JdbcTest
@Transactional
@ActiveProfiles("test")
@Import(JdbcReservationRepository.class)
class JdbcReservationRepositoryTest {
    @Autowired
    private JdbcReservationRepository reservationRepository;

    @Test
    void 예약_저장_레포지토리_테스트() {
        // given
        Reservation reservation = new Reservation(null, "브라운", 1L);

        // when
        Reservation savedReservation = reservationRepository.save(reservation);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedReservation).isNotNull();
            softly.assertThat(savedReservation.getId()).isNotNull();
            softly.assertThat(savedReservation.getName()).isEqualTo("브라운");
            softly.assertThat(savedReservation.getScheduleId()).isEqualTo(1L);
        });
    }

    @Test
    void 전체_예약_상세_조회_레포지토리_테스트() {
        // given
        Reservation reservation1 = new Reservation(null, "브라운", 1L);
        Reservation reservation2 = new Reservation(null, "네오", 2L);
        Reservation newReservation1 = reservationRepository.save(reservation1);
        Reservation newReservation2 = reservationRepository.save(reservation2);

        // when
        List<ReservationDetailProjection> reservations = reservationRepository.findAllDetails();

        // then
        assertThat(reservations).hasSize(6);
        assertThat(reservations).extracting(ReservationDetailProjection::id)
                .contains(newReservation1.getId(), newReservation2.getId());
    }

    @Test
    void 예약_삭제_레포지토리_테스트() {
        // given
        Reservation reservation = new Reservation(null, "브라운", 1L);
        Reservation savedReservation = reservationRepository.save(reservation);

        // when
        reservationRepository.deleteById(savedReservation.getId());

        // then
        List<ReservationDetailProjection> reservations = reservationRepository.findAllDetails();
        assertThat(reservations).hasSize(4);
        assertThat(reservations).extracting(ReservationDetailProjection::id)
                .doesNotContain(savedReservation.getId());
    }

    @Test
    @DisplayName("이용 가능한 시간을 조회할 수 있다.")
    void findTimeIdByDateAndThemeId_테스트() {
        // when
        Set<Long> result = reservationRepository.findTimeIdByDateAndThemeId(LocalDate.parse("2026-05-05"), 1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result).containsExactlyInAnyOrder(1L);
    }

    @Test
    @DisplayName("특정 이름을 기준으로 해당이름을 가진 예약들을 조회 할 수 있다.")
    void findDetailsByName_테스트() {
        // given
        String name = "a";

        // when
        List<ReservationDetailProjection> result = reservationRepository.findDetailsByName(name);

        // then
        assertThat(result).hasSize(1);
        assertThat(result).extracting(ReservationDetailProjection::name)
                .containsExactly(name);
    }

    @Test
    @DisplayName("특정이름을 기준으로 해당이름을 가진 예약을 삭제할 수 있다.")
    void deleteByIdAndName_테스트() {
        // when
        int affectedRow = reservationRepository.deleteByIdAndName(1, "a");

        // then
        assertThat(affectedRow).isEqualTo(1);
        assertThat(reservationRepository.findAllDetails())
                .extracting(ReservationDetailProjection::id)
                .doesNotContain(1L);
    }

    @Test
    @DisplayName("이름이 일치하지 않으면 예약은 삭제되지 않는다.")
    void deleteByIdAndName_이름불일치_테스트() {
        // when
        int affectedRow = reservationRepository.deleteByIdAndName(1, "x");

        // then
        assertThat(affectedRow).isEqualTo(0);
        assertThat(reservationRepository.findAllDetails())
                .extracting(ReservationDetailProjection::id)
                .contains(1L);
    }

    @Test
    @DisplayName("같은 스케줄에 본인 제외 다른 예약이 있으면 true를 반환한다.")
    void isDuplicateReservation_true반환_테스트() {
        // when
        boolean result = reservationRepository.isDuplicateReservation(1, 2);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("같은 스케줄에 본인 제외 다른 예약이 없으면 false를 반환한다.")
    void isDuplicateReservation_false반환_테스트() {
        // when
        boolean result = reservationRepository.isDuplicateReservation(1, 4);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("본인의 기존 예약을 변경가능한 스케줄로 변경할 수 있다.")
    void updateScheduleByIdAndName_테스트() {
        int affectedRow = reservationRepository.updateScheduleByIdAndName(1L, "a", 4L);
        assertThat(affectedRow).isEqualTo(1);
    }
}
