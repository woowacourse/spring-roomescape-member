package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.DuplicateException;
import roomescape.exception.NotFoundException;
import roomescape.fake.FakeThemeDao;
import roomescape.presentation.dto.ThemeRequest;
import roomescape.presentation.dto.ThemeResponse;

class ThemeServiceTest {

    private ThemeService themeService;

    @BeforeEach
    void setUp() {
        themeService = new ThemeService(new FakeThemeDao());
    }

    @Test
    @DisplayName("테마 요청 객체로 테마를 저장한다")
    void insert() {
        // given
        final ThemeRequest themeRequest = new ThemeRequest("테마", "소개", "썸네일");

        // when
        final ThemeResponse themeResponse = themeService.insert(themeRequest);

        // then
        assertAll(
                () -> assertThat(themeResponse.name()).isEqualTo(themeRequest.name()),
                () -> assertThat(themeResponse.description()).isEqualTo(themeRequest.description()),
                () -> assertThat(themeResponse.thumbnail()).isEqualTo(themeRequest.thumbnail())
        );
    }

    @Test
    @DisplayName("저장하려는 테마의 이름과 동일한 이름이 이미 존재한다면 예외가 발생한다")
    void insertWhenNameIsDuplicate() {
        // given
        final ThemeRequest themeRequest = new ThemeRequest("테마", "소개", "썸네일");
        themeService.insert(themeRequest);

        // when & then
        assertThatThrownBy(() -> themeService.insert(themeRequest))
                .isInstanceOf(DuplicateException.class);
    }

    @Test
    @DisplayName("모든 테마를 조회한다")
    void findAll() {
        // given
        final ThemeRequest themeRequest1 = new ThemeRequest("테마", "소개", "썸네일");
        final ThemeRequest themeRequest2 = new ThemeRequest("테마2", "소개2", "썸네일2");
        themeService.insert(themeRequest1);
        themeService.insert(themeRequest2);

        // when
        final List<ThemeResponse> themeResponses = themeService.findAll();

        // then
        assertThat(themeResponses).hasSize(2);
    }

    @Test
    @DisplayName("id를 통해 방탈출 예약 시간을 조회한다")
    void findByIdById() {
        // given
        final ThemeRequest themeRequest = new ThemeRequest("테마", "소개", "썸네일");
        final ThemeResponse themeResponse = themeService.insert(themeRequest);
        final Long id = themeResponse.id();

        // when
        final ThemeResponse findThemeResponse = themeService.findById(id);

        // then
        assertAll(
                () -> assertThat(findThemeResponse.id()).isEqualTo(id),
                () -> assertThat(findThemeResponse.name()).isEqualTo(themeRequest.name()),
                () -> assertThat(findThemeResponse.description()).isEqualTo(themeRequest.description()),
                () -> assertThat(findThemeResponse.thumbnail()).isEqualTo(themeRequest.thumbnail())
        );
    }

    @Test
    @DisplayName("id를 통해 테마를 조회할 때 대상이 없다면 예외가 발생한다")
    void findByIdWhenNotExists() {
        // given
        final Long notExistsId = 999L;

        // when & then
        assertThatThrownBy(() -> themeService.findById(notExistsId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("id를 통해 테마를 삭제한다")
    void deleteById() {
        // given
        final ThemeRequest themeRequest = new ThemeRequest("테마", "소개", "썸네일");
        final ThemeResponse themeResponse = themeService.insert(themeRequest);

        // when
        themeService.deleteById(themeResponse.id());

        // then
        final List<ThemeResponse> themeResponses = themeService.findAll();
        assertThat(themeResponses).isEmpty();
    }

    @Test
    @DisplayName("id를 통해 테마를 삭제할 때 대상이 없다면 예외가 발생한다")
    void deleteByIdWhenNotExists() {
        // given
        final Long notExistsId = 999L;

        // when & then
        assertThatThrownBy(() -> themeService.deleteById(notExistsId))
                .isInstanceOf(NotFoundException.class);
    }
}
