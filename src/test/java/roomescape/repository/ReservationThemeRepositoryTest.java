package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservationTheme.PopularThemeCondition;
import roomescape.domain.reservationTheme.ReservationTheme;
import roomescape.domain.reservationTheme.ReservationThemeCommand;
import roomescape.domain.reservationTheme.ReservationThemeWithCount;
import roomescape.repository.reservationTheme.JdbcReservationThemeRepository;
import roomescape.repository.reservationTheme.ReservationThemeRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationThemeRepositoryTest extends BaseRepositoryTest {
    private ReservationThemeRepository reservationThemeRepository;

    @Override
    protected void initTable() {
        createReservationThemeTable();
        insertReservationTheme("테마1", "테마 설명", "image url");

        this.reservationThemeRepository = new JdbcReservationThemeRepository(jdbcTemplate);
    }

    @Override
    protected void deleteTable() {
        deleteReservationThemeTable();
    }

    @Test
    @DisplayName("특정 예약 테마 정상적으로 가져오는 지 테스트")
    void getReservationThemeTest() {
        Optional<ReservationTheme> reservationTheme = reservationThemeRepository.getTheme(1L);

        assertThat(reservationTheme.isPresent()).isTrue();
        assertThat(reservationTheme.get()).isEqualTo(new ReservationTheme(1, "테마1", "테마 설명", "image url"));
    }

    @Test
    @DisplayName("존재하지 않는 특정 예약 테마 빈 값으로 가져오는 지 테스트")
    void getInvalidReservationThemeTest() {
        Optional<ReservationTheme> reservationTheme = reservationThemeRepository.getTheme(3L);

        assertThat(reservationTheme.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("전체 예약 테마 정상적으로 가져오는 지 테스트")
    void getAllReservationThemeTest() {
        List<ReservationTheme> reservationTimes = reservationThemeRepository.getAllTheme();

        assertThat(reservationTimes).containsExactly(new ReservationTheme(1, "테마1", "테마 설명", "image url"));
    }

    @Test
    @DisplayName("예약 테마 삭제 정상적으로 작동하는 지 테스트")
    void deleteReservationTest() {
        reservationThemeRepository.deleteTheme(1);
        List<ReservationTheme> reservationTimes = reservationThemeRepository.getAllTheme();

        assertThat(reservationTimes).isNotIn(new ReservationTheme(1, "테마1", "테마 설명", "image url"));
    }

    @Test
    @DisplayName("예약 테마 추가 정상적으로 작동하는 지 테스트")
    void insertReservationTest() {
        ReservationTheme reservationTheme = reservationThemeRepository.addTheme(new ReservationThemeCommand( "테마2", "테마 설명", "image url"));
        List<ReservationTheme> reservations = reservationThemeRepository.getAllTheme();

        ReservationTheme expectedReservationTheme = new ReservationTheme(2, "테마2", "테마 설명", "image url");

        assertThat(reservationTheme).isEqualTo(expectedReservationTheme);
        assertThat(reservations).contains(expectedReservationTheme);
    }

    @Test
    @DisplayName("특정 기간 인기 테마 정상적으로 가져오는 지 테스트")
    void getPopularThemeTest() {
        createReservationTimeTable();
        createReservationTable();

        String startDate = "2026-04-01";
        String endDate = "2026-05-01";
        long size = 2;

        insertReservationTheme("name1", "description1", "imageUrl1");
        insertReservationTheme("name2", "description2", "imageUrl2");
        insertReservationTheme("name3", "description3", "imageUrl3");

        insertReservationTime("16:29");

        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id, created_at) VALUES (?, ?, ?, ?, ?)", "reservation1", "2026-04-04", 1, 1, "2026-04-04");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id, created_at) VALUES (?, ?, ?, ?, ?)", "reservation2", "2026-04-04", 1, 2, "2026-04-04");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id, created_at) VALUES (?, ?, ?, ?, ?)", "reservation3", "2026-04-04", 1, 2, "2026-04-04");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id, created_at) VALUES (?, ?, ?, ?, ?)", "reservation3", "2026-04-04", 1, 2, "2026-04-04");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id, created_at) VALUES (?, ?, ?, ?, ?)", "reservation4", "2026-04-04", 1, 3, "2026-04-04");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id, created_at) VALUES (?, ?, ?, ?, ?)", "reservation5", "2026-04-04", 1, 3, "2026-04-04");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id, created_at) VALUES (?, ?, ?, ?, ?)", "reservation6", "2026-05-05", 1, 2, "2026-05-05");

        List<ReservationThemeWithCount> popularThemes = reservationThemeRepository.getPopularTheme(new PopularThemeCondition(startDate, endDate, size));
        assertAll(
                () -> assertThat(popularThemes.size()).isEqualTo(2),
                () -> assertThat(popularThemes.getFirst().id()).isEqualTo(2),
                () -> assertThat(popularThemes.getFirst().count()).isEqualTo(3),
                () -> assertThat(popularThemes.getLast().id()).isEqualTo(3),
                () -> assertThat(popularThemes.getLast().count()).isEqualTo(2)
        );

        deleteReservationTable();
        deleteReservationTimeTable();
    }
}
