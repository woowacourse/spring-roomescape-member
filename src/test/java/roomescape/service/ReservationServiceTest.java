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
import roomescape.dto.request.ReservationRequestDto;

@JdbcTest
@Import({ReservationService.class, ReservationJdbcDao.class, TimeJdbcDao.class, ThemeJdbcDao.class})
@ActiveProfiles("test")
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private TimeDao timeDao;
    @Autowired
    private ThemeDao themeDao;

    private Time savedTime1;
    private Time savedTime2;
    private Theme savedTheme1;
    private Theme savedTheme2;
    private ReservationRequestDto requestDto1;
    private ReservationRequestDto requestDto2;

    @BeforeEach
    void setUp() {
        savedTime1 = timeDao.insert(new Time(LocalTime.of(13, 0)));
        savedTime2 = timeDao.insert(new Time(LocalTime.of(14, 0)));
        savedTheme1 = themeDao.insert(new Theme(new Name("방탈출 이름1"), "http://thumbnail_url", "방탈출을 할 수 있다."));
        savedTheme2 = themeDao.insert(new Theme(new Name("방탈출 이름2"), "http://thumbnail_url", "방탈출을 할 수 있다."));
        requestDto1 = new ReservationRequestDto("유저1", LocalDate.of(2026, 5, 3), savedTime1.getId(),
                savedTheme1.getId());
        requestDto2 = new ReservationRequestDto("유저2", LocalDate.of(2026, 5, 3), savedTime2.getId(),
                savedTheme2.getId());
    }

    @Nested
    class FindAll {

        @Test
        void 예약이_없으면_빈_목록을_반환한다() {
            assertThat(reservationService.findAll()).isEmpty();
        }

        @Test
        void 전체_예약_목록을_반환한다() {
            List<Reservation> saved = new ArrayList<>();
            saved.add(reservationService.create(requestDto1));
            saved.add(reservationService.create(requestDto2));

            assertThat(reservationService.findAll())
                    .isEqualTo(saved);
        }
    }

    @Nested
    class FindById {

        @Test
        void 존재하는_id로_예약을_조회한다() {
            Reservation saved = reservationService.create(requestDto1);

            assertThat(reservationService.findById(saved.getId())).isEqualTo(saved);
        }

        @Test
        void 존재하지_않는_id를_조회하면_예외를_반환한다() {
            assertThatThrownBy(() -> reservationService.findById(-1L))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    class Create {

        @Test
        void 유효한_요청으로_예약을_생성한다() {
            Reservation saved = reservationService.create(requestDto1);

            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getName()).isEqualTo(requestDto1.name());
            assertThat(saved.getDate()).isEqualTo(requestDto1.date());
        }

        @Test
        void 시간이_존재하지_않으면_예외를_반환한다() {
            ReservationRequestDto dto = new ReservationRequestDto(
                    "유저1", LocalDate.of(2026, 5, 3), -1L, savedTheme1.getId());

            assertThatThrownBy(() -> reservationService.create(dto))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 테마가_존재하지_않으면_예외를_반환한다() {
            ReservationRequestDto dto = new ReservationRequestDto(
                    "유저1", LocalDate.of(2026, 5, 3), savedTime1.getId(), -1L);

            assertThatThrownBy(() -> reservationService.create(dto))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 동일한_테마_날짜_시간으로_예약을_생성하면_예외를_반환한다() {
            reservationService.create(requestDto1);

            assertThatThrownBy(() -> reservationService.create(requestDto1))
                    .isInstanceOf(ConflictException.class);
        }
    }

    @Nested
    class Delete {

        @Test
        void 예약을_삭제한다() {
            Reservation saved = reservationService.create(requestDto1);
            reservationService.delete(saved.getId());

            assertThat(reservationDao.existsById(saved.getId())).isFalse();
        }

        @Test
        void 존재하지_않는_id를_삭제하면_예외를_반환한다() {
            assertThatThrownBy(() -> reservationService.delete(-1L))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}
