package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.dto.app.ReservationAppRequest;
import roomescape.exception.reservation.DuplicatedReservationException;
import roomescape.exception.reservation.PastReservationException;
import roomescape.exception.reservation.ReservationTimeNotFoundException;

@SpringBootTest
@AutoConfigureTestDatabase
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @MockBean
    private ReservationRepository reservationRepository;
    @MockBean
    private ReservationTimeRepository reservationTimeRepository;
    @MockBean
    private ThemeRepository themeRepository;

    @DisplayName("예약을 저장하고, 해당 예약을 id값과 함께 반환한다.")
    @Test
    void save() {
        long reservationId = 1L;
        long timeId = 1L;
        long themeId = 1L;
        LocalDate date = LocalDate.parse("2050-01-01");
        String name = "브리";
        LocalTime time = LocalTime.MAX;
        ReservationTime reservationTime = new ReservationTime(timeId, time);
        Reservation reservation = new Reservation(name, date, reservationTime,
            new Theme("방탈출", "방탈출하는 게임", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        when(reservationTimeRepository.findById(timeId))
            .thenReturn(reservationTime);

        when(reservationRepository.save(any(Reservation.class)))
            .thenReturn(
                new Reservation(reservationId, reservation.getName(), reservation.getDate(), reservationTime,
                    new Theme("방탈출", "방탈출하는 게임",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")));

        ReservationAppRequest request = new ReservationAppRequest(name, date.toString(), timeId, themeId);

        Reservation actual = reservationService.save(request);
        Reservation expected = new Reservation(1L, reservation.getName(), reservation.getDate(), reservation.getTime(),
            new Theme("방탈출", "방탈출하는 게임", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        assertAll(
            () -> assertEquals(expected.getId(), actual.getId()),
            () -> assertEquals(expected.getDate(), actual.getDate()),
            () -> assertEquals(expected.getTime(), actual.getTime()),
            () -> assertEquals(expected.getName(), actual.getName())
        );
    }

    @DisplayName("실패: 빈 이름을 저장하면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void save_IllegalName(String name) {
        when(reservationTimeRepository.findById(1L))
            .thenReturn(new ReservationTime(LocalTime.parse("10:00")));
        when(themeRepository.findById(1L))
            .thenReturn(new Theme("방탈출1", "방탈출1을 한다.", "https://url"));
        assertThatThrownBy(
            () -> reservationService.save(new ReservationAppRequest(name, "2050-01-01", 1L, 1L)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("실패: 존재하지 않는 날짜 입력 시 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"2030-13-01", "2030-12-32"})
    void save_IllegalDate(String rawDate) {
        when(reservationTimeRepository.findById(1L))
            .thenReturn(new ReservationTime(LocalTime.parse("10:00")));
        when(themeRepository.findById(1L))
            .thenReturn(new Theme("방탈출1", "방탈출1을 한다.", "https://url"));
        assertThatThrownBy(() -> reservationService.save(new ReservationAppRequest("brown", rawDate, 1L, 1L)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("실패: 존재하지 않는 시간 ID 입력 시 예외가 발생한다.")
    @Test
    void save_TimeIdDoesntExist() {
        when(reservationTimeRepository.findById(1L))
            .thenThrow(EmptyResultDataAccessException.class);
        when(themeRepository.findById(1L))
            .thenReturn(new Theme("방탈출1", "방탈출1을 한다.", "https://url"));
        assertThatThrownBy(() -> reservationService.save(new ReservationAppRequest("brown", "2030-12-31", 1L, 1L)))
            .isInstanceOf(ReservationTimeNotFoundException.class);
    }

    @DisplayName("실패: 중복 예약을 생성하면 예외가 발생한다.")
    @Test
    void save_Duplication() {
        String rawDate = "2030-12-31";
        long timeId = 1L;
        long themeId = 1L;

        when(reservationRepository.countDuplication(LocalDate.parse(rawDate), timeId, themeId))
            .thenReturn(1L);
        when(themeRepository.findById(themeId))
            .thenReturn(new Theme("방탈출1", "방탈출1을 한다.", "https://url"));

        assertThatThrownBy(() -> reservationService.save(new ReservationAppRequest("brown", rawDate, timeId, themeId)))
            .isInstanceOf(DuplicatedReservationException.class);
    }

    @DisplayName("실패: 어제 날짜에 대한 예약을 생성하면 예외가 발생한다.")
    @Test
    void save_PastDateReservation() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        long timeId = 1L;
        long themeId = 1L;

        ReservationTime reservationTime = new ReservationTime(LocalTime.parse("10:00"));
        when(reservationTimeRepository.findById(timeId))
            .thenReturn(reservationTime);
        when(themeRepository.findById(themeId))
            .thenReturn(new Theme("방탈출1", "방탈출1을 한다.", "https://url"));

        assertThatThrownBy(
            () -> reservationService.save(new ReservationAppRequest("brown", yesterday.toString(), timeId, themeId))
        ).isInstanceOf(PastReservationException.class);
    }

    @DisplayName("실패: 같은 날짜에 대한 과거 시간 예약을 생성하면 예외가 발생한다.")
    @Test
    void save_TodayPastTimeReservation() {
        LocalDate today = LocalDate.now();
        LocalTime oneMinuteAgo = LocalTime.now().minusMinutes(1);

        long timeId = 1L;
        long themeId = 1L;

        ReservationTime reservationTime = new ReservationTime(oneMinuteAgo);
        Theme theme = new Theme("방탈출1", "방탈출1을 한다.", "https://url");

        when(reservationTimeRepository.findById(timeId))
            .thenReturn(reservationTime);
        when(themeRepository.findById(themeId))
            .thenReturn(theme);

        assertThatThrownBy(
            () -> reservationService.save(new ReservationAppRequest("brown", today.toString(), timeId, themeId))
        ).isInstanceOf(PastReservationException.class);
    }
}
