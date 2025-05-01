package roomescape.reservation_time.fixture;

import java.time.LocalTime;
import roomescape.reservation_time.ReservationTimeMapper;
import roomescape.reservation_time.domain.ReservationTime;
import roomescape.reservation_time.domain.dto.ReservationTimeReqDto;

public class ReservationTimeFixture {

    public static ReservationTimeReqDto createReqDto(LocalTime time) {
        return new ReservationTimeReqDto(time);
    }

    public static ReservationTime create(LocalTime time) {
        ReservationTimeReqDto reqDto = createReqDto(time);
        return ReservationTimeMapper.toEntity(reqDto);
    }
}
