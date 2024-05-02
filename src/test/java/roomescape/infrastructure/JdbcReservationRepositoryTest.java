package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.domain.PlayerName;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeName;
import roomescape.domain.ThemeRepository;

@JdbcTest
@Import(value = {JdbcReservationRepository.class, JdbcReservationTimeRepository.class, JdbcThemeRepository.class})
class JdbcReservationRepositoryTest {
    private static final String INSERT_SQL =
            "insert into reservation (id, name, date, time_id, theme_id) values (?, ?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final JdbcReservationRepository jdbcReservationRepository;
    private final ThemeRepository themeRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public JdbcReservationRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingColumns("name", "date", "time_id", "theme_id")
                .usingGeneratedKeyColumns("id");
        this.jdbcReservationRepository = new JdbcReservationRepository(jdbcTemplate);
        this.themeRepository = new JdbcThemeRepository(jdbcTemplate);
        this.reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
    }

    @DisplayName("id로 예약을 조회한다.")
    @Test
    void shouldReturnReservationWhenReservationIdExist() {
        long id = createReservation().getId();
        Optional<Reservation> foundReservation = jdbcReservationRepository.findById(id);
        assertThat(foundReservation).isPresent();
    }

    @DisplayName("id로 예약을 조회시 존재하지 않으면 빈 객체를 반환한다.")
    @Test
    void shouldEmptyReservationWhenReservationIdNotExist() {
        Optional<Reservation> reservation = jdbcReservationRepository.findById(99L);
        assertThat(reservation).isEmpty();
    }

    @DisplayName("존재하는 모든 예약을 반환한다.")
    @Test
    void shouldReturnAllReservationsWhenFindAll() {
        createReservation();
        List<Reservation> reservations = jdbcReservationRepository.findAll();
        assertThat(reservations).hasSize(1);
    }

    @DisplayName("예약을 저장하면 id를 가진 예약을 저장 후 반환한다.")
    @Test
    void shouldReturnReservationWithIdWhenReservationSave() {
        ReservationTime reservationTime = reservationTimeRepository.create(new ReservationTime(LocalTime.of(12, 0)));
        Theme theme = themeRepository.create(new Theme(new ThemeName("theme1"), "desc", "url"));
        Reservation reservationWithoutId = new Reservation(
                new PlayerName("test"),
                LocalDate.of(2024, 12, 25),
                reservationTime,
                theme
        );
        Reservation reservationWithId = jdbcReservationRepository.create(reservationWithoutId);
        int totalRowCount = getTotalRowCount();
        assertAll(
                () -> assertThat(reservationWithId.getId()).isNotNull(),
                () -> assertThat(totalRowCount).isEqualTo(1)
        );
    }

    @DisplayName("id로 예약을 삭제한다.")
    @Test
    void shouldDeleteReservationWhenReservationIdExist() {
        long id = createReservation().getId();
        jdbcReservationRepository.deleteById(id);
        int totalRowCount = getTotalRowCount();
        assertThat(totalRowCount).isZero();
    }

    @DisplayName("예약 시간 id를 가진 예약의 개수를 조회한다.")
    @Test
    void shouldReturnCountOfReservationWhenReservationTimeUsed() {
        long id = createReservation().getId();
        long count = jdbcReservationRepository.findReservationCountByTimeId(id);
        assertThat(count).isOne();
    }

    @DisplayName("날짜, 시간으로 저장된 예약이 있는지 확인한다.")
    @Test
    void shouldReturnIsExistReservationWhenReservationsNameAndDateAndTimeIsSame() {
        Reservation reservation = createReservation();
        ReservationTime time = reservation.getTime();
        Theme theme = reservation.getTheme();

        boolean isExist = jdbcReservationRepository.existBy(reservation.getDate(), time.getId(), theme.getId());
        assertThat(isExist).isTrue();
    }

    private Reservation createReservation() {
        ReservationTime reservationTime = reservationTimeRepository.create(new ReservationTime(LocalTime.of(12, 0)));
        Theme theme = themeRepository.create(new Theme(new ThemeName("theme1"), "desc", "url"));
        LocalDate date = LocalDate.of(2024, 12, 25);
        long id = jdbcInsert.executeAndReturnKey(Map.of(
                "name", "test",
                "date", date,
                "time_id", reservationTime.getId(),
                "theme_id", theme.getId()
        )).longValue();
        return new Reservation(id, new PlayerName("test"), date, reservationTime, theme);
    }

    private int getTotalRowCount() {
        String sql = "select count(*) from reservation";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
