package roomescape.service.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dto.reservation.ThemeRequest;
import roomescape.dto.reservation.ThemeResponse;
import roomescape.exceptions.EntityNotFoundException;
import roomescape.exceptions.reservation.ThemeDuplicateException;

@SpringBootTest
@Transactional
@Sql({"/fixtures/schema-test.sql", "/fixtures/data-test.sql"})
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Test
    @DisplayName("조회된 테마 엔티티를 DTO에 매핑해 반환한다.")
    void getThemes() {
        // when
        List<ThemeResponse> themeResponses = themeService.getThemes();

        // then
        assertThat(themeResponses.size()).isEqualTo(11);
        assertThat(themeResponses.getFirst().name()).isEqualTo("우테코 레벨1 탈출");
    }

    @Test
    @DisplayName("엔티티를 저장한 후, DTO로 반환한다.")
    void postReservationTime() {
        //given
        String given = "테스트";
        ThemeRequest request = new ThemeRequest(given, "설명", "썸네일");
        //when
        ThemeResponse actual = themeService.addTheme(request);
        //then
        AssertionsForClassTypes.assertThat(actual.name()).isEqualTo(given);
    }

    @Test
    @DisplayName("저장소에 없는 값을 삭제하려할 경우, 예외가 발생한다.")
    void deleteReservationTime() {
        assertThatThrownBy(() -> themeService.deleteTheme(999L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("테마 생성 시, 중복된 테마명일 경우 예외가 발생한다.")
    void addThemeIfDuplicationThemeName() {
        //given
        ThemeRequest request = new ThemeRequest("우테코 레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        //when&then
        assertThatThrownBy(() -> themeService.addTheme(request))
                .isInstanceOf(ThemeDuplicateException.class)
                .hasMessageContaining("중복된 테마명이 존재합니다.");
    }

    @Test
    @DisplayName("인기 테마 목록을 조회한다.")
    void readPopularThemesByPeriod() {
        // given
        int period = 7;
        int maxResults = 10;

        // when
        List<ThemeResponse> popularThemes = themeService.readPopularThemesByPeriod(period, maxResults);

        // then
        assertThat(popularThemes).hasSize(10);
        assertThat(popularThemes.get(0).name()).isEqualTo("우테코 레벨1 탈출");
        assertThat(popularThemes.get(1).name()).isEqualTo("우테코 레벨2 탈출");
        assertThat(popularThemes.get(2).name()).isEqualTo("우테코 레벨3 탈출");
        assertThat(popularThemes.get(3).name()).isEqualTo("우테코 레벨4 탈출");
        assertThat(popularThemes.get(4).name()).isEqualTo("우테코 레벨5 탈출");
        assertThat(popularThemes.get(5).name()).isEqualTo("우테코 레벨6 탈출");
        assertThat(popularThemes.get(6).name()).isEqualTo("우테코 레벨7 탈출");
        assertThat(popularThemes.get(7).name()).isEqualTo("우테코 레벨8 탈출");
        assertThat(popularThemes.get(8).name()).isEqualTo("우테코 레벨9 탈출");
        assertThat(popularThemes.get(9).name()).isEqualTo("우테코 레벨10 탈출");
    }
}
