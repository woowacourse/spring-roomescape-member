package roomescape.reservation.infra;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.application.query.ReservationDetail;
import roomescape.reservation.application.query.ReservationDetailDao;
import roomescape.support.TestDataHelper;

@JdbcTest
class JdbcReservationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    ReservationDetailDao reservationDao;
    TestDataHelper testHelper;

    @BeforeEach
    void setUp() {
        reservationDao = new JdbcReservationDao(jdbcTemplate);
        testHelper = new TestDataHelper(jdbcTemplate);
    }

    @DisplayName("예약 상세 정보 전체 조회를 테스트합니다.")
    @Test
    void findAll() {
        Long themeId = testHelper.insertTheme("테마1", "설명1", "img1.jpg");
        Long timeId = testHelper.insertReservationTime(LocalTime.of(10, 0));
        LocalDate date = LocalDate.of(2026, 5, 10);
        testHelper.insertReservation("스타크", date, themeId, timeId);

        List<ReservationDetail> details = reservationDao.findAll();
        ReservationDetail first = details.getFirst();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(details).hasSize(1);
            softly.assertThat(first.username()).isEqualTo("스타크");
            softly.assertThat(first.date()).isEqualTo(date);
            softly.assertThat(first.themeId()).isEqualTo(themeId);
            softly.assertThat(first.timeId()).isEqualTo(timeId);
            softly.assertThat(first.startAt()).isEqualTo(LocalTime.of(10, 0));
        });
    }
}
