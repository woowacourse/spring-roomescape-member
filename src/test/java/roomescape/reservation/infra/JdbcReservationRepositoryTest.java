package roomescape.reservation.infra;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.support.TestDataHelper;

@JdbcTest
class JdbcReservationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    ReservationRepository reservationRepository;
    TestDataHelper testHelper;

    @BeforeEach
    void setUp() {
        reservationRepository = new JdbcReservationRepository(jdbcTemplate);
        testHelper = new TestDataHelper(jdbcTemplate);
    }

    @DisplayName("ID로 예약 조회를 테스트합니다.")
    @Test
    void find_by_id() {
        Long themeId = testHelper.insertTheme("테마1", "설명1", "img1.jpg");
        Long timeId = testHelper.insertReservationTime(LocalTime.of(9, 0));
        LocalDate date = LocalDate.of(2026, 5, 6);
        Long reservationId = testHelper.insertReservation("스타크", date, themeId, timeId);

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(reservation.getId()).isEqualTo(reservationId);
            softly.assertThat(reservation.getName()).isEqualTo("스타크");
            softly.assertThat(reservation.getDate()).isEqualTo(date);
            softly.assertThat(reservation.getThemeId()).isEqualTo(themeId);
            softly.assertThat(reservation.getTimeId()).isEqualTo(timeId);
        });
    }

    @DisplayName("존재하지 않는 ID로 예약 조회 시 빈 Optional 반환을 테스트합니다.")
    @Test
    void find_by_id_not_found() {
        assertThat(reservationRepository.findById(1L)).isEmpty();
    }

    @DisplayName("방탈출 예약 추가를 테스트합니다.")
    @Test
    void save_user_reservation() {
        Long timeId = testHelper.insertReservationTime(LocalTime.of(9, 0));
        Long themeId = testHelper.insertTheme("theme name", "theme description", "theme img url");

        Reservation reservation = Reservation.builder()
                .name("name")
                .date(LocalDate.of(2026, 5, 4))
                .themeId(themeId)
                .timeId(timeId)
                .build();

        Reservation savedReservation = reservationRepository.save(reservation);

        SoftAssertions.assertSoftly(assertSoftly -> {
            assertSoftly.assertThat(savedReservation.getName()).isEqualTo("name");
            assertSoftly.assertThat(savedReservation.getDate()).isEqualTo(LocalDate.of(2026, 5, 4));
            assertSoftly.assertThat(savedReservation.getThemeId()).isEqualTo(themeId);
            assertSoftly.assertThat(savedReservation.getTimeId()).isEqualTo(timeId);
        });
    }

    @DisplayName("방탈출 예약 삭제를 테스트합니다.")
    @Test
    void delete_user_reservation() {
        Long themeId = testHelper.insertTheme("테마1", "설명1", "img1.jpg");
        Long timeId = testHelper.insertReservationTime(LocalTime.of(9, 0));
        LocalDate date = LocalDate.of(2026, 5, 6);
        Long reservationId = testHelper.insertReservation("스타크", date, themeId, timeId);

        assertThat(reservationRepository.delete(reservationId)).isEqualTo(1);
    }
}
