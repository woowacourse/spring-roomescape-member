package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.CustomBadRequest;
import roomescape.fixture.MemberFixture;
import roomescape.fixture.ReservationFixture;
import roomescape.fixture.ReservationTimeFixture;
import roomescape.fixture.ThemeFixture;
import roomescape.service.dto.output.ThemeOutput;

@SpringBootTest
@ActiveProfiles("test")
class ThemeServiceTest {

    @Autowired
    ThemeService themeService;
    @Autowired
    ReservationDao reservationDao;
    @Autowired
    ReservationTimeDao reservationTimeDao;
    @Autowired
    ThemeDao themeDao;
    @Autowired
    MemberDao memberDao;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("TRUNCATE TABLE reservation");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.update("TRUNCATE TABLE theme");
        jdbcTemplate.update("TRUNCATE TABLE reservation_time");
        jdbcTemplate.update("TRUNCATE TABLE member");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @DisplayName("예약 시간을 생성한다.")
    @Test
    void create_reservationTime() {
        assertThatCode(() -> themeService.createTheme(ThemeFixture.getInput("테마 1")))
                .doesNotThrowAnyException();
    }

    @DisplayName("유효하지 않은 값을 입력하면 예외를 발생한다.")
    @Test
    void throw_exception_when_input_is_invalid() {
        assertThatThrownBy(() -> themeService.createTheme(ThemeFixture.getInput("")))
                .isInstanceOf(CustomBadRequest.class);
    }

    @DisplayName("모든 요소를 받아온다.")
    @Test
    void get_all_themes() {
        themeDao.create(ThemeFixture.getDomain("테마 1"));
        themeDao.create(ThemeFixture.getDomain("테마 2"));

        final List<ThemeOutput> themeOutputs = themeService.getAllThemes();

        assertThat(themeOutputs).hasSize(2);
    }

    @DisplayName("존재하지 않는 테마 ID 를 삭제하려 하면 에외를 발생한다.")
    @Test
    void throw_exception_when_not_exist_id() {
        assertThatThrownBy(() -> themeService.deleteTheme(-1))
                .isInstanceOf(CustomBadRequest.class);
    }

    @DisplayName("특정 테마에 대한 예약이 존재하면 예외를 발생한다.")
    @Test
    void throw_exception_when_delete_id_that_exist_reservation() {
        final var member = memberDao.create(MemberFixture.getDomain());
        final var theme = themeDao.create(ThemeFixture.getDomain("테마 1"));
        final var time = reservationTimeDao.create(ReservationTimeFixture.getDomain());
        reservationDao.create(ReservationFixture.getDomain(member, time, theme));

        assertThatThrownBy(() -> themeService.deleteTheme(theme.id()))
                .isInstanceOf(CustomBadRequest.class);
    }

    @DisplayName("예약이 많은 테마 순으로 조회한다.")
    @Test
    void get_popular_themes() {
        final var member = memberDao.create(MemberFixture.getDomain());
        final Theme theme1 = themeDao.create(ThemeFixture.getDomain("테마 1"));
        final Theme theme2 = themeDao.create(ThemeFixture.getDomain("테마 2"));

        final var time1 = reservationTimeDao.create(ReservationTimeFixture.getDomain());
        final var time2 = reservationTimeDao.create(ReservationTimeFixture.getDomain("11:00"));
        final var time3 = reservationTimeDao.create(ReservationTimeFixture.getDomain("12:00"));

        reservationDao.create(ReservationFixture.getDomain(member, time1, theme1));
        reservationDao.create(ReservationFixture.getDomain(member, time2, theme1));
        reservationDao.create(ReservationFixture.getDomain(member, time3, theme2));

        final List<ThemeOutput> popularThemes = themeService.findPopularThemes("2024-06-02", 10);

        assertThat(popularThemes).containsExactly(ThemeOutput.from(theme1), ThemeOutput.from(theme2));
    }

    @DisplayName("지정된 개수만큼 인기 테마를 조회한다.")
    @Test
    void get_popular_themes_up_to_limit() {
        final var theme1 = themeDao.create(ThemeFixture.getDomain("테마 1"));
        final var theme2 = themeDao.create(ThemeFixture.getDomain("테마 2"));
        final var theme3 = themeDao.create(ThemeFixture.getDomain("테마 3"));

        final var time = reservationTimeDao.create(ReservationTimeFixture.getDomain());

        final var member = memberDao.create(MemberFixture.getDomain());

        createReservations(member, time, theme1, 1, 1);
        createReservations(member, time, theme2, 2, 2);
        createReservations(member, time, theme3, 4, 3);

        final List<ThemeOutput> popularThemes = themeService.findPopularThemes("2024-06-07", 2);

        assertThat(popularThemes).containsExactly(ThemeOutput.from(theme3), ThemeOutput.from(theme2));
    }

    private void createReservations(final Member member,
                                    final ReservationTime time,
                                    final Theme theme,
                                    final int startDate,
                                    final int count) {
        for (int i = startDate; i < startDate + count; i++) {
            final var date = ReservationDate.from(String.format("2024-06-%02d", i));
            final Reservation reservation = new Reservation(member, date, time, theme);
            reservationDao.create(reservation);
        }
    }
}
