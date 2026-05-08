package roomescape.reservation.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.JdbcReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.JdbcThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({
        JdbcReservationRepository.class,
        JdbcReservationTimeRepository.class,
        JdbcThemeRepository.class
})
class JdbcReservationRepositoryTest {

    @Autowired
    private JdbcReservationRepository jdbcReservationRepository;

    @Autowired
    private JdbcReservationTimeRepository jdbcReservationTimeRepository;

    @Autowired
    private JdbcThemeRepository jdbcThemeRepository;

    @Test
    @DisplayName("예약을 저장하고 조회한다.")
    void saveAndFindAll() {
        ReservationTime time = jdbcReservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme theme = jdbcThemeRepository.save(new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png"));
        Reservation reservation = jdbcReservationRepository.save(new Reservation("브라운", LocalDate.of(2023, 8, 5), time, theme));

        List<Reservation> reservations = jdbcReservationRepository.findAll();

        assertThat(reservations).hasSize(1);

        Reservation found = reservations.getFirst();
        assertThat(found.getId()).isEqualTo(reservation.getId());
        assertThat(found.getName()).isEqualTo("브라운");
        assertThat(found.getDate()).isEqualTo(LocalDate.of(2023, 8, 5));

        assertThat(found.getTime().getId()).isEqualTo(time.getId());
        assertThat(found.getTime().getStartAt()).isEqualTo(LocalTime.of(10, 0));

        assertThat(found.getTheme().getId()).isEqualTo(theme.getId());
        assertThat(found.getTheme().getName()).isEqualTo(theme.getName());
        assertThat(found.getTheme().getDescription()).isEqualTo(theme.getDescription());
        assertThat(found.getTheme().getThumbnail()).isEqualTo(theme.getThumbnail());
    }

    @Test
    @DisplayName("특정 날짜 및 테마 id를 가진 예약 정보를 조회한다.")
    void findByDateAndThemeId() {
        // given
        ReservationTime time = jdbcReservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme targetTheme = jdbcThemeRepository.save(new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png"));
        Theme nonTargetTheme = jdbcThemeRepository.save(new Theme("레벨2 입장", "우테코 레벨2에 입장하는 내용입니다.", "https://example.com/theme2.png"));

        LocalDate targetDate = LocalDate.of(2023, 8, 5);
        Reservation match = jdbcReservationRepository.save(new Reservation("브라운", targetDate, time, targetTheme));
        Reservation nonMatch1 = jdbcReservationRepository.save(new Reservation("브라운", LocalDate.of(2024, 9, 10), time, targetTheme));
        Reservation nonMatch2 = jdbcReservationRepository.save(new Reservation("브라운", targetDate, time, nonTargetTheme));


        // when
        List<Reservation> results = jdbcReservationRepository.findByDateAndThemeId(targetDate, targetTheme.getId());

        // then
        assertThat(results).hasSize(1)
                .contains(match)
                .doesNotContain(nonMatch1, nonMatch2);
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void deleteById() {
        ReservationTime time = jdbcReservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme theme = jdbcThemeRepository.save(new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png"));
        Reservation reservation = jdbcReservationRepository.save(new Reservation("브라운", LocalDate.of(2023, 8, 5), time, theme));

        jdbcReservationRepository.deleteById(reservation.getId());

        assertThat(jdbcReservationRepository.findAll()).isEmpty();
    }
}
