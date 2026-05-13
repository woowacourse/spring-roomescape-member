package roomescape.acceptance;

import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import roomescape.acceptance.step.ReservationSteps;
import roomescape.acceptance.step.ReservationTimeSteps;
import roomescape.acceptance.step.ThemeSteps;
import roomescape.support.DatabaseCleanUp;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ReservationAcceptanceTest {

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    private LocalDateTime futureDateTime;

    @BeforeEach
    void beforeEach() {
        futureDateTime = LocalDateTime.now().plusHours(10);
    }

    @Test
    void reservationTimeApiSuccessTest() {
        // 1. 시간 추가
        ReservationTimeSteps.createReservationTime(futureDateTime.toLocalTime().toString());

        // 2. 테마 추가
        ThemeSteps.createTheme("방탈출1", "방탈출1 설명", "theme/url.png");

        // 3. 예약 추가
        ReservationSteps.createReservation("예약자", futureDateTime.toLocalDate().toString(), 1L, 1L);

        // 4. 전체 예약 조회 사이즈로 예약 추가 확인
        ReservationSteps.checkAllReservationSize(1);

        // 5. 예약 삭제
        ReservationSteps.deleteReservation(1L);

        // 6. 전체 예약 조회 사이즈로 예약 삭제 확인
        ReservationSteps.checkAllReservationSize(0);
    }

    @Test
    void reservationTimeApiFailTest() {
        // 1. 시간 추가
        ReservationTimeSteps.createReservationTime("10:00");

        // 2. 테마 추가
        ThemeSteps.createTheme("방탈출1", "방탈출1 설명", "theme/url.png");

        // 3. 예약 추가
        ReservationSteps.createReservation("예약자", futureDateTime.toLocalDate().toString(), 1L, 1L);

        // 4. 날짜, 시간, 테마가 동일한 예약 추가 시 예외 발생
        ReservationSteps.createDuplicatedReservation("예약자", futureDateTime.toLocalDate().toString(), 1L, 1L);
    }

    @AfterEach
    void afterEach() {
        databaseCleanUp.execute();
    }
}
