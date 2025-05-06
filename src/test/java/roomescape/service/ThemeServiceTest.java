package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.fake.FakeReservationDao;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeDao;
import roomescape.fake.FakeThemeDao;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static roomescape.fixture.TestFixture.*;

public class ThemeServiceTest {

    FakeThemeDao themeDao;
    ReservationRepository repository;
    ThemeService themeService;

    public ThemeServiceTest() {
        themeDao = new FakeThemeDao();
        repository = new FakeReservationRepository(new FakeReservationDao(), new FakeReservationTimeDao(), themeDao);
        this.themeService = new ThemeService(repository);
    }

    @BeforeEach
    void setUp() {
        themeDao.clear();
    }

    @Test
    void 테마를_생성할_수_있다() {
        // given
        ThemeRequest theme = new ThemeRequest("진격의겨인", "재밌다", "abc");

        // when & then
        assertDoesNotThrow(() -> themeService.save(theme));
    }

    @Test
    void 중복된_테마_이름이_존재하는_경우_테마를_생성할_수_없다() {
        // given
        ThemeRequest theme = new ThemeRequest("진격의겨인", "재밌다", "abc");

        // when & then
        assertDoesNotThrow(() -> themeService.save(theme));
        assertThatThrownBy(() -> themeService.save(theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 해당 테마 이름이 이미 존재합니다.");
    }

    @Test
    void 특정_테마에_대한_예약이_존재하는_경우_테마를_삭제할_수_없다() {
        // given
        ReservationTime savedTime = repository.saveReservationTime(DEFAULT_TIME);
        Theme savedTheme = repository.saveTheme(DEFAULT_THEME);
        repository.saveReservation(new Reservation("예약", TOMORROW, savedTime, savedTheme));

        // when & then
        assertThatThrownBy(() -> themeService.deleteById(savedTheme.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 해당 테마에 대한 예약이 존재하기 때문에 삭제할 수 없습니다.");
    }
}
