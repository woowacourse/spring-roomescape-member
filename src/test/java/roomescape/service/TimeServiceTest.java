package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.RestApiException;
import roomescape.dao.ReservationDao;
import roomescape.dao.TimeDao;
import roomescape.dao.row.ReservationRow;
import roomescape.dao.row.ThemeRow;
import roomescape.dao.row.TimeRow;
import roomescape.dto.request.TimeRequestDto;
import roomescape.dto.response.TimeResponseDto;
import roomescape.fixture.FakeDatabase;
import roomescape.fixture.FakeReservationDao;
import roomescape.fixture.FakeTimeDao;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeServiceTest {

    private static final LocalDate FIXED_DATE = LocalDate.of(2026, 5, 10);
    private static final Long NOT_EXISTS_ID = Long.MAX_VALUE;

    private TimeService timeService;
    private TimeDao timeDao;
    private ReservationDao reservationDao;

    @BeforeEach
    void setUp() {
        FakeDatabase.clearAll();
        timeDao = new FakeTimeDao();
        reservationDao = new FakeReservationDao();
        timeService = new TimeService(timeDao, reservationDao);
    }

    private TimeRequestDto requestOf(int hour) {
        return new TimeRequestDto(LocalTime.of(hour, 0));
    }

    private TimeResponseDto givenTime(int hour) {
        return timeService.create(requestOf(hour));
    }

    private void givenReservationAt(TimeResponseDto time) {
        reservationDao.create(new ReservationRow(
                "달수",
                FIXED_DATE,
                new TimeRow(time.id(), time.startAt()),
                new ThemeRow("테마1", "https://test.com", "테스트용 설명입니다")));
    }


    @Test
    void 조회하려는_id가_존재하지_않으면_예외_처리한다() {
        assertThatThrownBy(() -> timeService.findById(NOT_EXISTS_ID))
                .isInstanceOf(RestApiException.class);
    }

    @Nested
    @DisplayName("시간을 생성할 때: ")
    class Create {

        @Test
        void 정상_요청이면_시간이_생성된다() {
            TimeRequestDto request = requestOf(10);
            TimeResponseDto response = timeService.create(request);

            assertThat(response.id()).isNotNull();
            assertThat(response.startAt()).isEqualTo(LocalTime.of(10, 0));
        }

        @Test
        void 동일한_시간이_이미_있으면_예외_처리한다() {
            givenTime(10);

            assertThatThrownBy(() -> timeService.create(requestOf(10)))
                    .isInstanceOf(RestApiException.class);
        }
    }

    @Nested
    @DisplayName("시간을 삭제할 때: ")
    class Delete {

        @Test
        void 정상_요청이면_삭제된다() {
            TimeResponseDto time = givenTime(10);

            timeService.delete(time.id());

            assertThatThrownBy(() -> timeService.findById(time.id()))
                    .isInstanceOf(RestApiException.class);
        }

        @Test
        void 삭제하려는_id가_존재하지_않으면_예외_처리한다() {
            assertThatThrownBy(() -> timeService.delete(NOT_EXISTS_ID))
                    .isInstanceOf(RestApiException.class);
        }

        @Test
        void 예약이_존재하면_삭제할_수_없다() {
            TimeResponseDto time = givenTime(10);
            givenReservationAt(time);

            assertThatThrownBy(() -> timeService.delete(time.id()))
                    .isInstanceOf(RestApiException.class);
        }
    }
}
