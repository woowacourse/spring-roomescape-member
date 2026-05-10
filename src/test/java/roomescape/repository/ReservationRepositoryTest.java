package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.service.BaseIntegrationTest;

class ReservationRepositoryTest extends BaseIntegrationTest {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationDataSource dataSource;

    private ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
    private Theme theme = new Theme(1L, "공포", "어마무시한 공포 테마", "https://theme.com/image.png", false);

    @BeforeEach
    void setUp() {
        dataSource.clearTable();
        dataSource.clearId();

        dataSource.insertTheme(theme.getName(), theme.getDescription(), theme.getThumbnailImageUrl());
        dataSource.insertReservationTime(reservationTime.getStartAt());
    }

    @Test
    void 예약을_저장하고_ID로_조회한다() {
        // given: 시간 먼저 저장
        Reservation reservation = Reservation.of("이프", LocalDate.now().plusDays(1), theme, reservationTime);

        // when
        Reservation saved = reservationRepository.save(reservation);

        // then
        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(dataSource.hasReservationById(saved.getId())).isTrue();
    }

    @Test
    void 동일한_날짜와_시간으로_저장하면_DB_제약조건_에러가_발생한다() {
        // given
        Reservation first = Reservation.of("이프", LocalDate.now().plusDays(1), theme, reservationTime);
        Reservation second = Reservation.of("아루", LocalDate.now().plusDays(1), theme, reservationTime);
        reservationRepository.save(first);

        // when & then: 서비스 로직 없이 DB의 UK Constraint 확인
        assertThatThrownBy(() -> reservationRepository.save(second))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void 특정_날짜와_시간에_예약이_존재하는지_확인한다() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        reservationRepository.save(Reservation.of("이프", date, theme, reservationTime));

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
        Reservation saved = reservationRepository.save(Reservation.of("이프", LocalDate.now().plusDays(1), theme, reservationTime));

        // when
        reservationRepository.deleteById(saved.getId());

        // then
        assertThat(dataSource.hasReservationById(saved.getId())).isFalse();
    }

    @Test
    void 페이징_조건에_맞는_예약_목록을_조회한다() {
        // given: 과거 부터 미래 예약 목록 주어짐
        dataSource.insertReservation("이프", LocalDate.now().minusDays(1), 1L, 1L);
        dataSource.insertReservation("이프", LocalDate.now(), 1L, 1L);
        dataSource.insertReservation("이프", LocalDate.now().plusDays(1), 1L, 1L);

        // when
        List<Reservation> reservations = reservationRepository.findAllByPaging(0, 10);

        // then
        assertThat(reservations).hasSize(3);
    }
}
