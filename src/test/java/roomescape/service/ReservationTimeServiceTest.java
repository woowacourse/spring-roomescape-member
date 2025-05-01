package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;

@DisplayNameGeneration(ReplaceUnderscores.class)
class ReservationTimeServiceTest {
    private ReservationTimeService reservationTimeService;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        ReservationTimeDao reservationTimeDao = new ReservationTimeDao(jdbcTemplate);
        ReservationDao reservationDao = new ReservationDao(jdbcTemplate);
        ThemeDao themeDao = new ThemeDao(jdbcTemplate);
        reservationTimeService = new ReservationTimeService(reservationDao, reservationTimeDao, themeDao);
    }

    @Test
    void 예약시간을_정상적으로_추가() {
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(10, 0));
        ReservationTimeResponse response = reservationTimeService.addTime(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.startAt()).isEqualTo(LocalTime.of(10, 0).toString());
    }

    @Test
    void 예약시간_리스트_정상적으로_조회() {
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(10, 0));
        reservationTimeService.addTime(request);

        List<ReservationTimeResponse> reservationTimes = reservationTimeService.getReservationTimes();

        assertThat(reservationTimes).hasSize(4);
    }

    @Test
    void 예약시간을_정상적으로_삭제() {
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(10, 0));
        ReservationTimeResponse saved = reservationTimeService.addTime(request);
        Long id = saved.id();

        reservationTimeService.deleteTime(id);
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.getReservationTimes();

        assertThat(reservationTimes).hasSize(3);
    }

    @Test
    void 삭제하려는_예약시간이_존재하지_않으면_예외() {
        assertThatThrownBy(() -> reservationTimeService.deleteTime(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
