package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import roomescape.reservation.domain.Reservation;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.JdbcTemplateReservationTimeRepository;

@JdbcTest
class ReservationRepositoryTest {
    private JdbcTemplateReservationRepository jdbcTemplateReservationRepository;
    private JdbcTemplateReservationTimeRepository jdbcTemplateReservationTimeRepository;
    private Long timeId;
    private Long reservationId;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcTemplateReservationRepository = new JdbcTemplateReservationRepository(jdbcTemplate);
        jdbcTemplateReservationTimeRepository = new JdbcTemplateReservationTimeRepository(jdbcTemplate);

        timeId = jdbcTemplateReservationTimeRepository.save(ReservationTime.create(LocalTime.of(15, 40)));
        ReservationTime reservationTime = jdbcTemplateReservationTimeRepository.findById(timeId).get();

        reservationId = jdbcTemplateReservationRepository.save(
                Reservation.create("한다", LocalDate.now().plusWeeks(1), reservationTime));
        jdbcTemplateReservationRepository.save(Reservation.create("판다", LocalDate.now().plusWeeks(1), reservationTime));
    }

    @Test
    @DisplayName("모든 예약 정보를 조회한다.")
    void findAll() {
        assertThat(jdbcTemplateReservationRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("예약을 추가한다.")
    void save() {
        //given & when
        ReservationTime reservationTime = jdbcTemplateReservationTimeRepository.findById(timeId).get();
        jdbcTemplateReservationRepository.save(
                Reservation.create("새로운사람", LocalDate.now().plusWeeks(3), reservationTime));

        //then
        assertThat(jdbcTemplateReservationRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void delete() {
        //given & when
        jdbcTemplateReservationRepository.delete(reservationId);

        //then
        assertThat(jdbcTemplateReservationRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("예약 날짜와 시간 ID 정보로 존재하는지 확인한다.")
    void exitsByDateAndTimeId() {
        assertThat(
                jdbcTemplateReservationRepository.existsByDateAndTimeId(LocalDate.now().plusWeeks(1), timeId)).isTrue();
        assertThat(jdbcTemplateReservationRepository.existsByDateAndTimeId(LocalDate.now().plusWeeks(3),
                timeId)).isFalse();
    }
}
