package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

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
@Import(JdbcReservationRepository.class)
class JdbcReservationRepositoryTest {

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
        long id = createReservation();
        Optional<Reservation> foundReservation = jdbcReservationRepository.findById(id);
        assertThat(foundReservation).isPresent();
    }

    @DisplayName("id로 예약을 조회시 존재하지 않으면 빈 객체를 반환한다.")
    @Test
    void shouldEmptyReservationWhenReservationIdNotExist() {
        Optional<Reservation> reservation = jdbcReservationRepository.findById(1L);

        assertThat(reservation).isEmpty();
    }

    @DisplayName("존재하는 모든 예약을 반환한다.")
    @Test
    void shouldReturnAllReservationsWhenFindAll() {
        createReservation();
        List<Reservation> reservations = jdbcReservationRepository.findAll();
        assertThat(reservations).hasSize(1);
    }

    @DisplayName("존재하는 예약이 없는 경우 빈 리스트를 반환한다.")
    @Test
    void shouldReturnEmptyListWhenReservationsNotExist() {
        List<Reservation> reservations = jdbcReservationRepository.findAll();
        assertThat(reservations).isEmpty();
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
        assertThat(reservationWithId.getId()).isNotNull();
    }

    @DisplayName("id로 예약을 삭제한다.")
    @Test
    void shouldDeleteReservationWhenReservationIdExist() {
        long reservationId = createReservation();
        Reservation reservation = jdbcReservationRepository.findById(reservationId).orElseThrow();

        jdbcReservationRepository.deleteById(reservation.getId());

        Integer count = jdbcTemplate.queryForObject("select count(*) from reservation", Integer.class);
        assertThat(count).isZero();
    }

    @DisplayName("예약 시간 id를 가진 예약의 개수를 조회한다.")
    @Test
    void shouldReturnCountOfReservationWhenReservationTimeUsed() {
        long id = createReservation();

        long count = jdbcReservationRepository.findReservationCountByTimeId(id);

        assertThat(count).isOne();
    }

    @DisplayName("날짜, 시간으로 저장된 예약이 있는지 확인한다.")
    @Test
    void shouldReturnIsExistReservationWhenReservationsNameAndDateAndTimeIsSame() {
        long id = createReservation();
        Reservation reservation = jdbcReservationRepository.findById(id).orElseThrow();
        long timeId = reservation.getTime().getId();

        boolean isExist = jdbcReservationRepository.existByDateAndTimeId(
                reservation.getDate(),
                timeId);

        assertThat(isExist).isTrue();
    }

    private long createReservation() {
        LocalTime time = LocalTime.of(12, 0);
        LocalDate date = LocalDate.of(2024, 12, 25);

        ReservationTime reservationTime = reservationTimeRepository.create(new ReservationTime(time));
        Theme theme = themeRepository.create(new Theme(new ThemeName("theme1"), "desc", "url"));

        return jdbcInsert.executeAndReturnKey(Map.of(
                "name", "test",
                "date", date,
                "time_id", reservationTime.getId(),
                "theme_id", theme.getId()
        )).longValue();
    }
}
