package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.model.ReservationTime;
import roomescape.model.Role;
import roomescape.model.Theme;
import roomescape.model.ThemeName;
import roomescape.model.User;
import roomescape.model.UserName;

class ReservationServiceTest {

    private ReservationService reservationService;
    private ReservationTimeDao reservationTimeDao;
    private ThemeDao themeDao;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @BeforeEach
    void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        ReservationDao reservationDao = new ReservationDao(namedParameterJdbcTemplate);
        reservationTimeDao = new ReservationTimeDao(namedParameterJdbcTemplate);
        themeDao = new ThemeDao(namedParameterJdbcTemplate);
        Clock clock = Clock.fixed(Instant.parse("2023-02-25T08:25:24Z"), ZoneId.systemDefault());
        reservationService = new ReservationService(reservationDao, reservationTimeDao, themeDao, clock);
    }

    @Test
    void 예약을_정상적으로_추가() {
        ReservationTime savedTime = reservationTimeDao.save(new ReservationTime(null, LocalTime.of(10, 0)));
        Theme savedTheme = themeDao.save(new Theme(null, new ThemeName("제목"), "de", "th"));
        ReservationRequest request = new ReservationRequest(LocalDate.of(2025, 12, 16), savedTime.getId(),
                savedTheme.getId());
        User user = new User(1L, new UserName("이름"), "aaaa@email.com", "", Role.USER);

        ReservationResponse response = reservationService.addReservation(user, request);

        assertThat(response.id()).isNotNull();
        assertThat(response.user().name()).isEqualTo("이름");
        assertThat(response.date()).isEqualTo(LocalDate.of(2025, 12, 16).toString());
    }

    @Test
    void 등록되지_않은_시간으로_예약_생성_시_예외_발생() {
        // given
        Long nonExistTimeId = 999L;
        ReservationRequest request = new ReservationRequest(
                LocalDate.of(2026, 12, 12),
                nonExistTimeId,
                1L
        );
        User user = new User(1L, new UserName("이름"), "aaaa@email.com", "", Role.USER);

        // when, then
        assertThatThrownBy(() -> reservationService.addReservation(user, request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 등록되지_않는_테마로_예약_생성_시_예외_발생() {
        // given
        Long nonExistThemeId = 999L;
        ReservationRequest request = new ReservationRequest(
                LocalDate.of(2026, 12, 12),
                1L,
                nonExistThemeId
        );
        User user = new User(1L, new UserName("이름"), "aaaa@email.com", "", Role.USER);

        // when, then
        assertThatThrownBy(() -> reservationService.addReservation(user, request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 과거_날짜로_예약_생성_시_예외_발생() {
        // given
        ReservationRequest request = new ReservationRequest(
                LocalDate.of(2023, 2, 25),
                1L,
                1L
        );
        User user = new User(1L, new UserName("이름"), "aaaa@email.com", "", Role.USER);

        // when, then
        assertThatThrownBy(() -> reservationService.addReservation(user, request))
                .isInstanceOf(IllegalStateException.class);

    }

    @Test
    void 날짜와_테마와_시간이_동시에_중복된_예약에_대해서_예외처리() {
        // given
        ReservationRequest request = new ReservationRequest(
                LocalDate.of(2023, 3, 1),
                1L,
                1L
        );
        User user = new User(1L, new UserName("이름"), "aaaa@email.com", "", Role.USER);

        // when, then
        assertThatThrownBy(() -> reservationService.addReservation(user, request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 예약_리스트를_정상적으로_조회() {
        List<ReservationResponse> reservations = reservationService.getReservations();
        assertThat(reservations.size()).isEqualTo(25);
    }

    @Test
    void 예약을_정상적으로_삭제() {
        ReservationTime savedTime = reservationTimeDao.save(new ReservationTime(null, LocalTime.of(10, 0)));
        Theme savedTheme = themeDao.save(new Theme(null, new ThemeName("제목"), "de", "th"));
        ReservationRequest request = new ReservationRequest(LocalDate.of(2025, 12, 16), savedTime.getId(),
                savedTheme.getId());
        User user = new User(1L, new UserName("이름"), "aaaa@email.com", "", Role.USER);
        ReservationResponse response = reservationService.addReservation(user, request);

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
