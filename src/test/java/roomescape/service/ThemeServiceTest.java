package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.row.ReservationRow;
import roomescape.dao.row.ThemeRow;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequestDto;
import roomescape.dto.response.ThemeResponseDto;
import roomescape.service.fake.FakeReservationDao;
import roomescape.service.fake.FakeThemeDao;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ThemeServiceTest {
    private ThemeService themeService;
    private ThemeDao themeDao;
    private ReservationDao reservationDao;

    private ThemeRequestDto requestDto1;
    private ThemeRequestDto requestDto2;

    @BeforeEach
    void setUp() {
        themeDao = new FakeThemeDao();
        reservationDao = new FakeReservationDao();
        themeService = new ThemeService(themeDao, reservationDao);

        requestDto1 = new ThemeRequestDto("테마1", "https://test1.com", "설명길이제한때문에");
        requestDto2 = new ThemeRequestDto("테마2", "https://test2.com", "설명길이제한때문에");
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
                    null,
                    new ThemeRow(savedTheme.id(), savedTheme.name(), savedTheme.thumbnailUrl(), savedTheme.description())));

            Long themeId = savedTheme.id();

            assertThatThrownBy(() -> themeService.delete(themeId))
                    .isInstanceOf(ConflictException.class);
        }
    }

    private ThemeResponseDto insertHandler(ThemeRequestDto requestDto1) {
        return themeService.create(requestDto1);
    }
}
