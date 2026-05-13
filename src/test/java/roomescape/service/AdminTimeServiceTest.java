package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import roomescape.dto.ReservationTimeRequest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminTimeServiceTest {

    @Autowired
    private AdminTimeService adminTimeService;

    @DisplayName("이미 사용중인 시간은 삭제하면 에러를 던진다.")
    @Test
    void 시간_삭제_예외_테스트(){
        long timeId = 1L;
        assertThatThrownBy(() -> adminTimeService.delete(timeId))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("이미 사용중인 시간은 추가하면 에러를 던진다.")
    @Test
    void 시간_추가_예외_테스트(){
        LocalTime startAt = LocalTime.of(10,0,0);
        ReservationTimeRequest request = new ReservationTimeRequest(startAt);
        assertThatThrownBy(() -> adminTimeService.save(request))
                .isInstanceOf(IllegalArgumentException.class);

    }
}