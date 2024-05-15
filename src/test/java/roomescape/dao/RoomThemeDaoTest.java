package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.MEMBER_PARAMETER_SOURCE;
import static roomescape.TestFixture.ROOM_THEME_FIXTURE;
import static roomescape.TestFixture.createMember;
import static roomescape.TestFixture.createReservationTime;
import static roomescape.TestFixture.createTheme;

import io.restassured.RestAssured;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.domain.RoomTheme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomThemeDaoTest {
    @LocalServerPort
    private int port;

    @Autowired
    private RoomThemeDao roomThemeDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("DELETE FROM member");
    }

    @DisplayName("저장된 모든 테마를 보여준다.")
    @Test
    void findAll() {
        // given & when
        List<RoomTheme> roomThemes = roomThemeDao.findAll();
        // then
        assertThat(roomThemes).isEmpty();
    }

    @DisplayName("테마를 랭킹순으로 보여준다.")
    @Test
    void findAllRanking() {
        // given
        Long memberId = createMember(jdbcTemplate, MEMBER_PARAMETER_SOURCE);
        Long timeId = createReservationTime(jdbcTemplate);
        Long themeId = createTheme(jdbcTemplate);
        roomThemeDao.save(new RoomTheme("a", "s", "d"));
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("date", LocalDate.now().minusDays(2))
                .addValue("memberId", memberId)
                .addValue("timeId", timeId)
                .addValue("themeId", themeId);
        new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id")
                .execute(parameterSource);
        // when
        List<RoomTheme> ranking = roomThemeDao.findAllRanking();
        // then
        assertThat(ranking).hasSize(1);
        assertThat(ranking.get(0).getName()).isEqualTo(
                ROOM_THEME_FIXTURE.getName());
    }

    @DisplayName("해당 id의 테마를 보여준다.")
    @Test
    void findById() {
        // given
        RoomTheme savedRoomTheme = roomThemeDao.save(ROOM_THEME_FIXTURE);
        // when
        RoomTheme foundRoomTheme = roomThemeDao.findById(savedRoomTheme.getId()).get();
        // then
        assertAll(
                () -> assertThat(foundRoomTheme.getId()).isEqualTo(savedRoomTheme.getId()),
                () -> assertThat(foundRoomTheme.getName()).isEqualTo(savedRoomTheme.getName()),
                () -> assertThat(foundRoomTheme.getDescription()).isEqualTo(
                        savedRoomTheme.getDescription()),
                () -> assertThat(foundRoomTheme.getThumbnail()).isEqualTo(
                        savedRoomTheme.getThumbnail())
        );
    }

    @DisplayName("해당 id의 테마가 없는 경우, Optional.empty()를 반환한다.")
    @Test
    void findByNotExistingId() {
        assertThat(roomThemeDao.findById(1L)).isEqualTo(Optional.empty());
    }

    @DisplayName("테마를 저장한다.")
    @Test
    void save() {
        // given & when
        RoomTheme savedRoomTheme = roomThemeDao.save(ROOM_THEME_FIXTURE);
        // then
        assertAll(
                () -> assertThat(roomThemeDao.findAll()).hasSize(1),
                () -> assertThat(savedRoomTheme.getName()).isEqualTo(ROOM_THEME_FIXTURE.getName()),
                () -> assertThat(savedRoomTheme.getDescription())
                        .isEqualTo(ROOM_THEME_FIXTURE.getDescription()),
                () -> assertThat(savedRoomTheme.getThumbnail())
                        .isEqualTo(ROOM_THEME_FIXTURE.getThumbnail())
        );
    }

    @DisplayName("테마를 삭제한다.")
    @Test
    void deleteTheme() {
        // given
        RoomTheme savedRoomTheme = roomThemeDao.save(ROOM_THEME_FIXTURE);
        // when
        roomThemeDao.deleteById(savedRoomTheme.getId());
        // then
        assertThat(roomThemeDao.findAll()).isEmpty();
    }

    @DisplayName("삭제 대상이 존재하지 않으면 false를 반환한다.")
    @Test
    void returnFalseWhenNotDeleted() {
        // given & when
        boolean deleted = roomThemeDao.deleteById(1L);
        // then
        assertThat(deleted).isFalse();
    }
}
