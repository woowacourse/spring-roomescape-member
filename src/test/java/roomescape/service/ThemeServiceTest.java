package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.test.fixture.ReservationTimeFixture.addReservationTimeInRepository;
import static roomescape.test.fixture.ThemeFixture.addThemeInRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeCreationRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.test.fake.FakeH2ReservationRepository;
import roomescape.test.fake.FakeH2ReservationTimeRepository;
import roomescape.test.fake.FakeH2ThemeRepository;

class ThemeServiceTest {

    private final ReservationRepository reservationRepository = new FakeH2ReservationRepository();
    private final ReservationTimeRepository reservationTimeRepository = new FakeH2ReservationTimeRepository();
    private final ThemeRepository themeRepository = new FakeH2ThemeRepository();
    private final ThemeService themeService = new ThemeService(reservationRepository, themeRepository);

    @DisplayName("테마를 추가할 수 있다.")
    @Test
    void canAddTheme() {
        ThemeCreationRequest request = new ThemeCreationRequest("theme", "description", "url");

        long savedId = themeService.addTheme(request);

        Theme savedTheme = themeRepository.findById(savedId).get();
        Theme expectedTheme = new Theme(1L, request.name(), request.description(), request.thumbnail());
        assertThat(savedTheme).isEqualTo(expectedTheme);
    }

    @DisplayName("모든 테마를 조회할 수 있다.")
    @Test
    void canFindAllTheme() {
        themeRepository.add(Theme.createWithoutId("theme1", "설명", "섬네일"));
        themeRepository.add(Theme.createWithoutId("theme2", "설명", "섬네일"));
        themeRepository.add(Theme.createWithoutId("theme3", "설명", "섬네일"));

        List<ThemeResponse> themes = themeService.findAllTheme();
        assertThat(themes).hasSize(3);
    }

    @DisplayName("ID를 통해 테마를 조회할 수 있다.")
    @Test
    void canFindThemeById() {
        long id = themeRepository.add(Theme.createWithoutId("theme", "description", "url"));

        ThemeResponse response = themeService.findThemeById(id);
        Theme actualTheme = new Theme(response.id(), response.name(), response.description(), response.thumbnail());
        Theme expectedTheme = new Theme(id, "theme", "description", "url");
        assertThat(actualTheme).isEqualTo(expectedTheme);
    }

    @DisplayName("ID를 통해 테마를 삭제할 수 있다.")
    @Test
    void canDeleteThemeById() {
        long id = themeRepository.add(Theme.createWithoutId("이름", "설명", "썸네일"));

        themeService.deleteThemeById(id);
        List<ThemeResponse> themes = themeService.findAllTheme();
        assertThat(themes).hasSize(0);
    }

    @DisplayName("삭제할 테마가 존재하지 않는 경우 예외를 발생시킨다.")
    @Test
    void cannotDeleteThemeWhenEmptyTheme() {
        assertThatThrownBy(() -> themeService.deleteThemeById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] ID에 해당하는 테마가 존재하지 않습니다.");
    }

    @DisplayName("예약이 존재하는 경우 테마를 삭제할 수 있다.")
    @Test
    void cannotDeleteThemeWhenReservationExist() {
        ReservationTime reservationTime = addReservationTimeInRepository(reservationTimeRepository, LocalTime.now());
        Theme theme = addThemeInRepository(themeRepository, "이름", "설명", "썸네일");
        Member member = new Member(1L, "사람", "email", "비번", "member");
        reservationRepository.add(
                Reservation.createWithoutId(member, LocalDate.now().plusDays(1), reservationTime, theme));

        assertThatThrownBy(() -> themeService.deleteThemeById(theme.getId()))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 예약이 이미 존재하는 테마를 제거할 수 없습니다.");
    }
}