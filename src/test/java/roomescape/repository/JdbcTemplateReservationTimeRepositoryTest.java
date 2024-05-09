package roomescape.repository;

import static org.junit.jupiter.api.Assertions.assertAll;

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
import roomescape.domain.Theme;

@SpringBootTest
class JdbcTemplateReservationTimeRepositoryTest {
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void init() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    @DisplayName("ReservationTime 을 잘 저장하는지 확인한다.")
    void save() {
        var beforeSave = reservationTimeRepository.findAll().stream().map(ReservationTime::getId).toList();
        ReservationTime saved = reservationTimeRepository.save(new ReservationTime(LocalTime.now()));
        var afterSave = reservationTimeRepository.findAll().stream().map(ReservationTime::getId).toList();

        Assertions.assertThat(afterSave)
                .containsAll(beforeSave)
                .contains(saved.getId());
    }

    @Test
    @DisplayName("ReservationTime 을 잘 조회하는지 확인한다.")
    void findAll() {
        List<ReservationTime> beforeSave = reservationTimeRepository.findAll();
        reservationTimeRepository.save(new ReservationTime(LocalTime.now()));
        reservationTimeRepository.save(new ReservationTime(LocalTime.now()));

        List<ReservationTime> afterSave = reservationTimeRepository.findAll();

        Assertions.assertThat(afterSave.size())
                .isEqualTo(beforeSave.size() + 2);
    }

    @Test
    @DisplayName("ReservationTime 을 잘 지우하는지 확인한다.")
    void delete() {
        List<ReservationTime> beforeSaveAndDelete = reservationTimeRepository.findAll();
        reservationTimeRepository.save(new ReservationTime(LocalTime.now()));

        reservationTimeRepository.delete(1L);

        List<ReservationTime> afterSaveAndDelete = reservationTimeRepository.findAll();

        Assertions.assertThat(beforeSaveAndDelete)
                .containsExactlyInAnyOrderElementsOf(afterSaveAndDelete);
    }

    @Test
    @DisplayName("특정 시작 시간을 가지는 예약 시간이 있는지 여부를 잘 반환하는지 확인한다.")
    void existsByStartAt() {
        LocalTime time = LocalTime.of(11, 20);
        reservationTimeRepository.save(new ReservationTime(time));

        assertAll(
                () -> Assertions.assertThat(reservationTimeRepository.existsByStartAt(time))
                        .isTrue(),
                () -> Assertions.assertThat(reservationTimeRepository.existsByStartAt(time.plusHours(1)))
                        .isFalse()
        );
    }

    @Test
    @DisplayName("특정 날짜와 테마에 예약이 있는 예약 시간의 목록을 잘 반환하는지 확인한다.")
    void findUsedTimeByDateAndTheme() {
        LocalDate date = LocalDate.of(2024, 12, 30);
        Theme theme = new Theme("name", "description", "http://example.com");
        theme = themeRepository.save(theme);
        ReservationTime time = new ReservationTime(LocalTime.of(12, 30));
        time = reservationTimeRepository.save(time);
        Member member = memberRepository.findById(1L).orElseThrow();
        reservationRepository.save(new Reservation(member, date, time, theme));

        List<ReservationTime> response = reservationTimeRepository.findUsedTimeByDateAndTheme(date, theme);

        Assertions.assertThat(response)
                .containsExactly(time);
    }
}
