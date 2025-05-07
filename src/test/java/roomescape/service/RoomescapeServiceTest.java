package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.exception.exception.DataNotFoundException;
import roomescape.exception.exception.DeletionNotAllowedException;
import roomescape.exception.exception.DuplicateReservationException;
import roomescape.exception.exception.PastReservationTimeException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomescapeServiceTest {

    @Autowired
    RoomescapeService service;

    @DisplayName("같은 날짜 및 시간 예약이 존재하면 예외를 던진다")
    @Test
    void addReservationWithDuplicatedReservation() {
        //given
        LocalDate date = LocalDate.now().plusDays(1);
        
        ReservationTimeResponse response = service.addReservationTime(
                new ReservationTimeRequest(LocalTime.parse("10:10")));

        service.addReservation(new ReservationRequest("test", date, 1L, response.timeId()));

        //when & then
        ReservationRequest duplicated = new ReservationRequest("test2", date, 1L, response.timeId());
        assertThatThrownBy(() -> service.addReservation(duplicated))
                .isInstanceOf(DuplicateReservationException.class)
                .hasMessage("[ERROR] 이미 존재하는 예약입니다. 다른 시간을 선택해 주세요.");

    }

    @DisplayName("현재 시점 이전의 예약을 생성할 시 예외를 던진다")
    @Test
    void addReservationBeforeCurrentDateTime() {
        // given
        service.addReservationTime(new ReservationTimeRequest(LocalTime.parse("10:10")));
        LocalDate date = LocalDate.now().minusDays(1);
        ReservationRequest request = new ReservationRequest("호떡", date, 1L, 1L);

        // then & when
        assertThatThrownBy(() -> service.addReservation(request))
                .isInstanceOf(PastReservationTimeException.class)
                .hasMessage("[ERROR] 현재 시각 이후로 예약해 주세요.");
    }

    @DisplayName("존재하지 않는 예약을 삭제하려는 경우 예외를 던진다")
    @Test
    void removeReservation() {
        //given
        long notExistId = 999;

        //when & then
        assertThatThrownBy(() -> service.removeReservation(notExistId))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("[ERROR] 예약번호 999번에 해당하는 예약이 없습니다.");
    }

    @DisplayName("존재하지 않는 예약시간을 삭제하려는 경우 예외를 던진다")
    @Test
    void removeReservationTime() {
        //given
        long notExistId = 999;

        //when & then
        assertThatThrownBy(() -> service.removeReservationTime(notExistId))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("[ERROR] 예약 시간 999번에 해당하는 시간이 없습니다.");
    }

    @DisplayName("존재하지 않는 예약테마를 삭제하려는 경우 예외를 던진다")
    @Test
    void removeReservationTheme() {
        //given
        long notExistId = 999;

        //when & then
        assertThatThrownBy(() -> service.removeReservationTheme(notExistId))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("[ERROR] 예약 테마 999번에 해당하는 테마가 없습니다.");
    }

    @DisplayName("예약시간과 연결된 예약이 존재하는 경우 예약시간을 삭제할 수 없다.")
    @Test
    void removeReservationTimeWithExistsReservation() {
        // given
        long existId = 1L;

        // when & then
        assertThatThrownBy(() -> service.removeReservationTime(existId))
                .isInstanceOf(DeletionNotAllowedException.class)
                .hasMessage("[ERROR] 예약이 연결된 시간은 삭제할 수 없습니다. 관련 예약을 먼저 삭제해주세요.");
    }

    @DisplayName("예약테마와 연결된 예약이 존재하는 경우 예약테마를 삭제할 수 없다.")
    @Test
    void removeReservationThemeWithExistsReservation() {
        // given
        long existId = 1L;

        // when & then
        assertThatThrownBy(() -> service.removeReservationTheme(existId))
                .isInstanceOf(DeletionNotAllowedException.class)
                .hasMessage("[ERROR] 예약이 연결된 테마는 삭제할 수 없습니다. 관련 예약을 먼저 삭제해주세요.");
    }
}
