package roomescape.service;

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
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.repository.H2ReservationRepository;
import roomescape.repository.H2ReservationTimeRepository;
import roomescape.repository.H2ThemeRepository;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.exception.ThemeNotFoundException;
import roomescape.service.theme.ThemeService;
import roomescape.service.theme.exception.DaysLimitException;
import roomescape.service.theme.exception.RowsLimitException;
import roomescape.service.theme.exception.ThemeUsedException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("테마 목록을 조회한다.")
    void getThemes() {
        // given
        final List<ThemeResponse> expected = sampleThemes.stream()
                .map(themeService::addTheme)
                .toList();

        // when
        final List<ThemeResponse> actual = themeService.getThemes();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("테마를 추가한다.")
    void addTheme() {
        // given
        final CreateThemeRequest themeRequest = sampleThemes.get(0);

        // when
        final ThemeResponse actual = themeService.addTheme(themeRequest);
        final ThemeResponse expected = new ThemeResponse(
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
        final ThemeResponse themeResponse = themeService.addTheme(sampleThemes.get(0));
        final Long themeId = themeResponse.id();
        final Member member = new Member(null, "a@b.c", "pw", "User", Role.USER);
        final ReservationTime time = new ReservationTime(null, "08:00");
        final Reservation reservation = new Reservation(
                null,
                LocalDate.now().plusDays(1),
                null,
                new Theme(themeId),
                null
        );

        final ReservationTime saveTime = reservationTimeRepository.save(time);
        final Member savedMember = memberRepository.save(member);
        final Reservation assignedReservation = reservation
                .assignTime(saveTime)
                .assignMember(savedMember);
        reservationRepository.save(assignedReservation);

        // when & then
        assertThatThrownBy(() -> themeService.deleteTheme(themeId))
                .isInstanceOf(ThemeUsedException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"7:10"}, delimiter = ':')
    @DisplayName("인기 테마를 조회한다.")
    void getPopularThemes(final int days, final int limit) {
        // given
        final ReservationTime time = reservationTimeRepository.save(new ReservationTime(null, "08:00"));
        final Member member = new Member(null, "a@b.c", "pw", "User", Role.USER);
        final List<ThemeResponse> themes = sampleThemes.stream()
                .map(themeService::addTheme)
                .toList();

        final List<Reservation> reservations = new ArrayList<>();
        createRandomReservations(days, themes, time, member, reservations);

        final LocalDate from = LocalDate.now().minusDays(days);
        final PopularThemeRequest request = new PopularThemeRequest(days, limit);

        // when
        final List<PopularThemeResponse> actual = themeService.getPopularThemes(request);
        final List<PopularThemeResponse> themesContainExpected = reservations.stream()
                .filter(r -> r.getDate().isAfter(from))
                .map(this::createPopularThemeResponseFromReservation)
                .distinct()
                .toList();

        // then
        assertThat(actual.size()).isLessThanOrEqualTo(limit);
        assertThat(themesContainExpected.containsAll(actual)).isTrue();
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

    private void createRandomReservations(
            final int days,
            final List<ThemeResponse> themes,
            final ReservationTime time,
            final Member member,
            final List<Reservation> reservations) {
        final Random random = new Random();
        for (int day = 1; day < days * 2; day++) {
            final LocalDate date = LocalDate.now().minusDays(day);
            for (final ThemeResponse theme : themes) {
                if (random.nextBoolean()) {
                    final Reservation reservation = new Reservation(
                            null,
                            date,
                            time,
                            new Theme(theme.id(), theme.name(), theme.description(), theme.thumbnail()),
                            member
                    );
                    reservations.add(reservationRepository.save(reservation));
                }
            }
        }
    }

    private PopularThemeResponse createPopularThemeResponseFromReservation(final Reservation reservation) {
        return new PopularThemeResponse(
                reservation.getTheme().getName(),
                reservation.getTheme().getThumbnail(),
                reservation.getTheme().getDescription()
        );
    }
}
