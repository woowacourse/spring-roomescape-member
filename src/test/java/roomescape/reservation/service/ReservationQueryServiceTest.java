package roomescape.reservation.service;

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
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.fixture.ThemeFixture;
import roomescape.reservation.application.dto.ReservationResult;
import roomescape.reservation.application.service.ReservationQueryService;
import roomescape.reservation.infra.JdbcReservationDao;
import roomescape.reservationtime.application.dto.ReservationTimeResult;
import roomescape.support.TestDataHelper;

@JdbcTest
@Import({ReservationQueryService.class, JdbcReservationDao.class})
class ReservationQueryServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationQueryService reservationQueryService;

    private TestDataHelper testHelper;

    @BeforeEach
    void setUp() {
        testHelper = new TestDataHelper(jdbcTemplate);
    }

    @DisplayName("예약 전체 조회를 테스트합니다.")
    @Test
    void find_all_reservations() {
        Long themeId = testHelper.insertTheme(ThemeFixture.horrorThemeCreateCommand());
        Long nineTimeId = testHelper.insertReservationTime(LocalTime.of(9, 0));
        Long tenTimeId = testHelper.insertReservationTime(LocalTime.of(10, 0));
        LocalDate earlierDate = LocalDate.of(2026, 5, 6);
        LocalDate laterDate = LocalDate.of(2026, 5, 7);

        testHelper.insertReservation("먼저 예약한 사람", earlierDate, themeId, nineTimeId);
        testHelper.insertReservation("나중에 예약한 사람", laterDate, themeId, tenTimeId);

        List<ReservationResult> reservations = reservationQueryService.findAll();

        ReservationResult first = reservations.getFirst();
        ReservationResult second = reservations.get(1);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(reservations).hasSize(2);
            softly.assertThat(first.id()).isPositive();
            softly.assertThat(first.name()).isEqualTo("먼저 예약한 사람");
            softly.assertThat(first.date()).isEqualTo(earlierDate);
            softly.assertThat(first.theme()).isEqualTo(ThemeFixture.horrorThemeQueryResult(themeId));
            softly.assertThat(first.time()).isEqualTo(new ReservationTimeResult(nineTimeId, LocalTime.of(9, 0)));
            softly.assertThat(second.id()).isPositive();
            softly.assertThat(second.name()).isEqualTo("나중에 예약한 사람");
            softly.assertThat(second.date()).isEqualTo(laterDate);
            softly.assertThat(second.theme()).isEqualTo(ThemeFixture.horrorThemeQueryResult(themeId));
            softly.assertThat(second.time()).isEqualTo(new ReservationTimeResult(tenTimeId, LocalTime.of(10, 0)));
        });
    }
}
