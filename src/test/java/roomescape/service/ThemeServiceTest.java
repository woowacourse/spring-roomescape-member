package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.controller.theme.CreateThemeRequest;
import roomescape.controller.theme.CreateThemeResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReserveName;
import roomescape.domain.Theme;
import roomescape.repository.H2ReservationRepository;
import roomescape.repository.H2ReservationTimeRepository;
import roomescape.repository.H2ThemeRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.exception.ThemeNotFoundException;
import roomescape.service.exception.ThemeUsedException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import({
        ThemeService.class,
        H2ReservationRepository.class,
        H2ReservationTimeRepository.class,
        H2ThemeRepository.class
})
class ThemeServiceTest {

    final List<CreateThemeRequest> sampleThemes = IntStream.range(1, 9)
            .mapToObj(i -> new CreateThemeRequest(
                    "Theme " + i,
                    "Description " + i,
                    "Thumbnail " + i
            )).toList();

    @Autowired
    ThemeService themeService;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("테마 목록을 조회한다.")
    void getThemes() {
        // given
        final List<CreateThemeResponse> expected = sampleThemes.stream()
                .map(themeService::addTheme)
                .toList();

        // when
        final List<CreateThemeResponse> actual = themeService.getThemes();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("테마를 추가한다.")
    void addTheme() {
        // given
        final CreateThemeRequest themeRequest = sampleThemes.get(0);

        // when
        final CreateThemeResponse actual = themeService.addTheme(themeRequest);
        final CreateThemeResponse expected = new CreateThemeResponse(
                actual.id(),
                themeRequest.name(),
                themeRequest.description(),
                themeRequest.thumbnail()
        );

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("테마를 삭제한다.")
    void deleteThemePresent() {
        // given
        final Long id = themeService.addTheme(sampleThemes.get(0)).id();

        // when & then
        assertThat(themeService.deleteTheme(id)).isOne();
    }

    @Test
    @DisplayName("존재하지 않는 테마를 삭제할 경우 예외가 발생한다.")
    void deleteThemeNotExist() {
        assertThatThrownBy(() -> themeService.deleteTheme(1L))
                .isInstanceOf(ThemeNotFoundException.class);
    }

    @Test
    @DisplayName("예약이 있는 테마를 삭제할 경우 예외가 발생한다.")
    void exceptionOnDeletingThemeAlreadyReserved() {
        final CreateThemeResponse themeResponse = themeService.addTheme(sampleThemes.get(0));
        final Long themeId = themeResponse.id();
        final ReservationTime time = new ReservationTime(null, "08:00");
        final Reservation reservation = new Reservation(
                null,
                new ReserveName("User 1"),
                LocalDate.now().plusDays(1),
                null,
                new Theme(themeId)
        );

        final ReservationTime saveTime = reservationTimeRepository.save(time);
        final Reservation assignedReservation = reservation.assignTime(saveTime);
        reservationRepository.save(assignedReservation);

        // when & then
        assertThatThrownBy(() -> themeService.deleteTheme(themeId))
                .isInstanceOf(ThemeUsedException.class);
    }
}
