package roomescape.reservation.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/data.sql"})
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    @Autowired
    private ThemeRepository themeRepository;

    private final ReservationTime reservationTime = new ReservationTime(1L, "10:00");
    private final Theme theme = new Theme(1L, "정글 모험", "열대 정글의 심연을 탐험하세요.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
    private final Reservation reservation = new Reservation(1L, "hotea", LocalDate.MAX.toString(), reservationTime, theme);

    @Test
    @DisplayName("특정 예약을 생성할 수 있다.")
    void save() {
        reservationTimeRepository.save(reservationTime);
        themeRepository.save(theme);
        assertThat(reservationRepository.save(reservation)).isEqualTo(1L);
    }

    @Test
    @DisplayName("예약을 모두 조회할 수 있다.")
    void findAll() {
        save();
        List<Reservation> reservationList = reservationRepository.findAll();
        assertAll(
                () -> assertThat(reservationList.get(0)).isEqualTo(reservation),
                () -> assertThat(reservationList.size()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("특정 예약을 조회할 수 있다.")
    void findById() {
        save();
        assertThat(reservationRepository.findById(1L)).isEqualTo(reservation);
    }

    @Test
    @DisplayName("특정 예약을 삭제 할 수 있다.")
    void deleteById() {
        save();
        assertThat(reservationRepository.deleteById(1L)).isEqualTo(1);
    }

    @Test
    @DisplayName("특정 날짜, 시간, 테마에 예약이 존재하는지 알 수 있다.")
    void checkReservationExists() {
        save();
        assertThat(reservationRepository.checkReservationExists(reservation.getDate().toString(), reservationTime.getId(), theme.getId())).isTrue();
    }
}
