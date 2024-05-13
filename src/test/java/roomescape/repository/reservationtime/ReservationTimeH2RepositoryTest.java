package roomescape.repository.reservationtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.InitialDataFixture.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
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
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.theme.ThemeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("/initial_test_data.sql")
class ReservationTimeH2RepositoryTest {

    @Autowired
    private ReservationTimeH2Repository reservationTimeH2Repository;
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

    @Test
    @DisplayName("ReservationTime을 저장한다.")
    void save() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(12, 0));

        ReservationTime saved = reservationTimeH2Repository.save(reservationTime);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("id에 맞는 ReservationTime을 제거한다.")
    void delete() {
        Member member = memberRepository.save(USER_1);
        Theme theme = themeRepository.save(THEME_1);
        ReservationTime time = reservationTimeRepository.save(RESERVATION_TIME_1);
        Reservation reservation = reservationRepository.save(new Reservation(member, LocalDate.now().plusDays(1), time, theme));

        Integer beforeCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation_time", Integer.class);
        reservationTimeH2Repository.delete(reservation.getId());
        Integer afterCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation_time", Integer.class);
        assertThat(afterCount).isEqualTo(beforeCount - 1);
    }

    @Test
    @DisplayName("참조되어 있는 시간을 삭제하는 경우 예외가 발생한다.")
    void deleteReferencedGetTime() {
        Member member = memberRepository.save(USER_1);
        Theme theme = themeRepository.save(THEME_1);
        ReservationTime time = reservationTimeRepository.save(RESERVATION_TIME_1);
        Reservation reservation = reservationRepository.save(new Reservation(member, LocalDate.now().plusDays(1), time, theme));

        assertThatThrownBy(() -> reservationTimeH2Repository.delete(reservation.getTime().getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("id에 맞는 ReservationTime을 찾는다.")
    void findByIdTest() {
        ReservationTime reservationTime = reservationTimeH2Repository.save(new ReservationTime(LocalTime.now()));
        ReservationTime found = reservationTimeH2Repository.findById(reservationTime.getId()).get();

        assertThat(found.getStartAt()).isEqualTo(reservationTime.getStartAt());
    }

    @Test
    @DisplayName("존재하지 않는 id가 들어오면 빈 Optional 객체를 반환한다.")
    void findEmpty() {
        Optional<ReservationTime> reservationTime = reservationTimeH2Repository.findById(-1L);

        assertThat(reservationTime.isEmpty()).isTrue();
    }
}
