package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.TimeDao;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.dto.MemberModel;
import roomescape.dto.request.ReservationAdminCreateRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ThemeResponse;
import roomescape.dto.response.TimeResponse;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    private final LocalDate date = LocalDate.of(2023, 8, 5);
    private final ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
    private final Theme theme = new Theme(1L, "테마1", "설명1", "https://image.jpg");
    private final Member member = new Member(1L, "켬미", "aaa@naver.com", Role.MEMBER);
    @Mock
    ReservationDao reservationDao;
    @Mock
    MemberDao memberDao;
    @Mock
    TimeDao timeDao;
    @Mock
    ThemeDao themeDao;

    @DisplayName("예약 정보를 읽을 수 있다.")
    @Test
    void readReservations() {
        ReservationService service = new ReservationService(null, reservationDao, memberDao, timeDao, themeDao);

        List<Reservation> reservations = List.of(new Reservation(1L, date, member, time, theme));
        when(reservationDao.readReservations()).thenReturn(reservations);

        List<ReservationResponse> expected = List.of(new ReservationResponse(
                1L, date, MemberModel.from(member), TimeResponse.from(time), ThemeResponse.from(theme)
        ));
        assertThat(service.readReservations()).isEqualTo(expected);
    }

    @DisplayName("예약 정보를 추가할 수 있다.")
    @Test
    void createReservation() {
        ReservationService service = new ReservationService(
                () -> LocalDateTime.of(2023, 8, 5, 9, 59),
                reservationDao, memberDao, timeDao, themeDao);
        Reservation reservation = new Reservation(1L, date, member, time, theme);

        lenient().when(reservationDao.createReservation(any(Reservation.class)))
                .thenReturn(reservation);
        lenient().when(reservationDao.existsReservationByDateAndTimeIdAndThemeId(any(LocalDate.class), any(Long.class), any(Long.class)))
                .thenReturn(false);
        lenient().when(memberDao.readMemberById(any(Long.class)))
                .thenReturn(Optional.of(member));
        lenient().when(timeDao.readTimeById(any(Long.class)))
                .thenReturn(Optional.of(time));
        lenient().when(themeDao.readThemeById(any(Long.class)))
                .thenReturn(Optional.of(theme));


        ReservationAdminCreateRequest request =
                new ReservationAdminCreateRequest(date, 1L, 1L, 1L);

        assertThatCode(() -> service.createReservation(request))
                .doesNotThrowAnyException();
    }

    @DisplayName("예약 시간 기존 시간보다 이전이면 예외를 던진다.")
    @Test
    void createReservation_whenReservationDateTimeBeforeCurrentTime() {
        ReservationService service = new ReservationService(
                () -> LocalDateTime.of(2023, 8, 5, 10, 1),
                reservationDao, memberDao, timeDao, themeDao);

        Reservation reservation = new Reservation(1L, date, member, time, theme);

        lenient().when(reservationDao.createReservation(any(Reservation.class)))
                .thenReturn(reservation);
        lenient().when(reservationDao.existsReservationByDateAndTimeIdAndThemeId(any(LocalDate.class), any(Long.class), any(Long.class)))
                .thenReturn(false);
        lenient().when(memberDao.readMemberById(any(Long.class)))
                .thenReturn(Optional.of(member));
        lenient().when(timeDao.readTimeById(any(Long.class)))
                .thenReturn(Optional.of(time));
        lenient().when(themeDao.readThemeById(any(Long.class)))
                .thenReturn(Optional.of(theme));


        ReservationAdminCreateRequest request =
                new ReservationAdminCreateRequest(date, 1L, 1L, 1L);

        assertThatThrownBy(() -> service.createReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약은 현재 시간 이후여야 합니다.");
    }

    @DisplayName("이미 존재하는 예약일 경우 예외를 던진다.")
    @Test
    void createReservation_whenAlreadyBookedReservation() {
        ReservationService service = new ReservationService(
                () -> LocalDateTime.of(2023, 8, 5, 9, 59),
                reservationDao, memberDao, timeDao, themeDao);

        lenient().when(reservationDao.existsReservationByDateAndTimeIdAndThemeId(any(LocalDate.class), any(Long.class), any(Long.class)))
                .thenReturn(true);
        lenient().when(memberDao.readMemberById(any(Long.class)))
                .thenReturn(Optional.of(member));
        lenient().when(timeDao.readTimeById(any(Long.class)))
                .thenReturn(Optional.of(time));
        lenient().when(themeDao.readThemeById(any(Long.class)))
                .thenReturn(Optional.of(theme));

        ReservationAdminCreateRequest request =
                new ReservationAdminCreateRequest(date, 1L, 1L, 1L);

        assertThatThrownBy(() -> service.createReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 시간대 해당 테마 예약은 이미 존재합니다.");
    }

    @DisplayName("없는 사용자인 경우 예외를 던진다.")
    @Test
    void createReservation_whenNotExistsMember() {
        ReservationService service = new ReservationService(
                () -> LocalDateTime.of(2023, 8, 5, 9, 59),
                reservationDao, memberDao, timeDao, themeDao);

        lenient().when(reservationDao.existsReservationByDateAndTimeIdAndThemeId(any(LocalDate.class), any(Long.class), any(Long.class)))
                .thenReturn(false);
        lenient().when(memberDao.readMemberById(any(Long.class)))
                .thenReturn(Optional.empty());
        lenient().when(timeDao.readTimeById(any(Long.class)))
                .thenReturn(Optional.of(time));
        lenient().when(themeDao.readThemeById(any(Long.class)))
                .thenReturn(Optional.of(theme));

        ReservationAdminCreateRequest request =
                new ReservationAdminCreateRequest(date, 1L, 1L, 1L);

        assertThatThrownBy(() -> service.createReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 사용자는 존재하지 않습니다.");
    }

    @DisplayName("없는 예약 시간인 경우 예외를 던진다.")
    @Test
    void createReservation_whenNotExistsTime() {
        ReservationService service = new ReservationService(
                () -> LocalDateTime.of(2023, 8, 5, 9, 59),
                reservationDao, memberDao, timeDao, themeDao);

        lenient().when(reservationDao.existsReservationByDateAndTimeIdAndThemeId(any(LocalDate.class), any(Long.class), any(Long.class)))
                .thenReturn(false);
        lenient().when(memberDao.readMemberById(any(Long.class)))
                .thenReturn(Optional.of(member));
        lenient().when(timeDao.readTimeById(any(Long.class)))
                .thenReturn(Optional.empty());
        lenient().when(themeDao.readThemeById(any(Long.class)))
                .thenReturn(Optional.of(theme));

        ReservationAdminCreateRequest request =
                new ReservationAdminCreateRequest(date, 1L, 1L, 1L);

        assertThatThrownBy(() -> service.createReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 예약 시간이 존재하지 않습니다.");
    }

    @DisplayName("없는 테마인 경우 예외를 던진다.")
    @Test
    void createReservation_whenNotExistsTheme() {
        ReservationService service = new ReservationService(
                () -> LocalDateTime.of(2023, 8, 5, 9, 59),
                reservationDao, memberDao, timeDao, themeDao);

        lenient().when(reservationDao.existsReservationByDateAndTimeIdAndThemeId(any(LocalDate.class), any(Long.class), any(Long.class)))
                .thenReturn(false);
        lenient().when(memberDao.readMemberById(any(Long.class)))
                .thenReturn(Optional.of(member));
        lenient().when(timeDao.readTimeById(any(Long.class)))
                .thenReturn(Optional.of(time));
        lenient().when(themeDao.readThemeById(any(Long.class)))
                .thenReturn(Optional.empty());

        ReservationAdminCreateRequest request =
                new ReservationAdminCreateRequest(date, 1L, 1L, 1L);

        assertThatThrownBy(() -> service.createReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테마가 존재하지 않습니다.");
    }

    @DisplayName("예약을 삭제할 수 있다.")
    @Test
    void deleteReservation() {
        ReservationService service = new ReservationService(null, reservationDao, memberDao, timeDao, themeDao);
        assertThatCode(() -> service.deleteReservation(1L))
                .doesNotThrowAnyException();
        ;
    }
}
