package roomescape.service.theme;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.controller.theme.CreateThemeRequest;
import roomescape.controller.theme.PopularThemeRequest;
import roomescape.controller.theme.PopularThemeResponse;
import roomescape.controller.theme.ThemeResponse;
import roomescape.domain.*;
import roomescape.repository.*;
import roomescape.repository.exception.ThemeNotFoundException;
import roomescape.service.theme.exception.DaysLimitException;
import roomescape.service.theme.exception.RowsLimitException;
import roomescape.service.theme.exception.ThemeUsedException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import({
        ThemeService.class,
        H2ReservationRepository.class,
        H2ReservationTimeRepository.class,
        H2ThemeRepository.class,
        H2MemberRepository.class})
class ThemeServiceTest {

    final List<CreateThemeRequest> sampleThemes = List.of(
            new CreateThemeRequest("Theme 1", "Desc 1", "Thumb 1"),
            new CreateThemeRequest("Theme 2", "Desc 2", "Thumb 2"),
            new CreateThemeRequest("Theme 3", "Desc 3", "Thumb 3"),
            new CreateThemeRequest("Theme 4", "Desc 4", "Thumb 4")
    );

    @Autowired
    ThemeService themeService;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ReservationTimeRepository reservationTimeRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("테마 목록을 조회한다.")
    void getThemes() {
        // given
        List<ThemeResponse> expected = sampleThemes.stream()
                .map(themeService::addTheme)
                .toList();

        // when
        List<ThemeResponse> actual = themeService.getThemes();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("테마를 추가한다.")
    void addTheme() {
        // given
        CreateThemeRequest themeRequest = sampleThemes.get(0);

        // when
        ThemeResponse actual = themeService.addTheme(themeRequest);
        ThemeResponse expected = new ThemeResponse(
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
        Long id = themeService.addTheme(sampleThemes.get(0)).id();

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
        ThemeResponse themeResponse = themeService.addTheme(sampleThemes.get(0));
        Long themeId = themeResponse.id();
        Member member = new Member(null, "User", "user@test.com", "user", Role.USER);
        ReservationTime time = new ReservationTime(null, "08:00");
        Reservation reservation = new Reservation(
                null,
                LocalDate.now().plusDays(1),
                null,
                new Theme(themeId),
                null
        );

        ReservationTime saveTime = reservationTimeRepository.save(time);
        Member savedMember = memberRepository.save(member);
        Reservation assignedReservation = reservation
                .assignTime(saveTime)
                .assignMember(savedMember);
        reservationRepository.save(assignedReservation);

        // when & then
        assertThatThrownBy(() -> themeService.deleteTheme(themeId))
                .isInstanceOf(ThemeUsedException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"1,1", "1,2", "1,3", "2,1", "2,2", "2,3", "3,1", "3,2", "3,3"}, delimiter = ',')
    @DisplayName("인기 테마를 조회한다.")
    void getPopularThemes(int days, int limit) {
        // given
        List<Reservation> reservations = generateAndSaveReservations();
        LocalDate from = LocalDate.now().minusDays(days);
        PopularThemeRequest request = new PopularThemeRequest(days, limit);

        // when
        List<PopularThemeResponse> actual = themeService.getPopularThemes(request);
        Map<PopularThemeResponse, Long> countByPopularTheme = reservations.stream()
                .filter(r -> r.getDate().isAfter(from) || r.getDate().isEqual(from))
                .map(r -> PopularThemeResponse.from(r.getTheme()))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        List<Long> expectedDescendingCounts = actual.stream()
                .map(countByPopularTheme::get)
                .toList();

        System.out.println("actual = " + actual);
        System.out.println("countByPopularTheme = " + countByPopularTheme);
        System.out.println("expectedDescendingCounts = " + expectedDescendingCounts);

        // then
        assertThat(actual.size()).isLessThanOrEqualTo(limit);
        assertThat(expectedDescendingCounts).isSortedAccordingTo((a, b) -> Math.toIntExact(b - a));
    }

    private List<Reservation> generateAndSaveReservations() {
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(null, "08:00"));
        Member member = memberRepository.save(
                new Member(null, "User", "user@test.com", "user", Role.USER)
        );
        List<Theme> themes = Stream.of(
                        new CreateThemeRequest("N1", "D1", "T1"),
                        new CreateThemeRequest("N2", "D2", "T2"),
                        new CreateThemeRequest("N3", "D3", "T3"),
                        new CreateThemeRequest("N4", "D4", "T4")
                )
                .map(themeService::addTheme)
                .map(res -> new Theme(res.id(), res.name(), res.description(), res.thumbnail()))
                .toList();

        LocalDate threeDaysAgo = LocalDate.now().minusDays(3);
        LocalDate twoDaysAgo = LocalDate.now().minusDays(2);
        LocalDate yesterday = LocalDate.now().minusDays(1);

        List<Reservation> reservations = List.of(
                new Reservation(null, threeDaysAgo, time, themes.get(0), member),
                new Reservation(null, threeDaysAgo, time, themes.get(1), member),
                new Reservation(null, threeDaysAgo, time, themes.get(3), member),

                new Reservation(null, twoDaysAgo, time, themes.get(1), member),
                new Reservation(null, twoDaysAgo, time, themes.get(2), member),
                new Reservation(null, twoDaysAgo, time, themes.get(3), member),

                new Reservation(null, yesterday, time, themes.get(2), member),
                new Reservation(null, yesterday, time, themes.get(3), member)
        );

        return reservations.stream()
                .map(reservationRepository::save)
                .toList();
    }

    @Test
    @DisplayName("초과된 날짜 수로 인기 테마를 조회할 경우 예외가 발생한다.")
    void exceptionOnGettingPopularThemesExceedDays() {
        assertThatThrownBy(() ->
                getPopularThemes(31, 10))
                .isInstanceOf(DaysLimitException.class);
    }

    @Test
    @DisplayName("초과된 테마 개수로 인기 테마를 조회할 경우 예외가 발생한다.")
    void exceptionOnGettingPopularThemesExceedRows() {
        assertThatThrownBy(() ->
                getPopularThemes(7, 101))
                .isInstanceOf(RowsLimitException.class);
    }
}
