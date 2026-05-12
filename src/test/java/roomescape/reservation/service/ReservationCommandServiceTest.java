package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.fixture.ReservationFixture;
import roomescape.fixture.ThemeFixture;
import roomescape.global.exception.NotFoundException;
import roomescape.reservation.application.dto.ReservationCreateCommand;
import roomescape.reservation.application.dto.ReservationResult;
import roomescape.reservation.application.dto.ReservationUpdateCommand;
import roomescape.global.exception.RoomEscapeException;
import roomescape.reservation.application.service.ReservationCommandService;
import roomescape.reservation.infra.JdbcReservationRepository;
import roomescape.reservationtime.application.dto.ReservationTimeResult;
import roomescape.reservationtime.infra.JdbcReservationTimeRepository;
import roomescape.support.TestClockConfig;
import roomescape.support.TestDataHelper;
import roomescape.theme.infra.JdbcThemeRepository;

@JdbcTest
@Import({
        ReservationCommandService.class,
        JdbcReservationRepository.class,
        JdbcThemeRepository.class,
        JdbcReservationTimeRepository.class,
        TestClockConfig.class
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

        ReservationCreateCommand request = ReservationFixture.futureStarkCreateCommand(themeId, timeId);
        ReservationResult result = reservationCommandService.save(request);

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
        testHelper.insertReservation("스타크", ReservationFixture.futureReservationDate(), themeId, timeId);

        ReservationCreateCommand request = ReservationFixture.futureKayaCreateCommand(themeId, timeId);
        assertThatThrownBy(() -> reservationCommandService.save(request))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("이미 해당 날짜와 시간에 예약이 존재합니다.");
    }

    @DisplayName("현재 시간보다 이전 시간의 예약 생성 예외를 테스트합니다.")
    @Test
    void save_past_reservation_exception() {
        Long themeId = testHelper.insertTheme(ThemeFixture.horrorThemeCreateCommand());
        Long timeId = testHelper.insertReservationTime(LocalTime.of(10, 0));

        assertThatThrownBy(() -> reservationCommandService.save(
                ReservationFixture.pastStarkCreateCommand(themeId, timeId)
        ))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("현재 시간보다 이전 시간으로 예약을 할 수 없습니다.");
    }

    @DisplayName("예약 삭제를 테스트합니다.")
    @Test
    void delete_reservation() {
        Long themeId = testHelper.insertTheme(ThemeFixture.horrorThemeCreateCommand());
        Long timeId = testHelper.insertReservationTime(LocalTime.of(10, 0));
        Long reservationId = testHelper.insertReservation(
                "스타크",
                ReservationFixture.futureReservationDate(),
                themeId,
                timeId
        );

        assertThatNoException().isThrownBy(() -> reservationCommandService.delete(reservationId));
    }

    @DisplayName("삭제할 예약이 없을 시 예외 발생을 테스트합니다.")
    @Test
    void delete_not_found_reservation_exception() {
        assertThatThrownBy(() -> reservationCommandService.delete(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약입니다.");
    }

    @DisplayName("삭제할 예약이 현재 시간보다 이전 시간일 경우 예외 발생을 테스트합니다.")
    @Test
    void delete_past_reservation_exception() {
        Long themeId = testHelper.insertTheme(ThemeFixture.horrorThemeCreateCommand());
        Long timeId = testHelper.insertReservationTime(LocalTime.of(10, 0));
        Long reservationId = testHelper.insertReservation(
                "스타크",
                ReservationFixture.pastReservationDate(),
                themeId,
                timeId
        );

        assertThatThrownBy(() -> reservationCommandService.delete(reservationId))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("이미 지나간 예약은 삭제할 수 없습니다.");
    }

    static Stream<Arguments> futureDates() {
        return Stream.of(
                Arguments.of(TestClockConfig.CURRENT_DATE, LocalTime.of(0, 0)),
                Arguments.of(ReservationFixture.futureReservationDate(), LocalTime.of(11, 0))
        );
    }

    @DisplayName("사용자의 방탈출 예약 날짜/시간 변경을 테스트합니다.")
    @ParameterizedTest
    @MethodSource("futureDates")
    void update_reservation(LocalDate updateDate, LocalTime updateTime) {
        Long themeId = testHelper.insertTheme(ThemeFixture.horrorThemeCreateCommand());
        Long timeId = testHelper.insertReservationTime(LocalTime.of(0, 0));
        Long reservationId = testHelper.insertReservation(
                "스타크",
                ReservationFixture.pastReservationDate(),
                themeId,
                timeId
        );

        Long updateTimeId = testHelper.insertReservationTime(updateTime);
        ReservationResult result = reservationCommandService.update(
                reservationId,
                new ReservationUpdateCommand(updateDate, updateTimeId)
        );

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(result.date()).isEqualTo(updateDate);
            softly.assertThat(result.time().id()).isEqualTo(updateTimeId);
        });
    }

    @DisplayName("존재하지 않는 예약 업데이트 시도 시 예외 발생을 테스트합니다.")
    @Test
    void update_not_found_reservation_exception() {
        Long newTimeId = testHelper.insertReservationTime(LocalTime.of(11, 0));

        assertThatThrownBy(
                () -> reservationCommandService.update(1L, ReservationFixture.futureStarkUpdateCommand(newTimeId)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약입니다.");
    }

    @DisplayName("변경하려는 예약 날짜에 이미 예약이 존재할 시 예외 발생을 테스트합니다.")
    @Test
    void update_already_reserved_exception() {
        Long themeId = testHelper.insertTheme(ThemeFixture.horrorThemeCreateCommand());
        Long tenTimeId = testHelper.insertReservationTime(LocalTime.of(10, 0));
        Long starkReservationId = testHelper.insertReservation(
                "스타크",
                ReservationFixture.pastReservationDate(),
                themeId,
                tenTimeId
        );

        Long elevenTimeId = testHelper.insertReservationTime(LocalTime.of(11, 0));
        testHelper.insertReservation(
                "카야",
                ReservationFixture.futureReservationDate(),
                themeId,
                elevenTimeId
        );

        assertThatThrownBy(() -> reservationCommandService.update(
                starkReservationId,
                ReservationFixture.futureStarkUpdateCommand(elevenTimeId))
        )
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("변경하려는 날짜와 시간에 이미 예약이 존재합니다.");
    }

    @DisplayName("변경하려는 날짜가 현재 시각보다 이전일 경우 예외 발생을 테스트합니다.")
    @Test
    void update_past_date_reservation_exception() {
        Long themeId = testHelper.insertTheme(ThemeFixture.horrorThemeCreateCommand());
        Long timeId = testHelper.insertReservationTime(LocalTime.of(0, 0));
        Long reservationId = testHelper.insertReservation(
                "스타크",
                ReservationFixture.futureReservationDate(),
                themeId,
                timeId
        );

        assertThatThrownBy(() ->
                reservationCommandService.update(
                        reservationId,
                        new ReservationUpdateCommand(TestClockConfig.CURRENT_DATE.minusDays(1), timeId)
                ))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("현재 시간보다 이전 시간으로 예약을 할 수 없습니다.");
    }
}
