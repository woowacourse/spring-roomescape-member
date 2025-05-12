package roomescape.service.reservation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.reservation.ReservationAvailableTimeResponse;

@SpringBootTest
@Transactional
@Sql({"/fixtures/schema-test.sql", "/fixtures/data-test.sql"})
class UserReservationTimeServiceTest {

    @Autowired
    private UserReservationTimeService service;

    @Test
    @DisplayName("사용자가 날짜와 테마를 선택하면 예약 가능한 시간들을 DTO로 반환한다.")
    void readAvailableReservationTimes() {
        //given
        LocalDate givenDate = LocalDate.now().plusDays(10);
        Long givenTheme = 10L;
        LocalTime givenBooked = LocalTime.of(9, 0);
        LocalTime givenUnBooked = LocalTime.of(11, 0);

        //when
        List<ReservationAvailableTimeResponse> actual = service.readAvailableReservationTimes(givenDate, givenTheme);
        //then
        assertThat(actual.size()).isEqualTo(15);
        ReservationAvailableTimeResponse bookedResponse = actual.stream()
                .filter(current -> current.startAt().equals(givenBooked))
                .findFirst()
                .orElseThrow(() -> new AssertionError("예약된 시간이 결과에 없습니다"));
        assertThat(bookedResponse.isBooked()).isTrue();

        ReservationAvailableTimeResponse availableResponse = actual.stream()
                .filter(current -> current.startAt().equals(givenUnBooked))
                .findFirst()
                .orElseThrow(() -> new AssertionError("가능한 시간이 결과에 없습니다"));
        assertThat(availableResponse.isBooked()).isFalse();
    }
}
