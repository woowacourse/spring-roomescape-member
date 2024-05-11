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
import roomescape.domain.*;
import roomescape.repository.*;
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
        H2ThemeRepository.class,
        H2MemberRepository.class})
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
        Member member = new Member(null, "User", "a@b.c", "pw", Role.USER);
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
    @CsvSource(value = {"7:10"}, delimiter = ':')
    @DisplayName("인기 테마를 조회한다.")
    void getPopularThemes(int days, int limit) {
        // given
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(null, "08:00"));
        Member member = memberRepository.save(
                new Member(null, "User", "a@b.c", "pw", Role.USER)
        );
        List<ThemeResponse> themes = sampleThemes.stream()
                .map(themeService::addTheme)
                .toList();

        List<Reservation> reservations = new ArrayList<>();
        createRandomReservations(days, themes, time, member, reservations);

        LocalDate from = LocalDate.now().minusDays(days);
        PopularThemeRequest request = new PopularThemeRequest(days, limit);

        // when
        List<PopularThemeResponse> actual = themeService.getPopularThemes(request);
        List<PopularThemeResponse> themesContainExpected = reservations.stream()
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
            int days,
            List<ThemeResponse> themes,
            ReservationTime time,
            Member member,
            List<Reservation> reservations) {
        Random random = new Random();
        for (int day = 1; day < days * 2; day++) {
            LocalDate date = LocalDate.now().minusDays(day);
            for (ThemeResponse theme : themes) {
                if (random.nextBoolean()) {
                    Reservation reservation = new Reservation(
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

    private PopularThemeResponse createPopularThemeResponseFromReservation(Reservation reservation) {
        return new PopularThemeResponse(
                reservation.getTheme().getName(),
                reservation.getTheme().getThumbnail(),
                reservation.getTheme().getDescription()
        );
    }
}
