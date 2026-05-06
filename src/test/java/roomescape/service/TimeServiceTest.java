package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.dao.TimeDao;
import roomescape.domain.Time;
import roomescape.dto.TimeRequestDto;
import roomescape.service.fake.FakeTimeDao;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeServiceTest {

    private TimeService timeService;
    private TimeDao timeDao;

    @BeforeEach
    void setUp() {
        timeDao = new FakeTimeDao();
        timeService = new TimeService(timeDao);
    }


    @Test
    void 조회하려는_id가_존재하지_않으면_예외_처리한다() {
        Long notExistsTimeId = 1L;
        assertThatThrownBy(() -> timeService.findById(notExistsTimeId))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void 삭제하려는_id가_존재하지_않으면_예외_처리한다() {
        Long notExistsTimeId = 1L;
        assertThatThrownBy(() -> timeService.delete(notExistsTimeId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}