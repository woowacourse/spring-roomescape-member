package roomescape.service;

import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.entity.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.AvailableReservationTimeResponse;
import roomescape.error.ReservationException;
import roomescape.stub.StubReservationRepository;
import roomescape.stub.StubReservationTimeRepository;

class ReservationTimeServiceTest {

    private final StubReservationRepository stubReservationRepository = new StubReservationRepository();
    private final StubReservationTimeRepository stubReservationTimeRepository = new StubReservationTimeRepository();
    private final ReservationTimeService sut = new ReservationTimeService(
            stubReservationTimeRepository, stubReservationRepository);

    @Test
    @DisplayName("예약 시간이 저장된다")
    void saveTime() {
        // given
        var request = new ReservationTimeRequest(LocalTime.of(12, 30));

        // when
        var response = sut.saveTime(request);

        // then
        assertSoftly(soft -> {
            soft.assertThat(response.id()).isNotNull();
            soft.assertThat(response.startAt()).isEqualTo(LocalTime.of(12, 30));
        });
    }

    @Test
    @DisplayName("모든 예약 시간을 조회한다")
    void findAll() {
        // given
        stubReservationTimeRepository.save(new ReservationTime(1L, LocalTime.of(10, 0)));

        // when
        var responses = sut.findAll();

        // then
        assertThat(responses).hasSize(1);
    }

    @Test
    @DisplayName("예약 시간이 삭제된다")
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
    @DisplayName("예약이 존재하는 시간을 삭제하지 못 한다")
    void delete_reservation_exists() {
        // given
        stubReservationRepository.setExistsByReservationTimeId(true);

        // when // then
        assertThatThrownBy(() -> sut.delete(1L))
                .isInstanceOf(ReservationException.class)
                .hasMessage("해당 시간으로 예약된 건이 존재합니다.");
    }

    @DisplayName("예약 가능한 시간을 조회한다")
    @Test
    void findAllAvailable() {
        // given
        var time1 = stubReservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        var time2 = stubReservationTimeRepository.save(new ReservationTime(LocalTime.of(11, 0)));
        stubReservationTimeRepository.setBookedTimeIds(Set.of(time1.getId()));

        // when
        var responses = sut.findAllAvailable(LocalDate.of(2025, 7, 1), 1L);
        var bookedMap = responses.stream()
                .collect(toMap(
                        AvailableReservationTimeResponse::id,
                        AvailableReservationTimeResponse::alreadyBooked));

        // then
        assertSoftly(soft -> {
            soft.assertThat(bookedMap.get(time1.getId())).isTrue();
            soft.assertThat(bookedMap.get(time2.getId())).isFalse();
        });
    }
}
