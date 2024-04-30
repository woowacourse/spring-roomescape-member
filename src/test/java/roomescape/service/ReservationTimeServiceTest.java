package roomescape.service;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.dto.app.ReservationTimeAppRequest;

@SpringBootTest
@AutoConfigureTestDatabase
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;
    @MockBean
    private ReservationTimeRepository reservationTimeRepository;

    @DisplayName("예약 시간을 저장하고, 해당 시간을 id값과 함께 반환한다.")
    @Test
    void save() {
        long timeId = 1L;
        LocalTime startAt = LocalTime.now();

        when(reservationTimeRepository.save(any(ReservationTime.class)))
            .thenReturn(new ReservationTime(timeId, startAt));

        ReservationTime actual = reservationTimeService.save(new ReservationTimeAppRequest(startAt.toString()));
        ReservationTime expected = new ReservationTime(timeId, startAt);

        assertAll(
            () -> assertEquals(expected.getId(), actual.getId()),
            () -> assertEquals(expected.getStartAt(), actual.getStartAt())
        );
    }

    @DisplayName("실패: 잘못된 시간 포맷을 저장하면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"24:00", "-1:00", "10:60"})
    @NullAndEmptySource
    void save_IllegalTimeFormat(String time) {
        assertThatThrownBy(() -> reservationTimeService.save(new ReservationTimeAppRequest(time)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
