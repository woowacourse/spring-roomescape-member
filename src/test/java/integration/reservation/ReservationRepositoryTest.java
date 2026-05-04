package integration.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import integration.BaseIntegrationTest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;

class ReservationRepositoryTest extends BaseIntegrationTest {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationDataSource dataSource;

    private ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

    @BeforeEach
    void setUp() {
        dataSource.insertReservationTime(reservationTime.getStartAt());
    }

    @AfterEach
    void tearDown() {
        dataSource.clearTable();
        dataSource.clearId();
    }

    @Test
    void 예약을_저장하고_ID로_조회한다() {
        // given: 시간 먼저 저장
        Reservation reservation = new Reservation("이프", LocalDate.now().plusDays(1), reservationTime);

        // when
        Reservation saved = reservationRepository.save(reservation);

        // then
        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(dataSource.hasReservationById(saved.getId())).isTrue();
    }

    @Test
    void 동일한_날짜와_시간으로_저장하면_DB_제약조건_에러가_발생한다() {
        // given
        Reservation first = new Reservation("이프", LocalDate.now().plusDays(1), reservationTime);
        Reservation second = new Reservation("아루", LocalDate.now().plusDays(1), reservationTime);
        reservationRepository.save(first);

        // when & then: 서비스 로직 없이 DB의 UK Constraint 확인
        assertThatThrownBy(() -> reservationRepository.save(second))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void 특정_날짜와_시간에_예약이_존재하는지_확인한다() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        reservationRepository.save(new Reservation("이프", date, reservationTime));

        // when & then
        Long otherTimeId = 99L;
        LocalDate otherDate = date.plusDays(1);
        assertThat(reservationRepository.existByDateAndTimeId(date, 1L)).isTrue();
        assertThat(reservationRepository.existByDateAndTimeId(date, otherTimeId)).isFalse();
        assertThat(reservationRepository.existByDateAndTimeId(otherDate, 1L)).isFalse();
    }

    @Test
    void 예약을_삭제한다() {
        // given
        Reservation saved = reservationRepository.save(new Reservation("이프", LocalDate.now().plusDays(1), reservationTime));

        // when
        reservationRepository.delete(saved.getId());

        // then
        assertThat(dataSource.hasReservationById(saved.getId())).isFalse();
    }

    @Test
    void 모든_예약_목록을_조회한다() {
        // given
        reservationRepository.save(new Reservation("이프", LocalDate.now().plusDays(1), reservationTime));
        reservationRepository.save(new Reservation("이프", LocalDate.now().plusDays(2), reservationTime));

        // when
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        assertThat(reservations).hasSize(2);
    }
}
