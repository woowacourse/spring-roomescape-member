package roomescape.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import roomescape.domain.reservation.dto.BookedReservationTimeResponse;
import roomescape.domain.reservation.dto.ReservationCreateRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.dto.ReservationTimeResponse;
import roomescape.domain.reservation.dto.ThemeResponse;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.repository.ReservationTimeRepository;
import roomescape.domain.reservation.repository.ThemeRepository;
import roomescape.domain.reservation.repository.impl.ReservationDAO;
import roomescape.domain.reservation.repository.impl.ReservationTimeDAO;
import roomescape.domain.reservation.repository.impl.ThemeDAO;

@JdbcTest
@Import({ReservationDAO.class, ReservationTimeDAO.class, ThemeDAO.class, UserDao.class})
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

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository,
                userRepository);

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
        final User user = User.withoutId(name, "admin@naver.com", "1234", Roles.USER);
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
        final User user = User.withoutId(name, "admin@naver.com", "1234", Roles.USER);
        final Long userId = userRepository.save(user)
                .getId();

        final LocalDate date = nextDay();

        final ReservationCreateRequest requestDto = new ReservationCreateRequest(date, timeId, themeId);

        // when
        final ReservationResponse result = reservationService.create(requestDto, userId);

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

    private static LocalDate nextDay() {
        return now.toLocalDate()
                .plusDays(1);
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
        final User user = User.withoutId(name, "admin@naver.com", "1234", Roles.USER);
        final Long userId = userRepository.save(user)
                .getId();

        final ReservationCreateRequest requestDto = new ReservationCreateRequest(date, timeId, themeId);
        reservationService.create(requestDto, userId);

        // when & then
        assertThatThrownBy(() -> reservationService.create(requestDto, userId)).isInstanceOf(
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
        final User user = User.withoutId(name, "admin@naver.com", "1234", Roles.USER);
        final Long userId = userRepository.save(user)
                .getId();

        final ReservationCreateRequest requestDto = new ReservationCreateRequest(date, timeId, themeId);

        // when & then
        assertThatThrownBy(() -> reservationService.create(requestDto, userId)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 예약 시간 ID로 저장하면 예외를 반환한다.")
    @Test
    void test6() {
        final Theme savedTheme = themeRepository.save(Theme.withoutId("포스티", "공포", "wwww.um.com"));
        final Long themeId = savedTheme.getId();

        final LocalDate date = nextDay();

        final Long notExistId = 1000L;
        final ReservationCreateRequest requestDto = new ReservationCreateRequest(date, notExistId, themeId);

        final Name name = new Name("꾹");
        final User user = User.withoutId(name, "admin@naver.com", "1234", Roles.USER);
        final Long userId = userRepository.save(user)
                .getId();

        assertThatThrownBy(() -> reservationService.create(requestDto, userId)).isInstanceOf(
                EntityNotFoundException.class);
    }

    @DisplayName("존재하지 않는 유저 ID로 저장하면 예외를 반환한다.")
    @Test
    void notExistUserIdTest() {
        final LocalDate date = now.toLocalDate()
                .plusDays(1);

        final LocalTime time = LocalTime.of(8, 0);
        final ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.withoutId(time));
        final Long timeId = savedTime.getId();

        final Long themeId = themeRepository.save(Theme.withoutId("테마", "설명", "wwww.um.com"))
                .getId();
        final ReservationCreateRequest requestDto = new ReservationCreateRequest(date, timeId, themeId);

        final Long userId = 1L;
        assertThatThrownBy(() -> reservationService.create(requestDto, userId)).isInstanceOf(
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
        final User user = User.withoutId(name, "admin@naver.com", "1234", Roles.USER);
        final Long userId = userRepository.save(user)
                .getId();

        final Long notExistId = 1000L;
        final ReservationCreateRequest requestDto = new ReservationCreateRequest(date, timeId, notExistId);

        assertThatThrownBy(() -> reservationService.create(requestDto, userId)).isInstanceOf(
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

        final User user = userRepository.save(User.withoutId(name, "admin@naver.com", "1234", Roles.USER));

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
        final User user = userRepository.save(User.withoutId(name, "admin@naver.com", "1234", Roles.USER));

        reservationRepository.save(Reservation.withoutId(user, date, reservationTime1, savedTheme));

        // when
        final List<BookedReservationTimeResponse> responses = reservationService.getAvailableTimes(date, themeId);

        // then
        assertThat(responses).hasSize(2);

        final List<Boolean> booleans = responses.stream()
                .map(BookedReservationTimeResponse::alreadyBooked)
                .toList();
        assertThat(booleans).containsExactlyInAnyOrder(true, false);
    }

}
