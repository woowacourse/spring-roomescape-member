package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.dto.AvailableTimeResponse;

@JdbcTest
@Import({ReservationTimeRepository.class, ThemeRepository.class, ReservationRepository.class})
class ReservationTimeRepositoryTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("id 로 엔티티를 찾는다.")
    void findByIdTest() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.now());
        Long timeId = reservationTimeRepository.save(reservationTime);
        ReservationTime findReservationTime = reservationTimeRepository.findById(timeId).get();

        assertThat(findReservationTime.getId()).isEqualTo(timeId);
    }

    @Test
    @DisplayName("전체 엔티티를 조회한다.")
    void findAllTest() {
        ReservationTime reservationTime1 = new ReservationTime(LocalTime.now());
        ReservationTime reservationTime2 = new ReservationTime(LocalTime.now());
        reservationTimeRepository.save(reservationTime1);
        reservationTimeRepository.save(reservationTime2);
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        assertThat(reservationTimes.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("해당 시간을 참조하는 Reservation이 있는지 찾는다.")
    void findReservationInSameIdTest() {
        Long themeId = themeRepository.save(new Theme(new Name("공포"), "무서운 테마", "https://i.pinimg.com/236x.jpg"));
        Theme theme = themeRepository.findById(themeId).get();

        Long timeId = reservationTimeRepository.save(new ReservationTime(LocalTime.now()));
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId).get();

        Reservation reservation = new Reservation(new Name("kaki"), LocalDate.now(), theme, reservationTime);
        Long reservationId = reservationRepository.save(reservation);
        boolean exist = reservationTimeRepository.findReservationInSameId(timeId).isPresent();

        assertThat(exist).isTrue();
    }

    @Test
    @DisplayName("예약 가능 시간을 조회한다")
    void findAvailableTest() {
        Long themeId = themeRepository.save(new Theme(new Name("공포"), "무서운 테마", "https://i.pinimg.com/236x.jpg"));
        Theme theme = themeRepository.findById(themeId).get();
        Long timeId1 = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("16:00")));
        ReservationTime reservationTime1 = reservationTimeRepository.findById(timeId1).get();
        Long timeId2 = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("15:00")));
        ReservationTime reservationTime2 = reservationTimeRepository.findById(timeId2).get();

        Reservation reservation = new Reservation(new Name("hogi"), LocalDate.parse("2024-07-07"), theme,
                reservationTime1);
        Long reservationId = reservationRepository.save(reservation);

        List<AvailableTimeResponse> availableTime = reservationTimeRepository.findAvailableTime(
                LocalDate.parse("2024-07-07"), themeId);

        assertAll(
                () -> assertThat(availableTime.get(0).alreadyBooked()).isFalse(),
                () -> assertThat(availableTime.get(1).alreadyBooked()).isTrue()
        );
    }

    @Test
    @DisplayName("id를 받아 삭제한다.")
    void deleteTest() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.now());
        Long timeId = reservationTimeRepository.save(reservationTime);
        reservationTimeRepository.delete(timeId);
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        assertThat(reservationTimes.size()).isEqualTo(0);
    }
}
