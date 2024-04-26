package roomescape.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

        ReservationAppRequest request = new ReservationAppRequest(timeId, date, name);

        Reservation actual = reservationService.save(request);
        Reservation expected = new Reservation(1L, reservation.getName(), reservation.getDate(), reservation.getTime());

        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getDate(), actual.getDate()),
                () -> assertEquals(expected.getTime(), actual.getTime()),
                () -> assertEquals(expected.getName(), actual.getName())
        );
    }
}
