package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.exception.InvalidValueException;
import roomescape.dto.theme.ThemeCreateRequest;
import roomescape.dto.theme.ThemeResponse;
import roomescape.service.exception.InvalidRequestException;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:clean_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ThemeService themeService;

    @Nested
    @DisplayName("테마 조회 테스트")
    class FindTheme {

        @MockBean
        private Clock clock;

        @Test
        @DisplayName("모든 테마 정보를 조회한다.")
        void findAll() {
            jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('방탈출1', '방탈출 1번', '썸네일1')");
            jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('방탈출2', '방탈출 2번', '썸네일2')");

            List<ThemeResponse> results = themeService.findAll();

            assertThat(results).hasSize(2);
        }

        @Test
        @Sql(value = "classpath:popular_data.sql")
        @DisplayName("전달한 날짜 기준 이전 일주일 간 예약이 많이된 상위 10개의 테마를 조회한다.")
        void findPopulars() {
            given(clock.instant()).willReturn(Instant.parse("2024-05-10T00:00:00Z"));
            given(clock.getZone()).willReturn(ZoneId.of("Asia/Seoul"));

            List<ThemeResponse> results = themeService.findPopulars();

            assertAll(
                    () -> assertThat(results).hasSize(10),
                    () -> assertThat(results.get(0).id()).isSameAs(1L),
                    () -> assertThat(results.get(1).id()).isSameAs(2L),
                    () -> assertThat(results.get(2).id()).isSameAs(3L),
                    () -> assertThat(results.get(3).id()).isSameAs(4L),
                    () -> assertThat(results.get(4).id()).isSameAs(5L)
            );
        }
    }

    @Nested
    @DisplayName("테마 생성 테스트")
    class CreateNewTheme {

        @Test
        @DisplayName("테마를 추가한다.")
        void add() {
            ThemeCreateRequest request = ThemeCreateRequest.of("방탈출1", "1번 방탈출", "썸네일1");

            ThemeResponse result = themeService.add(request);

            assertAll(
                    () -> assertThat(themeService.findAll()).hasSize(1),
                    () -> assertThat(result.name()).isEqualTo("방탈출1"),
                    () -> assertThat(result.description()).isEqualTo("1번 방탈출"),
                    () -> assertThat(result.thumbnail()).isEqualTo("썸네일1")
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   "})
        @DisplayName("테마명이 공백이면 예외가 발생한다.")
        void addThemeByNullOrEmptyName(String name) {
            ThemeCreateRequest request = ThemeCreateRequest.of(name, "방탈출 설명", "방탈출 썸네일");

            assertThatThrownBy(() -> themeService.add(request))
                    .isInstanceOf(InvalidValueException.class);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   "})
        @DisplayName("테마 설명이 공백이면 예외가 발생한다.")
        void addThemeByNullOrEmptyDescription(String description) {
            ThemeCreateRequest request = ThemeCreateRequest.of("방탈출 이름", description, "방탈출 썸네일");

            assertThatThrownBy(() -> themeService.add(request))
                    .isInstanceOf(InvalidValueException.class);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   "})
        @DisplayName("테마 썸네일이 공백이면 예외가 발생한다.")
        void addThemeByNullOrEmptyThumbnail(String thumbnail) {
            ThemeCreateRequest request = ThemeCreateRequest.of("방탈출 이름", "방탈출 설명", thumbnail);

            assertThatThrownBy(() -> themeService.add(request))
                    .isInstanceOf(InvalidValueException.class);
        }
    }
    @Nested
    @DisplayName("테마 삭제 테스트")
    class DeleteTheme {

        @Test
        @DisplayName("테마를 삭제한다.")
        void delete() {
            themeService.add(ThemeCreateRequest.of("방탈출1", "1번 방탈출", "방탈출 설명"));

            themeService.delete(1L);

            assertThat(themeService.findAll()).isEmpty();
        }

        @Test
        @DisplayName("테마 삭제시 아이디가 존재하지 않으면 예외가 발생한다.")
        void deleteByNotExistId() {
            assertThatThrownBy(() -> themeService.delete(-1L))
                    .isInstanceOf(InvalidRequestException.class);
        }
    }
}
