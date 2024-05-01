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
import roomescape.dto.app.ReservationAppRequest;

@SpringBootTest
@AutoConfigureTestDatabase
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @MockBean
    private ReservationRepository reservationRepository;
    @MockBean
    private ReservationTimeRepository reservationTimeRepository;

    @DisplayName("예약을 저장하고, 해당 예약을 id값과 함께 반환한다.")
    @Test
    void save() {
        long reservationId = 1L;
        long timeId = 1L;
        LocalDate date = LocalDate.now();
        String name = "브리";
        LocalTime time = LocalTime.MIN;
        ReservationTime reservationTime = new ReservationTime(timeId, time);
        Reservation reservation = new Reservation(name, date, reservationTime);

        when(reservationTimeRepository.findById(timeId))
            .thenReturn(reservationTime);

        when(reservationRepository.save(any(Reservation.class)))
            .thenReturn(
                new Reservation(reservationId, reservation.getName(), reservation.getDate(), reservationTime));

        ReservationAppRequest request = new ReservationAppRequest(timeId, date.toString(), name);

        Reservation actual = reservationService.save(request);
        Reservation expected = new Reservation(1L, reservation.getName(), reservation.getDate(), reservation.getTime());

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
        assertThatThrownBy(
            () -> reservationService.save(new ReservationAppRequest(1L, LocalDate.MAX.toString(), name)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("실패: 존재하지 않는 날짜 입력 시 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"2030-13-01", "2030-12-32"})
    @NullAndEmptySource
    void save_IllegalDate(String rawDate) {
        assertThatThrownBy(() -> reservationService.save(new ReservationAppRequest(1L, rawDate, "brown")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("실패: 존재하지 않는 시간 ID 입력 시 예외가 발생한다.")
    @Test
    void save_TimeIdDoesntExist() {
        when(reservationTimeRepository.findById(1L))
            .thenThrow(EmptyResultDataAccessException.class);

        assertThatThrownBy(() -> reservationService.save(new ReservationAppRequest(1L, "2030-12-31", "brown")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("실패: 중복 예약을 생성하면 예외가 발생한다.")
    @Test
    void save_Duplication() {
        String rawDate = "2030-12-31";
        long timeId = 1L;
        when(reservationRepository.countByDateAndTimeId(LocalDate.parse(rawDate), timeId))
            .thenReturn(1L);

        assertThatThrownBy(() -> reservationService.save(new ReservationAppRequest(timeId, rawDate, "brown")))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
