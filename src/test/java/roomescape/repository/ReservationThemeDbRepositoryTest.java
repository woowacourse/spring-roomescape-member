package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.repository.dao.ReservationThemeH2Dao;

@JdbcTest
@ActiveProfiles("test")
@Import({ReservationThemeDbRepository.class, ReservationThemeH2Dao.class})
class ReservationThemeDbRepositoryTest {

    @Autowired
    private ReservationThemeDbRepository reservationThemeDbRepository;

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
    }

    @DisplayName("존재하지 않는 id를 조회하면 예외를 발생시킨다")
    @Test
    void getByIdException() {
        // given
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름1", "설명1", "썸네일1");

        // when & then
        assertThatThrownBy(() -> reservationThemeDbRepository.getById(2L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하는 id를 조회하면 조회된 예약 객체를 반환한다")
    @Test
    void getById() {
        // given
        String name = "이름";
        String description = "설명";
        String thumbnail = "썸네일";
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", name, description, thumbnail);

        // when
        ReservationTheme reservationTheme = reservationThemeDbRepository.getById(1L);

        // then
        assertSoftly(softly -> {
            softly.assertThat(reservationTheme.name()).isEqualTo(name);
            softly.assertThat(reservationTheme.description()).isEqualTo(description);
            softly.assertThat(reservationTheme.thumbnail()).isEqualTo(thumbnail);
        });
    }
}
