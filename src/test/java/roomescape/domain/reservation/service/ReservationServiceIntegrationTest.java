package roomescape.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
import roomescape.domain.reservation.dto.reservation.ReservationCreateDto;
import roomescape.domain.reservation.dto.reservation.ReservationResponse;
import roomescape.domain.reservation.dto.reservation.ReservationUserCreateRequest;
import roomescape.domain.reservation.dto.reservationtime.BookedReservationTimeResponse;
import roomescape.domain.reservation.dto.reservationtime.ReservationTimeResponse;
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
@Import({ReservationDAO.class, ReservationTimeDAO.class, ThemeDAO.class, UserDao.class, ReservationService.class})
public class ReservationServiceIntegrationTest {

    private static LocalDateTime now;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationService reservationService;

    private static LocalDate nextDay() {
        return now.toLocalDate()
                .plusDays(1);
    }

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
    }

    @DisplayName("모든 예약 정보를 가져온다")
    @Test
    void test1() {
        // given
        final Theme savedTheme = themeRepository.save(Theme.withoutId("포스티", "공포", "wwww.um.com"));

        final LocalTime time = LocalTime.of(8, 0);
        final ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.withoutId(time));

        final LocalDate date = LocalDate.of(2024, 4, 29);

        final Name name = new Name("꾹");
        final User user = User.withoutId(name, "admin@naver.com", PasswordFixture.generate(), Roles.USER);
        final User savedUser = userRepository.save(user);

        reservationRepository.save(Reservation.withoutId(savedUser, date, savedTime, savedTheme));

        // when
        final List<ReservationResponse> response = reservationService.getAll(null, null, null, null);

        // then
        assertThat(response).hasSize(1);
    }

    @DisplayName("예약 정보가 없다면 빈 리스트를 반환한다.")
    @Test
    void test2() {
        final List<ReservationResponse> result = reservationService.getAll(null, null, null, null);

        assertThat(result).isEmpty();
    }

    @DisplayName("예약을 추가한다.")
    @Test
    void test3() {
        // given

        final Theme savedTheme = themeRepository.save(Theme.withoutId("포스티", "공포", "wwww.um.com"));
        final Long themeId = savedTheme.getId();

        final LocalTime time = LocalTime.of(8, 0);
        final ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.withoutId(time));
        final Long timeId = savedTime.getId();

        final Name name = new Name("꾹");
        final User user = User.withoutId(name, "admin@naver.com", PasswordFixture.generate(), Roles.USER);
        final Long userId = userRepository.save(user)
                .getId();

        final LocalDate date = nextDay();

        final ReservationCreateDto reservationCreateDto = new ReservationCreateDto(userId, timeId, themeId, date);

        // when
        final ReservationResponse result = reservationService.create(reservationCreateDto);

        // then
        final SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(result.name())
                .isEqualTo(name.getName());
        softAssertions.assertThat(result.date())
                .isEqualTo(date);
        softAssertions.assertThat(result.time())
                .isEqualTo(new ReservationTimeResponse(timeId, time));
        softAssertions.assertThat(result.theme())
                .isEqualTo(new ThemeResponse(themeId, savedTheme.getName(), savedTheme.getDescription(),
                        savedTheme.getThumbnail()));

        softAssertions.assertAll();
    }

    @DisplayName("이미 존재하는 예약과 동일하면 예외가 발생한다.")
    @Test
    void test4() {
        // given
        final Theme savedTheme = themeRepository.save(Theme.withoutId("포스티", "공포", "wwww.um.com"));
        final Long themeId = savedTheme.getId();

        final LocalTime time = LocalTime.of(8, 0);
        final ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.withoutId(time));
        final Long timeId = savedTime.getId();
        final LocalDate date = nextDay();

        final Name name = new Name("꾹");
        final User user = User.withoutId(name, "admin@naver.com", PasswordFixture.generate(), Roles.USER);
        final Long userId = userRepository.save(user)
                .getId();

        final ReservationCreateDto reservationCreateDto = new ReservationCreateDto(userId, timeId, themeId, date);
        reservationService.create(reservationCreateDto);

        // when & then
        assertThatThrownBy(() -> reservationService.create(reservationCreateDto)).isInstanceOf(
                AlreadyInUseException.class);
    }

    @DisplayName("과거 날짜에 예약을 추가하면 예외가 발생한다.")
    @Test
    void test5() {
        // given
        final Theme savedTheme = themeRepository.save(Theme.withoutId("포스티", "공포", "wwww.um.com"));
        final Long themeId = savedTheme.getId();

        final LocalDate date = now.toLocalDate();
        final LocalTime pastTime = now.toLocalTime()
                .minusMinutes(1);

        final ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.withoutId(pastTime));
        final Long timeId = savedTime.getId();

        final Name name = new Name("꾹");
        final User user = User.withoutId(name, "admin@naver.com", PasswordFixture.generate(), Roles.USER);
        final Long userId = userRepository.save(user)
                .getId();

        final ReservationCreateDto reservationCreateDto = new ReservationCreateDto(userId, timeId, themeId, date);

        // when & then
        assertThatThrownBy(() -> reservationService.create(reservationCreateDto)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 예약 시간 ID로 저장하면 예외를 반환한다.")
    @Test
    void test6() {
        final Theme savedTheme = themeRepository.save(Theme.withoutId("포스티", "공포", "wwww.um.com"));
        final Long themeId = savedTheme.getId();

        final LocalDate date = nextDay();

        final Long notExistId = 1000L;
        final ReservationUserCreateRequest requestDto = new ReservationUserCreateRequest(date, notExistId, themeId);

        final Name name = new Name("꾹");
        final User user = User.withoutId(name, "admin@naver.com", PasswordFixture.generate(), Roles.USER);
        final Long userId = userRepository.save(user)
                .getId();

        final ReservationCreateDto reservationCreateDto = new ReservationCreateDto(userId, notExistId, themeId, date);

        assertThatThrownBy(() -> reservationService.create(reservationCreateDto)).isInstanceOf(
                EntityNotFoundException.class);
    }

    @DisplayName("존재하지 않는 유저 ID로 저장하면 예외를 반환한다.")
    @Test
    void notExistUserIdTest() {
        final long notExistUserId = 1000L;

        final LocalDate date = now.toLocalDate()
                .plusDays(1);

        final LocalTime time = LocalTime.of(8, 0);
        final ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.withoutId(time));
        final Long timeId = savedTime.getId();

        final Long themeId = themeRepository.save(Theme.withoutId("테마", "설명", "wwww.um.com"))
                .getId();
        final ReservationCreateDto reservationCreateDto = new ReservationCreateDto(notExistUserId, timeId, themeId,
                date);

        // when & then

        assertThatThrownBy(() -> reservationService.create(reservationCreateDto)).isInstanceOf(
                EntityNotFoundException.class);
    }

    @DisplayName("존재하지 않는 테마 ID로 저장하면 예외를 반환한다.")
    @Test
    void notExistThemeIdTest() {
        final LocalDate date = now.toLocalDate()
                .plusDays(1);

        final LocalTime time = LocalTime.of(8, 0);
        final ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.withoutId(time));
        final Long timeId = savedTime.getId();

        final Name name = new Name("꾹");
        final User user = User.withoutId(name, "admin@naver.com", PasswordFixture.generate(), Roles.USER);
        final Long userId = userRepository.save(user)
                .getId();

        final Long notExistId = 1000L;
        final ReservationCreateDto reservationCreateDto = new ReservationCreateDto(userId, timeId, notExistId, date);

        assertThatThrownBy(() -> reservationService.create(reservationCreateDto)).isInstanceOf(
                EntityNotFoundException.class);
    }

    @DisplayName("예약을 삭제한다")
    @Test
    void test7() {
        // given
        final Theme savedTheme = themeRepository.save(Theme.withoutId("포스티", "공포", "wwww.um.com"));

        final LocalTime time = LocalTime.of(8, 0);
        final ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.withoutId(time));

        final LocalDate date = nextDay();
        final Name name = new Name("꾹");

        final User user = userRepository.save(
                User.withoutId(name, "admin@naver.com", PasswordFixture.generate(), Roles.USER));

        final Reservation savedReservation = reservationRepository.save(
                Reservation.withoutId(user, date, savedTime, savedTheme));

        final Long id = savedReservation.getId();

        // then
        assertThatCode(() -> reservationService.delete(id)).doesNotThrowAnyException();
    }

    @DisplayName("예약이 존재하지 않으면 예외를 반환한다.")
    @Test
    void test8() {
        final Long id = 1L;
        assertThatThrownBy(() -> reservationService.delete(id)).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("가능한 시간 대에 대하여 반환한다.")
    @Test
    void test9() {
        // given
        final LocalDate date = nextDay();

        final LocalTime time1 = LocalTime.of(8, 0);
        final LocalTime time2 = LocalTime.of(9, 0);

        final ReservationTime reservationTime1 = reservationTimeRepository.save(ReservationTime.withoutId(time1));
        reservationTimeRepository.save(ReservationTime.withoutId(time2));

        final Theme savedTheme = themeRepository.save(Theme.withoutId("포스티", "공포", "wwww.um.com"));
        final Long themeId = savedTheme.getId();

        final Name name = new Name("꾹");
        final User user = userRepository.save(
                User.withoutId(name, "admin@naver.com", PasswordFixture.generate(), Roles.USER));

        reservationRepository.save(Reservation.withoutId(user, date, reservationTime1, savedTheme));

        // when
        final List<BookedReservationTimeResponse> responses = reservationService.getAvailableTimes(date, themeId);

        // then
        final SoftAssertions softly = new SoftAssertions();

        softly.assertThat(responses)
                .hasSize(2);

        final List<Boolean> booleans = responses.stream()
                .map(BookedReservationTimeResponse::alreadyBooked)
                .toList();
        softly.assertThat(booleans)
                .containsExactlyInAnyOrder(true, false);

        final Map<LocalTime, Boolean> timeBookingStatus = responses.stream()
                .collect(Collectors.toMap(response -> response.timeResponse()
                        .startAt(), BookedReservationTimeResponse::alreadyBooked));

        softly.assertThat(timeBookingStatus.get(time1))
                .isTrue(); // 8:00은 예약됨
        softly.assertThat(timeBookingStatus.get(time2))
                .isFalse(); // 9:00은 예약 가능

        softly.assertAll();
    }

    @DisplayName("필터 조건에 맞는 예약만 조회한다")
    @Test
    void getAll_WithFilters_ReturnsFilteredReservations() {
        // given
        final Theme theme1 = themeRepository.save(Theme.withoutId("테마1", "설명1", "url1"));
        final Theme theme2 = themeRepository.save(Theme.withoutId("테마2", "설명2", "url2"));

        final LocalTime time = LocalTime.of(8, 0);
        final ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.withoutId(time));

        final LocalDate today = LocalDate.now();
        final LocalDate tomorrow = today.plusDays(1);

        final User user1 = userRepository.save(
                User.withoutId(new Name("사용자1"), "user1@mail.com", PasswordFixture.generate(), Roles.USER));
        final User user2 = userRepository.save(
                User.withoutId(new Name("사용자2"), "user2@mail.com", PasswordFixture.generate(), Roles.USER));

        // 테마1, 사용자1, 오늘 예약
        reservationRepository.save(Reservation.withoutId(user1, today, savedTime, theme1));
        // 테마2, 사용자1, 내일 예약
        reservationRepository.save(Reservation.withoutId(user1, tomorrow, savedTime, theme2));
        // 테마1, 사용자2, 내일 예약
        reservationRepository.save(Reservation.withoutId(user2, tomorrow, savedTime, theme1));

        // when & then
        // 테마1 필터링
        assertThat(reservationService.getAll(theme1.getId(), null, null, null)).hasSize(2);
        // 사용자1 필터링
        assertThat(reservationService.getAll(null, user1.getId(), null, null)).hasSize(2);
        // 오늘 날짜 필터링
        assertThat(reservationService.getAll(null, null, today, today)).hasSize(1);
        // 테마1 & 내일 필터링
        assertThat(reservationService.getAll(theme1.getId(), null, tomorrow, tomorrow)).hasSize(1);
        // 모든 필터 조합
        assertThat(reservationService.getAll(theme1.getId(), user2.getId(), tomorrow, tomorrow)).hasSize(1);
        // 일치하는 결과가 없는 필터 조합
        assertThat(reservationService.getAll(theme2.getId(), user2.getId(), null, null)).isEmpty();
    }
}
