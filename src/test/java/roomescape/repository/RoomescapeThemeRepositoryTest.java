package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.ReservationTheme;

@JdbcTest
@ActiveProfiles("test")
class RoomescapeThemeRepositoryTest {

    @Autowired
    DataSource dataSource;
    JdbcTemplate template;
    RoomescapeThemeRepository repository;

    @BeforeEach
    void setUp() {
        template = new JdbcTemplate(dataSource);
        repository = new RoomescapeThemeRepositoryImpl(dataSource);
        template.update("insert into reservation_theme (name,description, thumbnail) values (?,?,?)", "방탈출1", "1", "1");
    }

    @AfterEach
    void tearDown() {
        template.execute("DELETE FROM reservation_theme");
        template.execute("ALTER TABLE reservation_theme ALTER COLUMN id RESTART WITH 1");
    }

    @DisplayName("id로 테마 데이터를 성공적으로 가져온다.")
    @Test
    void findById() {
        //given
        final long id = 1L;

        //when
        final ReservationTheme theme = repository.findById(id).get();

        //then
        assertThat(theme.getId()).isEqualTo(id);
    }

    @DisplayName("모든 테마 데이터를 성공적으로 가져온다.")
    @Test
    void findAll() {
        //given & when
        final List<ReservationTheme> themes = repository.findAll();

        //then
        assertThat(themes).isNotEmpty();
    }

    @DisplayName("주간 인기테마를 성공적으로 가져온다.")
    @Test
    void findTopThemeOrderByCountWithinDaysDesc() {
        //given & when
        int popular_count_days = 7;
        final List<ReservationTheme> weeklyThemeOrderByCountDesc = repository.findTopThemeOrderByCountWithinDaysDesc(
                popular_count_days);

        //then
        assertThat(weeklyThemeOrderByCountDesc).hasSizeLessThanOrEqualTo(10);
    }

    @DisplayName("테마를 성공적으로 저장한다.")
    @Test
    void save() {
        //given
        final ReservationTheme reservationTheme = new ReservationTheme("테마1", "디스크립션1", "썸네일1");

        //when
        final ReservationTheme expected = repository.save(reservationTheme);

        //then
        assertAll(
                () -> assertThat(expected.getName()).isEqualTo(reservationTheme.getName()),
                () -> assertThat(expected.getDescription()).isEqualTo(reservationTheme.getDescription()),
                () -> assertThat(expected.getThumbnail()).isEqualTo(reservationTheme.getThumbnail())
        );
    }

    @DisplayName("id로 테마를 성공적으로 삭제한다.")
    @Test
    void deleteById() {
        //given
        final ReservationTheme reservationTheme = new ReservationTheme("테마1", "디스크립션1", "썸네일1");

        //when
        final ReservationTheme expected = repository.save(reservationTheme);
        boolean result = repository.deleteById(expected.getId());

        //
        assertThat(result).isTrue();
    }
}
