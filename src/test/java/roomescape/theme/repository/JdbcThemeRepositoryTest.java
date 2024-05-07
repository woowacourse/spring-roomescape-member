package roomescape.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.fixture.Fixture;
import roomescape.theme.model.Theme;

@ActiveProfiles("test")
@JdbcTest
@Sql(scripts = {"/delete-data.sql", "/init-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class JdbcThemeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private JdbcThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        themeRepository = new JdbcThemeRepository(jdbcTemplate, jdbcTemplate.getDataSource());
    }

    @Test
    @DisplayName("Theme 저장한 후 저장한 테마 값을 반환한다.")
    void save() {
        Theme theme = new Theme(null, "마크", "노력함", "https://asd.cmom");

        assertThat(themeRepository.save(theme))
                .isEqualTo(new Theme(4L, "마크", "노력함", "https://asd.cmom"));
    }

    @Test
    @DisplayName("Theme 테이블의 있는 모든 데이터를 조회한다.")
    void findAll() {
        assertThat(themeRepository.findAll())
                .containsExactly(
                        Fixture.THEME_1,
                        Fixture.THEME_2,
                        Fixture.THEME_3);
    }

    @Test
    @DisplayName("Theme 테이블의 주어진 id와 동일한 데이터를 조회한다.")
    void findById() {
        assertThat(themeRepository.findById(1L))
                .isEqualTo(Optional.of(Fixture.THEME_1));
    }

    @Test
    @DisplayName("Theme 테이블에 주어진 id와 없는 경우 빈 옵셔널을 반환한다.")
    void findById_Return_EmptyOptional() {
        assertThat(themeRepository.findById(99999L))
                .isNotPresent();
    }

    @Test
    @DisplayName("Theme 테이블에서 많이 예약된 테마 10개를 내림차순으로 가져온다.")
    void findOrderByReservation() {
        assertThat(themeRepository.findOrderByReservation())
                .containsExactlyInAnyOrderElementsOf(List.of(
                        Fixture.THEME_2,
                        Fixture.THEME_1));
    }

    @Test
    @DisplayName("예약되지 않은 테마는 가져오지 않는다.")
    void findOrderByReservation2() {
        themeRepository.save(new Theme(null, "예약되지 않은 테마", "테마 설명", "https://asd.cmom"));

        List<Theme> orderByReservation = themeRepository.findOrderByReservation();

        assertThat(orderByReservation)
                .containsExactlyInAnyOrderElementsOf(List.of(
                        Fixture.THEME_2,
                        Fixture.THEME_1));
    }

    @Test
    @DisplayName("예약이 많은 순서대로 테마 10개만 가져온다.")
    void findOrderByReservation3() {
        // 테마 10개 추가 및 1 ~ 11번 테마를 사용하는 예약 생성
        generateReservationBy11Theme();

        assertThat(themeRepository.findOrderByReservation())
                .hasSize(10);
    }

    private void generateReservationBy11Theme() {
        // 테마 10개 추가
        IntStream.range(0, 10).forEach(i ->
                jdbcTemplate.update("insert into theme (name, description, thumbnail) values ('추가 테마%d', '설명', 'https://asd.cmom')".formatted(i)));
        // 1 ~ 11번 테마를 사용하는 예약 생성
        IntStream.range(1, 12).forEach(i ->
            jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values ('마크' , %d-05-01, 1, %d)".formatted(2124 + i, i)));
    }

    @Test
    @DisplayName("주어진 id와 동일한 데이터를 삭제한다.")
    void deleteById() {
        themeRepository.deleteById(3L);

        assertThat(themeRepository.findById(3L))
                .isNotPresent();
    }
}
