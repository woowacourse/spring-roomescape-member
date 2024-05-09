package roomescape.controller.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.controller.BaseControllerTest;

class AdminControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("어드민이 예약을 추가한다.")
    void addAdminReservation() {
        doReturn(ADMIN_ID).when(jwtTokenProvider).getMemberId(any());


    }
}
