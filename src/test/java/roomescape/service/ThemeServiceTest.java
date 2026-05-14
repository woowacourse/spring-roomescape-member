package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationQueryDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.row.ReservationRow;
import roomescape.dao.row.ThemeRow;
import roomescape.dao.row.TimeRow;
import roomescape.dto.request.ThemeRequestDto;
import roomescape.dto.response.ThemeResponseDto;
import roomescape.fixture.FakeDatabase;
import roomescape.fixture.FakeReservationDao;
import roomescape.fixture.FakeReservationQueryDao;
import roomescape.fixture.FakeThemeDao;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ThemeServiceTest {
    private ThemeService themeService;
    private ThemeDao themeDao;
    private ReservationDao reservationDao;
    private ReservationQueryDao reservationQueryDao;

    private ThemeRequestDto requestDto1;
    private ThemeRequestDto requestDto2;

    @BeforeEach
    void setUp() {
        themeDao = new FakeThemeDao();
        reservationDao = new FakeReservationDao();
        reservationQueryDao = new FakeReservationQueryDao();
        themeService = new ThemeService(themeDao, reservationDao, reservationQueryDao);

        requestDto1 = new ThemeRequestDto("테마1", "https://test1.com", "설명길이제한때문에");
        requestDto2 = new ThemeRequestDto("테마2", "https://test2.com", "설명길이제한때문에");
        FakeDatabase.clearAll();
    }

    @Test
    void 시간이_이미_존재한다면_예외를_반환한다() {
        insertHandler(requestDto1);
        assertThatThrownBy(() -> themeService.create(requestDto1))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void 조회하려는_id가_존재하지_않으면_예외_처리한다() {
        Long notExistsThemeId = 1L;

        assertThatThrownBy(() -> themeService.findById(notExistsThemeId))
                .isInstanceOf(NotFoundException.class);
    }

    private ThemeResponseDto insertHandler(ThemeRequestDto requestDto1) {
        return themeService.create(requestDto1);
    }

    @Nested
    class 삭제할_때 {

        @Test
        void 삭제하려는_id가_존재하지_않으면_예외_처리한다() {
            Long notExistsThemeId = 1L;

            assertThatThrownBy(() -> themeService.delete(notExistsThemeId))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 예약_id가_존재하면_예외를_반환한다() {
            ThemeResponseDto savedTheme = themeService.create(requestDto1);
            reservationDao.create(new ReservationRow(
                    "달수",
                    LocalDate.now(),
                    new TimeRow(LocalTime.of(10, 0)),
                    new ThemeRow(savedTheme.id(), savedTheme.name(), savedTheme.thumbnailUrl(), savedTheme.description())));

            Long themeId = savedTheme.id();

            assertThatThrownBy(() -> themeService.delete(themeId))
                    .isInstanceOf(ConflictException.class);
        }
    }
}
