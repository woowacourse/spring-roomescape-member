package roomescape.theme.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.theme.domain.Theme;

@JdbcTest
class JdbcThemeRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    JdbcThemeRepository themeRepository;

    @Autowired
    public JdbcThemeRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.themeRepository = new JdbcThemeRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("기존에 이미 존재하는 시간을 추가하면 예외가 발생한다.")
    void saveTest_duplicate() {
        // given
        themeRepository.save(
                new Theme(null, "테마", "테마 설명", "썸네일_url")
        );

        // when & then
        assertThatThrownBy(
                () ->  themeRepository.save(
                        new Theme(null, "테마", "테마 설명", "썸네일_url")
                )
        ).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("ID가 없으면 예외가 발생한다.")
    void deleteByIdTest_id_not_exist() {
        assertThatThrownBy(
                () -> themeRepository.deleteById(999L)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 id의 테마가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("ID가 사용되고 있으면 예외가 발생한다.")
    void deleteByIdTest_used() {
        //given
        jdbcTemplate.update(
                "INSERT INTO reservation_time (start_at) VALUES (?)",
                Time.valueOf(LocalTime.of(10, 0))
        );

        long timeId = jdbcTemplate.queryForObject(
                "SELECT id FROM reservation_time WHERE start_at = ?",
                Long.class,
                Time.valueOf(LocalTime.of(10, 0))
        );

        themeRepository.save(
                new Theme(null, "테마", "테마 설명", "썸네일_url")
        );

        Long themeId = jdbcTemplate.queryForObject(
                "SELECT id FROM theme WHERE name = ?",
                Long.class,
                "테마"
        );

        jdbcTemplate.update("""
            insert into reservation(name, reservation_date, time_id, theme_id)
            values (?, ?, ?, ?)
        """, "브라운", LocalDate.of(2026, 5, 6),timeId, themeId
        );

        //when & then
        assertThatThrownBy(
                () -> themeRepository.deleteById(themeId)
        ).isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage("예약에 사용 중인 테마는 삭제할 수 없습니다.");
    }
}
