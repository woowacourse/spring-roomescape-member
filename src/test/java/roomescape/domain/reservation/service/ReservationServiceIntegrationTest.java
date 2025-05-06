package roomescape.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
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
import org.springframework.test.context.ActiveProfiles;
import roomescape.common.exception.AlreadyInUseException;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.reservation.dto.BookedReservationTimeResponse;
import roomescape.domain.reservation.dto.ReservationRequest;
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
import roomescape.domain.reservation.utils.FixedClock;

@ActiveProfiles("test")
@JdbcTest
@Import({ReservationDAO.class, ReservationTimeDAO.class, ThemeDAO.class})
public class ReservationServiceIntegrationTest {

    private static LocalDateTime now;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    private ReservationService reservationService;

    private static LocalDate nextDay() {
        return now.toLocalDate()
                .plusDays(1);
    }

    @BeforeEach
    void setUp() {
        now = LocalDateTime.of(2025, 4, 30, 12, 0);
        final Clock clock = FixedClock.from(now);

        reservationService = new ReservationService(clock, reservationRepository, reservationTimeRepository,
                themeRepository);

    }

    @DisplayName("모든 예약 정보를 가져온다")
    @Test
    void test1() {
        // given
        final Theme savedTheme = themeRepository.save(Theme.withoutId("포스티", "공포", "wwww.um.com"));

        final LocalTime time = LocalTime.of(8, 0);
        final ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.withoutId(time));

        final LocalDate date = LocalDate.of(2024, 4, 29);
        reservationRepository.save(Reservation.withoutId("꾹", date, savedTime, savedTheme));

        // when
        final List<ReservationResponse> response = reservationService.getAll();

        // then
        assertThat(response).hasSize(1);
    }

    @DisplayName("예약 정보가 없다면 빈 리스트를 반환한다.")
    @Test
    void test2() {
        final List<ReservationResponse> result = reservationService.getAll();

        assertThat(result).isEmpty();
    }

    @DisplayName("예약을 추가한다.")
    @Test
    void test3() {
        // given
        final String name = "꾹";

        final Theme savedTheme = themeRepository.save(Theme.withoutId("포스티", "공포", "wwww.um.com"));
        final Long themeId = savedTheme.getId();

        final LocalTime time = LocalTime.of(8, 0);
        final ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.withoutId(time));
        final Long timeId = savedTime.getId();

        final LocalDate date = nextDay();

        final ReservationRequest requestDto = new ReservationRequest(name, date, timeId, themeId);

        // when
        final ReservationResponse result = reservationService.create(requestDto);

        // then
        final SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(result.name())
                .isEqualTo(name);
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
        final String name = "꾹";

        final Theme savedTheme = themeRepository.save(Theme.withoutId("포스티", "공포", "wwww.um.com"));
        final Long themeId = savedTheme.getId();

        final LocalTime time = LocalTime.of(8, 0);
        final ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.withoutId(time));
        final Long timeId = savedTime.getId();
        final LocalDate date = nextDay();

        final ReservationRequest requestDto = new ReservationRequest(name, date, timeId, themeId);
        reservationService.create(requestDto);

        // when & then
        assertThatThrownBy(() -> reservationService.create(requestDto)).isInstanceOf(AlreadyInUseException.class);
    }

    @DisplayName("과거 날짜에 예약을 추가하면 예외가 발생한다.")
    @Test
    void test5() {
        // given
        final String name = "꾹";

        final Theme savedTheme = themeRepository.save(Theme.withoutId("포스티", "공포", "wwww.um.com"));
        final Long themeId = savedTheme.getId();

        final LocalDate date = now.toLocalDate();
        final LocalTime pastTime = now.toLocalTime()
                .minusMinutes(1);

        final ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.withoutId(pastTime));
        final Long timeId = savedTime.getId();

        final ReservationRequest requestDto = new ReservationRequest(name, date, timeId, themeId);

        // when & then
        assertThatThrownBy(() -> reservationService.create(requestDto)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 예약 시간 ID로 저장하면 예외를 반환한다.")
    @Test
    void test6() {
        final Theme savedTheme = themeRepository.save(Theme.withoutId("포스티", "공포", "wwww.um.com"));
        final Long themeId = savedTheme.getId();

        final LocalDate date = nextDay();

        final Long notExistId = 1000L;
        final ReservationRequest requestDto = new ReservationRequest("꾹", date, notExistId, themeId);

        assertThatThrownBy(() -> reservationService.create(requestDto)).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("존재하지 않는 테마 ID로 저장하면 예외를 반환한다.")
    @Test
    void notExistThemeId() {
        final LocalDate date = now.toLocalDate()
                .plusDays(1);

        final LocalTime time = LocalTime.of(8, 0);
        final ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.withoutId(time));
        final Long timeId = savedTime.getId();

        final Long notExistId = 1000L;
        final ReservationRequest requestDto = new ReservationRequest("꾹", date, timeId, notExistId);

        assertThatThrownBy(() -> reservationService.create(requestDto)).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("예약을 삭제한다")
    @Test
    void test7() {
        // given
        final Theme savedTheme = themeRepository.save(Theme.withoutId("포스티", "공포", "wwww.um.com"));

        final LocalTime time = LocalTime.of(8, 0);
        final ReservationTime savedTime = reservationTimeRepository.save(ReservationTime.withoutId(time));

        final LocalDate date = nextDay();
        final Reservation savedReservation = reservationRepository.save(
                Reservation.withoutId("꾹", date, savedTime, savedTheme));

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
        final ReservationTime reservationTime2 = reservationTimeRepository.save(ReservationTime.withoutId(time2));

        final Theme savedTheme = themeRepository.save(Theme.withoutId("포스티", "공포", "wwww.um.com"));
        final Long themeId = savedTheme.getId();

        reservationRepository.save(Reservation.withoutId("꾹", date, reservationTime1, savedTheme));

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
