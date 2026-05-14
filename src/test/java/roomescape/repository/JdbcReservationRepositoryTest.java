package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
@Import({JdbcReservationRepository.class, JdbcReservationTimeRepository.class, JdbcThemeRepository.class})
class JdbcReservationRepositoryTest {

    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ReservationTimeRepository reservationTimeRepository;
    @Autowired
    ThemeRepository themeRepository;

    @DisplayName("예약을 저장한다")
    @Test
    void 예약을_저장하면_id를_부여한다() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.save(
                ReservationTime.withoutId(LocalTime.parse("10:20:30"))
        );
        Theme theme = themeRepository.save(
                Theme.withoutId("귀신찾기", "귀신을 찾는다", "example.com")
        );
        Reservation reservation =
                Reservation.withoutId("루드비코", LocalDate.parse("2026-05-06"), reservationTime, theme);

        // when
        Reservation saved = reservationRepository.save(reservation);

        // then
        assertThat(saved.getId()).isNotNull();
    }

    @DisplayName("예약을 id로 조회한다")
    @Test
    void 예약을_id로_조회한다() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.save(
                ReservationTime.withoutId(LocalTime.parse("10:20:30"))
        );
        Theme theme = themeRepository.save(
                Theme.withoutId("귀신찾기", "귀신을 찾는다", "example.com")
        );
        Reservation reservation =
                Reservation.withoutId("루드비코", LocalDate.parse("2026-05-06"), reservationTime, theme);

        // when
        Reservation saved = reservationRepository.save(reservation);
        Optional<Reservation> result = reservationRepository.findById(saved.getId());

        // then
        assertThat(result.get())
                .usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @DisplayName("저장된 모든 예약을 조회한다")
    @Test
    void 저장된_모든_예약을_조회한다() {
        // given
        ReservationTime reservationTime1 = reservationTimeRepository.save(
                ReservationTime.withoutId(LocalTime.parse("10:20:30"))
        );
        Theme theme = themeRepository.save(
                Theme.withoutId("귀신찾기", "귀신을 찾는다", "example.com")
        );
        Reservation rudevicoReservation =
                Reservation.withoutId("루드비코", LocalDate.parse("2026-05-06"), reservationTime1, theme);

        ReservationTime reservationTime2 = reservationTimeRepository.save(
                ReservationTime.withoutId(LocalTime.parse("11:20:30"))
        );
        Reservation cocoReservation =
                Reservation.withoutId("코코", LocalDate.parse("2026-05-06"), reservationTime2, theme);

        // when
        Reservation savedRudevicoReservation = reservationRepository.save(rudevicoReservation);
        Reservation savedCocoReservation = reservationRepository.save(cocoReservation);

        List<Reservation> foundReservations = reservationRepository.findAll();

        // then
        assertThat(foundReservations)
                .hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(savedRudevicoReservation, savedCocoReservation);
    }

    @DisplayName("특정 사용자에 대한 모든 예약을 조회한다")
    @Test
    void 사용자_이름을_조건으로_모든_예약을_조회한다() {
        // given
        ReservationTime reservationTime1 = reservationTimeRepository.save(
                ReservationTime.withoutId(LocalTime.parse("10:20:30"))
        );
        Theme theme = themeRepository.save(
                Theme.withoutId("귀신찾기", "귀신을 찾는다", "example.com")
        );
        Reservation rudevicoReservation =
                Reservation.withoutId("루드비코", LocalDate.parse("2026-05-06"), reservationTime1, theme);

        ReservationTime reservationTime2 = reservationTimeRepository.save(
                ReservationTime.withoutId(LocalTime.parse("11:20:30"))
        );
        Reservation cocoReservation =
                Reservation.withoutId("코코", LocalDate.parse("2026-05-06"), reservationTime2, theme);

        Reservation savedRudevicoReservation = reservationRepository.save(rudevicoReservation);
        Reservation savedCocoReservation = reservationRepository.save(cocoReservation);

        // when
        List<Reservation> foundReservations = reservationRepository.findAllByUsername("루드비코");

        // then
        assertThat(foundReservations)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(savedRudevicoReservation);
    }

    @DisplayName("id에 해당하는 예약을 삭제한다")
    @Test
    void 예약을_삭제한다() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.save(
                ReservationTime.withoutId(LocalTime.parse("10:20:30"))
        );
        Theme theme = themeRepository.save(
                Theme.withoutId("귀신찾기", "귀신을 찾는다", "example.com")
        );
        Reservation reservation =
                Reservation.withoutId("루드비코", LocalDate.parse("2026-05-06"), reservationTime, theme);

        // when
        reservationRepository.deleteById(reservation.getId());
        Optional<Reservation> result = reservationRepository.findById(reservation.getId());

        // then
        assertThat(result).isNotPresent();
    }

    @DisplayName("특정 사용자에 대한 예약을 취소한다")
    @Test
    void 사용자_이름과_날짜와_예약_시간과_테마를_기준으로_예약을_취소한다() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.save(
                ReservationTime.withoutId(LocalTime.parse("10:20"))
        );
        Theme theme = themeRepository.save(
                Theme.withoutId("귀신찾기", "귀신을 찾는다", "example.com")
        );
        Reservation reservation =
                Reservation.withoutId("루드비코", LocalDate.now().plusDays(1), reservationTime, theme);

        Reservation saved = reservationRepository.save(reservation);

        // when
        reservationRepository.deleteByNameAndDateAndTimeIdAndThemeId(
                saved.getName(),
                saved.getDate(),
                saved.getTimeId(),
                saved.getThemeId()
        );

        // then
        assertThat(reservationRepository.findById(saved.getId())).isEmpty();
    }

    @DisplayName("예약의 날짜와 시간을 변경한다")
    @Test
    void 전달한_예약_도메인_객체와_동일한_id를_가지는_행의_날짜와_시간을_변경한다() {
        // given
        ReservationTime time = reservationTimeRepository.save(
                ReservationTime.withoutId(LocalTime.parse("10:00"))
        );
        ReservationTime timeForUpdate = reservationTimeRepository.save(
                ReservationTime.withoutId(LocalTime.parse("14:00"))
        );
        Theme theme = themeRepository.save(
                Theme.withoutId("귀신찾기", "귀신을 찾는다", "example.com")
        );

        Reservation saved = reservationRepository.save(
                Reservation.withoutId(
                        "루드비코",
                        LocalDate.now().plusDays(1),
                        time,
                        theme
                )
        );

        // when
        Reservation reservationForUpdate = new Reservation(
                saved.getId(),
                "루드비코",
                LocalDate.now().plusDays(1),
                timeForUpdate,
                theme
        );
        reservationRepository.update(reservationForUpdate);

        // then
        Reservation foundAfterUpdate = reservationRepository.findById(saved.getId())
                .orElseThrow();
        assertThat(foundAfterUpdate)
                .usingRecursiveComparison()
                .comparingOnlyFields("id", "name")
                .isEqualTo(saved);
        assertThat(foundAfterUpdate)
                .usingRecursiveComparison()
                .ignoringFields("id", "name")
                .isEqualTo(reservationForUpdate);
    }
}
