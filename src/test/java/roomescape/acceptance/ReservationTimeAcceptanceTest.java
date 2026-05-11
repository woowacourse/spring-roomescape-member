package roomescape.acceptance;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import roomescape.acceptance.step.ReservationSteps;
import roomescape.acceptance.step.ReservationTimeSteps;
import roomescape.acceptance.step.ThemeSteps;
import roomescape.support.DatabaseCleanUp;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ReservationTimeAcceptanceTest {

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Test
    void reservationTimeApiSuccessTest() {
        // 1. 시간 추가
        ReservationTimeSteps.createReservationTime("10:00");

        // 2. 전체 시간 조회 사이즈로 시간 추가 확인
        ReservationTimeSteps.checkAllReservationTimeSize(1);

        // 3. 테마 추가
        ThemeSteps.createTheme("방탈출1", "방탈출1 설명", "theme/url.png");

        // 4. 특정 날짜, 테마의 예약 가능한 시간 조회
        ReservationTimeSteps.checkAvailableReservation("2026-05-01", 1L, true);

        // 5. 예약 추가
        ReservationSteps.createReservation("예약자", "2026-05-01", 1L, 1L);

        // 6. 예약 추가 후 해당 날짜, 테마의 시간 예약 가능 상태 변경 조회
        ReservationTimeSteps.checkAvailableReservation("2026-05-01", 1L, false);

        // 7. 예약 삭제
        ReservationSteps.deleteReservation(1L);

        // 8. 시간 삭제
        ReservationTimeSteps.deleteReservationTime(1L);

        // 9. 전체 시간 조회 사이즈로 시간 삭제 확인
        ReservationTimeSteps.checkAllReservationTimeSize(0);
    }

    @AfterEach
    void afterEach() {
        databaseCleanUp.execute();
    }
}
