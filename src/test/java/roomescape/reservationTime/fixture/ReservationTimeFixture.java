package roomescape.reservationTime.fixture;

import java.time.LocalTime;
import roomescape.reservationTime.ReservationTimeMapper;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.dto.ReservationTimeRequestDto;

public class ReservationTimeFixture {

    public static ReservationTimeRequestDto createRequestDto(LocalTime time) {
        return new ReservationTimeRequestDto(time);
    }

    public static ReservationTime create(LocalTime time) {
        ReservationTimeRequestDto requestDto = createRequestDto(time);
        return ReservationTimeMapper.toEntity(requestDto);
    }
}
