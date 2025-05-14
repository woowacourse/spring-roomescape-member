package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.RoomTheme;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcRoomThemeRepository.class)
@ActiveProfiles("test")
@Sql(scripts = "/schema.sql")
@Sql(scripts = "/reservation-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcRoomThemeRepositoryTest {

    @Autowired
    RoomThemeRepository roomThemeRepository;

    @Test
    @DisplayName("예약 가능 시간 데이터를 저장한다")
    void insert() {
        //given
        RoomTheme roomTheme = new RoomTheme("name", "description", "thumbnail");

        //when
        long actual = roomThemeRepository.insert(roomTheme);

        //then
        assertThat(actual).isEqualTo(3L);
    }

    @ParameterizedTest
    @CsvSource(value = {"test,true", "notExist,false"})
    @DisplayName("name을 기반으로 테마 존재 여부를 확인한다")
    void existsByName(String name, boolean expected) {
        //when
        boolean actual = roomThemeRepository.existsByName(name);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하는 모든 테마를 조회한다")
    void findAll() {
        //when
        List<RoomTheme> actual = roomThemeRepository.findAll();

        //then
        assertThat(actual).hasSize(2);
    }

    @Test
    @DisplayName("id를 기반으로 데이터를 조회한다")
    void findById() {
        //given
        long targetId = 1L;

        //when
        Optional<RoomTheme> actual = roomThemeRepository.findById(targetId);

        //then
        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("존재하지 않는 id를 기반으로 데이터를 조회하면 empty이다")
    void findByIdWhenNotExistedId() {
        //given
        long targetId = 100L;

        //when
        Optional<RoomTheme> actual = roomThemeRepository.findById(targetId);

        //then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("특정 기간 내에 존재하는 예약 횟수를 기반으로 인기 테마를 조회한다")
    @Sql(scripts = {"/schema.sql", "/popular-theme-test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findPopularThemes() {
        //given
        LocalDate start = LocalDate.of(2025, 5, 1);
        LocalDate end = LocalDate.of(2025, 5, 7);
        int topLimit = 4;

        //when
        List<RoomTheme> actual = roomThemeRepository.findPopularThemes(start, end, topLimit);

        //then
        int totalCount = 3;
        List<RoomTheme> expectedOrder = List.of(
                new RoomTheme(100, "top1", "description1", "thumbnail1"),
                new RoomTheme(200, "top2", "description2", "thumbnail2"),
                new RoomTheme(300, "top3", "description3", "thumbnail3")
        );

        Assertions.assertAll(
                () -> assertThat(actual).hasSize(totalCount),
                () -> assertThat(actual).containsExactlyElementsOf(expectedOrder)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"2,true", "100,false"})
    @DisplayName("대상 id가 존재할 경우 데이터를 삭제한다")
    void deleteById(long targetId, boolean expected) {
        //when
        boolean actual = roomThemeRepository.deleteById(targetId);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
