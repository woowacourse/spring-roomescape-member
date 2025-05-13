package roomescape.reservation.application.service;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.application.dto.CreateReservationRequest;
import roomescape.reservation.application.repository.ReservationRepository;
import roomescape.reservation.application.repository.ReservationTimeRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.infrastructure.fake.FakeReservationDao;
import roomescape.reservation.infrastructure.fake.FakeReservationTimeDao;
import roomescape.reservation.infrastructure.fake.FakeThemeDao;
import roomescape.reservation.presentation.dto.ThemeRequest;
import roomescape.reservation.presentation.dto.ThemeResponse;

public class ThemeServiceTest {
    private FakeThemeDao themeRepository;
    private ThemeService themeService;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void init() {
        themeRepository = new FakeThemeDao();
        reservationRepository = new FakeReservationDao();
        reservationTimeRepository = new FakeReservationTimeDao();
        themeService = new ThemeService(reservationRepository, themeRepository);
    }

    @Test
    @DisplayName("테마 추가 테스트")
    void createThemeTest() {
        // given
        ThemeRequest themeRequest = new ThemeRequest(
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );

        // when
        ThemeResponse theme = themeService.createTheme(themeRequest);

        // then
        assertThat(theme.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("테마 전체 조회 테스트")
    void getThemesTest() {
        // given
        ThemeRequest themeRequest = new ThemeRequest(
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );
        themeService.createTheme(themeRequest);

        // when
        List<ThemeResponse> themes = themeService.getThemes();

        // then
        assertThat(themes).hasSize(1);
    }

    @Test
    @DisplayName("테마 삭제 테스트")
    void deleteThemeTest() {
        // given
        ThemeRequest themeRequest = new ThemeRequest(
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );
        themeService.createTheme(themeRequest);

        // when
        themeService.deleteTheme(1L);

        // then
        assertThat(themeService.getThemes()).hasSize(0);
    }

    @Test
    @DisplayName("인기 테마 목록 조회 테스트")
    void getPopularThemesTest() {
        // given
        setFakeReservationsAndThemes();

        // when
        List<ThemeResponse> popularThemes = themeService.getPopularThemes();

        // then
        assertThat(popularThemes).hasSize(10);
        Assertions.assertAll(() -> {
            for (int i = 0; i < 10; i++) {
                assertThat(popularThemes.get(i).getId()).isEqualTo(10 - i);
            }
        });
    }

    private void setFakeReservationsAndThemes() {
        List<Reservation> reservations = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            ThemeResponse theme = setTheme(i);
            Theme currentTheme = new Theme((long) i, theme.getName(), theme.getDescription(), theme.getThumbnail());

            LocalTime requestTime = LocalTime.now().minusHours(i);
            ReservationTime reservationTime = reservationTimeRepository.insert(requestTime);

            createFakeReservations(currentTheme, reservationTime, reservations, i);
        }

        themeRepository.setReservations(reservations);
    }

    private void createFakeReservations(Theme currentTheme, ReservationTime reservationTime,
                                        List<Reservation> reservations, int count) {
        for (int i = 1; i <= count; i++) {
            Member member = new Member((long) i, "name", "email@email.com", "password", Role.USER);

            CreateReservationRequest reservationRequest = new CreateReservationRequest(
                    member,
                    currentTheme,
                    new ReservationDate(LocalDate.now().minusDays(2)),
                    reservationTime
            );
            reservations.add(reservationRepository.insert(reservationRequest));
        }
    }

    private ThemeResponse setTheme(int i) {
        ThemeRequest themeRequest = new ThemeRequest(
                "레벨 탈출:" + i,
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );
        return themeService.createTheme(themeRequest);
    }
}
