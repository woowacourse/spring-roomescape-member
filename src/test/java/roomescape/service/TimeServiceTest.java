package roomescape.service;

import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.TimeRequest;

@SpringBootTest
@Transactional
public class TimeServiceTest {

    @Autowired
    private TimeService timeService;

    @Test
    public void 존재하지_않는_시간를_삭제하는경우_예외가_발생한다() {
        // then
        Assertions.assertThatThrownBy(() -> timeService.removeById(-1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 존재하지_않는_시간에_대한_추가는_정상동작된다() {
        // given
        TimeRequest timeRequest = new TimeRequest(LocalTime.of(8, 0));

        // when
        Assertions.assertThatCode(() -> timeService.register(timeRequest))
                .doesNotThrowAnyException();
    }

    @Test
    public void 이미_있는_시간에_대한_추가하는_예외가_발생한다() {
        // given
        TimeRequest timeRequest = new TimeRequest(LocalTime.of(10, 0));

        // when
        Assertions.assertThatThrownBy(() -> timeService.register(timeRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 예약이_존재하는_시간에_대한_삭제는_예외가_발생한다() {
        // when
        Assertions.assertThatThrownBy(() -> timeService.removeById(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
