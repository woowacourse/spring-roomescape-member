package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.dto.request.ThemeRequest;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeThemeRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static roomescape.fixture.TestFixture.*;

public class ThemeServiceTest {

    ReservationRepository reservationRepository;
    ThemeRepository themeRepository;
    ThemeService themeService;

    public ThemeServiceTest() {
        reservationRepository = new FakeReservationRepository();
        themeRepository = new FakeThemeRepository();
        this.themeService = new ThemeService(reservationRepository, themeRepository);
    }

    @BeforeEach
    void setUp() {
        ((FakeThemeRepository) themeRepository).clear();
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
        Theme savedTheme = themeRepository.save(DEFAULT_THEME);
        reservationRepository.save(new Reservation("예약", TOMORROW, DEFAULT_TIME, savedTheme));

        // when & then
        assertThatThrownBy(() -> themeService.deleteById(savedTheme.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 해당 테마에 대한 예약이 존재하기 때문에 삭제할 수 없습니다.");
    }
}
