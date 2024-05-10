package roomescape.reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.handler.exception.CustomException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.ReservationRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.fixture.MemberFixture.memberFixture;
import static roomescape.fixture.ReservationTimeFixture.reservationTimeFixture;
import static roomescape.fixture.ThemeFixture.themeFixture;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationServiceTest {

    private final LocalDate reservationDate = LocalDate.of(2999, 8, 5);
    private final LocalDateTime createdAt = LocalDateTime.of(2024, 5, 8, 12, 30);
    private final ReservationRequest reservationRequest = new ReservationRequest(reservationDate, reservationTimeFixture.getId(), themeFixture.getId());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", themeFixture.getName(), themeFixture.getDescription(), themeFixture.getThumbnail());
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", reservationTimeFixture.getStartAt());
        jdbcTemplate.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)", memberFixture.getName(), memberFixture.getEmail(), memberFixture.getPassword(), memberFixture.getRole().toString());
    }

    @DisplayName("예약시간이 없는 경우 예외가 발생한다.")
    @Test
    void reservationTimeIsNotExist() {
        ReservationRequest request = new ReservationRequest(reservationDate, 100L, themeFixture.getId());
        assertThatThrownBy(() -> reservationService.createReservation(memberFixture, request))
                .isInstanceOf(CustomException.class);
    }

    @DisplayName("예약 생성 테스트")
    @Test
    void createReservation() {
        reservationService.createReservation(memberFixture, reservationRequest);
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @DisplayName("모든 예약 조회 테스트")
    @Test
    void findAllReservations() {
        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id, created_at) VALUES (?, ?, ?, ?, ?)",
                memberFixture.getId(), reservationDate, reservationTimeFixture.getId(), themeFixture.getId(), createdAt);
        List<Reservation> reservations = reservationService.findAllReservations();

        assertAll(
                () -> assertThat(reservations).hasSize(1),
                () -> assertThat(reservations.get(0).getMember()).isEqualTo(memberFixture),
                () -> assertThat(reservations.get(0).getDate()).isEqualTo(reservationDate)
        );
    }

    @DisplayName("예약 삭제 테스트")
    @Test
    void deleteReservation() {
        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id, created_at) VALUES (?, ?, ?, ?, ?)", 1, "2999-12-12", 1, 1, "2024-05-08 12:30");
        reservationService.deleteReservation(1L);
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);
        assertThat(count).isEqualTo(0);
    }
}
