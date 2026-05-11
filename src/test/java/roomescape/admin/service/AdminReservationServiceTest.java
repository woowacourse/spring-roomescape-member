package roomescape.admin.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminReservationServiceTest {

    @Autowired
    private AdminReservationService adminReservationService;

    @DisplayName("관리자는 전체 예약 내역을 조회할 수 있다.")
    @Test
    void getAllReservations() {
        List<Reservation> result =
                adminReservationService.getAllReservations();

        // test-data.sql 기준:
        // 과거 인기 집계용 14건
        // 미래 예약 5건
        // 총 19건
        assertThat(result).hasSize(19);

        assertThat(result)
                .extracting(Reservation::getName)
                .contains(
                        "김철수",
                        "이영희",
                        "박민준",
                        "최수진"
                );
    }
}