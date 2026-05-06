package roomescape.domain.theme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.theme.dto.AdminThemeResponse;
import roomescape.domain.theme.dto.CreateThemeRequest;
import roomescape.domain.theme.dto.CreateThemeResponse;
import roomescape.domain.theme.dto.ThemeResponse;

class ThemeServiceTest {

    @Test
    void 관리자용_테마_목록을_조회한다() {
        // given
        FakeThemeRepository themeRepository = new FakeThemeRepository();
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        themeRepository.findAllResult = List.of(
            Theme.of(1L, "미스터리", "이게 뭘까? 바로바로 추리 테마", "theme/mystery")
        );
        ThemeService themeService = new ThemeService(themeRepository,reservationRepository);

        // when
        List<AdminThemeResponse> responses = themeService.getAllThemeForAdmin();

        // then
        assertSoftly(softly -> {
            assertThat(responses.size()).isEqualTo(1);
            assertThat(responses.getFirst().id()).isEqualTo(1L);
            assertThat(responses.getFirst().name()).isEqualTo("미스터리");
            assertThat(responses.getFirst().content()).isEqualTo("이게 뭘까? 바로바로 추리 테마");
            assertThat(responses.getFirst().url()).isEqualTo("theme/mystery");
        });
    }

    @Test
    void 사용자용_테마_목록을_조회한다() {
        // given
        FakeThemeRepository themeRepository = new FakeThemeRepository();
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        themeRepository.findAllResult = List.of(
            Theme.of(1L, "미스터리", "이게 뭘까? 바로바로 추리 테마", "theme/mystery")
        );
        ThemeService themeService = new ThemeService(themeRepository, reservationRepository);

        // when
        List<ThemeResponse> responses = themeService.getAllTheme();

        // then
        assertSoftly(softly -> {
            assertThat(responses.size()).isEqualTo(1);
            assertThat(responses.getFirst().id()).isEqualTo(1L);
            assertThat(responses.getFirst().name()).isEqualTo("미스터리");
            assertThat(responses.getFirst().content()).isEqualTo("이게 뭘까? 바로바로 추리 테마");
            assertThat(responses.getFirst().url()).isEqualTo("theme/mystery");
        });
    }

    @Test
    void 테마를_생성한다() {
        // given
        FakeThemeRepository themeRepository = new FakeThemeRepository();
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        ThemeService themeService = new ThemeService(themeRepository, reservationRepository);

        // when
        CreateThemeResponse response = themeService.createTheme(
            new CreateThemeRequest("미스터리", "이게 뭘까? 바로바로 추리 테마", "theme/mystery")
        );

        // then
        assertSoftly(softly -> {
            assertThat(response.id()).isEqualTo(1L);
            assertThat(response.name()).isEqualTo("미스터리");
            assertThat(response.content()).isEqualTo("이게 뭘까? 바로바로 추리 테마");
            assertThat(response.url()).isEqualTo("theme/mystery");
            assertThat(themeRepository.savedTheme.getName()).isEqualTo("미스터리");
            assertThat(themeRepository.savedTheme.getContent()).isEqualTo("이게 뭘까? 바로바로 추리 테마");
            assertThat(themeRepository.savedTheme.getUrl()).isEqualTo("theme/mystery");
        });
    }

    @Test
    void 테마를_삭제한다() {
        // given
        FakeThemeRepository themeRepository = new FakeThemeRepository();
        FakeReservationRepository reservationRepository = new FakeReservationRepository();
        ThemeService themeService = new ThemeService(themeRepository, reservationRepository);

        // when
        themeService.deleteTheme(1L);

        // then
        assertThat(themeRepository.deletedId).isEqualTo(1L);
    }

    private static class FakeThemeRepository implements ThemeRepository {

        private List<Theme> findAllResult = List.of();
        private Theme savedTheme;
        private Long deletedId;

        @Override
        public List<Theme> findAll() {
            return findAllResult;
        }

        @Override
        public Optional<Theme> findById(Long id) {
            return findAllResult.stream().filter(theme -> theme.getId().equals(id)).findFirst();
        }

        @Override
        public Theme save(Theme theme) {
            savedTheme = theme;
            return Theme.of(1L, theme.getName(), theme.getContent(), theme.getUrl());
        }

        @Override
        public int deleteById(Long id) {
            deletedId = id;
            return 1;
        }
    }

    private static class FakeReservationRepository implements ReservationRepository{

        @Override
        public Reservation save(Reservation reservation) {
            return null;
        }

        @Override
        public List<Reservation> findAll() {
            return List.of();
        }

        @Override
        public int deleteById(Long id) {
            return 0;
        }

        @Override
        public int countByTimeId(Long timeId) {
            return 0;
        }

        @Override
        public int countByReservationDateId(Long dateId) {
            return 0;
        }

        @Override
        public List<Long> findReservedTimes(Long themeId, Long dateId) {
            return List.of();
        }

        @Override
        public List<Theme> findPopularThemes(int rankLimit, LocalDate startDay, LocalDate today) {
            return List.of();
        }
    }
}
