package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.exception.InvalidReservationException;
import roomescape.service.dto.ThemeRequest;
import roomescape.service.dto.ThemeResponse;

@SpringBootTest
@Sql(scripts = "/test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class ThemeServiceTest {
    @Autowired
    private ThemeService themeService;

    @DisplayName("테마를 생성한다.")
    @Test
    void create() {
        //given
        ThemeRequest themeRequest = new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        ThemeResponse themeResponse = themeService.create(themeRequest);

        //then
        assertThat(themeResponse.id()).isNotZero();
        themeService.deleteById(2);
    }

    @DisplayName("이미 존재하는 테마 이름으로 테마를 생성하면 예외를 발생시킨다.")
    @Test
    void cannotCreateByDuplicatedName() {
        //given
        ThemeRequest themeRequest = new ThemeRequest("레벨1 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        //when&then
        assertThatThrownBy(() -> themeService.create(themeRequest))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("이미 존재하는 테마 이름입니다.");
    }

    @DisplayName("모든 테마를 조회한다.")
    @Test
    void findAll() {
        //when
        List<ThemeResponse> responses = themeService.findAll();

        //then
        assertThat(responses).hasSize(1);
    }

    @DisplayName("테마를 삭제한다.")
    @Test
    void deleteById() {
        //given
        ThemeRequest themeRequest = new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        ThemeResponse themeResponse = themeService.create(themeRequest);

        //when
        themeService.deleteById(themeResponse.id());

        //then
        assertThat(themeService.findAll()).hasSize(1);
    }

    @DisplayName("예약이 존재하는 테마를 삭제하면 예외가 발생한다.")
    @Test
    void cannotDeleteByReservation() {
        assertThatThrownBy(() -> themeService.deleteById(1))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("해당 테마로 예약이 존재해서 삭제할 수 없습니다.");
    }

    @DisplayName("인기 테마를 조회한다.")
    @Test
    void findPopularThemes() {
        //when
        List<ThemeResponse> result = themeService.findPopularThemes();

        //then
        assertThat(result).hasSize(0);
    }
}
