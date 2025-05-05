package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roomescape.reservation.business.model.entity.Reservation;
import roomescape.reservation.business.model.entity.ReservationTime;
import roomescape.theme.business.model.entity.Theme;
import roomescape.reservation.business.model.repository.ReservationDao;
import roomescape.reservation.business.model.repository.ReservationTimeDao;
import roomescape.theme.business.model.repository.ThemeDao;
import roomescape.reservation.business.service.ReservationService;
import roomescape.reservation.presentation.request.ReservationRequest;
import roomescape.reservation.presentation.response.AvailableReservationTimeResponse;
import roomescape.reservation.presentation.response.ReservationResponse;
import roomescape.reservation.presentation.response.ReservationTimeResponse;
import roomescape.theme.presentation.response.ThemeResponse;

class ReservationServiceTest {

    private ReservationDao reservationDao;
    private ReservationTimeDao reservationTimeDao;
    private ThemeDao themeDao;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        this.reservationDao = Mockito.mock(ReservationDao.class);
        this.reservationTimeDao = Mockito.mock(ReservationTimeDao.class);
        this.themeDao = Mockito.mock(ThemeDao.class);
        this.reservationService = new ReservationService(reservationDao, reservationTimeDao, themeDao);
    }

    @Test
    void 예약기록을_조회한다() {
        Mockito.when(reservationDao.findAll())
                .thenReturn(List.of(
                        new Reservation(1L, "드라고", LocalDate.of(2025, 1, 1),
                                new ReservationTime(1L, LocalTime.of(10, 0)), new Theme(1L, "테마1", "", "")),
                        new Reservation(2L, "엠제이", LocalDate.of(2025, 1, 2),
                                new ReservationTime(1L, LocalTime.of(10, 0)), new Theme(1L, "테마1", "", ""))
                ));

        assertThat(reservationService.findAll()).isEqualTo(
                List.of(
                        new ReservationResponse(1L, "드라고", LocalDate.of(2025, 1, 1),
                                new ReservationTimeResponse(1L, LocalTime.of(10, 0)),
                                new ThemeResponse(1L, "테마1", "", "")),
                        new ReservationResponse(2L, "엠제이", LocalDate.of(2025, 1, 2),
                                new ReservationTimeResponse(1L, LocalTime.of(10, 0)),
                                new ThemeResponse(1L, "테마1", "", ""))
                )
        );
    }

    @Test
    void 예약을_추가한다() {
        ReservationRequest request = new ReservationRequest("드라고", LocalDate.now().plusDays(1), 1L, 1L);
        Mockito.when(reservationTimeDao.findById(Mockito.any()))
                .thenReturn(Optional.of(new ReservationTime(1L, LocalTime.of(10, 0))));
        Mockito.when(themeDao.findById(Mockito.any()))
                .thenReturn(Optional.of(new Theme(1L, "인터스텔라", "", "")));
        Mockito.when(reservationDao.findByDateAndThemeId(Mockito.any(), Mockito.any()))
                .thenReturn(List.of());
        Mockito.when(reservationDao.save(Mockito.any()))
                .thenReturn(new Reservation(1L, "드라고", LocalDate.now().plusDays(1),
                        new ReservationTime(1L, LocalTime.of(10, 0)), new Theme(1L, "인터스텔라", "", "")));

        assertThat(reservationService.add(request))
                .isEqualTo(new ReservationResponse(
                        1L,
                        "드라고",
                        LocalDate.now().plusDays(1),
                        new ReservationTimeResponse(1L, LocalTime.of(10, 0)),
                        new ThemeResponse(1L, "인터스텔라", "", "")
                ));
    }

    @Test
    void 예약을_삭제한다() {
        Long id = 1L;
        Mockito.when(reservationDao.deleteById(Mockito.any()))
                .thenReturn(1);

        assertThatCode(() -> reservationService.deleteById(id))
                .doesNotThrowAnyException();
    }

    @Test
    void 예약가능한_시간을_조회한다() {
        Long themeId = 1L;
        String date = "2025-01-01";

        Mockito.when(reservationTimeDao.findAll())
                .thenReturn(List.of(
                        new ReservationTime(1L, LocalTime.of(10, 0))
                ));
        Mockito.when(themeDao.findById(Mockito.any()))
                .thenReturn(
                        Optional.of(new Theme(1L, "인터스텔라", "", ""))
                );
        Mockito.when(reservationDao.findByDateAndThemeId(Mockito.any(), Mockito.any()))
                .thenReturn(List.of());

        assertThat(reservationService.findAvailableReservationTime(themeId, date))
                .isEqualTo(List.of(
                        new AvailableReservationTimeResponse(1L, LocalTime.of(10, 0), false)
                ));
    }
}
