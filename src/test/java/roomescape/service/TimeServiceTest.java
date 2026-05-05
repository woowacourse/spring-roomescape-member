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

    private void saveTimeHandler(Time... times) {
        for (Time time : times) {
            timeDao.insert(time);
        }
    }

    @BeforeEach
    void setUp() {
        timeDao = new FakeTimeDao();
        timeService = new TimeService(timeDao);
    }

    @Test
    void 시간을_생성하면_id가_부여된_시간이_생성된다() {
        Time time = timeService.create(new TimeRequestDto(LocalTime.of(13, 30)));

        assertThat(time).isNotNull();
        assertThat(time.getId()).isEqualTo(1L);
    }

    @Test
    void 지정한_id를_가진_시간을_조회할_수_있다() {
        Time time1 = new Time(LocalTime.of(13, 0));
        Time time2 = new Time(LocalTime.of(14, 0));

        saveTimeHandler(time1, time2);
        Time timeById = timeService.findById(2L);

        assertThat(timeById.getStartAt()).isEqualTo(time2.getStartAt());
    }

    @Test
    void 조회하려는_id가_존재하지_않으면_예외_처리한다() {
        Long id = 1L;
        assertThatThrownBy(() -> {
            Time byId = timeService.findById(id);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_시간을_조회할_수_있다() {
        Time time1 = new Time(LocalTime.of(13, 0));
        Time time2 = new Time(LocalTime.of(14, 0));

        saveTimeHandler(time1, time2);

        List<Time> times = timeService.findAll();

        assertThat(times.size()).isEqualTo(2);
    }

    @Test
    void 삭제하려는_id가_존재하지_않으면_예외_처리한다() {
        Long id = 1L;
        assertThatThrownBy(() -> {
            timeService.delete(1L);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 지정한_id를_가진_시간을_삭제할_수_있다() {
        Time time = new Time(LocalTime.of(13, 0));
        saveTimeHandler(time);

        timeService.delete(1L);

        List<Time> times = timeService.findAll();
        assertThat(times.size()).isEqualTo(0);
    }
}