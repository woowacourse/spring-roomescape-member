package roomescape.infra;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@JdbcTest
@Import({
        JdbcReservationRepository.class,
        JdbcReservationTimeRepository.class,
        JdbcThemeRepository.class
})
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository timeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    void 예약을_저장한다() {
        ReservationTime reservationTime = createReservationTime(LocalTime.of(10, 0));
        Theme theme = createTheme();

        Long saveId = reservationRepository.save(new Reservation(
                "브라운", LocalDate.parse("2026-05-15"), reservationTime, theme));

        Optional<Reservation> reservation = reservationRepository.findById(saveId);

        assertThat(reservation).isPresent();

        assertThat(reservation.get().getId()).isNotNull();
        assertThat(reservation.get().getName()).isEqualTo("브라운");
        assertThat(reservation.get().getDate()).isEqualTo(LocalDate.parse("2026-05-15"));
        assertThat(reservation.get().getTime().getId()).isEqualTo(reservationTime.getId());
        assertThat(reservation.get().getTheme().getId()).isEqualTo(theme.getId());
    }

    @Test
    void 모든_예약을_날짜_내림차순_시간_오름차순으로_조회한다() {
        ReservationTime tenClock = createReservationTime(LocalTime.of(10, 0));
        ReservationTime twelveClock = createReservationTime(LocalTime.of(12, 0));
        Theme theme = createTheme();

        reservationRepository.save(new Reservation(
                "브리", LocalDate.parse("2026-05-15"), tenClock, theme));
        reservationRepository.save(new Reservation(
                "브라운", LocalDate.parse("2026-05-16"), twelveClock, theme));
        reservationRepository.save(new Reservation(
                "브리", LocalDate.parse("2026-05-16"), tenClock, theme));

        List<Reservation> reservations = reservationRepository.findAll();

        assertThat(reservations).hasSize(3);

        Reservation firstResult = reservations.getFirst();
        assertThat(firstResult.getName()).isEqualTo("브리");
        assertThat(firstResult.getDate()).isEqualTo(LocalDate.parse("2026-05-16"));
        assertThat(firstResult.getTime().getId()).isEqualTo(tenClock.getId());
        assertThat(firstResult.getTheme().getId()).isEqualTo(theme.getId());
    }

    @Test
    void 예약_id에_맞는_예약을_조회한다() {
        ReservationTime tenClock = createReservationTime(LocalTime.of(10, 0));
        ReservationTime twelveClock = createReservationTime(LocalTime.of(12, 0));
        Theme theme = createTheme();

        reservationRepository.save(new Reservation(
                "브라운", LocalDate.parse("2026-05-16"), twelveClock, theme));
        Long findId = reservationRepository.save(new Reservation(
                "브리", LocalDate.parse("2026-05-16"), tenClock, theme));

        Optional<Reservation> reservation = reservationRepository.findById(findId);

        assertThat(reservation).isPresent();

        assertThat(reservation.get().getId()).isNotNull();
        assertThat(reservation.get().getName()).isEqualTo("브리");
        assertThat(reservation.get().getDate()).isEqualTo(LocalDate.parse("2026-05-16"));
        assertThat(reservation.get().getTime().getId()).isEqualTo(tenClock.getId());
        assertThat(reservation.get().getTheme().getId()).isEqualTo(theme.getId());
    }

    @Test
    void 날짜_테마에_따른_예약_id_조회() {
        LocalDate findDate = LocalDate.parse("2026-05-16");
        ReservationTime tenClock = createReservationTime(LocalTime.of(10, 0));
        ReservationTime twelveClock = createReservationTime(LocalTime.of(12, 0));
        Theme findTheme = createTheme();

        Long firstSaveId = reservationRepository.save(new Reservation(
                "브라운", findDate, twelveClock, findTheme));
        Long secondSaveId = reservationRepository.save(new Reservation(
                "브리", findDate, tenClock, findTheme));
        Long thirdSaveId = reservationRepository.save(new Reservation(
                "브리", LocalDate.parse("2026-05-15"), tenClock, findTheme));

        Set<Long> findReservationsId = reservationRepository.findReservedTimeIdsByDateAndThemeId(findDate,
                findTheme.getId());

        assertThat(findReservationsId).hasSize(2);
        assertThat(findReservationsId.contains(firstSaveId)).isTrue();
        assertThat(findReservationsId.contains(secondSaveId)).isTrue();
        assertThat(findReservationsId.contains(thirdSaveId)).isFalse();
    }

    @Test
    void 시간_id_존재여부() {
        ReservationTime reservationTime = createReservationTime(LocalTime.of(10, 0));
        Theme theme = createTheme();

        reservationRepository.save(new Reservation(
                "브라운", LocalDate.parse("2026-05-15"), reservationTime, theme));

        assertThat(reservationRepository.existsByTimeId(reservationTime.getId())).isTrue();
        assertThat(reservationRepository.existsByTimeId(2L)).isFalse();
    }

    @Test
    void 테마_id_존재여부() {
        ReservationTime reservationTime = createReservationTime(LocalTime.of(10, 0));
        Theme theme = createTheme();

        reservationRepository.save(new Reservation(
                "브라운", LocalDate.parse("2026-05-15"), reservationTime, theme));

        assertThat(reservationRepository.existsByThemeId(theme.getId())).isTrue();
        assertThat(reservationRepository.existsByThemeId(2L)).isFalse();
    }

    @Test
    void 날짜_시간_테마_id가_존재한다() {
        LocalDate findDate = LocalDate.parse("2026-05-16");
        ReservationTime reservationTime = createReservationTime(LocalTime.of(10, 0));
        Theme theme = createTheme();

        reservationRepository.save(new Reservation(
                "브라운", findDate, reservationTime, theme));

        assertThat(reservationRepository.existsBy(findDate, reservationTime.getId(), theme.getId())).isTrue();
    }

    @Test
    void 예약을_삭제한다() {
        ReservationTime reservationTime = createReservationTime(LocalTime.of(10, 0));
        Theme theme = createTheme();

        Long saveId = reservationRepository.save(new Reservation(
                "브라운", LocalDate.parse("2026-05-15"), reservationTime, theme));
        reservationRepository.deleteById(saveId);

        assertThat(reservationRepository.findById(saveId)).isEmpty();
    }

    private ReservationTime createReservationTime(LocalTime time) {
        ReservationTime reservationTime = new ReservationTime(time);
        Long id = timeRepository.save(reservationTime);
        return new ReservationTime(id, reservationTime.getStartAt());
    }

    private Theme createTheme() {
        Theme theme = new Theme("방탈출 제목", "방탈출 설명", "thumbnail.png");
        Long id = themeRepository.save(theme);
        return new Theme(
                id,
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnailImageUrl()
        );
    }
}
