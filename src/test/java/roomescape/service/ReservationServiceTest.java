package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.domain.vo.Name;
import roomescape.dto.ReservationRequestDto;
import roomescape.service.fake.FakeReservationDao;
import roomescape.service.fake.FakeThemeDao;
import roomescape.service.fake.FakeTimeDao;

class ReservationServiceTest {

    private ReservationService reservationService;
    private FakeReservationDao reservationDao;
    private FakeTimeDao timeDao;
    private FakeThemeDao themeDao;

    @BeforeEach
    void setUp() {
        reservationDao = new FakeReservationDao();
        timeDao = new FakeTimeDao();
        themeDao = new FakeThemeDao();
        reservationService = new ReservationService(reservationDao, timeDao, themeDao);

        timeDao.insert(new Time(LocalTime.of(13, 0)));
        timeDao.insert(new Time(LocalTime.of(14, 0)));

        themeDao.insert(new Theme(new Name("방탈출 이름1"), "http://thumbnail_url", "방탈출을 할 수 있다."));
        themeDao.insert(new Theme(new Name("방탈출 이름1"), "http://thumbnail_url", "방탈출을 할 수 있다."));
    }

    @Nested
    @DisplayName("예약을 생성할때: ")
    class Create {

        @Test
        void 시간이_존재하지_않는다면_예외_처리한다() {
            Long notExistsTimeId = 3L;
            Long existsThemeId = 1L;

            ReservationRequestDto requestDto = new ReservationRequestDto("유저1",
                    LocalDate.of(2026, 5, 3), notExistsTimeId, existsThemeId);

            assertThatThrownBy(() -> reservationService.create(requestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테마가_존재하지_않는다면_예외_처리한다() {
            Long existsTimeId = 1L;
            Long notExistsThemeId = 3L;

            ReservationRequestDto requestDto = new ReservationRequestDto("유저1",
                    LocalDate.of(2026, 5, 3), existsTimeId, notExistsThemeId);

            assertThatThrownBy(() -> reservationService.create(requestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 조회할_id가_존재하지_않으면_예외처리한다() {
        Long notExistsReservationId = 3L;

        assertThatThrownBy(() -> reservationService.findById(notExistsReservationId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 삭제하려는_id가_존재하지_않으면_예외_처리한다() {
        Long notExistsReservationId = 3L;

        assertThatThrownBy(() -> reservationService.delete(notExistsReservationId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Reservation createDtoHandler(ReservationRequestDto requestDto) {
        return reservationService.create(requestDto);
    }
}
