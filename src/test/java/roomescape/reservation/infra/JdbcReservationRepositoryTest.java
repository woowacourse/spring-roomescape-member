package roomescape.reservation.infra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(scripts = {"/truncate.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class JdbcReservationRepositoryTest {
    @Autowired
    private JdbcReservationRepository reservationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 예약_저장_레포지토리_테스트() {
        Reservation reservation = new Reservation(null, "브라운", LocalDate.of(2026, 5, 5),
                new ReservationTime(3L, LocalTime.of(12, 0)),
                new Theme(1L, "세기의 도둑", "보안을 뚫고 보석을 훔쳐라", "https://example.com/themes/time.jpg")
        );

        Reservation savedReservation = reservationRepository.save(reservation);

        assertThat(savedReservation.getName()).isEqualTo("브라운");
        assertThat(savedReservation.getDate()).isEqualTo(LocalDate.of(2026, 5, 5));
        assertThat(savedReservation.getTime().getId()).isEqualTo(3L);
        assertThat(savedReservation.getTime().getStartAt()).isEqualTo(LocalTime.of(12, 0));
    }

    @Test
    void 전체_예약_조회_레포지토리_테스트() {
        List<Reservation> reservations = reservationRepository.findAll();

        assertThat(reservations).hasSize(9);
        assertThat(reservations)
                .extracting(Reservation::getName)
                .startsWith("kim", "kim", "lee");
        assertThat(reservations)
                .extracting(Reservation::getDate)
                .startsWith(LocalDate.of(2026, 5, 5), LocalDate.of(2026, 5, 5), LocalDate.of(2026, 5, 5));
        assertThat(reservations)
                .extracting(reservation -> reservation.getTime().getId())
                .startsWith(1L, 2L, 1L);
        assertThat(reservations)
                .extracting(reservation -> reservation.getTime().getStartAt())
                .startsWith(LocalTime.of(10, 0), LocalTime.of(11, 0), LocalTime.of(10, 0));
    }

    @Test
    void 예약_삭제_레포지토리_테스트() {
        reservationRepository.deleteById(1L);
        int rowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);

        assertThat(rowCount).isEqualTo(8);
    }

    @Test
    @DisplayName("이미 예약된 시간을 조회할 수 있다.")
    void findTimeIdByDateAndThemeId_테스트() {
        List<Long> result = reservationRepository.findTimeIdByDateAndThemeId(LocalDate.parse("2026-05-05"), 1L);

        assertThat(result).containsExactly(1L, 2L);
    }

    @Test
    void 중복된_예약은_DB_유니크_제약으로_저장에_실패한다() {
        Reservation reservation = new Reservation(null, "브라운", LocalDate.of(2026, 5, 5),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new Theme(1L, "세기의 도둑", "보안을 뚫고 보석을 훔쳐라", "https://example.com/themes/time.jpg")
        );

        assertThatThrownBy(() -> reservationRepository.save(reservation))
                .isInstanceOf(DuplicateKeyException.class);
    }
}
