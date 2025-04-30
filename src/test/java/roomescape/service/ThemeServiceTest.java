package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.exceptions.EntityNotFoundException;
import roomescape.fake.ThemeFakeRepository;
import roomescape.repository.ThemeRepository;

class ThemeServiceTest {

    private final ThemeRepository themeRepository = new ThemeFakeRepository();
    private final ThemeService themeService = new ThemeService(themeRepository);

    @Test
    @DisplayName("조회된 테마 엔티티를 DTO에 매핑해 반환한다.")
    void test_readAllTheme() {
        // when
        List<ThemeResponse> themeResponses = themeService.readAllTheme();

        // then
        assertThat(themeResponses.size()).isEqualTo(1);
        assertThat(themeResponses.getFirst().name()).isEqualTo("레벨2 탈출");
    }

    @Test
    @DisplayName("엔티티를 저장한 후, DTO로 반환한다.")
    void test_postReservationTime() {
        //given
        String given = "테스트";
        ThemeRequest request = new ThemeRequest(given, "설명", "썸네일");
        //when
        ThemeResponse actual = themeService.postTheme(request);
        //then
        AssertionsForClassTypes.assertThat(actual.name()).isEqualTo(given);
    }

    @Test
    @DisplayName("저장소에 없는 값을 삭제하려할 경우, 예외가 발생한다.")
    void test_deleteReservationTime() {
        assertThatThrownBy(() -> themeService.deleteTheme(999L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
