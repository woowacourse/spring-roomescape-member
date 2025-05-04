package roomescape.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

@JdbcTest
@Import(ReservationJdbcDao.class)
public class ReservationJdbcDaoTest {

    private static final RowMapper<Reservation> RESERVATION_ROW_MAPPER_WITHOUT_JOIN = (resultSet, rowNum) ->
            new Reservation(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getDate("date").toLocalDate(),
                    null,
                    null
            );

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long savedId;
    private LocalDate date;
    private Reservation savedReservation;
    private ReservationTime reservationTime;
    private Theme theme;

    @BeforeEach
    void setUp() {
        Theme tempTheme = new Theme("새로운 테마", "새로운 테마입니다", "Image");
        Long themeId = saveNewTheme(tempTheme);
        this.theme = new Theme(themeId, tempTheme.getName(), tempTheme.getDescription(), tempTheme.getThumbnail());

        ReservationTime tempReservationTime = new ReservationTime(LocalTime.of(12, 30));
        Long reservationTimeId = saveNewReservationTime(tempReservationTime);
        this.reservationTime = new ReservationTime(reservationTimeId, tempReservationTime.getStartAt());

        this.date = LocalDate.now().plusDays(1);
        this.savedReservation = new Reservation("히로", date, reservationTime, theme);
        this.savedId = saveNewReservation(savedReservation);
    }

    @Test
    @DisplayName("모든 예약을 조회한다")
    void test1() {
        // when
        List<Reservation> reservations = reservationDao.findAll();

        // then
        List<String> names = reservations.stream()
                .map(Reservation::getName).toList();

        assertAll(
                () -> assertThat(reservations).hasSize(1),
                () -> assertThat(names).containsExactly("히로")
        );
    }

    @Test
    @DisplayName("예약을 저장한다")
    void test2() {
        // given
        Reservation reservation = new Reservation("히로", date, reservationTime, theme);

        // when
        Long savedId = reservationDao.saveReservation(reservation);

        // then
        List<Reservation> foundReservations = jdbcTemplate.query(
                "SELECT * FROM reservation where id = ?",
                RESERVATION_ROW_MAPPER_WITHOUT_JOIN,
                savedId
        );

        assertAll(
                () -> assertThat(foundReservations).hasSize(1),
                () -> assertThat(foundReservations.getFirst().getName()).isEqualTo("히로"),
                () -> assertThat(foundReservations.getFirst().getDate()).isEqualTo(date)
        );

    }

    @Test
    @DisplayName("날짜와 시각을 이용해 테마를 찾는다")
    void test3() {
        // when
        Optional<Reservation> foundReservation = reservationDao.findByDateAndTime(
                new Reservation("히로", this.date, this.reservationTime, this.theme)
        );

        // then
        assertAll(
                () -> assertThat(foundReservation.isPresent()).isTrue(),
                () -> assertThat(foundReservation.get().getName()).isEqualTo("히로"),
                () -> assertThat(foundReservation.get().getDate()).isEqualTo(this.date)
        );
    }

    //
    @Test
    @DisplayName("id 를 이용해 예약을 삭제한다")
    void test4() {
        // when
        reservationDao.deleteById(savedId);

        // then
        List<Reservation> foundReservations = jdbcTemplate.query(
                "SELECT * FROM reservation where id = ?",
                RESERVATION_ROW_MAPPER_WITHOUT_JOIN,
                savedId
        );

        assertThat(foundReservations).hasSize(0);
    }

    private Long saveNewReservation(Reservation reservation) {
        String sql = "INSERT INTO reservation (name, date, time_id, theme_id) values (?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setDate(2, Date.valueOf(reservation.getDate()));
            ps.setLong(3, reservation.getTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }


    private Long saveNewTheme(Theme theme) {
        String sql = "INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            ps.setString(3, theme.getThumbnail());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    private Long saveNewReservationTime(ReservationTime reservationTime) {
        String sql = "INSERT INTO reservation_time (start_at) values(?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setTime(1, java.sql.Time.valueOf(reservationTime.getStartAt()));
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
}
