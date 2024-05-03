package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.exception.DuplicatedModelException;
import roomescape.exception.PastReservationException;
import roomescape.service.dto.ReservationAppRequest;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ReservationTimeRepository reservationTimeRepository;
    @Mock
    private ThemeRepository themeRepository;

    @DisplayName("예약을 저장하고, 해당 예약을 id값과 함께 반환한다.")
    @Test
    void save() {
        long reservationId = 1L;
        long timeId = 1L;
        long themeId = 1L;
        ReservationDate reservationDate = new ReservationDate("2050-01-01");
        String name = "브리";
        String time = "10:00";
        ReservationTime reservationTime = new ReservationTime(timeId, time);
        Theme theme = new Theme("방탈출", "방탈출하는 게임",
            "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        Reservation reservation = new Reservation(name, reservationDate, reservationTime,
            theme);

        when(reservationTimeRepository.findById(timeId))
            .thenReturn(Optional.of(reservationTime));

        when(themeRepository.findById(themeId))
            .thenReturn(Optional.of(theme));

        when(reservationRepository.save(any(Reservation.class)))
            .thenReturn(
                new Reservation(reservationId, reservation.getName(), reservation.getReservationDate(), reservationTime,
                    new Theme("방탈출", "방탈출하는 게임",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")));

        ReservationAppRequest request = new ReservationAppRequest(name, reservationDate.getDate().toString(), timeId,
            themeId);

        Reservation actual = reservationService.save(request);
        Reservation expected = new Reservation(1L, reservation.getName(), reservation.getReservationDate(),
            reservation.getReservationTime(),
            new Theme("방탈출", "방탈출하는 게임",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        assertAll(
            () -> assertEquals(expected.getId(), actual.getId()),
            () -> assertEquals(expected.getReservationDate(), actual.getReservationDate()),
            () -> assertEquals(expected.getReservationTime(), actual.getReservationTime()),
            () -> assertEquals(expected.getName(), actual.getName())
        );
    }

    @DisplayName("실패: 빈 이름을 저장하면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void save_IllegalName(String name) {
        when(reservationTimeRepository.findById(1L))
            .thenReturn(Optional.of(new ReservationTime("10:00")));
        when(themeRepository.findById(1L))
            .thenReturn(Optional.of(new Theme("방탈출1", "방탈출1을 한다.", "https://url")));
        assertThatThrownBy(
            () -> reservationService.save(new ReservationAppRequest(name, "2050-01-01", 1L, 1L)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("실패: 존재하지 않는 날짜 입력 시 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"2030-13-01", "2030-12-32"})
    void save_IllegalDate(String rawDate) {
        assertThatThrownBy(() -> reservationService.save(new ReservationAppRequest("brown", rawDate, 1L, 1L)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("실패: 존재하지 않는 시간 ID 입력 시 예외가 발생한다.")
    @Test
    void save_TimeIdDoesntExist() {
        assertThatThrownBy(() -> reservationService.save(new ReservationAppRequest("brown", "2030-12-31", 1L, 1L)))
            .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("실패: 중복 예약을 생성하면 예외가 발생한다.")
    @Test
    void save_Duplication() {
        String rawDate = "2030-12-31";
        long timeId = 1L;
        long themeId = 1L;

        when(themeRepository.findById(themeId))
            .thenReturn(Optional.of(new Theme("방탈출1", "방탈출1을 한다.", "https://url")));
        when(reservationTimeRepository.findById(timeId))
            .thenReturn(Optional.of(new ReservationTime("10:00")));
        when(reservationRepository.isDuplicated(LocalDate.parse(rawDate), timeId, themeId))
            .thenReturn(true);

        assertThatThrownBy(() -> reservationService.save(new ReservationAppRequest("brown", rawDate, timeId, themeId)))
            .isInstanceOf(DuplicatedModelException.class);
    }

    @DisplayName("실패: 어제 날짜에 대한 예약을 생성하면 예외가 발생한다.")
    @Test
    void save_PastDateReservation() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        long timeId = 1L;
        long themeId = 1L;

        ReservationTime reservationTime = new ReservationTime("10:00");
        when(reservationTimeRepository.findById(timeId))
            .thenReturn(Optional.of(reservationTime));
        when(themeRepository.findById(themeId))
            .thenReturn(Optional.of(new Theme("방탈출1", "방탈출1을 한다.", "https://url")));

        assertThatThrownBy(
            () -> reservationService.save(new ReservationAppRequest("brown", yesterday.toString(), timeId, themeId))
        ).isInstanceOf(PastReservationException.class);
    }

    @DisplayName("실패: 같은 날짜에 대한 과거 시간 예약을 생성하면 예외가 발생한다.")
    @Test
    void save_TodayPastTimeReservation() {
        LocalDate today = LocalDate.now();
        String oneMinuteAgo = LocalTime.now().minusMinutes(1).toString();

        long timeId = 1L;
        long themeId = 1L;

        ReservationTime reservationTime = new ReservationTime(oneMinuteAgo);
        Theme theme = new Theme("방탈출1", "방탈출1을 한다.", "https://url");

        when(reservationTimeRepository.findById(timeId))
            .thenReturn(Optional.of(reservationTime));
        when(themeRepository.findById(themeId))
            .thenReturn(Optional.of(theme));

        assertThatThrownBy(
            () -> reservationService.save(new ReservationAppRequest("brown", today.toString(), timeId, themeId))
        ).isInstanceOf(PastReservationException.class);
    }
}
