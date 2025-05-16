package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.entity.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.error.ReservationException;
import roomescape.stub.StubReservationRepository;
import roomescape.stub.StubThemeRepository;

class ThemeServiceTest {

    private final StubReservationRepository stubReservationRepository = new StubReservationRepository();
    private final StubThemeRepository stubThemeRepository = new StubThemeRepository();
    private final ThemeService sut = new ThemeService(stubThemeRepository, stubReservationRepository);

    @Test
    @DisplayName("테마가 저장된다")
    void saveTheme() {
        // given
        var request = new ThemeRequest("이름3", "설명3", "썸네일3");

        // when
        var response = sut.saveTheme(request);

        // then
        assertSoftly(soft -> {
            soft.assertThat(response.id()).isNotNull();
            soft.assertThat(response.name()).isEqualTo("이름3");
            soft.assertThat(response.description()).isEqualTo("설명3");
            soft.assertThat(response.thumbnail()).isEqualTo("썸네일3");
        });
    }

    @Test
    @DisplayName("모든 테마를 조회한다")
    void findAll() {
        // given
        stubThemeRepository.save(new Theme(1L, "이름", "설명", "썸네일"));

        // when
        var responses = sut.findAll();

        // then
        assertThat(responses).hasSize(1);
    }

    @Test
    @DisplayName("테마가 삭제된다")
    void delete() {
        // given
        var savedTheme = stubThemeRepository.save(new Theme(1L, "이름", "설명", "썸네일"));

        // when
        sut.delete(savedTheme.getId());
        var foundTheme = stubThemeRepository.findById(savedTheme.getId());

        // then
        assertThat(foundTheme).isEmpty();
    }

    @Test
    @DisplayName("예약이 존재하는 테마를 삭제하지 못 한다")
    void delete_reservation_exists() {
        // given
        stubReservationRepository.setExistsByThemeId(true);

        // when // then
        assertThatThrownBy(() -> sut.delete(1L))
                .isInstanceOf(ReservationException.class)
                .hasMessage("해당 테마로 예약된 건이 존재합니다.");
    }

    @Test
    @DisplayName("인기 테마를 조회한다")
    void findAllPopular() {
        // given
        var theme1 = sut.saveTheme(new ThemeRequest("이름1", "설명1", "썸네일1"));
        var theme2 = sut.saveTheme(new ThemeRequest("이름2", "설명2", "썸네일2"));

        stubThemeRepository.setPopularThemeIds(List.of(theme2.id(), theme1.id()));

        // when
        var popularThemes = sut.findAllPopular();

        // then
        assertSoftly(soft -> {
            soft.assertThat(popularThemes.get(0).name()).isEqualTo("이름2");
            soft.assertThat(popularThemes.get(1).name()).isEqualTo("이름1");
        });
    }
}
