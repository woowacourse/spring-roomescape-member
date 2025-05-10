package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.controller.user.ReservationController;

public class ReservationControllerTest {

    private final ReservationController reservationController = new ReservationController(null);

    @Test
    @DisplayName("컨트롤러는 DB 접근 책임을 가지지 않는다.")
    void seperateWithDatabaseAccess() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }
}
