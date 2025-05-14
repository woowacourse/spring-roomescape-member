package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationThemeRequest;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.SignUpRequest;
import roomescape.exception.exception.DataNotFoundException;
import roomescape.exception.exception.DeletionNotAllowedException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class ReservationTimeServiceTest {

    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationThemeService themeService;
    @Autowired
    ReservationTimeService timeService;
    @Autowired
    MemberService memberService;

    @DisplayName("예약시간과 연결된 예약이 존재하는 경우 예약시간을 삭제할 수 없다.")
    @Test
    void removeReservationTimeWithExistsReservation() {
        // given
        memberService.signUpMember(new SignUpRequest("제프리", "jeffrey@gmail.com", "1234!@#$"));
        themeService.addReservationTheme(new ReservationThemeRequest("테마1", "설명1", "썸네일1"));
        timeService.addReservationTime(new ReservationTimeRequest(LocalTime.parse("10:40")));

        ReservationRequest request = new ReservationRequest(LocalDate.parse("2025-08-03"), 1L, 1L);
        reservationService.addReservation(request, 1L);
        long existId = 1L;

        // when & then
        assertThatThrownBy(() -> timeService.removeReservationTime(existId)).isInstanceOf(DeletionNotAllowedException.class)
                .hasMessage("[ERROR] 예약이 연결된 시간은 삭제할 수 없습니다. 관련 예약을 먼저 삭제해주세요.");
    }

    @DisplayName("존재하지 않는 예약시간을 삭제하려는 경우 예외를 던진다")
    @Test
    void removeReservationTime() {
        //given
        long notExistId = 999;

        //when & then
        assertThatThrownBy(() -> timeService.removeReservationTime(notExistId)).isInstanceOf(DataNotFoundException.class)
                .hasMessage("[ERROR] 예약 시간 999번에 해당하는 시간이 없습니다.");
    }

}
