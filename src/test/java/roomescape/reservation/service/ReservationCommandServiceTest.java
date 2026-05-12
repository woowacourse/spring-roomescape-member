package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.fixture.ReservationFixture;
import roomescape.fixture.ThemeFixture;
import roomescape.global.exception.NotFoundException;
import roomescape.reservation.application.dto.ReservationCreateCommand;
import roomescape.reservation.application.dto.ReservationResult;
import roomescape.global.exception.RoomEscapeException;
import roomescape.reservation.application.service.ReservationCommandService;
import roomescape.reservation.infra.JdbcReservationRepository;
import roomescape.reservationtime.application.dto.ReservationTimeResult;
import roomescape.reservationtime.infra.JdbcReservationTimeRepository;
import roomescape.support.TestDataHelper;
import roomescape.theme.infra.JdbcThemeRepository;

@JdbcTest
@Import({
        ReservationCommandService.class,
        JdbcReservationRepository.class,
        JdbcThemeRepository.class,
        JdbcReservationTimeRepository.class
})
class ReservationCommandServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationCommandService reservationCommandService;

    private TestDataHelper testHelper;

    @BeforeEach
    void setUp() {
        testHelper = new TestDataHelper(jdbcTemplate);
    }

    @DisplayName("사용자의 방탈출 예약 생성을 테스트합니다.")
    @Test
    void save_user_reservation_successfully() {
        Long themeId = testHelper.insertTheme(ThemeFixture.horrorThemeCreateCommand());
        Long timeId = testHelper.insertReservationTime(LocalTime.of(10, 0));
        ReservationCreateCommand request = ReservationFixture.starkCreateCommand(themeId, timeId);

        ReservationResult result = reservationCommandService.save(request, LocalDateTime.of(2000, 1, 1, 0, 0));

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(result.id()).isPositive();
            softly.assertThat(result.name()).isEqualTo(request.name());
            softly.assertThat(result.date()).isEqualTo(request.date());
            softly.assertThat(result.theme()).isEqualTo(ThemeFixture.horrorThemeQueryResult(themeId));
            softly.assertThat(result.time()).isEqualTo(new ReservationTimeResult(timeId, LocalTime.of(10, 0)));
        });
    }

    @DisplayName("중복된 날짜와 테마와 시간의 예약 생성 예외를 테스트합니다.")
    @Test
    void save_duplicated_reservation_exception() {
        Long themeId = testHelper.insertTheme(ThemeFixture.horrorThemeCreateCommand());
        Long timeId = testHelper.insertReservationTime(LocalTime.of(10, 0));
        testHelper.insertReservation("스타크", LocalDate.of(2026, 5, 6), themeId, timeId);

        assertThatThrownBy(() -> reservationCommandService.save(
                ReservationFixture.kayaCreateCommand(themeId, timeId),
                LocalDateTime.of(2026, 5, 5, 0, 0)
        ))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("이미 해당 날짜와 시간에 예약이 존재합니다.");
    }

    @DisplayName("현재 시간보다 이전 시간의 예약 생성 예외를 테스트합니다.")
    @Test
    void save_past_reservation_exception() {
        Long themeId = testHelper.insertTheme(ThemeFixture.horrorThemeCreateCommand());
        Long timeId = testHelper.insertReservationTime(LocalTime.of(10, 0));

        assertThatThrownBy(() -> reservationCommandService.save(
                ReservationFixture.starkCreateCommand(themeId, timeId),
                LocalDateTime.of(2026, 5, 6, 11, 0)
        ))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("현재 시간보다 이전 시간으로 예약을 할 수 없습니다.");
    }

    @DisplayName("예약 삭제를 테스트합니다.")
    @Test
    void delete_reservation() {
        Long themeId = testHelper.insertTheme(ThemeFixture.horrorThemeCreateCommand());
        Long timeId = testHelper.insertReservationTime(LocalTime.of(10, 0));
        Long reservationId = testHelper.insertReservation("스타크", LocalDate.of(2026, 5, 6), themeId, timeId);

        assertThatNoException().isThrownBy(
                () -> reservationCommandService.delete(reservationId, LocalDateTime.of(2025, 5, 6, 10, 0)));
    }

    @DisplayName("삭제할 예약이 없을 시 예외 발생을 테스트합니다.")
    @Test
    void delete_not_found_reservation_exception() {
        assertThatThrownBy(() -> reservationCommandService.delete(1L, LocalDateTime.of(2025, 5, 6, 10, 0)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약입니다.");
    }

    @DisplayName("삭제할 예약이 현재 시간보다 이전 시간일 경우 예외 발생을 테스트합니다.")
    @Test
    void delete_past_reservation_exception() {
        Long themeId = testHelper.insertTheme(ThemeFixture.horrorThemeCreateCommand());
        Long timeId = testHelper.insertReservationTime(LocalTime.of(10, 0));
        Long reservationId = testHelper.insertReservation("스타크", LocalDate.of(2026, 5, 6), themeId, timeId);

        assertThatThrownBy(() -> reservationCommandService.delete(reservationId, LocalDateTime.of(2027, 5, 6, 10, 0)))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("이미 지나간 예약은 삭제할 수 없습니다.");
    }
}
