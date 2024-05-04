package roomescape.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.domain.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@AutoConfigureTestDatabase
@Transactional
@SpringBootTest
public class ReservationTimeRepositoryTest {
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("예약 시간을 저장한다")
    @Test
    void saveTest() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));

        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        assertAll(
                () -> assertThat(savedReservationTime.getId()).isNotNull(),
                () -> assertThat(savedReservationTime.getStartAt()).isEqualTo("10:00")
        );
    }

    @DisplayName("예약 시간을 모두 조회한다")
    @Test
    void findAllTest() {
        for (int time = 10; time < 20; time++) {
            reservationTimeRepository.save(new ReservationTime(LocalTime.of(time, 0)));
        }

        assertThat(reservationTimeRepository.findAll())
                .hasSize(10);
    }

    @DisplayName("예약 시간을 조회한다")
    @Test
    void findByIdTest() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        assertThat(reservationTimeRepository.findById(savedReservationTime.getId()))
                .isPresent();
    }

    @DisplayName("예약 시간이 없는 경우에 조회하면 빈 값을 반환한다")
    @Test
    void findByIdExceptionTest() {
        Optional<ReservationTime> reservationTime = reservationTimeRepository.findById(1L);

        assertThat(reservationTime)
                .isEmpty();
    }

    @DisplayName("예약 시간을 삭제한다")
    @Test
    void deleteByIdTest() {
        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));

        reservationTimeRepository.deleteById(reservationTime.getId());

        assertThat(reservationTimeRepository.findAll())
                .isEmpty();
    }

    @DisplayName("존재하지 않는 예약 시간을 삭제할 경우, 예외가 발생하지 않는다")
    @Test
    void deleteByIdNotExistTest() {
        assertThatCode(() -> reservationTimeRepository.deleteById(100L))
                .doesNotThrowAnyException();
    }

    @DisplayName("예약 시간을 삭제할 경우, 참조된 예약이 있으면 예외를 발생한다")
    @Test
    void deleteByIdExceptionTest() {
        ReservationTime savedReservationTime = reservationTimeRepository.save(
                new ReservationTime(LocalTime.of(10, 0)));
        Theme savedTheme = themeRepository.save(
                new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        Reservation reservation = new Reservation("피케이", LocalDate.now(), savedReservationTime, savedTheme);
        Reservation savedReservation = reservationRepository.save(reservation);

        assertThatThrownBy(() -> reservationTimeRepository.deleteById(savedReservationTime.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 시간은 예약이 존재하여 삭제할 수 없습니다. ");
    }
}
