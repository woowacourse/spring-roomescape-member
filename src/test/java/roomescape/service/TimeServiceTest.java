package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.dao.TimeDao;
import roomescape.dto.request.TimeRequestDto;
import roomescape.service.fake.FakeTimeDao;

class TimeServiceTest {

    private TimeService timeService;
    private TimeDao timeDao;

    private TimeRequestDto timeRequestDto1;
    private TimeRequestDto timeRequestDto2;

    @BeforeEach
    void setUp() {
        timeDao = new FakeTimeDao();
        timeService = new TimeService(timeDao);

        timeRequestDto1 = new TimeRequestDto(LocalTime.of(10, 0));
        timeRequestDto2 = new TimeRequestDto(LocalTime.of(12, 0));

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

    @Test
    void 이름이_존재하면_예외를_반환한다() {
        timeService.create(timeRequestDto1);

        assertThatThrownBy(() -> timeService.create(timeRequestDto1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
