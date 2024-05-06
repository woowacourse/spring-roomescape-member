package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.ReservationCreateRequest;
import roomescape.exception.ExistReservationException;
import roomescape.exception.IllegalReservationException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("예약을 생성한 아이디를 반환한다.")
    @Test
    void save() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명", "썸네일");


        // when
        long id = reservationService.save(new ReservationCreateRequest(null, "커비", LocalDate.of(2099, 12, 31), 1L, 1L));

        // then
        assertThat(id).isEqualTo(1);
    }

    @DisplayName("과거 날짜의 예약을 생성하려고 하면 예외를 발생한다.")
    @Test
    void save_pastDate_IllegalReservationException() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명", "썸네일");


        // when && then
        assertThatThrownBy(() -> reservationService.save(new ReservationCreateRequest(null, "커비", LocalDate.of(2000,1,1), 1L, 1L)))
                .isInstanceOf(IllegalReservationException.class);
    }

    @DisplayName("과거 시간의 예약을 생성하려고 하면 예외를 발생한다.")
    @Test
    void save_pastTime_IllegalTimeException() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "00:01");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명", "썸네일");


        // when && then
        assertThatThrownBy(() -> reservationService.save(new ReservationCreateRequest(null, "커비", LocalDate.now(), 1L, 1L)))
                .isInstanceOf(IllegalReservationException.class);
    }


    @DisplayName("중복된 예약을 생성하려고 하면 예외를 발생한다.")
    @Test
    void save_duplicatedTime_IllegalTimeException() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "커비", "2099-12-31", 1, 1);

        // when && then
        assertThatThrownBy(() -> reservationService.save(new ReservationCreateRequest(null, "커비", LocalDate.of(2099,12,31), 1L, 1L)))
                .isInstanceOf(ExistReservationException.class);
    }
}
