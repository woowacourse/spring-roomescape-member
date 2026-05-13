package roomescape.reservation.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

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

    private ReservationTime time;
    private Theme theme;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");

        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "공포방", "무서운방입니다.", "image-url");
        this.time = jdbcTemplate.queryForObject("SELECT * FROM reservation_time",
                (rs, rowNum) -> new ReservationTime(
                        rs.getLong("id"),
                        LocalTime.parse(rs.getString("start_at"))));
        this.theme = jdbcTemplate.queryForObject("SELECT * FROM theme",
                (rs, rowNum) -> new Theme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("thumbnail"))
        );
    }

    @Test
    void 예약을_저장한다() {
        Reservation saved = reservationRepository.save(new Reservation(null, "브라운", LocalDate.of(2026, 5, 10), time, theme));

        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM reservation WHERE id = ?", Integer.class, saved.getId());
        assertThat(saved.getId()).isPositive();;
    }

    @Test
    void 예약_전체_조회시_예약_시간_테마를_함께_반환한다() {
        reservationRepository.save(new Reservation(
                null, "브라운", LocalDate.of(2026, 5, 10), time, theme));

        List<Reservation> result = reservationRepository.findAllWithTime();

        assertThat(result).hasSize(1);
        Reservation reservation = result.get(0);
        assertThat(reservation.getName()).isEqualTo("브라운");
        assertThat(reservation.getDate()).isEqualTo(LocalDate.of(2026, 5, 10));
        assertThat(reservation.getTime().getId()).isEqualTo(time.getId());
        assertThat(reservation.getTime().getStartAt()).isEqualTo(LocalTime.of(10, 0));
        assertThat(reservation.getTheme().getId()).isEqualTo(theme.getId());
        assertThat(reservation.getTheme().getName()).isEqualTo("공포방");
        assertThat(reservation.getTheme().getDescription()).isEqualTo("무서운방입니다.");
        assertThat(reservation.getTheme().getThumbnail()).isEqualTo("image-url");
    }

    @Test
    void 예약을_삭제하면_삭제된_행_수가_반환되고_DB에서_삭제된다() {
        Reservation savedReservation = reservationRepository.save(new Reservation(
                null, "브라운", LocalDate.of(2026, 5, 10), time, theme));

        int deletedRows = reservationRepository.deleteById(savedReservation.getId());

        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM reservation WHERE id = ?", Integer.class, savedReservation.getId());
        assertThat(deletedRows).isEqualTo(1);
        assertThat(count).isZero();
    }

    @Test
    void 동일한테마_동일한시간대에_예약이_존재한다면_true를_반환한다() {
        // given
        reservationRepository.save(new Reservation(
                null, "어셔", LocalDate.of(2026, 5, 10), time, theme));

        // when
        boolean result = reservationRepository.existsByTimeIdAndThemeId(LocalDate.of(2026, 5, 10), time.getId(), theme.getId());

        // then
        assertThat(result).isTrue();
    }
    @Test
    void 동일한테마_동일한시간대에_예약이_존재하지_않으면_false를_반환한다() {
        // when
        boolean result = reservationRepository.existsByTimeIdAndThemeId(LocalDate.of(2026, 5, 10), time.getId(), theme.getId());

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 해당_시간에_예약이_존재하면_true를_반환한다() {
        // given
        reservationRepository.save(new Reservation(
                null, "어셔", LocalDate.of(2026, 5, 10), time, theme));

        // when
        boolean result = reservationRepository.existsByTimeId(time.getId());

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 해당_시간에_예약이_존재하지_않다면_false를_반환한다() {
        // when
        boolean result = reservationRepository.existsByTimeId(time.getId());

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 해당_테마에_예약이_존재하면_true를_반환한다() {
        // given
        reservationRepository.save(new Reservation(
                null, "어셔", LocalDate.of(2026, 5, 10), time, theme));

        // when
        boolean result = reservationRepository.existsByThemeId(theme.getId());

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 해당_테마에_예약이_존재하지_않다면_false를_반환한다() {
        // when
        boolean result = reservationRepository.existsByThemeId(theme.getId());

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 사용자_이름으로_등록된_예약이_존재한다면_이름과_동일한_예약들을_반환한다() {
        // given
        reservationRepository.save(new Reservation(
                null, "어셔", LocalDate.of(2026, 5, 10), time, theme));
        reservationRepository.save(new Reservation(
                null, "어셔", LocalDate.of(2026, 5, 11), time, theme));

        // when
        List<Reservation> result = reservationRepository.findByName("어셔");

        // then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(r -> r.getName().equals("어셔"));
    }

    @Test
    void 동일한_사용자_이름으로_등록된_예약이_존재하지_않는다면_빈값을_반환한다() {
        // given
        List<Reservation> result = reservationRepository.findByName("어셔");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void 다른_이름의_사용자_예약은_조회되지_않는다() {
        // given
        reservationRepository.save(new Reservation(
                null, "어셔", LocalDate.of(2026, 5, 10), time, theme));
        reservationRepository.save(new Reservation(
                null, "레서", LocalDate.of(2026, 5, 11), time, theme));

        // when
        List<Reservation> result = reservationRepository.findByName("어셔");

        // then
        assertThat(result).hasSize(1);
        assertThat(result).allMatch(r -> r.getName().equals("어셔"));
    }
}
