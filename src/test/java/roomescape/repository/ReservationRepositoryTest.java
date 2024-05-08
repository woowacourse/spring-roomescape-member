package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;

@SpringBootTest
@Transactional
class ReservationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("예약 생성")
    @Test
    void save() {
        final ReservationTime reservationTime = new ReservationTime(LocalTime.parse("08:00"));
        final ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        final Theme theme = new Theme("이름", "설명", "썸네일");
        final Theme savedTheme = themeRepository.save(theme);

        final Member member = new Member("생강", "email@email.com", "1234");
        final Member savedMember = memberRepository.save(member);

        final Reservation reservation = new Reservation(savedMember, LocalDate.parse("2025-01-01"), savedReservationTime, savedTheme);
        final Reservation savedReservation = reservationRepository.save(reservation);
        assertAll(
                () -> assertThat(savedReservation.getMember().getName()).isEqualTo("생강"),
                () -> assertThat(savedReservation.getDate()).isEqualTo("2025-01-01"),
                () -> assertThat(savedReservation.getTime()).isEqualTo(savedReservationTime),
                () -> assertThat(savedReservation.getTheme()).isEqualTo(savedTheme)
        );
    }

    @DisplayName("존재하는 예약 삭제")
    @Test
    void deleteExistById() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?), (?)", "08:00", "07:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO member (name, email, password) VALUES (?, ?, ?)", "백호", "email@email.com", "1234");
        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?), (?, ?, ?, ?)",
                1L, "2024-07-07", 1L, 1L,
                1L, "2024-08-12", 2L, 1L);

        assertThatCode(() -> reservationRepository.deleteById(1L))
                .doesNotThrowAnyException();
    }
}
