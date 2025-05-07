package roomescape.business.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.business.model.entity.Reservation;
import roomescape.business.model.entity.ReservationTime;
import roomescape.business.model.entity.Theme;
import roomescape.business.model.entity.User;
import roomescape.business.model.repository.ReservationRepository;
import roomescape.business.model.repository.ReservationTimeRepository;
import roomescape.business.model.repository.ThemeRepository;
import roomescape.business.model.repository.UserRepository;
import roomescape.exception.auth.ForbiddenException;
import roomescape.exception.business.AlreadyReservedException;
import roomescape.exception.business.ReservationNotFoundException;
import roomescape.exception.business.ThemeNotFoundException;
import roomescape.exception.business.UserNotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void 사용자_ID로_예약을_추가하고_반환한다() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        String timeId = "time-id";
        String themeId = "theme-id";
        String userId = "user-id";

        User user = User.restore(userId, "USER", "Test User", "test@example.com", "password");
        ReservationTime reservationTime = ReservationTime.restore(timeId, LocalTime.of(10, 0));
        Theme theme = Theme.restore(themeId, "Test Theme", "Description", "thumbnail.jpg");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(reservationTimeRepository.findById(timeId)).thenReturn(Optional.of(reservationTime));
        when(themeRepository.findById(themeId)).thenReturn(Optional.of(theme));
        when(reservationRepository.isDuplicateDateAndTimeAndTheme(eq(date), eq(LocalTime.of(10, 0)), eq(theme)))
                .thenReturn(false);

        // when
        Reservation result = reservationService.addAndGet(date, timeId, themeId, userId);

        // then
        assertThat(result).isNotNull();
        verify(userRepository).findById(userId);
        verify(reservationTimeRepository).findById(timeId);
        verify(themeRepository).findById(themeId);
        verify(reservationRepository).isDuplicateDateAndTimeAndTheme(eq(date), any(LocalTime.class), eq(theme));
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 예약 시 예외가 발생한다")
    void addAndGet_NonExistingReserverId_ThrowsException() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        String timeId = "time-id";
        String themeId = "theme-id";
        String userId = "nonexistent-id";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.addAndGet(date, timeId, themeId, userId))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findById(userId);
        verifyNoInteractions(reservationTimeRepository);
        verifyNoInteractions(themeRepository);
        verifyNoInteractions(reservationRepository);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간 ID로 예약 시 예외가 발생한다")
    void addAndGet_NonExistingTimeId_ThrowsException() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        String timeId = "nonexistent-time-id";
        String themeId = "theme-id";
        String userId = "user-id";

        User user = User.restore(userId, "USER", "Test User", "test@example.com", "password");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(reservationTimeRepository.findById(timeId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.addAndGet(date, timeId, themeId, userId))
                .isInstanceOf(ReservationNotFoundException.class);

        verify(userRepository).findById(userId);
        verify(reservationTimeRepository).findById(timeId);
        verifyNoInteractions(themeRepository);
        verifyNoInteractions(reservationRepository);
    }

    @Test
    @DisplayName("존재하지 않는 테마 ID로 예약 시 예외가 발생한다")
    void addAndGet_NonExistingThemeId_ThrowsException() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        String timeId = "time-id";
        String themeId = "nonexistent-theme-id";
        String userId = "user-id";

        User user = User.restore(userId, "USER", "Test User", "test@example.com", "password");
        ReservationTime reservationTime = ReservationTime.restore(timeId, LocalTime.of(10, 0));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(reservationTimeRepository.findById(timeId)).thenReturn(Optional.of(reservationTime));
        when(themeRepository.findById(themeId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.addAndGet(date, timeId, themeId, userId))
                .isInstanceOf(ThemeNotFoundException.class);

        verify(userRepository).findById(userId);
        verify(reservationTimeRepository).findById(timeId);
        verify(themeRepository).findById(themeId);
        verifyNoInteractions(reservationRepository);
    }

    @Test
    @DisplayName("이미 예약된 날짜, 시간, 테마로 예약 시 예외가 발생한다")
    void addAndGet_AlreadyReserved_ThrowsException() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        String timeId = "time-id";
        String themeId = "theme-id";
        String userId = "user-id";

        User user = User.restore(userId, "USER", "Test User", "test@example.com", "password");
        ReservationTime reservationTime = ReservationTime.restore(timeId, LocalTime.of(10, 0));
        Theme theme = Theme.restore(themeId, "Test Theme", "Description", "thumbnail.jpg");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(reservationTimeRepository.findById(timeId)).thenReturn(Optional.of(reservationTime));
        when(themeRepository.findById(themeId)).thenReturn(Optional.of(theme));
        when(reservationRepository.isDuplicateDateAndTimeAndTheme(eq(date), any(LocalTime.class), eq(theme)))
                .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.addAndGet(date, timeId, themeId, userId))
                .isInstanceOf(AlreadyReservedException.class);

        verify(userRepository).findById(userId);
        verify(reservationTimeRepository).findById(timeId);
        verify(themeRepository).findById(themeId);
        verify(reservationRepository).isDuplicateDateAndTimeAndTheme(eq(date), any(LocalTime.class), eq(theme));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    @DisplayName("필터를 적용하여 모든 예약을 조회할 수 있다")
    void getAll_ReturnsFilteredReservations() {
        // given
        String themeId = "theme-id";
        String userId = "user-id";
        LocalDate dateFrom = LocalDate.now();
        LocalDate dateTo = LocalDate.now().plusDays(7);

        User user1 = User.restore("user-id-1", "USER", "User One", "user1@example.com", "password1");
        User user2 = User.restore("user-id-2", "USER", "User Two", "user2@example.com", "password2");
        ReservationTime time1 = ReservationTime.restore("time-id-1", LocalTime.of(10, 0));
        ReservationTime time2 = ReservationTime.restore("time-id-2", LocalTime.of(14, 0));
        Theme theme1 = Theme.restore("theme-id-1", "Theme One", "Description One", "thumbnail1.jpg");
        Theme theme2 = Theme.restore("theme-id-2", "Theme Two", "Description Two", "thumbnail2.jpg");

        List<Reservation> expectedReservations = Arrays.asList(
                Reservation.restore("reservation-id-1", user1, dateFrom, time1, theme1),
                Reservation.restore("reservation-id-2", user2, dateFrom.plusDays(1), time2, theme2)
        );

        when(reservationRepository.findAllWithFilter(themeId, userId, dateFrom, dateTo))
                .thenReturn(expectedReservations);

        // when
        List<Reservation> result = reservationService.getAll(themeId, userId, dateFrom, dateTo);

        // then
        assertThat(result).isEqualTo(expectedReservations);
        verify(reservationRepository).findAllWithFilter(themeId, userId, dateFrom, dateTo);
    }

    @Test
    @DisplayName("예약을 삭제할 수 있다")
    void delete_ExistingReservation_DeletesReservation() {
        // given
        final User user = User.create("dompoo", "dompoo@email.com", "password");
        final Reservation reservation = Reservation.create(
                user,
                LocalDate.now().plusDays(5),
                ReservationTime.create(LocalTime.of(10, 0)),
                Theme.create("theme", "", "")
        );
        when(reservationRepository.findById(reservation.id())).thenReturn(Optional.of(reservation));

        // when
        reservationService.delete(reservation.id(), user.id());

        // then
        verify(reservationRepository).findById(reservation.id());
        verify(reservationRepository).deleteById(reservation.id());
    }

    @Test
    @DisplayName("존재하지 않는 예약 삭제 시 예외가 발생한다")
    void delete_NonExistingReservation_ThrowsException() {
        // given
        String reservationId = "nonexistent-id";

        final User user = User.create("dompoo", "dompoo@email.com", "password");
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.delete(reservationId, user.id()))
                .isInstanceOf(ReservationNotFoundException.class);

        verify(reservationRepository).findById(reservationId);
        verify(reservationRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("예약자가 아닌 유저가 삭제 시도시 예외가 발생한다")
    void delete_ReservationOfOtherUser_ThrowsException() {
        // given
        final User user1 = User.create("dompoo", "dompoo@email.com", "password");
        final User user2 = User.create("lemon", "lemon@email.com", "password");
        final Reservation reservation = Reservation.create(
                user1,
                LocalDate.now().plusDays(5),
                ReservationTime.create(LocalTime.of(10, 0)),
                Theme.create("theme", "", "")
        );

        when(reservationRepository.findById(reservation.id())).thenReturn(Optional.of(reservation));

        // when & then
        assertThatThrownBy(() -> reservationService.delete(reservation.id(), user2.id()))
                .isInstanceOf(ForbiddenException.class);

        verify(reservationRepository).findById(reservation.id());
        verify(reservationRepository, never()).deleteById(anyString());
    }
}
