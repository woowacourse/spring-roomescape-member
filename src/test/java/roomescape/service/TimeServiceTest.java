package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.dao.ReservationDao;
import roomescape.dao.TimeDao;
import roomescape.dao.row.ReservationRow;
import roomescape.dao.row.TimeRow;
import roomescape.domain.Time;
import roomescape.dto.request.TimeRequestDto;
import roomescape.service.fake.FakeReservationDao;
import roomescape.service.fake.FakeTimeDao;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeServiceTest {

    private TimeService timeService;
    private TimeDao timeDao;
    private ReservationDao reservationDao;

    private TimeRequestDto timeRequestDto1;
    private TimeRequestDto timeRequestDto2;

    @BeforeEach
    void setUp() {
        timeDao = new FakeTimeDao();
        reservationDao = new FakeReservationDao();
        timeService = new TimeService(timeDao, reservationDao);

        timeRequestDto1 = new TimeRequestDto(LocalTime.of(10, 0));
        timeRequestDto2 = new TimeRequestDto(LocalTime.of(12, 0));

    }


    @Test
    void 조회하려는_id가_존재하지_않으면_예외_처리한다() {
        Long notExistsTimeId = 1L;
        assertThatThrownBy(() -> timeService.findById(notExistsTimeId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 이름이_존재하면_예외를_반환한다() {
        timeService.create(timeRequestDto1);

        assertThatThrownBy(() -> timeService.create(timeRequestDto1))
                .isInstanceOf(ConflictException.class);
    }

    @Nested
    class 삭제할_때 {
        @Test
        void 삭제하려는_id가_존재하지_않으면_예외_처리한다() {
            Long notExistsTimeId = 1L;
            assertThatThrownBy(() -> timeService.delete(notExistsTimeId))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 예약이_존재하면_삭제할_수_없다() {
            Time savedTime = timeService.create(timeRequestDto1);
            reservationDao.create(new ReservationRow("달수", LocalDate.now(), TimeRow.from(savedTime), null));

            assertThatThrownBy(() -> timeService.delete(savedTime.getId()))
                    .isInstanceOf(ConflictException.class);
        }
    }
}
