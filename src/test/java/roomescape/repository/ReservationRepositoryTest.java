package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ThemeResponse;
import roomescape.dto.TimeResponse;
import roomescape.model.Reservation;

@JdbcTest
public class ReservationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ReservationRepository reservationRepository;

    @BeforeEach
    public void setUp() {
        reservationRepository = new ReservationRepository(jdbcTemplate);
    }

    @Test
    void 모든_예약을_조회한다() {
        // when
        List<Reservation> reservations = reservationRepository.findAll();
        // then
        Assertions.assertEquals(14, reservations.size());
    }

    @Test
    void 예약을_삭제할_수_있다() {
        // when
        reservationRepository.deleteById(2L);
        // then
        Assertions.assertEquals(13, reservationRepository.findAll().size());
    }

    @Test
    void 예약을_생성할_수_있다() {
        // given
        ReservationRequest reservationRequest = new ReservationRequest("포비", LocalDate.of(2026, 5, 5), 1L, 1L);
        TimeResponse timeResponse = new TimeResponse(1L, LocalTime.of(10, 0));
        ThemeResponse themeResponse = new ThemeResponse(1L, "붉은 요람",
                "10년 전 의문의 화재로 폐쇄된 안개마을 영아원. 그곳에서 들려오는 울음소리의 정체를 밝혀야 합니다.", "https://escape.com/images/red-cradle.png");

        // when
        Reservation reservation = reservationRepository.save(reservationRequest, timeResponse, themeResponse);

        // then
        Assertions.assertEquals(15, reservationRepository.findAll().size());
        Assertions.assertEquals(15L, reservation.getId());
        Assertions.assertEquals(reservationRequest.name(), reservation.getName());
        Assertions.assertEquals(reservationRequest.date(), reservation.getDate());
        Assertions.assertEquals(timeResponse, TimeResponse.from(reservation.getTime()));
        Assertions.assertEquals(themeResponse, ThemeResponse.from(reservation.getTheme()));
    }
}
