package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class ThemeJdbcDaoTest extends DaoTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private ReservationDao reservationDao;


    @Test
    @DisplayName("테마를 저장한다.")
    void save() {
        // given
        final Theme theme = new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        // when
        final Theme savedTheme = themeDao.save(theme);

        // then
        assertThat(savedTheme.getId()).isNotNull();
    }

    @Test
    @DisplayName("테마 목록을 조회한다.")
    void findAll() {
        // given
        final Theme theme = new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        themeDao.save(theme);

        // when
        final List<Theme> themes = themeDao.findAll();

        // then
        assertThat(themes).hasSize(1);
    }

    @Test
    @DisplayName("Id에 해당하는 테마를 조회한다.")
    void findById() {
        // given
        final Theme theme = new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        final Theme savedTheme = themeDao.save(theme);

        // when
        final Optional<Theme> foundTheme = themeDao.findById(savedTheme.getId());

        // then
        assertThat(foundTheme).isNotEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 Id로 테마를 조회하면 빈 옵셔널을 반환한다.")
    void returnEmptyOptionalWhenFindByNotExistingId() {
        // given
        final Long notExistingId = 1L;

        // when
        final Optional<Theme> foundTheme = themeDao.findById(notExistingId);

        // then
        assertThat(foundTheme).isEmpty();
    }

    @Test
    @DisplayName("Id에 해당하는 테마를 삭제한다.")
    void deleteById() {
        // given
        final Theme theme = new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        final Theme savedTheme = themeDao.save(theme);

        // when
        themeDao.deleteById(savedTheme.getId());

        // then
        final List<Theme> themes = themeDao.findAll();
        assertThat(themes).hasSize(0);
    }

    @Test
    @DisplayName("최근 일주일을 기준으로 예약이 많은 순으로 테마 10개를 조회한다.")
    void findTopThemesByReservationCountDuringPeriod() {
        // given
        final Member member = memberDao.save(new Member(new Name("냥인"), "nyangin@email.com", "1234", Role.USER));
        final ReservationTime reservationTime1 = reservationTimeDao.save(new ReservationTime("18:00"));
        final ReservationTime reservationTime2 = reservationTimeDao.save(new ReservationTime("19:00"));
        final Theme theme1 = themeDao.save(new Theme("호러", "매우 무섭습니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        final Theme theme2 = themeDao.save(new Theme("추리", "매우 어렵습니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        reservationDao.save(new Reservation(member, "2034-05-08", reservationTime1, theme1));
        reservationDao.save(new Reservation(member, "2034-05-08", reservationTime2, theme1));
        reservationDao.save(new Reservation(member, "2034-05-08", reservationTime1, theme2));
        final LocalDate period = AggregationPeriod.calculateAggregationPeriod(LocalDate.parse("2034-05-08"));
        final int limit = AggregationLimit.getAggregationLimit();

        // when
        final List<Theme> allOrderByReservationCountInLastWeek = themeDao.findTopThemesByReservationCountDuringPeriod(period, limit);

        // then
        assertThat(allOrderByReservationCountInLastWeek).extracting(Theme::getName)
                .containsExactly(theme1.getName(), theme2.getName());
    }
}
