package roomescape.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.common.exception.AlreadyInUseException;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.repository.UserRepository;
import roomescape.domain.auth.repository.impl.UserDao;
import roomescape.domain.reservation.dto.theme.ThemeRequest;
import roomescape.domain.reservation.dto.theme.ThemeResponse;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.repository.ReservationTimeRepository;
import roomescape.domain.reservation.repository.ThemeRepository;
import roomescape.domain.reservation.repository.impl.ReservationDAO;
import roomescape.domain.reservation.repository.impl.ReservationTimeDAO;
import roomescape.domain.reservation.repository.impl.ThemeDAO;
import roomescape.utils.PasswordFixture;

@JdbcTest
@Import({ReservationDAO.class, ReservationTimeDAO.class, ThemeDAO.class, UserDao.class})
class ThemeServiceIntegrationTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private UserRepository userRepository;

    private ThemeService themeService;

    @BeforeEach
    void setUp() {

        themeService = new ThemeService(themeRepository, reservationRepository);
    }

    @DisplayName("모든 테마 정보를 가져온다")
    @Test
    void test1() {
        // given
        themeRepository.save(Theme.withoutId("테마1", "테마1", "www.m.com"));
        themeRepository.save(Theme.withoutId("테마2", "테마2", "www.m.com"));

        // when
        final List<ThemeResponse> responses = themeService.getAll();

        // then
        assertThat(responses).hasSize(2);
    }

    @DisplayName("테마가 없다면 빈 테마 정보를 가져온다")
    @Test
    void test2() {
        // when
        final List<ThemeResponse> responses = themeService.getAll();

        // then
        assertThat(responses).isEmpty();
    }

    @DisplayName("테마를 추가한다")
    @Test
    void test3() {
        // given
        final ThemeRequest request = new ThemeRequest("테마1", "테마1", "www.m.com");

        // when
        final ThemeResponse response = themeService.create(request);

        // then

        final SoftAssertions softly = new SoftAssertions();

        softly.assertThat(response.id())
                .isNotNull();
        softly.assertThat(response.name())
                .isEqualTo("테마1");
        softly.assertThat(response.description())
                .isEqualTo("테마1");
        softly.assertThat(response.thumbnail())
                .isEqualTo("www.m.com");

        softly.assertAll();
    }

    @DisplayName("테마를 삭제한다")
    @Test
    void test4() {
        // given
        final ThemeRequest request = new ThemeRequest("테마1", "테마1", "www.m.com");
        final ThemeResponse response = themeService.create(request);
        final Long themeId = response.id();

        // when
        themeService.delete(themeId);

        // then
        assertThat(themeRepository.findAll()).isEmpty();
    }

    @DisplayName("없는 테마를 삭제할 수 없다")
    @Test
    void test5() {
        // when & then
        assertThatThrownBy(() -> themeService.delete(1L)).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("예약 정보가 있는 테마의 경우 삭제할 수 없다")
    @Test
    void test6() {
        // given
        final Theme savedTheme = themeRepository.save(Theme.withoutId("포스티", "공포", "wwww.um.com"));
        final Long themeId = savedTheme.getId();

        final LocalTime time = LocalTime.of(8, 0);
        final ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.withoutId(time));

        final LocalDate date = LocalDate.of(2024, 4, 29);
        final Name name = new Name("꾹");
        final User user = userRepository.save(
                User.withoutId(name, "admin@naver.com", PasswordFixture.generate(), Roles.USER));

        reservationRepository.save(Reservation.withoutId(user, date, savedTime, savedTheme));

        // when & then
        assertThatThrownBy(() -> themeService.delete(themeId)).isInstanceOf(AlreadyInUseException.class);
    }

    @DisplayName("1~8일 전 사이의 예약이 많은 최대 10개의 테마 정보를 가져온다")
    @Test
    void test7() {
        // given
        final ReservationTime reservationTime = reservationTimeRepository.save(
                ReservationTime.withoutId(LocalTime.of(10, 0)));
        final LocalDate date = LocalDate.now();

        final Theme theme1 = themeRepository.save(Theme.withoutId("테마1", "테마1", "www.m.com"));
        final Theme theme2 = themeRepository.save(Theme.withoutId("테마2", "테마2", "www.m.com"));
        final Theme theme3 = themeRepository.save(Theme.withoutId("테마3", "테마3", "www.m.com"));
        final Name name = new Name("꾹");
        final User user = userRepository.save(
                User.withoutId(name, "admin@naver.com", PasswordFixture.generate(), Roles.USER));

        reservationRepository.save(Reservation.withoutId(user, date.minusDays(2), reservationTime, theme1));
        reservationRepository.save(Reservation.withoutId(user, date.minusDays(3), reservationTime, theme1));
        reservationRepository.save(Reservation.withoutId(user, date.minusDays(4), reservationTime, theme2));
        reservationRepository.save(Reservation.withoutId(user, date.minusDays(1), reservationTime, theme1));
        reservationRepository.save(Reservation.withoutId(user, date.minusDays(5), reservationTime, theme2));
        reservationRepository.save(Reservation.withoutId(user, date.minusDays(6), reservationTime, theme3));

        // when
        final List<ThemeResponse> responses = themeService.getPopularThemes();

        // then
        assertThat(responses).containsExactly(ThemeResponse.from(theme1), ThemeResponse.from(theme2),
                ThemeResponse.from(theme3));
    }
}
