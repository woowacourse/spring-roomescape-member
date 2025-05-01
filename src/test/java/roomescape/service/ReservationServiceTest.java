package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

class ReservationServiceTest {

    private ReservationService reservationService;
    private ReservationTimeDao reservationTimeDao;
    private ThemeDao themeDao;

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        ReservationDao reservationDao = new ReservationDao(jdbcTemplate);
        reservationTimeDao = new ReservationTimeDao(jdbcTemplate);
        themeDao = new ThemeDao(jdbcTemplate);
        reservationService = new ReservationService(reservationDao, reservationTimeDao, themeDao);
    }

    @Test
    void 예약을_정상적으로_추가() {
        ReservationTime savedTime = reservationTimeDao.save(new ReservationTime(null, LocalTime.of(10, 0)));
        Theme savedTheme = themeDao.save(new Theme(null, "제목", "de", "th"));
        ReservationRequest request = new ReservationRequest("이름", LocalDate.of(2025, 12, 16), savedTime.getId(),
                savedTheme.getId());

        ReservationResponse response = reservationService.addReservation(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("이름");
        assertThat(response.date()).isEqualTo(LocalDate.of(2025, 12, 16).toString());
    }

    @Test
    void 예약_리스트를_정상적으로_조회() {
        List<ReservationResponse> reservations = reservationService.getReservations();
        assertThat(reservations.size()).isEqualTo(25);
    }

    @Test
    void 예약을_정상적으로_삭제() {
        ReservationTime savedTime = reservationTimeDao.save(new ReservationTime(null, LocalTime.of(10, 0)));
        Theme savedTheme = themeDao.save(new Theme(null, "제목", "de", "th"));
        ReservationRequest request = new ReservationRequest("이름", LocalDate.of(2025, 12, 16), savedTime.getId(),
                savedTheme.getId());
        ReservationResponse response = reservationService.addReservation(request);

        reservationService.deleteReservation(response.id());
        List<ReservationResponse> reservations = reservationService.getReservations();

        assertThat(reservations).hasSize(25);
    }

    @Test
    void 예약이_존재하지_않으면_예외발생() {
        assertThatThrownBy(() -> reservationService.deleteReservation(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
