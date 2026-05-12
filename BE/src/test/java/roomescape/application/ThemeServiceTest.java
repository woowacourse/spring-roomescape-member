package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.PopularThemeRepository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.fake.FakePopularThemeRepository;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeThemeRepository;
import roomescape.global.exception.BusinessException;

class ThemeServiceTest {

    private static final String TEST_THEMA_NAME = "테스트 테마";
    private static final String TEST_THEMA_DESCRIPTION = "테스트 테마 설명";
    private static final String TEST_THEMA_THUMBNAIL = "https://good.com/thumb-nail";

    private List<Reservation> reservations;
    private ThemeRepository themeRepository;
    private PopularThemeRepository popularThemeRepository;
    private ThemeService themeService;
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        reservations = new ArrayList<>();
        themeRepository = new FakeThemeRepository();
        popularThemeRepository = new FakePopularThemeRepository(
                reservations,
                themeRepository.findAll()
        );
        reservationRepository = new FakeReservationRepository();
        themeService = new ThemeService(themeRepository, popularThemeRepository, reservationRepository);
    }

    @Test
    @DisplayName("저장 시도 시 오류가 발생하지 않는다")
    void save_success() {
        // when
        Theme theme = themeService.save(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);

        // then
        assertThat(theme.getId()).isNotNull();
        assertThat(theme.getName()).isEqualTo(TEST_THEMA_NAME);
        assertThat(theme.getDescription()).isEqualTo(TEST_THEMA_DESCRIPTION);
        assertThat(theme.getThumbnailUrl()).isEqualTo(TEST_THEMA_THUMBNAIL);
        assertThat(themeRepository.findById(theme.getId())).contains(theme);
    }

    @Test
    @DisplayName("아이디 기반으로 조회 시 오류가 발생하지 않음.")
    void findById_success() {
        // given
        Theme saved = themeService.save(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);

        // when
        var theme = themeService.findById(saved.getId());

        // then
        assertThat(theme).contains(saved);
    }

    @Test
    @DisplayName("전부 다 잘 찾아오는 것 시도 시 오류 발생 안함.")
    void findAll_success() {
        // given
        Theme saved = themeService.save(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);
        Theme saved2 = themeService.save(TEST_THEMA_NAME+"2", TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);

        // when
        List<Theme> themes = themeService.findAll();

        // then
        assertThat(themes).containsExactly(saved, saved2);
    }

    @Test
    @DisplayName("아이디 기반 삭제 시도 시 오류 발생 안함")
    void deleteById_success() {
        // given
        Theme saved = themeService.save(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);
        Long deleteTagetId = saved.getId();

        // when
        themeService.deleteById(deleteTagetId);

        // then
        assertThat(themeRepository.findById(deleteTagetId)).isEmpty();
    }

    @Test
    @DisplayName("정렬 기준으로 테마들을 조회 시 오류 발생 안함")
    void findTopNByPeriod_success() {
        // given
        Theme firstTheme = themeRepository.save(Theme.create("인기 테마", "설명", "https://good.com/thumb-nail/1"));
        Theme secondTheme = themeRepository.save(Theme.create("보통 테마", "설명", "https://good.com/thumb-nail/2"));
        LocalDate today = LocalDate.now();
        reservations.add(Reservation.create("테스터1", today, null, firstTheme));
        reservations.add(Reservation.create("테스터2", today, null, firstTheme));
        reservations.add(Reservation.create("테스터3", today, null, secondTheme));
        popularThemeRepository = new FakePopularThemeRepository(reservations, themeRepository.findAll());
        themeService = new ThemeService(themeRepository, popularThemeRepository, reservationRepository);

        // when
        List<Theme> themes = themeService.findTopNByPeriod(
                today.minusDays(1),
                today,
                "POPULAR",
                2L
        );

        // then
        assertThat(themes).containsExactly(firstTheme, secondTheme);
    }

    @Test
    @DisplayName("예약 테마 id가 참조되고 있으면 삭제할 때 예외가 발생한다")
    void deleteTimeWithReferencedReservationTime() {
        // given
        ReservationTime time = ReservationTime.create(LocalTime.now().plusHours(1));
        Theme theme = themeRepository.save(Theme.create("인기 테마", "설명", "https://good.com/thumb-nail/1"));
        reservationRepository.save(Reservation.create("테스터", LocalDate.now(), time, theme));

        // when & then
        assertThatThrownBy(() -> themeService.deleteById(theme.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("예약이 연결된 테마는 삭제할 수 없습니다.");
        assertThat(themeRepository.findById(theme.getId())).contains(theme);
    }
}
