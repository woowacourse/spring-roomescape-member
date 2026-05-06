package roomescape.service;

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

    private ReservationRequestDto requestDto1;
    private ReservationRequestDto requestDto2;

    @BeforeEach
    void setUp() {
        reservationDao = new FakeReservationDao();
        timeDao = new FakeTimeDao();
        themeDao = new FakeThemeDao();
        reservationService = new ReservationService(reservationDao, timeDao, themeDao);

        Time time1 = timeDao.insert(new Time(LocalTime.of(13, 0)));
        Time time2 = timeDao.insert(new Time(LocalTime.of(14, 0)));

        Theme theme1 = themeDao.insert(new Theme(new Name("방탈출 이름1"), "http://thumbnail_url", "방탈출을 할 수 있다."));
        Theme theme2 = themeDao.insert(new Theme(new Name("방탈출 이름2"), "http://thumbnail_url", "방탈출을 할 수 있다."));

        requestDto1 = new ReservationRequestDto(
                "유저1", LocalDate.of(2026, 5, 3), time1.getId(), theme1.getId());
        requestDto2 = new ReservationRequestDto(
                "유저2", LocalDate.of(2026, 5, 3), time2.getId(), theme2.getId());
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

        @Test
        void 테마_날짜_시간_동일한_예약이면_예외를_반환한다() {
            Reservation saved = createDtoHandler(requestDto1);

            assertThatThrownBy(() -> reservationService.create(requestDto1))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
