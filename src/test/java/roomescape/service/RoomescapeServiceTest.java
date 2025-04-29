package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomescapeServiceTest {

    @Autowired
    RoomescapeService service;

    @DisplayName("같은 날짜 및 시간 예약이 존재하면 예외를 던진다")
    @Test
    void addReservation() {
        //given
        LocalDate date = LocalDate.of(2025, 4, 16);

        ReservationTimeResponse response = service.addReservationTime(
                new ReservationTimeRequest(LocalTime.parse("10:10")));

        service.addReservation(new ReservationRequest("test", date, response.id()));

        //when & then
        ReservationRequest duplicated = new ReservationRequest("test2", date, response.id());
        Assertions.assertThatThrownBy(() -> service.addReservation(duplicated))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이미 존재하는 예약시간입니다.");
    }

    @DisplayName("존재하지 않는 예약을 삭제하려는 경우 예외를 던진다")
    @Test
    void removeReservation() {
        //given
        long notExistId = 999;

        //when & then
        Assertions.assertThatThrownBy(() -> service.removeReservation(notExistId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 예약번호 999번은 존재하지 않습니다.");
    }

}
