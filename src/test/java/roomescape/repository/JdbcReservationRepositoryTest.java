package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class JdbcReservationRepositoryTest {

    @Autowired
    JdbcReservationRepository reservationRepository;
    @Autowired
    JdbcReservationTimeRepository reservationTimeRepository;
    @Autowired
    JdbcThemeRepository themeRepository;

    @DisplayName("예약시간을 저장한다")
    @Test
    void 예약시간을_저장하면_id를_부여한다() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.save(
                new ReservationTime(LocalTime.parse("10:20:30")));
        Theme theme = themeRepository.save(new Theme("귀신찾기", "귀신을 찾는다", "example.com"));
        Reservation reservation = new Reservation("루드비코", LocalDate.parse("2026-05-06"),
                reservationTime,
                theme);

        // when
        Reservation saved = reservationRepository.save(reservation);

        // then
        assertThat(saved.getId()).isNotNull();
    }

    @DisplayName("예약시간을 id로 조회한다")
    @Test
    void 예약시간을_id로_조회한다() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.save(
                new ReservationTime(LocalTime.parse("10:20:30")));
        Theme theme = themeRepository.save(new Theme("귀신찾기", "귀신을 찾는다", "example.com"));
        Reservation reservation = new Reservation("루드비코", LocalDate.parse("2026-05-06"),
                reservationTime,
                theme);

        // when
        Reservation saved = reservationRepository.save(reservation);
        Optional<Reservation> result = reservationRepository.findById(saved.getId());

        // then
        assertThat(result.get()).isEqualTo(saved);
    }

    @DisplayName("저장된 모든 예약시간을 조회한다")
    @Test
    void 저장된_모든_예약시간을_조회한다() {
        // given
        ReservationTime reservationTime1 = reservationTimeRepository.save(
                new ReservationTime(LocalTime.parse("10:20:30")));
        Theme theme = themeRepository.save(new Theme("귀신찾기", "귀신을 찾는다", "example.com"));
        Reservation rudevicoReservation = new Reservation("루드비코", LocalDate.parse("2026-05-06"),
                reservationTime1,
                theme);

        ReservationTime reservationTime2 = reservationTimeRepository.save(
                new ReservationTime(LocalTime.parse("11:20:30")));
        Reservation cocoReservation = new Reservation("코코", LocalDate.parse("2026-05-06"),
                reservationTime2,
                theme);

        // when
        Reservation savedRudevicoReservation = reservationRepository.save(rudevicoReservation);
        Reservation savedCocoReservation = reservationRepository.save(cocoReservation);

        List<Reservation> foundReservations = reservationRepository.findAll();

        // then
        assertThat(foundReservations)
                .hasSize(2)
                .containsExactlyInAnyOrder(
                        savedRudevicoReservation, savedCocoReservation
                );
    }

    @DisplayName("인기 테마의 id를 조회한다")
    @Test
    @Sql("/data.sql")
    void 최근_1주_동안의_예약_상위_10개의_테마를_조회한다() {
        List<Long> popularThemes = reservationRepository.findPopularThemeIds();
        assertThat(popularThemes)
                .containsExactly(
                        1L, 2L, 3L, // 1순위: 테마의 예약 수 내림차순 정렬
                        6L, 5L, 4L, 8L, 7L, // 2순위: 예약 수가 같으면 테마 이름 오름차순 정렬
                        10L, 9L // 예약 개수가 0개여도, 상위 10위 이내라면 조회되어야 함 (예약 개수 0개인 테마들은 2순위 정렬 기준으로 비교)
                );
    }

    @DisplayName("id에 해당하는 예약시간을 삭제한다")
    @Test
    void 예약시간을_삭제한다() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.save(
                new ReservationTime(LocalTime.parse("10:20:30")));
        Theme theme = themeRepository.save(new Theme("귀신찾기", "귀신을 찾는다", "example.com"));
        Reservation reservation = new Reservation("루드비코", LocalDate.parse("2026-05-06"),
                reservationTime,
                theme);

        // when
        reservationRepository.delete(reservation.getId());
        Optional<Reservation> result = reservationRepository.findById(reservation.getId());

        // then
        assertThat(result).isNotPresent();
    }
}
