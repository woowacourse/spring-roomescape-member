package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import roomescape.service.dto.ServiceReservationTimeAvailabilityResponse;
import roomescape.service.dto.ServiceReservationTimeRequest;
import roomescape.service.dto.ServiceReservationTimeResponse;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Test
    void createTest() {
        ServiceReservationTimeResponse responseDto = reservationTimeService.create(
                new ServiceReservationTimeRequest(LocalTime.of(10, 0)));

        assertThat(responseDto).isEqualTo(new ServiceReservationTimeResponse(1L, LocalTime.of(10, 0)));
    }

    @Test
    void readAllTest() {
        reservationTimeService.create(new ServiceReservationTimeRequest(LocalTime.of(10, 0)));
        reservationTimeService.create(new ServiceReservationTimeRequest(LocalTime.of(11, 0)));

        List<ServiceReservationTimeResponse> responseDtos = reservationTimeService.readAll();

        assertThat(responseDtos.getFirst()).isEqualTo(new ServiceReservationTimeResponse(1L, LocalTime.of(10, 0)));
        assertThat(responseDtos.get(1)).isEqualTo(new ServiceReservationTimeResponse(2L, LocalTime.of(11, 0)));
    }

    @Test
    @Sql(scripts = "/available-time-test-data.sql")
    void readAvailabilityByDateAndThemeTest() {
        List<ServiceReservationTimeAvailabilityResponse> responseDtos = reservationTimeService.readAvailabilityByDateAndTheme(
                LocalDate.of(2026, 5, 1), 1L);

        assertThat(responseDtos.getFirst().available()).isFalse();
        assertThat(responseDtos.get(1).available()).isTrue();
    }

    @Test
    void deleteTest() {
        reservationTimeService.create(new ServiceReservationTimeRequest(LocalTime.of(10, 0)));
        reservationTimeService.delete(1L);

        List<ServiceReservationTimeResponse> responseDtos = reservationTimeService.readAll();

        assertThat(responseDtos.size()).isEqualTo(0);
    }
}
