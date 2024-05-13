package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import roomescape.domain.ReservationTime;
import roomescape.exception.EntityExistsException;
import roomescape.exception.ForeignKeyViolationException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("이미 존재하는 예약 시간을 생성할 경우, 예외를 발생한다.")
    void createException() {
        assertThatThrownBy(() -> reservationTimeService.create(new ReservationTime(0, LocalTime.of(11, 0))))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("Time 11:00 already exists.");
    }

    @Test
    @DisplayName("예약이 있는 시간을 삭제하려 할 경우 예외를 발생한다.")
    void deleteException() {
        long id = 1L;
        assertThatThrownBy(() -> reservationTimeService.delete(id))
                .isInstanceOf(ForeignKeyViolationException.class)
                .hasMessage("Cannot delete a time with id " + id + " as being referred by reservation.");
    }
}
