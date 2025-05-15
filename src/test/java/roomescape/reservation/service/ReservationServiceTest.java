package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.global.exception.RoomEscapeException.BadRequestException;
import roomescape.global.exception.RoomEscapeException.ResourceNotFoundException;
import roomescape.reservation.dto.request.CreateReservationRequest;
import roomescape.reservation.dto.response.AdminReservationPageResponse;
import roomescape.reservation.dto.response.CreateReservationResponse;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservationtime.repository.ReservationTimeDao;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.repository.ThemeDao;
import roomescape.theme.service.ThemeService;
import roomescape.user.repository.UserDao;
import roomescape.user.service.UserService;

class ReservationServiceTest {

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();

        ReservationDao reservationDao = new ReservationDao(dataSource);
        ReservationTimeDao reservationTimeDao = new ReservationTimeDao(dataSource);
        ThemeDao themeDao = new ThemeDao(dataSource);
        UserDao userDao = new UserDao(dataSource);

        ReservationTimeService reservationTimeService = new ReservationTimeService(reservationDao, reservationTimeDao);
        ThemeService themeService = new ThemeService(reservationDao, reservationTimeDao, themeDao);
        UserService userService = new UserService(userDao);
        reservationService = new ReservationService(reservationDao, userService, reservationTimeService, themeService);
    }

    @Test
    void 예약을_정상적으로_추가() {
        LocalDate date = LocalDate.now().plusDays(1);
        Long nonExistThemeId = 1L;
        Long timeId = 1L;
        Long userId = 1L;
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                date,
                nonExistThemeId,
                timeId,
                userId
        );

        CreateReservationResponse response = reservationService.addReservation(createReservationRequest);

        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo("어드민1");
        assertThat(response.date()).isEqualTo(LocalDate.now().plusDays(1).toString());
    }

    @Test
    void 등록되지_않은_시간으로_예약_생성_시_예외_발생() {
        // given
        LocalDate date = LocalDate.now();
        Long themeId = 1L;
        Long timeId = 999L;
        Long userId = 1L;
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                date,
                themeId,
                timeId,
                userId
        );

        // when, then
        assertThatThrownBy(() -> reservationService.addReservation(createReservationRequest))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 등록되지_않는_테마로_예약_생성_시_예외_발생() {
        // given
        LocalDate date = LocalDate.of(2026, 12, 12);
        Long themeId = 999L;
        Long timeId = 1L;
        Long userId = 1L;
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                date,
                themeId,
                timeId,
                userId
        );

        // when, then
        assertThatThrownBy(() -> reservationService.addReservation(createReservationRequest))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 과거_날짜로_예약_생성_시_예외_발생() {
        // given
        LocalDate date = LocalDate.of(2023, 2, 25);
        Long themeId = 1L;
        Long timeId = 1L;
        Long userId = 1L;
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                date,
                themeId,
                timeId,
                userId
        );

        // when, then
        assertThatThrownBy(() -> reservationService.addReservation(createReservationRequest))
                .isInstanceOf(BadRequestException.class);

    }

    @Test
    void 날짜와_테마와_시간이_동시에_중복된_예약에_대해서_예외처리() {
        // given
        LocalDate date = LocalDate.of(2023, 3, 1);
        Long themeId = 1L;
        Long timeId = 1L;
        Long userId = 1L;
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                date,
                themeId,
                timeId,
                userId
        );

        // when, then
        assertThatThrownBy(() -> reservationService.addReservation(createReservationRequest))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void 예약을_정상적으로_삭제() {
        LocalDate date = LocalDate.now().plusDays(1);
        Long themeId = 1L;
        Long timeId = 1L;
        Long userId = 1L;
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                date,
                themeId,
                timeId,
                userId
        );
        CreateReservationResponse response = reservationService.addReservation(createReservationRequest);

        assertThatCode(() -> reservationService.deleteReservation(response.id()))
                .doesNotThrowAnyException();
    }

    @Test
    void 예약이_존재하지_않으면_예외발생() {
        assertThatThrownBy(() -> reservationService.deleteReservation(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 해당하는_페이지의_예약을_반환한다() {
        // given
        int page = 2;

        // when
        AdminReservationPageResponse reservationsWithTotalPage = reservationService.getReservationsByPage(page,
                null, null, null, null);

        // then
        assertThat(reservationsWithTotalPage.reservations()).hasSize(10);
        assertThat(reservationsWithTotalPage.reservations().getFirst().id()).isEqualTo(15L);
        assertThat(reservationsWithTotalPage.reservations().getLast().id()).isEqualTo(6L);
    }

    @Test
    void 페이지의_개수보다_큰_페이지를_요청하면_예외가_발생한다() {
        // given
        int page = 4;

        // when, then
        assertThatThrownBy(() -> reservationService.getReservationsByPage(page, null, null, null, null))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
