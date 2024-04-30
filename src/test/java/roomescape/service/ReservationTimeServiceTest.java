package roomescape.service;

import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.ReservationTimeRequest;
import roomescape.repository.CollectionReservationTimeRepository;
import roomescape.repository.ReservationTimeRepository;

class ReservationTimeServiceTest {

    @Test
    @DisplayName("중복된 시간은 생성할 수 없는지 검증")
    void saveFailCauseDuplicate() {
        ReservationTimeRepository reservationTimeRepository = new CollectionReservationTimeRepository();
        ReservationTimeService reservationTimeService = new ReservationTimeService(reservationTimeRepository);
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(10, 0));
        reservationTimeService.save(reservationTimeRequest);

        Assertions.assertThatThrownBy(() -> reservationTimeService.save(reservationTimeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                //TODO 커스텀 에러
                .hasMessage("중복된 시간은 생성할 수 없습니다.");
    }
}
