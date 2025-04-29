package roomescape.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.entity.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

public class ReservationTimeServiceTest {

    FakeReservationTimeRepository fakeReservationTimeRepository = new FakeReservationTimeRepository();
    ReservationTimeService reservationTimeService = new ReservationTimeService(fakeReservationTimeRepository);

    @Test
    @DisplayName("시간을 저장한다.")
    void createReservationTime() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(10, 0));
        // when
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.createReservationTime(
                reservationTimeRequest);

        // then
        Assertions.assertThat(reservationTimeResponse.startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("중복된 시간을 저장시 예외가 발생한다..")
    void throwExceptionWhenCreateDuplicatedReservationTime() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(10, 0));
        ReservationTimeRequest duplicatedReservationTimeRequest = new ReservationTimeRequest(LocalTime.of(10, 0));
        // when
        reservationTimeService.createReservationTime(reservationTimeRequest);

        // then
        Assertions.assertThatThrownBy(
                        () -> reservationTimeService.createReservationTime(duplicatedReservationTimeRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 시간 목록을 가져온다")
    void getAllReservationTime() {

        // given
        // when
        List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.getAllReservationTime();
        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(reservationTimeResponses).hasSize(1);
            softAssertions.assertThat(reservationTimeResponses.getFirst().startAt()).isEqualTo(LocalTime.of(22, 0));
        });
    }

    @Test
    @DisplayName("아이디를 통해 예약 시간을 삭제한다")
    void deleteReservationTimeById() {
        // given
        long id = 1;

        // when
        // then
        Assertions.assertThatNoException().isThrownBy(() -> reservationTimeService.delete(id));
    }


    static class FakeReservationTimeRepository implements ReservationTimeRepository {

        List<ReservationTime> reservationTimes = new ArrayList<>();

        public FakeReservationTimeRepository() {
            reservationTimes.add(new ReservationTime(1, LocalTime.of(22, 0)));
        }

        @Override
        public ReservationTime save(ReservationTime reservationTime) {
            reservationTimes.add(reservationTime);
            return reservationTime;
        }

        @Override
        public List<ReservationTime> findAll() {
            return reservationTimes;
        }

        @Override
        public int deleteById(Long id) {
            boolean existingId = reservationTimes.stream().anyMatch(reservation -> reservation.getId() == id);
            if (existingId) {
                reservationTimes.removeIf(reservation -> reservation.getId() == id);
                return 1;
            }
            return 0;
        }

        @Override
        public ReservationTime findById(Long timeId) {
            return reservationTimes.stream()
                    .filter(reservationTime ->
                            reservationTime.getId().equals(timeId)
                    )
                    .findFirst()
                    .orElse(null);
        }
    }
}
