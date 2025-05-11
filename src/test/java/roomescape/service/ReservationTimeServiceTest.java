package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.error.ReservationException;
import roomescape.stub.StubReservationRepository;
import roomescape.stub.StubReservationTimeRepository;

class ReservationTimeServiceTest {

    private final StubReservationRepository stubReservationRepository = new StubReservationRepository();
    private final StubReservationTimeRepository stubReservationTimeRepository = new StubReservationTimeRepository();
    private final ReservationTimeService sut = new ReservationTimeService(
            stubReservationTimeRepository, stubReservationRepository);

    @Test
    @DisplayName("예약_시간이_저장된다")
    void saveTime() {
        // given
        var request = new ReservationTimeRequest(LocalTime.of(12, 30));

        // when
        var response = sut.saveTime(request);

        // then
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(response.id()).isNotNull();
            soft.assertThat(response.startAt()).isEqualTo(LocalTime.of(12, 30));
        });
    }

    @Test
    @DisplayName("모든_예약_시간을_조회한다")
    void findAll() {
        // given
        stubReservationTimeRepository.save(new ReservationTime(1L, LocalTime.of(10, 0)));

        // when
        var responses = sut.findAll();

        // then
        assertThat(responses).hasSize(1);
    }

    @Test
    @DisplayName("예약_시간이_삭제된다")
    void delete() {
        // given
        var savedTime = stubReservationTimeRepository.save(new ReservationTime(1L, LocalTime.of(10, 0)));

        // when
        sut.delete(savedTime.getId());
        var foundTime = stubReservationTimeRepository.findById(savedTime.getId());

        // then
        assertThat(foundTime).isEmpty();
    }

    @Test
    @DisplayName("예약이_존재하는_시간을_삭제하지_못_한다")
    void delete_throws_if_reservation_exists() {
        // given
        stubReservationRepository.setExistsByReservationTimeId(true);

        // when & then
        assertThatThrownBy(() -> sut.delete(1L))
                .isInstanceOf(ReservationException.class)
                .hasMessage("해당 시간으로 예약된 건이 존재합니다.");
    }
}
