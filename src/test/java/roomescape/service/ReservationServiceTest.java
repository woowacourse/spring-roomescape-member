package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.TimeDao;
import roomescape.dao.row.ThemeRow;
import roomescape.dao.row.TimeRow;
import roomescape.dto.request.ReservationRequestDto;
import roomescape.dto.response.ReservationResponseDto;
import roomescape.service.fake.FakeReservationDao;
import roomescape.service.fake.FakeThemeDao;
import roomescape.service.fake.FakeTimeDao;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationServiceTest {

    private static final Clock FIXED_CLOCK = Clock.fixed(
            Instant.parse("2026-05-10T03:00:00Z"),
            ZoneId.of("Asia/Seoul")
    );
    private static final LocalDate TODAY = LocalDate.of(2026, 5, 10);

    private ReservationService reservationService;
    private ReservationDao reservationDao;
    private TimeDao timeDao;
    private ThemeDao themeDao;


    private ReservationRequestDto requestDto1;
    private ReservationRequestDto requestDto2;

    @BeforeEach
    void setUp() {
        reservationDao = new FakeReservationDao();
        timeDao = new FakeTimeDao();
        themeDao = new FakeThemeDao();
        reservationService = new ReservationService(reservationDao, timeDao, themeDao, FIXED_CLOCK);

        TimeRow time1 = timeDao.create(new TimeRow(LocalTime.of(14, 0)));
        TimeRow time2 = timeDao.create(new TimeRow(LocalTime.of(15, 0)));

        ThemeRow theme1 = themeDao.create(new ThemeRow("방탈출 이름1", "http://thumbnail_url", "방탈출을 할 수 있다."));
        ThemeRow theme2 = themeDao.create(new ThemeRow("방탈출 이름2", "http://thumbnail_url", "방탈출을 할 수 있다."));

        requestDto1 = new ReservationRequestDto(
                "유저1", TODAY, time1.id(), theme1.id());
        requestDto2 = new ReservationRequestDto(
                "유저2", TODAY, time2.id(), theme2.id());
    }

    @Test
    void 조회할_id가_존재하지_않으면_예외처리한다() {
        Long notExistsReservationId = 3L;

        assertThatThrownBy(() -> reservationService.findById(notExistsReservationId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 삭제하려는_id가_존재하지_않으면_예외_처리한다() {
        Long notExistsReservationId = 3L;

        assertThatThrownBy(() -> reservationService.delete(notExistsReservationId))
                .isInstanceOf(NotFoundException.class);
    }

    private ReservationResponseDto createDtoHandler(ReservationRequestDto requestDto) {
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
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 테마가_존재하지_않는다면_예외_처리한다() {
            Long existsTimeId = 1L;
            Long notExistsThemeId = 3L;

            ReservationRequestDto requestDto = new ReservationRequestDto("유저1",
                    TODAY, existsTimeId, notExistsThemeId);

            assertThatThrownBy(() -> reservationService.create(requestDto))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 테마_날짜_시간_동일한_예약이면_예외를_반환한다() {
            ReservationResponseDto saved = createDtoHandler(requestDto1);
            assertThatThrownBy(() -> reservationService.create(requestDto1))
                    .isInstanceOf(ConflictException.class);
        }
    }
}
