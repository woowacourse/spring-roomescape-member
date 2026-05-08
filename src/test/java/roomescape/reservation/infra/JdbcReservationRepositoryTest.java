package roomescape.reservation.infra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Schedule;
import roomescape.reservation.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
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
        Reservation reservation = new Reservation(null, "브라운",
                new Schedule(
                        1L,
                        LocalDate.of(2026, 5, 5),
                        new ReservationTime(2L, LocalTime.of(11, 0)),
                        new Theme(1L, "세기의 도둑", "보안을 뚫고 보석을 훔쳐라", "https://example.com/themes/time.jpg")
                )
        );

        // when
        Reservation savedReservation = reservationRepository.save(reservation);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedReservation).isNotNull();
            softly.assertThat(savedReservation.getId()).isNotNull();
            softly.assertThat(savedReservation.getName()).isEqualTo("브라운");
            softly.assertThat(savedReservation.getSchedule().getDate()).isEqualTo(LocalDate.of(2026, 5, 5));
            softly.assertThat(savedReservation.getSchedule().getTime().getId()).isEqualTo(2L);
            softly.assertThat(savedReservation.getSchedule().getTime().getStartAt()).isEqualTo(LocalTime.of(11, 0));
            softly.assertThat(savedReservation.getSchedule().getTheme().getId()).isEqualTo(1L);
            softly.assertThat(savedReservation.getSchedule().getTheme().getName()).isEqualTo("세기의 도둑");
            softly.assertThat(savedReservation.getSchedule().getTheme().getDescription()).isEqualTo("보안을 뚫고 보석을 훔쳐라");
            softly.assertThat(savedReservation.getSchedule().getTheme().getThumbnailUrl()).isEqualTo("https://example.com/themes/time.jpg");
        });
    }

    @Test
    void 전체_예약_조회_레포지토리_테스트() {
        // given
        Reservation reservation1 = new Reservation(null, "브라운",
                new Schedule(
                        1L,
                        LocalDate.of(2026, 5, 5),
                        new ReservationTime(2L, LocalTime.of(11, 0)),
                        new Theme(1L, "세기의 도둑", "보안을 뚫고 보석을 훔쳐라", "https://example.com/themes/time.jpg")
                )
        );
        Reservation reservation2 = new Reservation(null, "네오",
                new Schedule(
                        2L,
                        LocalDate.of(2026, 5, 6),
                        new ReservationTime(2L, LocalTime.of(11, 0)),
                        new Theme(2L, "세기의 도둑2", "보안을 뚫고 보석을 훔쳐라2", "https://example.com/themes2/time.jpg")
                )
        );
        Reservation newReservation1 = reservationRepository.save(reservation1);
        Reservation newReservation2 = reservationRepository.save(reservation2);

        // when
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        assertThat(reservations).hasSize(6);
        assertThat(reservations).extracting(Reservation::getId)
                .contains(newReservation1.getId(), newReservation2.getId());
    }

    @Test
    void 예약_삭제_레포지토리_테스트() {
        // given
        Reservation reservation = new Reservation(null, "브라운",
                new Schedule(
                        1L,
                        LocalDate.of(2026, 5, 5),
                        new ReservationTime(2L, LocalTime.of(11, 0)),
                        new Theme(1L, "세기의 도둑", "보안을 뚫고 보석을 훔쳐라", "https://example.com/themes/time.jpg")
                )
        );
        Reservation savedReservation = reservationRepository.save(reservation);

        // when
        reservationRepository.deleteById(savedReservation.getId());

        // then
        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).hasSize(4);
        assertThat(reservations).extracting(Reservation::getId)
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
}
