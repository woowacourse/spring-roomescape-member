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
import roomescape.dto.request.PopularThemeRequestDto;
import roomescape.dto.request.ThemeRequestDto;
import roomescape.dto.response.AvailableTimeResponseDto;

@JdbcTest
@Import({ThemeService.class, ThemeJdbcDao.class, ReservationJdbcDao.class, TimeJdbcDao.class})
@ActiveProfiles("test")
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;
    @Autowired
    private ThemeDao themeDao;
    @Autowired
    private TimeDao timeDao;
    @Autowired
    private ReservationDao reservationDao;

    private ThemeRequestDto requestDto1;
    private ThemeRequestDto requestDto2;

    @BeforeEach
    void setUp() {
        requestDto1 = new ThemeRequestDto("테마1", "http://thumbnail_url", "설명1");
        requestDto2 = new ThemeRequestDto("테마2", "http://thumbnail_url", "설명2");
    }

    @Nested
    class FindAll {

        @Test
        void 테마가_없으면_빈_목록을_반환한다() {
            assertThat(themeService.findAll()).isEmpty();
        }

        @Test
        void 전체_테마_목록을_반환한다() {
            List<Theme> saved = new ArrayList<>();
            saved.add(themeService.create(requestDto1));
            saved.add(themeService.create(requestDto2));

            assertThat(themeService.findAll())
                    .isEqualTo(saved);
        }
    }

    @Nested
    class FindById {

        @Test
        void 존재하는_id로_테마를_조회한다() {
            Theme saved = themeService.create(requestDto1);

            assertThat(themeService.findById(saved.getId())).isEqualTo(saved);
        }

        @Test
        void 존재하지_않는_id를_조회하면_예외를_반환한다() {
            assertThatThrownBy(() -> themeService.findById(-1L))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    class Create {

        @Test
        void 유효한_요청으로_테마를_생성한다() {
            Theme saved = themeService.create(requestDto1);

            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getName().getValue()).isEqualTo(requestDto1.name());
        }

        @Test
        void 중복된_테마_이름으로_생성하면_예외를_반환한다() {
            themeService.create(requestDto1);

            assertThatThrownBy(() -> themeService.create(requestDto1))
                    .isInstanceOf(ConflictException.class);
        }
    }

    @Nested
    class Delete {

        @Test
        void 테마를_삭제한다() {
            Theme saved = themeService.create(requestDto1);
            assertThat(themeDao.existsById(saved.getId())).isTrue();

            themeService.delete(saved.getId());

            assertThat(themeDao.existsById(saved.getId())).isFalse();
        }

        @Test
        void 존재하지_않는_id를_삭제하면_예외를_반환한다() {
            assertThatThrownBy(() -> themeService.delete(-1L))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 예약이_있는_테마는_삭제할_수_없다() {
            Theme savedTheme = themeService.create(requestDto1);
            Time savedTime = timeDao.insert(new Time(LocalTime.of(13, 0)));
            reservationDao.insert(new Reservation("유저", LocalDate.now(), savedTime, savedTheme));

            assertThatThrownBy(() -> themeService.delete(savedTheme.getId()))
                    .isInstanceOf(ConflictException.class);
        }
    }

    @Nested
    class FindAvailableTimesById {

        @Test
        void 테마의_이용_가능한_시간_목록을_반환한다() {
            Theme savedTheme = themeService.create(requestDto1);
            Time bookedTime = timeDao.insert(new Time(LocalTime.of(13, 0)));
            Time availableTime = timeDao.insert(new Time(LocalTime.of(14, 0)));
            LocalDate date = LocalDate.of(2026, 5, 10);
            reservationDao.insert(new Reservation("유저", date, bookedTime, savedTheme));

            List<AvailableTimeResponseDto> result = themeService.findAvailableTimesById(savedTheme.getId(), date);

            assertThat(result).containsExactlyInAnyOrder(
                    new AvailableTimeResponseDto(bookedTime.getId(), bookedTime.getStartAt(), true),
                    new AvailableTimeResponseDto(availableTime.getId(), availableTime.getStartAt(), false)
            );
        }
    }

    @Nested
    class FindPopulars {

        @Test
        void 인기_테마_목록을_limit만큼_반환한다() {
            Theme popularTheme = themeService.create(requestDto1);
            themeService.create(requestDto2);
            Time savedTime = timeDao.insert(new Time(LocalTime.of(13, 0)));
            reservationDao.insert(new Reservation("유저", LocalDate.now(), savedTime, popularTheme));

            List<Theme> result = themeService.findPopulars(new PopularThemeRequestDto(1, 7));

            assertThat(result)
                    .hasSize(1)
                    .containsExactly(popularTheme);
        }

        @Test
        void 예약_수가_많은_테마가_먼저_반환된다() {
            Theme morePopular = themeService.create(requestDto1);
            Theme lessPopular = themeService.create(requestDto2);
            Time savedTime1 = timeDao.insert(new Time(LocalTime.of(13, 0)));
            Time savedTime2 = timeDao.insert(new Time(LocalTime.of(14, 0)));

            reservationDao.insert(new Reservation("유저1", LocalDate.now(), savedTime1, morePopular));
            reservationDao.insert(new Reservation("유저2", LocalDate.now(), savedTime2, morePopular));
            reservationDao.insert(new Reservation("유저3", LocalDate.now(), savedTime1, lessPopular));

            List<Theme> result = themeService.findPopulars(new PopularThemeRequestDto(2, 7));

            assertThat(result).containsExactly(morePopular, lessPopular);
        }

        @Test
        void 기간_밖의_예약은_집계하지_않는다() {
            Theme inPeriodTheme = themeService.create(requestDto1);
            Theme outOfPeriodTheme = themeService.create(requestDto2);
            Time savedTime = timeDao.insert(new Time(LocalTime.of(13, 0)));

            reservationDao.insert(new Reservation("유저1", LocalDate.now(), savedTime, inPeriodTheme));
            reservationDao.insert(new Reservation("유저2", LocalDate.now().minusDays(2), savedTime, outOfPeriodTheme));

            List<Theme> result = themeService.findPopulars(new PopularThemeRequestDto(2, 1));

            assertThat(result).containsExactly(inPeriodTheme, outOfPeriodTheme);
        }

        @Test
        void 예약이_없는_테마는_반환하지_않는다() {
            List<Theme> result = themeService.findPopulars(new PopularThemeRequestDto(10, 7));

            assertThat(result).isEmpty();
        }
    }
}
