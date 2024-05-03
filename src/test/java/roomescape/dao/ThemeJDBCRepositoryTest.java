package roomescape.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class ThemeJDBCRepositoryTest {
    private ThemeRepository themeRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ReservationRepository reservationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        themeRepository = new ThemeJDBCRepository(jdbcTemplate);
        reservationTimeRepository = new ReservationTimeJDBCRepository(jdbcTemplate);
        reservationRepository = new ReservationJDBCRepository(jdbcTemplate);
    }

    @DisplayName("새로운 테마를 저장한다.")
    @Test
    void saveTheme() {
        //given
        Theme theme = saveThemeByName("레벨2 탈출");

        //when
        Theme result = themeRepository.save(theme);

        //then
        assertThat(result.getId()).isNotZero();
    }

    @DisplayName("전체 테마 목록을 조회한다.")
    @Test
    void findAll() {
        //given
        saveThemeByName("레벨2 탈출");

        //when
        List<Theme> themes = themeRepository.findAll();

        //then
        assertThat(themes).hasSize(1);
    }

    @DisplayName("테마를 삭제한다.")
    @Test
    void deleteTheme() {
        //given
        Theme theme = saveThemeByName("레벨2 탈출");
        int expectedSize = 0;

        //when
        themeRepository.deleteById(theme.getId());

        //then
        assertThat(themeRepository.findAll()).hasSize(expectedSize);
    }

    @DisplayName("같은 테마 이름이 존재한다.")
    @Test
    void existsByName() {
        //given
        String name = "레벨2 탈출";
        Theme theme = saveThemeByName(name);
        themeRepository.save(theme);

        //when
        boolean result = themeRepository.existsByName(name);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("같은 테마 이름이 존재하지 않는다.")
    @Test
    void notExistsByName() {
        //given
        String name = "레벨2 탈출";
        Theme theme = saveThemeByName(name);
        themeRepository.save(theme);

        //when
        String otherName = "레벨3 탈출";
        boolean result = themeRepository.existsByName(otherName);

        //then
        assertThat(result).isFalse();
    }

    @DisplayName("id로 테마를 조회한다.")
    @Test
    void findById() {
        //given
        Theme theme = saveThemeByName("레벨2 탈출");

        //when
        Theme result = themeRepository.findById(theme.getId()).get();

        //then
        assertThat(result.getId()).isEqualTo(theme.getId());
    }

    @DisplayName("최근 일주일 기준 예약 많은 테마 10개를 조회한다. - 예약이 많은 순으로 반환: theme1 2개, theme2 1개, theme3 0개")
    @Test
    void findPopularThemesByOrder() {
        //given
        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime("10:00"));
        String name1 = "레벨1 탈출";
        String name2 = "레벨2 탈출";
        String name3 = "레벨3 탈출";
        Theme theme1 = saveThemeByName(name1);
        Theme theme2 = saveThemeByName(name2);
        Theme theme3 = saveThemeByName(name3);
        saveReservation(reservationTime, "2222-04-01", theme1);
        saveReservation(reservationTime, "2222-04-02", theme1);
        saveReservation(reservationTime, "2222-04-03", theme2);

        //when
        List<Theme> themes = themeRepository.findByReservationTermAndCount("2222-04-01", "2222-04-07", 10);

        //then
        assertThat(themes.stream().map(Theme::getName).toList()).containsExactly(name1, name2);
    }

    @DisplayName("최근 일주일 기준 예약 많은 테마 10개를 조회한다. - 최근 일주일 예약 개수 기준: theme1 1개, theme2 2개, theme3 0개")
    @Test
    void findPopularThemesByTerm() {
        //given
        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime("10:00"));
        String name1 = "레벨1 탈출";
        String name2 = "레벨2 탈출";
        String name3 = "레벨3 탈출";
        Theme theme1 = saveThemeByName(name1);
        Theme theme2 = saveThemeByName(name2);
        Theme theme3 = saveThemeByName(name3);
        saveReservation(reservationTime, "2222-04-01", theme1);
        saveReservation(reservationTime, "2222-03-01", theme1);
        saveReservation(reservationTime, "2222-03-02", theme1);
        saveReservation(reservationTime, "2222-04-02", theme2);
        saveReservation(reservationTime, "2222-04-03", theme2);

        //when
        List<Theme> themes = themeRepository.findByReservationTermAndCount("2222-04-01", "2222-04-07", 10);

        //then
        assertThat(themes.stream().map(Theme::getName).toList()).containsExactly(name2, name1);
    }

    private void saveReservation(ReservationTime reservationTime, String date, Theme theme1) {
        Reservation reservation = new Reservation("브라운", new ReservationDate(date), reservationTime, theme1);
        reservationRepository.save(reservation);
    }

    private Theme saveThemeByName(String name) {
        return themeRepository.save(new Theme(name, "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
    }
}
