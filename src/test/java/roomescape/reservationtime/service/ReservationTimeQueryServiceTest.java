package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.fixture.ThemeFixture;
import roomescape.reservationtime.application.dto.AvailableReservationTimeResult;
import roomescape.reservationtime.application.dto.ReservationTimeCreateCommand;
import roomescape.reservationtime.application.dto.ReservationTimeResult;
import roomescape.reservationtime.application.service.ReservationTimeCommandService;
import roomescape.reservationtime.application.service.ReservationTimeQueryService;
import roomescape.reservation.infra.JdbcReservationRepository;
import roomescape.reservationtime.infra.JdbcAvailableTimeDao;
import roomescape.reservationtime.infra.JdbcReservationTimeRepository;
import roomescape.support.TestDataHelper;

@JdbcTest
@Import({
        ReservationTimeCommandService.class,
        ReservationTimeQueryService.class,
        JdbcReservationTimeRepository.class,
        JdbcReservationRepository.class,
        JdbcAvailableTimeDao.class
})
public class ReservationTimeQueryServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationTimeCommandService timeCommandService;

    @Autowired
    private ReservationTimeQueryService timeQueryService;

    @DisplayName("예약 시간의 전체 조회를 테스트합니다.")
    @Test
    void find_all_times() {
        ReservationTimeResult nine = timeCommandService.save(
                new ReservationTimeCreateCommand(LocalTime.of(9, 0))
        );
        ReservationTimeResult ten = timeCommandService.save(
                new ReservationTimeCreateCommand(LocalTime.of(10, 0))
        );
        ReservationTimeResult eleven = timeCommandService.save(
                new ReservationTimeCreateCommand(LocalTime.of(11, 0))
        );

        List<ReservationTimeResult> times = timeQueryService.findAll();

        assertThat(times).containsExactly(
                new ReservationTimeResult(nine.id(), LocalTime.of(9, 0)),
                new ReservationTimeResult(ten.id(), LocalTime.of(10, 0)),
                new ReservationTimeResult(eleven.id(), LocalTime.of(11, 0))
        );
    }

    @DisplayName("날짜/테마 선택 시 예약 가능한 시간 조회를 테스트합니다.")
    @Test
    void find_available_times_by_date_and_theme() {
        TestDataHelper testHelper = new TestDataHelper(jdbcTemplate);
        Long themeId = testHelper.insertTheme(ThemeFixture.horrorThemeCreateCommand());

        ReservationTimeResult nine = timeCommandService.save(
                new ReservationTimeCreateCommand(LocalTime.of(9, 0))
        );
        ReservationTimeResult ten = timeCommandService.save(
                new ReservationTimeCreateCommand(LocalTime.of(10, 0))
        );
        ReservationTimeResult eleven = timeCommandService.save(
                new ReservationTimeCreateCommand(LocalTime.of(11, 0))
        );

        LocalDate reservationDate = LocalDate.of(2026, 5, 6);
        testHelper.insertReservation("스타크", reservationDate, themeId, nine.id());

        List<AvailableReservationTimeResult> times = timeQueryService.findAvailableTimes(themeId, reservationDate);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(times).hasSize(3);
            softly.assertThat(times).containsExactly(
                    new AvailableReservationTimeResult(nine.id(), LocalTime.of(9, 0), false),
                    new AvailableReservationTimeResult(ten.id(), LocalTime.of(10, 0), true),
                    new AvailableReservationTimeResult(eleven.id(), LocalTime.of(11, 0), true)
            );
        });
    }
}
