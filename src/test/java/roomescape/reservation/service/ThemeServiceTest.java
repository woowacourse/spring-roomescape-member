package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.dao.FakeReservationDao;
import roomescape.reservation.dao.FakeThemeDao;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ThemeRepository;
import roomescape.reservation.dto.ThemeRequest;
import roomescape.reservation.dto.ThemeResponse;

@DisplayName("테마 로직 테스트")
class ThemeServiceTest {
    ReservationRepository reservationRepository;
    ThemeRepository themeRepository;
    ThemeService themeService;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationDao();
        themeRepository = new FakeThemeDao(reservationRepository);
        themeService = new ThemeService(themeRepository);
    }

    @DisplayName("테마 조회에 성공한다.")
    @Test
    void findAll() {
        //given
        long id = 1;
        String name = "name";
        String description = "description";
        String thumbnail = "thumbnail";
        Theme theme = new Theme(id, name, description, thumbnail);
        themeRepository.save(theme);
        //when
        List<ThemeResponse> themes = themeService.findAllThemes();

        //then
        assertAll(
                () -> assertThat(themes).hasSize(1),
                () -> assertThat(themes.get(0).name()).isEqualTo(name),
                () -> assertThat(themes.get(0).description()).isEqualTo(description),
                () -> assertThat(themes.get(0).thumbnail()).isEqualTo(thumbnail)
        );
    }

    @DisplayName("테마 생성에 성공한다.")
    @Test
    void create() {
        //given
        String name = "name";
        String description = "description";
        String thumbnail = "thumbnail";
        ThemeRequest themeRequest = new ThemeRequest(name, description, thumbnail);

        //when
        ThemeResponse themeResponse = themeService.create(themeRequest);

        //then
        assertAll(
                () -> assertThat(themeResponse.name()).isEqualTo(name),
                () -> assertThat(themeResponse.thumbnail()).isEqualTo(thumbnail),
                () -> assertThat(themeResponse.description()).isEqualTo(description)
        );
    }

    @DisplayName("테마 삭제에 성공한다.")
    @Test
    void delete() {
        //given
        long id = 1;
        String name = "name";
        String description = "description";
        String thumbnail = "thumbnail";
        Theme theme = new Theme(id, name, description, thumbnail);
        themeRepository.save(theme);

        //when
        themeService.delete(id);

        //then
        assertThat(themeRepository.findAll()).hasSize(0);
    }
}
