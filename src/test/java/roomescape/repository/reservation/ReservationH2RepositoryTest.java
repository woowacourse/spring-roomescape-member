package roomescape.repository.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.member.MemberRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;
import roomescape.repository.theme.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.InitialDataFixture.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("/initial_test_data.sql")
class ReservationH2RepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    private Reservation reservation;

    @BeforeEach
    void setup() {
        Member member = memberRepository.save(USER_1);
        Theme theme = themeRepository.save(THEME_1);
        ReservationTime time = reservationTimeRepository.save(RESERVATION_TIME_1);
        reservation = reservationRepository.save(new Reservation(member, LocalDate.now().plusDays(1), time, theme));
    }

    @Test
    @DisplayName("Reservation을 저장하면 id가 포함된 Reservation이 반환된다.")
    void save() {
        assertThat(reservation.getId()).isNotNull();
    }

    @Test
    @DisplayName("Reservation을 제거한다.")
    void delete() {
        Integer beforeCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);

        reservationRepository.delete(reservation.getId());
        Integer afterCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);

        assertThat(afterCount).isEqualTo(beforeCount - 1);
    }
}
