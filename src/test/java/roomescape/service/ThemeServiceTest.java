package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.DuplicateEntityException;
import roomescape.repository.ThemeRepository;
import roomescape.repository.fake.FakeThemeRepository;
import roomescape.web.dto.theme.ThemeRequest;
import roomescape.web.dto.theme.ThemeResponse;

class ThemeServiceTest {

    private ThemeRepository themeRepository;
    private ThemeService themeService;

    @BeforeEach
    void setUp() {
        this.themeRepository = new FakeThemeRepository();
        this.themeService = new ThemeService(themeRepository);
    }

    @Test
    void 새로운_테마를_정상적으로_등록한다() {
        // given
        ThemeRequest request = new ThemeRequest("공포테마", "무서운 테마입니다.", "http://image.png");

        // when
        ThemeResponse response = themeService.register(request);

        // then
        assertThat(response).extracting(ThemeResponse::id, ThemeResponse::name, ThemeResponse::description,
                ThemeResponse::thumbnailImageUrl).containsExactly(1L, "공포테마", "무서운 테마입니다.", "http://image.png");
    }

    @Test
    void 이미_존재하는_이름으로_테마_등록을_시도하면_예외가_발생한다() {
        // given
        themeService.register(new ThemeRequest("공포테마", "설명", "http://image.png"));

        ThemeRequest duplicateRequest = new ThemeRequest("공포테마", "다른 설명", "http://image2.png");

        // when & then
        assertThatThrownBy(() -> themeService.register(duplicateRequest)).isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("이미 존재하는 테마입니다. 테마 명: 공포테마");
    }

    @Test
    void 테마_식별자로_테마를_삭제할_수_있다() {
        // given
        ThemeRequest request = new ThemeRequest("공포테마", "설명", "http://image.png");
        ThemeResponse response = themeService.register(request);

        // when
        themeService.remove(response.id());

        // then
        assertThatCode(() -> themeService.register(request)).doesNotThrowAnyException();
    }

    @Test
    void 활성_테마_목록을_페이징_조건에_맞게_조회한다() {
        // given
        ThemeResponse first = themeService.register(new ThemeRequest("첫번째", "첫번째 설명", "http://image1.png"));
        ThemeResponse second = themeService.register(new ThemeRequest("두번째", "두번째 설명", "http://image2.png"));
        ThemeResponse inactive = themeService.register(new ThemeRequest("세번째", "세번째 설명", "http://image3.png"));
        themeService.remove(inactive.id());

        // when
        List<ThemeResponse> responses = themeService.getAllActiveThemesByPaging(0, 10);

        // then
        assertThat(responses).extracting(ThemeResponse::id).containsExactly(first.id(), second.id());
    }

    @Test
    void 인기_테마_목록을_조회_개수만큼_조회한다() {
        // given
        ThemeResponse first = themeService.register(new ThemeRequest("첫번째", "첫번째 설명", "http://image1.png"));
        ThemeResponse second = themeService.register(new ThemeRequest("두번째", "두번째 설명", "http://image2.png"));
        ThemeResponse inactive = themeService.register(new ThemeRequest("세번째", "세번째 설명", "http://image3.png"));
        themeService.remove(inactive.id());

        // when
        List<ThemeResponse> responses = themeService.getPopularThemes(LocalDate.now().minusDays(7), LocalDate.now(), 1);

        // then
        assertThat(responses).extracting(ThemeResponse::id).containsExactly(first.id());
        assertThat(responses).extracting(ThemeResponse::id).doesNotContain(second.id(), inactive.id());
    }

}
