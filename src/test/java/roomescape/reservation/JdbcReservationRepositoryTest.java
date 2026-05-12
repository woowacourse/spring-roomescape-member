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
    @DisplayName("이름을 기준으로 예약들을 조회 할 수 있다.")
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
}
