package roomescape.theme.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.exception.DuplicateResourceException;
import roomescape.theme.controller.dto.ThemeRequest;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    private ThemeService themeService;
    private Clock fixedClock;

    @BeforeEach
    void setUp() {
        fixedClock = Clock.fixed(
                Instant.parse("2026-05-07T10:00:00Z"),
                ZoneId.of("UTC")
        );
        themeService = new ThemeService(themeRepository, fixedClock);
    }

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("새로운 테마 이름을 입력받으면 성공적으로 저장한다.")
        void saveSuccess() {
            // given
            ThemeRequest request = new ThemeRequest("공포의 수랏간", "매우 무섭습니다.", "https://example.com/image.png");
            given(themeRepository.existsByName(request.name())).willReturn(false);

            Theme savedTheme = new Theme(1L, request.name(), request.description(), request.thumbnailUrl());
            given(themeRepository.save(any(Theme.class))).willReturn(savedTheme);

            // when
            Theme result = themeService.save(request);

            // then
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("공포의 수랏간");
            verify(themeRepository, times(1)).save(any(Theme.class));
        }

        @Test
        @DisplayName("이미 존재하는 테마 이름이면 DuplicateResourceException이 발생한다.")
        void saveFailByDuplicateName() {
            // given
            ThemeRequest request = new ThemeRequest("중복 이름", "설명", "url");
            given(themeRepository.existsByName(request.name())).willReturn(true);

            // when & then
            assertThatThrownBy(() -> themeService.save(request))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("이미 존재하는 테마 이름입니다.");

            verify(themeRepository, never()).save(any(Theme.class));
        }
    }

    @Nested
    @DisplayName("findPopularThemes 메서드는")
    class FindPopularThemes {

        @Test
        @DisplayName("현재 날짜 기준으로 7일 전부터 오늘까지의 인기 테마를 조회한다.")
        void findPopularThemesCalculatesCorrectDate() {
            // given
            LocalDate endDate = LocalDate.now(fixedClock);
            LocalDate startDate = endDate.minusDays(7);
            int limit = 10;

            Theme popularTheme = new Theme(1L, "인기 테마", "설명", "url");
            given(themeRepository.findPopularThemes(startDate, endDate, limit))
                    .willReturn(List.of(popularTheme));

            // when
            List<Theme> result = themeService.findPopularThemes();

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("인기 테마");
            verify(themeRepository).findPopularThemes(startDate, endDate, limit);
        }
    }

    @Nested
    @DisplayName("getById 메서드는")
    class GetById {

        @Test
        @DisplayName("존재하는 ID로 조회하면 테마를 반환한다.")
        void getByIdSuccess() {
            // given
            Long id = 1L;
            Theme theme = new Theme(id, "테마", "설명", "url");
            given(themeRepository.findById(id)).willReturn(Optional.of(theme));

            // when
            Theme result = themeService.getById(id);

            // then
            assertThat(result.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 IllegalArgumentException이 발생한다.")
        void getByIdFail() {
            // given
            Long id = 999L;
            given(themeRepository.findById(id)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> themeService.getById(id))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("해당 ID의 테마가 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteById {

        @Test
        @DisplayName("ID를 전달받아 레포지토리에 삭제를 위임한다.")
        void deleteByIdDelegatesToRepository() {
            // when
            themeService.deleteById(1L);

            // then
            verify(themeRepository, times(1)).deleteById(1L);
        }
    }
}
