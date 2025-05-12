package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.auth.domain.Role;
import roomescape.member.domain.Member;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.exception.ReservationTimeInUseException;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.request.ThemeCreateRequest;
import roomescape.theme.dto.response.ThemeResponse;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.FakeThemeRepository;
import roomescape.theme.repository.ThemeRepository;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {
    private final Member member = Member.of(1L, "Danny", "danny@example.com", "password", Role.MEMBER);
    private ThemeService themeService;
    @Mock
    private ReservationRepository reservationRepository;
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        themeRepository = new FakeThemeRepository(reservationRepository);
        themeService = new ThemeService(themeRepository, reservationRepository);
    }

    @Test
    void create_shouldReturnThemeResponse_whenSuccessful() {
        ThemeCreateRequest request = new ThemeCreateRequest("추리", "셜록 추리 게임", "image.png");

        ThemeResponse response = themeService.create(request);

        assertThat(response.name()).isEqualTo("추리");
        assertThat(response.description()).isEqualTo("셜록 추리 게임");
    }

    @Test
    void getThemes_shouldReturnAllThemes() {
        themeService.create(new ThemeCreateRequest("추리", "셜록 추리 게임", "image.png"));
        themeService.create(new ThemeCreateRequest("모험", "모험의 세계", "image2.png"));

        List<ThemeResponse> themes = themeService.getThemes();

        assertThat(themes).hasSize(2);
    }

    @Test
    void delete_shouldThrowException_whenThemeNotFound() {
        assertThatThrownBy(() -> themeService.delete(999L))
                .isInstanceOf(ThemeNotFoundException.class)
                .hasMessageContaining("요청한 id와 일치하는 테마 정보가 없습니다.");
    }

    @Test
    void delete_shouldThrowException_whenReservationExists() {
        Mockito.when(reservationRepository.existsByThemeId(1L)).thenReturn(true);

        assertThatThrownBy(() -> themeService.delete(1L))
                .isInstanceOf(ReservationTimeInUseException.class)
                .hasMessage("해당 테마에 대한 예약이 존재하여 삭제할 수 없습니다.");
    }

    @Test
    void delete_shouldRemoveTheme_whenNoReservationExists() {
        Theme theme = themeRepository.save(Theme.of(1L, "추리", "셜록 추리 게임", "image.png"));

        themeService.delete(theme.getId());

        List<ThemeResponse> themes = themeService.getThemes();
        assertThat(themes).isEmpty();
    }
}
