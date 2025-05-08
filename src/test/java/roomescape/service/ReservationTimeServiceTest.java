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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        ReservationTimeDao reservationTimeDao = new ReservationTimeDao(namedParameterJdbcTemplate);
        ReservationDao reservationDao = new ReservationDao(namedParameterJdbcTemplate);
        ThemeDao themeDao = new ThemeDao(namedParameterJdbcTemplate);
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
    void 예약시간이_중복인_경우_예외처리() {
        // given
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(13, 40));

        // when, then
        assertThatThrownBy(() -> reservationTimeService.addTime(request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 예약시간_리스트_정상적으로_조회() {
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.getReservationTimes();

        assertThat(reservationTimes).hasSize(4);
    }

    @Test
    void 예약시간을_정상적으로_삭제() {
        Long id = 4L;

        reservationTimeService.deleteTime(id);
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.getReservationTimes();

        assertThat(reservationTimes).hasSize(3);
    }

    @Test
    void 삭제하려는_예약시간이_존재하지_않으면_예외() {
        assertThatThrownBy(() -> reservationTimeService.deleteTime(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 삭제_시_해당_시간의_예약_존재_시_예외발생() {
        // given
        Long id = 1L;

        // when, then
        assertThatThrownBy(() -> reservationTimeService.deleteTime(id))
                .isInstanceOf(IllegalStateException.class);
    }
}
