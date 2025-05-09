package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roomescape.dto.request.LoginCheckRequest;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.*;
import roomescape.entity.*;
import roomescape.repository.MemberDao;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.repository.ThemeDao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class ReservationServiceTest {

    private ReservationDao reservationDao;
    private ReservationTimeDao reservationTimeDao;
    private ThemeDao themeDao;
    private ReservationService reservationService;
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        this.reservationDao = Mockito.mock(ReservationDao.class);
        this.reservationTimeDao = Mockito.mock(ReservationTimeDao.class);
        this.themeDao = Mockito.mock(ThemeDao.class);
        this.memberDao = Mockito.mock(MemberDao.class);
        this.reservationService = new ReservationService(reservationDao, reservationTimeDao, themeDao, memberDao);
    }

    @Test
    void 예약기록을_조회한다() {
        Mockito.when(reservationDao.findAll())
            .thenReturn(List.of(
                new Reservation(1L, new LoginMember(1L, "어드민", "admin@email.com", Role.ADMIN), LocalDate.of(2025, 1, 1), new ReservationTime(1L, LocalTime.of(10, 0)), new Theme(1L, "테마1", "", "")),
                new Reservation(2L, new LoginMember(2L, "브라운", "brown@email.com", Role.USER), LocalDate.of(2025, 1, 2), new ReservationTime(1L, LocalTime.of(10, 0)), new Theme(1L, "테마1", "", ""))
            ));

        assertThat(reservationService.findAll()).isEqualTo(
            List.of(
                new ReservationResponse(1L, LoginCheckResponse.from(1L, "어드민"), LocalDate.of(2025, 1, 1), new ReservationTimeResponse(1L, LocalTime.of(10, 0)), new ThemeResponse(1L, "테마1", "", "")),
                new ReservationResponse(2L, LoginCheckResponse.from(2L, "브라운"), LocalDate.of(2025, 1, 2), new ReservationTimeResponse(1L, LocalTime.of(10, 0)), new ThemeResponse(1L, "테마1", "", ""))
            )
        );
    }

    @Test
    void 예약을_추가한다() {
        ReservationRequest request = new ReservationRequest(LocalDate.now().plusDays(1), 1L, 1L);
        Mockito.when(reservationTimeDao.findById(Mockito.any()))
            .thenReturn(Optional.of(new ReservationTime(1L, LocalTime.of(10, 0))));
        Mockito.when(themeDao.findById(Mockito.any()))
            .thenReturn(Optional.of(new Theme(1L, "인터스텔라", "", "")));
        Mockito.when(reservationDao.findByDateAndThemeId(Mockito.any(), Mockito.any()))
            .thenReturn(List.of());
        Mockito.when(reservationDao.save(Mockito.any()))
            .thenReturn(
                new Reservation(1L, new LoginMember(1L, "어드민", "email@email.com", Role.ADMIN),
                    LocalDate.now().plusDays(1),
                    new ReservationTime(1L, LocalTime.of(10, 0)),
                    new Theme(1L, "인터스텔라", "", "")
                )
            );

        assertThat(reservationService.add(request, LoginCheckRequest.of(1L, "어드민", "email@email.com", Role.ADMIN)))
            .isEqualTo(new ReservationResponse(
                1L,
                LoginCheckResponse.from(1L, "어드민"),
                LocalDate.now().plusDays(1),
                new ReservationTimeResponse(1L, LocalTime.of(10, 0)),
                new ThemeResponse(1L, "인터스텔라", "", "")
            ));
    }

    @Test
    void 예약을_삭제한다() {
        Long id = 1L;
        Mockito.when(reservationDao.findById(Mockito.any()))
            .thenReturn(Optional.of(new Reservation(1L, new LoginMember(1L, "어드민", "email@email.com", Role.ADMIN)
                , null, null, null)));


        assertThatCode(() -> reservationService.deleteById(id))
            .doesNotThrowAnyException();
    }

    @Test
    void 예약가능한_시간을_조회한다() {
        Long themeId = 1L;
        LocalDate date = LocalDate.of(2025, 1, 1);

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

    @Test
    void 검색조건으로_예약을_조회한다() {
        Long memberId = 1L;
        Long themeId = 1L;
        LocalDate from = LocalDate.of(2025, 1, 1);
        LocalDate to = LocalDate.of(2025, 1, 3);

        List<Reservation> reservations = List.of(
            new Reservation(1L, new LoginMember(memberId, "어드민", "admin@email.com", Role.ADMIN),
                LocalDate.of(2025, 1, 2),
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new Theme(themeId, "테마1", "", ""))
        );

        Mockito.when(reservationDao.findByMemberIdAndThemeIdAndDateBetween(memberId, themeId, from, to))
            .thenReturn(reservations);

        List<ReservationResponse> result = reservationService.search(memberId, themeId, from, to);

        assertThat(result).isEqualTo(List.of(
            new ReservationResponse(1L,
                LoginCheckResponse.from(memberId, "어드민"),
                LocalDate.of(2025, 1, 2),
                new ReservationTimeResponse(1L, LocalTime.of(10, 0)),
                new ThemeResponse(themeId, "테마1", "", ""))
        ));
    }
}