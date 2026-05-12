package roomescape.reservation.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcReservationRepository.class)
class JdbcReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("id로 특정 예약을 조회한다.")
    public void findById() {
        // given
        ReservationTime time = insertReservationTime(LocalTime.of(10, 0));
        Theme theme = insertTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png");
        Reservation reservation = insertReservation("브라운", LocalDate.of(2023, 8, 5), time, theme);

        // when
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservation.getId());

        // then
        assertThat(optionalReservation).isPresent();
        Reservation found = optionalReservation.get();
        assertThat(found)
                .extracting(
                        Reservation::getId, Reservation::getGuestName, Reservation::getDate,
                        Reservation::getTime, Reservation::getTheme
                ).containsExactly(
                        reservation.getId(), reservation.getGuestName(), reservation.getDate(),
                        reservation.getTime(), reservation.getTheme()
                );
    }

    @Test
    @DisplayName("예약의 목록을 조회한다")
    void findAll() {
        // given
        ReservationTime time = insertReservationTime(LocalTime.of(10, 0));
        Theme theme = insertTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png");
        Reservation reservation = insertReservation("브라운", LocalDate.of(2023, 8, 5), time, theme);

        // when
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        assertThat(reservations).hasSize(1);

        Reservation found = reservations.getFirst();
        assertThat(found.getId()).isEqualTo(reservation.getId());
        assertThat(found.getGuestName()).isEqualTo("브라운");
        assertThat(found.getDate()).isEqualTo(LocalDate.of(2023, 8, 5));

        assertThat(found.getTime().getId()).isEqualTo(time.getId());
        assertThat(found.getTime().getStartAt()).isEqualTo(LocalTime.of(10, 0));

        assertThat(found.getTheme().getId()).isEqualTo(theme.getId());
        assertThat(found.getTheme().getName()).isEqualTo(theme.getName());
        assertThat(found.getTheme().getDescription()).isEqualTo(theme.getDescription());
        assertThat(found.getTheme().getThumbnail()).isEqualTo(theme.getThumbnail());
    }

    @Test
    @DisplayName("예약을 저장한다..")
    public void save() {
        // given
        ReservationTime time = insertReservationTime(LocalTime.of(10, 0));
        Theme theme = insertTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png");
        Reservation reservation = new Reservation("브라운", LocalDate.of(2023, 8, 5), time, theme);

        // when
        Reservation saved = reservationRepository.save(reservation);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved)
                .extracting(
                        Reservation::getGuestName, Reservation::getDate,
                        Reservation::getTime, Reservation::getTheme
                ).containsExactly(
                        reservation.getGuestName(), reservation.getDate(),
                        reservation.getTime(), reservation.getTheme()
                );
    }

    @Test
    @DisplayName("예약의 날짜 및 시간을 수정한다.")
    public void updateDateAndTime() {
        // given
        ReservationTime time = insertReservationTime(LocalTime.of(10, 0));
        Theme theme = insertTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png");
        Reservation reservation = insertReservation("브라운", LocalDate.of(2023, 8, 5), time, theme);

        LocalDate updatedDate = LocalDate.of(2023, 9, 7);
        ReservationTime updatedTime = insertReservationTime(LocalTime.of(12, 0));

        // when
        boolean result = reservationRepository.updateDateAndTime(reservation.getId(), updatedDate, updatedTime.getId());

        // then
        assertThat(result).isTrue();

        Map<String, Object> map = findById(reservation);
        LocalDate date = ((Date) map.get("date")).toLocalDate();
        Long timeId = ((Number) map.get("time_id")).longValue();
        assertThat(date).isEqualTo(updatedDate);
        assertThat(timeId).isEqualTo(updatedTime.getId());
    }

    private Map<String, Object> findById(Reservation reservation) {
        return jdbcTemplate.queryForMap("""
        SELECT
            r.date,
            t.id AS time_id
        FROM reservation r
        INNER JOIN reservation_time t
            ON r.time_id = t.id
        WHERE r.id = ?
        """, reservation.getId());
    }

    @Test
    @DisplayName("특정 날짜, 시간, 테마를 가진 예약이 존재하는지 확인한다.")
    public void existsByDateAndTimeIdAndThemeId() {
        // given
        ReservationTime time = insertReservationTime(LocalTime.of(10, 0));
        ReservationTime time2 = insertReservationTime(LocalTime.of(12, 0));
        Theme theme = insertTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png");
        Theme theme2 = insertTheme("레벨3 탈출", "우테코 레벨3을 탈출하는 내용입니다.", "https://example.com/theme.png");
        LocalDate targetDate = LocalDate.of(2023, 8, 5);
        insertReservation("브라운", targetDate, time, theme);

        // when
        boolean exists = reservationRepository.existsByDateAndTimeIdAndThemeId(targetDate, time.getId(), theme.getId());
        boolean notExists = reservationRepository.existsByDateAndTimeIdAndThemeId(targetDate, time2.getId(), theme2.getId());

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void deleteById() {
        ReservationTime time = insertReservationTime(LocalTime.of(10, 0));
        Theme theme = insertTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png");
        Reservation reservation = insertReservation("브라운", LocalDate.of(2023, 8, 5), time, theme);

        boolean deleted = reservationRepository.deleteById(reservation.getId());

        assertThat(deleted).isTrue();
        assertThat(reservationRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 예약은 삭제되지 않는다.")
    public void deleteById_fail() {
        // given
        Long id = 1L;

        // when
        boolean deleted = reservationRepository.deleteById(id);

        // then
        assertThat(deleted).isFalse();
    }

    @Test
    @DisplayName("특정 예약 시간 id를 가진 예약이 존재하는지 확인한다.")
    public void existByTimeId() {
        // given
        ReservationTime time = insertReservationTime(LocalTime.of(10, 0));
        ReservationTime time2 = insertReservationTime(LocalTime.of(12, 0));
        Theme theme = insertTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png");
        insertReservation("브라운", LocalDate.of(2023, 8, 5), time, theme);

        // when
        boolean exists = reservationRepository.existByTimeId(time.getId());
        boolean notExists = reservationRepository.existByTimeId(time2.getId());

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("특정 테마 id를 가진 예약이 존재하는지 확인한다.")
    public void existByThemeId() {
        // given
        ReservationTime time = insertReservationTime(LocalTime.of(10, 0));
        Theme theme = insertTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png");
        Theme theme2 = insertTheme("레벨3 탈출", "우테코 레벨3을 탈출하는 내용입니다.", "https://example.com/theme.png");
        insertReservation("브라운", LocalDate.of(2023, 8, 5), time, theme);

        // when
        boolean exists = reservationRepository.existByThemeId(theme.getId());
        boolean notExists = reservationRepository.existByThemeId(theme2.getId());

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
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

    private Reservation insertReservation(String guestName, LocalDate date, ReservationTime time, Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("""
                    INSERT INTO reservation (guest_name, date, time_id, theme_id)
                    VALUES (?, ?, ?, ?)
                    """, new String[]{"id"});
            preparedStatement.setString(1, guestName);
            preparedStatement.setDate(2, Date.valueOf(date));
            preparedStatement.setLong(3, time.getId());
            preparedStatement.setLong(4, theme.getId());
            return preparedStatement;
        }, keyHolder);

        return new Reservation(getGeneratedId(keyHolder), guestName, date, time, theme);
    }

    private Long getGeneratedId(KeyHolder keyHolder) {
        return keyHolder.getKey().longValue();
    }
}
