package roomescape.reservation.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(ReservationRepository.class)
class ReservationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationRepository reservationRepository;

    private Long timeId;
    private Long themeId;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");

        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "공포방", "무서운방입니다.", "image-url");

        this.timeId = jdbcTemplate.queryForObject("SELECT id FROM reservation_time", Long.class);
        this.themeId = jdbcTemplate.queryForObject("SELECT id FROM theme", Long.class);
    }

    @Test
    void 예약을_저장하면_생성된_id가_반환되고_DB에_저장된다() {
        Long id = reservationRepository.save("브라운", LocalDate.of(2026, 5, 10), timeId, themeId);

        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM reservation WHERE id = ?", Integer.class, id);

        assertThat(id).isPositive();
        assertThat(count).isEqualTo(1);
    }

    @Test
    void 예약_전체_조회시_예약_시간_테마를_함께_반환한다() {
        reservationRepository.save("브라운", LocalDate.of(2026, 5, 10), timeId, themeId);

        List<Reservation> result = reservationRepository.findAllWithTime();

        assertThat(result).hasSize(1);
        Reservation reservation = result.get(0);
        assertThat(reservation.getName()).isEqualTo("브라운");
        assertThat(reservation.getDate()).isEqualTo(LocalDate.of(2026, 5, 10));
        assertThat(reservation.getTime().getId()).isEqualTo(timeId);
        assertThat(reservation.getTime().getStartAt()).isEqualTo(LocalTime.of(10, 0));
        assertThat(reservation.getTheme().getId()).isEqualTo(themeId);
        assertThat(reservation.getTheme().getName()).isEqualTo("공포방");
        assertThat(reservation.getTheme().getDescription()).isEqualTo("무서운방입니다.");
        assertThat(reservation.getTheme().getThumbnail()).isEqualTo("image-url");
    }

    @Test
    void 예약을_삭제하면_삭제된_행_수가_반환되고_DB에서_삭제된다() {
        Long id = reservationRepository.save("브라운", LocalDate.of(2026, 5, 10), timeId, themeId);

        int deletedRows = reservationRepository.deleteById(id);

        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM reservation WHERE id = ?", Integer.class, id);
        assertThat(deletedRows).isEqualTo(1);
        assertThat(count).isZero();
    }
}
