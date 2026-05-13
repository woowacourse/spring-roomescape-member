package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.domain.ReservationTime;
import roomescape.domain.fixture.ReservationTimeFixture;
import roomescape.global.exception.EntityNotFoundException;
import roomescape.service.BaseIntegrationTest;

class ReservationTimeRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationTimeDataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource.clearTable();
        dataSource.clearId();
    }

    @Test
    void 시간을_저장하고_ID로_조회한다() {
        // given
        LocalTime reservationStartTime = LocalTime.of(10, 0);
        ReservationTime time = ReservationTimeFixture.createReservationTime(reservationStartTime);

        // when
        ReservationTime saved = reservationTimeRepository.save(time);

        // then
        Optional<ReservationTime> found = reservationTimeRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getStartAt()).isEqualTo(reservationStartTime);
    }

    @Test
    void 같은_시간으로_저장하면_참조_무결성_예외가_발생한다() {
        // given
        LocalTime reservationStartTime = LocalTime.of(10, 0);
        ReservationTime time = ReservationTimeFixture.createReservationTime(reservationStartTime);
        reservationTimeRepository.save(time);

        // when & then
        assertThatThrownBy(() -> reservationTimeRepository.save(time))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void 시간을_수정한다() {
        // given
        ReservationTime saved = reservationTimeRepository.save(ReservationTimeFixture.createDefaultReservationTime());

        // when
        saved.deactivate();
        reservationTimeRepository.update(saved);
        ReservationTime time = reservationTimeRepository.findById(saved.getId()).get();

        // then
        assertThat(time.isActive()).isFalse();
    }

    @Test
    void 수정할_시간이_존재하지_않으면_예외가_발생한다() {
        // given
        ReservationTime time = ReservationTimeFixture.createDefaultReservationTime();

        // when & then
        assertThatThrownBy(() -> reservationTimeRepository.update(time))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("존재하지 않는 시간 정보입니다.");
    }

    @Test
    void 특정_시간이_존재하는지_확인한다() {
        // given
        LocalTime targetTime = LocalTime.of(10, 0);
        reservationTimeRepository.save(ReservationTimeFixture.createReservationTime(targetTime));

        // when & then
        LocalTime otherTime = LocalTime.of(11, 0);
        assertThat(reservationTimeRepository.existsByStartAt(targetTime)).isTrue();
        assertThat(reservationTimeRepository.existsByStartAt(otherTime)).isFalse();
    }

    @Test
    void 모든_시간_목록을_조회한다() {
        // given
        reservationTimeRepository.save(ReservationTimeFixture.createReservationTime(LocalTime.of(11, 0)));
        reservationTimeRepository.save(ReservationTimeFixture.createReservationTime(LocalTime.of(10, 0)));

        // when
        List<ReservationTime> times = reservationTimeRepository.findAllByPaging(0, 10);

        // then
        assertThat(times).hasSize(2);
        assertThat(times)
                .extracting(ReservationTime::getStartAt)
                .containsExactly(LocalTime.of(10, 0), LocalTime.of(11, 0));
    }

    @Test
    void 예약_상태_구성을_위한_시간_슬롯을_조회한다() {
        // given
        dataSource.insertTimeByStartToEndWithOneHourLotation(10, 18);

        // when
        List<ReservationTime> result = reservationTimeRepository.findTimeSlotsForReservationStatus();

        // then
        assertThat(result).hasSize(9);
        assertThat(result)
                .extracting(ReservationTime::getStartAt)
                .isSorted();
    }
}
