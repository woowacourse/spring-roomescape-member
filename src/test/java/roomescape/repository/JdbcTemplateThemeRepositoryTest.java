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
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Sha256Encryptor;
import roomescape.domain.Theme;

@SpringBootTest
class JdbcTemplateThemeRepositoryTest {

    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void init() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    @DisplayName("전체 테마 조회를 잘 하는지 확인")
    void findAll() {
        Theme theme = new Theme("name", "description", "http://example.com");
        theme = themeRepository.save(theme);
        List<Theme> allTheme = themeRepository.findAll();

        Assertions.assertThat(allTheme)
                .containsExactly(theme);
    }

    @Test
    void findAndOrderByPopularity() {
        Theme theme1 = themeRepository.save(new Theme("name1", "description1", "http://thumbnail1"));
        Theme theme2 = themeRepository.save(new Theme("name2", "description2", "http://thumbnail2"));
        Theme theme3 = themeRepository.save(new Theme("name3", "description3", "http://thumbnail3"));

        ReservationTime reservationTime1 = reservationTimeRepository.save(new ReservationTime(LocalTime.of(1, 30)));
        ReservationTime reservationTime2 = reservationTimeRepository.save(new ReservationTime(LocalTime.of(2, 30)));
        ReservationTime reservationTime3 = reservationTimeRepository.save(new ReservationTime(LocalTime.of(3, 30)));

        LocalDate date = LocalDate.now().plusDays(1);
        Sha256Encryptor encryptor = new Sha256Encryptor();
        Member member = new Member(1L, "name", "email@email.com", encryptor.encrypt("1234"));
        reservationRepository.save(new Reservation(member, date, reservationTime2, theme2));
        reservationRepository.save(new Reservation(member, date, reservationTime1, theme2));
        reservationRepository.save(new Reservation(member, date, reservationTime3, theme2));

        reservationRepository.save(new Reservation(member, date, reservationTime1, theme1));
        reservationRepository.save(new Reservation(member, date, reservationTime2, theme1));

        reservationRepository.save(new Reservation(member, date, reservationTime1, theme3));

        List<Theme> result = themeRepository.findAndOrderByPopularity(date, date.plusDays(1), 10);
        Assertions.assertThat(result)
                .containsExactly(theme2, theme1, theme3);
    }

    @Test
    @DisplayName("테마가 잘 지워지는지 확인")
    void delete() {
        Theme theme = themeRepository.save(new Theme("name1", "description1", "http://thumbnail"));

        themeRepository.delete(theme.getId());

        Assertions.assertThat(themeRepository.findAll())
                .isEmpty();
    }
}
