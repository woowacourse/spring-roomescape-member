package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.TestFixture.*;
import static roomescape.TestFixture.RESERVATION_TIME_SEVEN;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemePopularFilter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class ThemeDaoTest extends DaoTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private ReservationDao reservationDao;

    private Theme theme;

    @BeforeEach
    void setUp() {
        theme = themeDao.save(THEME_DETECTIVE());
    }

    @Test
    @DisplayName("테마를 저장한다.")
    void save() {
        // given
        final Theme theme = THEME_HORROR();

        // when
        final Theme actual = themeDao.save(theme);

        // then
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    @DisplayName("테마 목록을 조회한다.")
    void findAll() {
        // when
        final List<Theme> actual = themeDao.findAll();

        // then
        assertThat(actual).hasSize(1);
    }

    @Test
    @DisplayName("Id에 해당하는 테마를 조회한다.")
    void findById() {
        // when
        final Optional<Theme> actual = themeDao.findById(theme.getId());

        // then
        assertThat(actual).isNotEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 Id로 테마를 조회하면 빈 옵셔널을 반환한다.")
    void returnEmptyOptionalWhenFindByNotExistingId() {
        // given
        final Long notExistingId = 2L;

        // when
        final Optional<Theme> actual = themeDao.findById(notExistingId);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("Id에 해당하는 테마를 삭제한다.")
    void deleteById() {
        // when
        themeDao.deleteById(theme.getId());

        // then
        final List<Theme> actual = themeDao.findAll();
        assertThat(actual).hasSize(0);
    }

    @Test
    @DisplayName("인기 테마 목록을 조회한다.")
    void findPopularThemes() {
        // given
        final Member member = memberDao.save(MEMBER_MIA());
        final ReservationTime reservationTime1 = reservationTimeDao.save(RESERVATION_TIME_SIX());
        final ReservationTime reservationTime2 = reservationTimeDao.save(RESERVATION_TIME_SEVEN());
        final Theme theme2 = themeDao.save(THEME_HORROR());
        reservationDao.save(new Reservation(member, DATE_MAY_EIGHTH, reservationTime1, theme2));
        reservationDao.save(new Reservation(member, DATE_MAY_EIGHTH, reservationTime2, theme2));
        reservationDao.save(new Reservation(member, DATE_MAY_EIGHTH, reservationTime1, theme));
        final ThemePopularFilter themePopularFilter
                = ThemePopularFilter.getThemePopularFilter(LocalDate.parse(DATE_MAY_EIGHTH));

        // when
        final List<Theme> actual = themeDao.findPopularThemesBy(themePopularFilter);

        // then
        assertThat(actual).extracting(Theme::getName)
                .containsExactly(theme2.getName(), theme.getName());
    }
}
