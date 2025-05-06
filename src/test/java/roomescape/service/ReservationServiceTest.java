package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
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
import roomescape.dto.ReservationsWithTotalPageRequest;
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
    void 등록되지_않은_시간으로_예약_생성_시_예외_발생() {
        // given
        Long nonExistTimeId = 999L;
        ReservationRequest request = new ReservationRequest(
                "testName",
                LocalDate.of(2026, 12, 12),
                nonExistTimeId,
                1L
        );

        // when, then
        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 등록되지_않는_테마로_예약_생성_시_예외_발생() {
        // given
        Long nonExistThemeId = 999L;
        ReservationRequest request = new ReservationRequest(
                "testName",
                LocalDate.of(2026, 12, 12),
                1L,
                nonExistThemeId
        );

        // when, then
        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 과거_날짜로_예약_생성_시_예외_발생() {
        // given
        ReservationRequest request = new ReservationRequest(
                "testName",
                LocalDate.of(2023, 2, 25),
                1L,
                1L
        );

        // when, then
        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(IllegalStateException.class);

    }

    @Test
    void 날짜와_테마와_시간이_동시에_중복된_예약에_대해서_예외처리() {
        // given
        ReservationRequest request = new ReservationRequest(
                "testName",
                LocalDate.of(2023, 3, 1),
                1L,
                1L
        );

        // when, then
        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 예약을_정상적으로_삭제() {
        ReservationTime savedTime = reservationTimeDao.save(new ReservationTime(null, LocalTime.of(10, 0)));
        Theme savedTheme = themeDao.save(new Theme(null, "제목", "de", "th"));
        ReservationRequest request = new ReservationRequest("이름", LocalDate.of(2025, 12, 16), savedTime.getId(),
                savedTheme.getId());
        ReservationResponse response = reservationService.addReservation(request);

        assertThatCode(() -> reservationService.deleteReservation(response.id()))
                .doesNotThrowAnyException();
    }

    @Test
    void 예약이_존재하지_않으면_예외발생() {
        assertThatThrownBy(() -> reservationService.deleteReservation(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 해당하는_페이지의_예약을_반환한다() {
        // given
        int page = 2;

        // when
        ReservationsWithTotalPageRequest reservationsWithTotalPage = reservationService.getReservationsByPage(page);

        // then
        assertThat(reservationsWithTotalPage.reservations()).hasSize(10);
        assertThat(reservationsWithTotalPage.reservations().getFirst().id()).isEqualTo(11L);
        assertThat(reservationsWithTotalPage.reservations().getLast().id()).isEqualTo(20L);
    }

    @Test
    void 페이지의_개수보다_큰_페이지를_요청하면_예외가_발생한다() {
        // given
        int page = 4;

        // when, then
        assertThatThrownBy(() -> reservationService.getReservationsByPage(page))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
