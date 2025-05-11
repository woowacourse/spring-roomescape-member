package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.application.service.ThemeService;
import roomescape.domain.exception.ReservationExistException;
import roomescape.domain.exception.ThemeDuplicatedException;
import roomescape.domain.model.Reservation;
import roomescape.domain.model.Theme;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.ThemeRepository;
import roomescape.fake.ReservationDaoFake;
import roomescape.fake.ThemeDaoFake;
import roomescape.infrastructure.dao.ReservationDao;
import roomescape.infrastructure.dao.ThemeDao;
import roomescape.infrastructure.repository.ReservationRepositoryImpl;
import roomescape.infrastructure.repository.ThemeRepositoryImpl;
import roomescape.presentation.dto.request.ThemeRequest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static roomescape.fixture.TestFixture.*;

public class ThemeServiceTest {

    ReservationDao reservationDao;
    ReservationRepository reservationRepository;
    ThemeDao themeDao;
    ThemeRepository themeRepository;
    ThemeService themeService;

    public ThemeServiceTest() {
        reservationDao = new ReservationDaoFake();
        reservationRepository = new ReservationRepositoryImpl(reservationDao);
        themeDao = new ThemeDaoFake();
        themeRepository = new ThemeRepositoryImpl(themeDao);
        this.themeService = new ThemeService(reservationRepository, themeRepository);
    }

    @BeforeEach
    void setUp() {
        ((ReservationDaoFake) reservationDao).clear();
        ((ThemeDaoFake) themeDao).clear();
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
                .isInstanceOf(ThemeDuplicatedException.class);
    }

    @Test
    void 특정_테마에_대한_예약이_존재하는_경우_테마를_삭제할_수_없다() {
        // given
        Theme savedTheme = themeRepository.save(DEFAULT_THEME);
        reservationRepository.save(new Reservation(1L, TOMORROW, DEFAULT_TIME, savedTheme));

        // when & then
        assertThatThrownBy(() -> themeService.deleteById(savedTheme.getId()))
                .isInstanceOf(ReservationExistException.class);
    }
}
