package roomescape.reservationtime.repository;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.dto.ReservationTimeAvailability;
import roomescape.theme.domain.Theme;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcReservationTimeRepository.class)
class JdbcReservationTimeRepositoryTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    void 예약_시간을_저장하고_조회한다() {
        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));

        Optional<ReservationTime> found = reservationTimeRepository.findById(reservationTime.getId());

        assertThat(found).isPresent();
        ReservationTime time = found.get();
        assertThat(time.getId()).isEqualTo(reservationTime.getId());
        assertThat(time.getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 예약_시간_목록을_조회한다() {
        insertReservationTime(LocalTime.of(10, 0));

        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        assertThat(reservationTimes).hasSize(1);
        assertThat(reservationTimes.getFirst().getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void 예약_시간_존재_여부를_조회한다() {
        insertReservationTime(LocalTime.of(10, 0));

        boolean exists = reservationTimeRepository.existsByStartAt(LocalTime.of(10, 0));

        assertThat(exists).isTrue();
    }

    @Test
    void 예약_시간을_삭제한다() {
        ReservationTime reservationTime = insertReservationTime(LocalTime.of(10, 0));

        boolean deleted = reservationTimeRepository.deleteById(reservationTime.getId());

        assertThat(deleted).isTrue();
        assertThat(reservationTimeRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간은 삭제되지 않는다.")
    public void deleteById_fail() {
        // given
        Long id = 1L;

        // when
        boolean deleted = reservationTimeRepository.deleteById(id);

        // then
        assertThat(deleted).isFalse();
    }

    @Test
    @DisplayName("이용 가능한 시간을 조회한다.")
    public void findAllByDateAndThemeIdWithAvailability() {
        // given
        ReservationTime time = insertReservationTime(LocalTime.of(10, 0));
        ReservationTime time2 = insertReservationTime(LocalTime.of(12, 0));
        Theme targetTheme = insertTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png");
        Theme nonTargetTheme = insertTheme("레벨3 탈출", "우테코 레벨3을 탈출하는 내용입니다.", "https://example.com/theme.png");

        LocalDate targetDate = LocalDate.of(2023, 8, 5);
        insertReservation("브라운", targetDate, time, targetTheme);
        insertReservation("브라운", LocalDate.of(2024, 9, 10), time, targetTheme);
        insertReservation("브라운", targetDate, time, nonTargetTheme);

        // when
        List<ReservationTimeAvailability> availableTimes = reservationTimeRepository.findAllByDateAndThemeIdWithAvailability(targetDate, targetTheme.getId());

        // then
        assertThat(availableTimes).hasSize(2)
                .extracting(ReservationTimeAvailability::getReservationTime,
                        ReservationTimeAvailability::isAvailable)
                .containsExactly(Tuple.tuple(time, false), Tuple.tuple(time2, true));
    }

    private ReservationTime insertReservationTime(LocalTime startAt) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    INSERT INTO reservation_time (start_at)
                    VALUES (?)
                    """, new String[]{"id"});
            preparedStatement.setString(1, startAt.toString());
            return preparedStatement;
        }, keyHolder);

        return new ReservationTime(getGeneratedId(keyHolder), startAt);
    }

    private Theme insertTheme(String name, String description, String thumbnail) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    INSERT INTO theme (name, description, thumbnail)
                    VALUES (?, ?, ?)
                    """, new String[]{"id"});
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, thumbnail);
            return preparedStatement;
        }, keyHolder);

        return new Theme(getGeneratedId(keyHolder), name, description, thumbnail);
    }

    private void insertReservation(String guestName, LocalDate date, ReservationTime time, Theme theme) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    INSERT INTO reservation (guest_name, date, time_id, theme_id)
                    VALUES (?, ?, ?, ?)
                    """);
            preparedStatement.setString(1, guestName);
            preparedStatement.setDate(2, Date.valueOf(date));
            preparedStatement.setLong(3, time.getId());
            preparedStatement.setLong(4, theme.getId());
            return preparedStatement;
        });
    }

    private Long getGeneratedId(KeyHolder keyHolder) {
        return keyHolder.getKey().longValue();
    }
}
