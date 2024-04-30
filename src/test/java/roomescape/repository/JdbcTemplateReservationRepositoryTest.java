package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@SpringBootTest
class JdbcTemplateReservationRepositoryTest {
    private static final ReservationTime DEFAULT_TIME = new ReservationTime(1L, LocalTime.of(11, 56));
    private static final Theme DEFAULT_THEME = new Theme(1L, "이름", "설명", "썸네일");

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void init() {
        jdbcTemplate.update("delete from reservation");
        jdbcTemplate.update("ALTER TABLE reservation alter column id restart with 1");
        jdbcTemplate.update("delete from reservation_time");
        jdbcTemplate.update("ALTER TABLE reservation_time alter column id restart with 1");
        jdbcTemplate.update("insert into reservation_time(start_at) values('11:56')");
    }

    @Test
    @DisplayName("Reservation 을 잘 저장하는지 확인한다.")
    void save() {
        var beforeSave = reservationRepository.findAll();
        Reservation saved = reservationRepository.save(
                new Reservation("test", LocalDate.now(), DEFAULT_TIME, DEFAULT_THEME));
        var afterSave = reservationRepository.findAll();

        Assertions.assertThat(afterSave)
                .containsAll(beforeSave)
                .contains(saved);
    }

    @Test
    @DisplayName("Reservation 을 잘 조회하는지 확인한다.")
    void findAll() {
        List<Reservation> beforeSave = reservationRepository.findAll();
        reservationRepository.save(new Reservation("test", LocalDate.now(), DEFAULT_TIME, DEFAULT_THEME));
        reservationRepository.save(new Reservation("test2", LocalDate.now(), DEFAULT_TIME, DEFAULT_THEME));

        List<Reservation> afterSave = reservationRepository.findAll();
        Assertions.assertThat(afterSave.size())
                .isEqualTo(beforeSave.size() + 2);
    }

    @Test
    @DisplayName("Reservation 을 잘 지우는지 확인한다.")
    void delete() {
        List<Reservation> beforeSaveAndDelete = reservationRepository.findAll();
        reservationRepository.save(new Reservation("test", LocalDate.now(), DEFAULT_TIME, DEFAULT_THEME));

        reservationRepository.delete(1L);

        List<Reservation> afterSaveAndDelete = reservationRepository.findAll();

        Assertions.assertThat(beforeSaveAndDelete)
                .containsExactlyElementsOf(afterSaveAndDelete);
    }
}
