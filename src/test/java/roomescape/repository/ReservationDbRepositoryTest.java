package roomescape.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.Reservation;
import roomescape.repository.dao.ReservationH2Dao;

@JdbcTest
@ActiveProfiles("test")
@Import({ReservationDbRepository.class, ReservationH2Dao.class})
class ReservationDbRepositoryTest {

    @Autowired
    private ReservationDbRepository reservationDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("""
                SET REFERENTIAL_INTEGRITY FALSE;
                TRUNCATE TABLE reservation;
                ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1;
                TRUNCATE TABLE reservation_time;
                ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1;
                TRUNCATE TABLE theme;
                ALTER TABLE theme ALTER COLUMN id RESTART WITH 1;
                SET REFERENTIAL_INTEGRITY TRUE;
                """);
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명", "썸네일");
    }

    @DisplayName("존재하지 않는 id를 조회하면 예외를 발생시킨다")
    @Test
    void getByIdException() {
        // given
        jdbcTemplate.update("INSERT INTO RESERVATION(name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "브라운",
                LocalDate.now().plusDays(20), "1", "1");

        // when & then
        assertThatThrownBy(() -> reservationDbRepository.getById(2L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하는 id를 조회하면 조회된 예약 객체를 반환한다")
    @Test
    void getById() {
        // given
        String name = "브라운";
        LocalDate date = LocalDate.now().plusDays(20);
        jdbcTemplate.update("INSERT INTO RESERVATION(name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", name,
                date, "1", "1");

        // when
        Reservation reservation = reservationDbRepository.getById(1L);

        // then
        assertThat(reservation.name()).isEqualTo(name);
        assertThat(reservation.date()).isEqualTo(date);
    }
}
