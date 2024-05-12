package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import roomescape.controller.rest.request.ReservationRequest;
import roomescape.exception.EntityExistsException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_METHOD)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    @DisplayName("중복된 예약을 생성하면 예외가 발생한다.")
    void createByAdminDuplicateException() {
        assertThatThrownBy(
                () -> reservationService.createByAdmin(
                        new ReservationRequest("User1", LocalDate.parse("2024-05-01"), 1L, 1L)))
                .isInstanceOf(EntityExistsException.class);
    }
}
