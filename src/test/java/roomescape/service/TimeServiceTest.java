package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.TimeDao;
import roomescape.dao.jdbc.ReservationJdbcDao;
import roomescape.dao.jdbc.ThemeJdbcDao;
import roomescape.dao.jdbc.TimeJdbcDao;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.domain.vo.Name;
import roomescape.dto.request.TimeRequestDto;

@JdbcTest
@Import({TimeService.class, TimeJdbcDao.class, ReservationJdbcDao.class, ThemeJdbcDao.class})
@ActiveProfiles("test")
class TimeServiceTest {

    @Autowired
    private TimeService timeService;
    @Autowired
    private TimeDao timeDao;
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ThemeDao themeDao;

    private TimeRequestDto timeRequestDto1;
    private TimeRequestDto timeRequestDto2;

    @BeforeEach
    void setUp() {
        timeRequestDto1 = new TimeRequestDto(LocalTime.of(10, 0));
        timeRequestDto2 = new TimeRequestDto(LocalTime.of(12, 0));
    }

    @Nested
    class FindAll {

        @Test
        void 시간이_없으면_빈_목록을_반환한다() {
            assertThat(timeService.findAll()).isEmpty();
        }

        @Test
        void 전체_시간_목록을_반환한다() {
            List<Time> saved = new ArrayList<>();
            saved.add(timeService.create(timeRequestDto1));
            saved.add(timeService.create(timeRequestDto2));

            assertThat(timeService.findAll())
                    .isEqualTo(saved);
        }
    }

    @Nested
    class FindById {

        @Test
        void 존재하는_id로_시간을_조회한다() {
            Time saved = timeService.create(timeRequestDto1);

            assertThat(timeService.findById(saved.getId())).isEqualTo(saved);
        }

        @Test
        void 존재하지_않는_id를_조회하면_예외를_반환한다() {
            assertThatThrownBy(() -> timeService.findById(-1L))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    class Create {

        @Test
        void 유효한_요청으로_시간을_생성한다() {
            Time saved = timeService.create(timeRequestDto1);

            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getStartAt()).isEqualTo(timeRequestDto1.startAt());
        }

        @Test
        void 중복된_시간을_생성하면_예외를_반환한다() {
            timeService.create(timeRequestDto1);

            assertThatThrownBy(() -> timeService.create(timeRequestDto1))
                    .isInstanceOf(ConflictException.class);
        }
    }

    @Nested
    class Delete {

        @Test
        void 시간을_삭제한다() {
            Time saved = timeService.create(timeRequestDto1);
            timeService.delete(saved.getId());

            assertThat(timeDao.existsById(saved.getId())).isFalse();
        }

        @Test
        void 존재하지_않는_id를_삭제하면_예외를_반환한다() {
            assertThatThrownBy(() -> timeService.delete(-1L))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 예약이_있는_시간은_삭제할_수_없다() {
            Time savedTime = timeService.create(timeRequestDto1);
            Theme savedTheme = themeDao.insert(new Theme(new Name("테마"), "http://thumbnail_url", "설명"));
            reservationDao.insert(new Reservation("유저", LocalDate.now(), savedTime, savedTheme));
            
            Long id = savedTime.getId();
            assertThatThrownBy(() -> timeService.delete(id))
                    .isInstanceOf(ConflictException.class);
        }
    }
}
