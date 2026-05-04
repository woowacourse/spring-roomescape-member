package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Test
    @DisplayName("존재하지 않는 시간 아이디는 삭제할 수 없다")
    void 존재하지_않는_예약_삭제_시_에러(){
        Long fakeId = 999L;
        assertThatThrownBy(()-> reservationService.delete(fakeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 예약");
    }
}
