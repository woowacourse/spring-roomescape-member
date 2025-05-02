package roomescape.reservationTime.fixture;

import java.time.LocalTime;
import roomescape.reservationTime.ReservationTimeMapper;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.dto.ReservationTimeReqDto;

public class ReservationTimeFixture {

    public static ReservationTimeReqDto createReqDto(LocalTime time) {
        return new ReservationTimeReqDto(time);
    }

    public static ReservationTime create(LocalTime time) {
        ReservationTimeReqDto reqDto = createReqDto(time);
        return ReservationTimeMapper.toEntity(reqDto);
    }
}
