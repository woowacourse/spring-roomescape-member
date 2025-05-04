package roomescape.reservationtime.fixture;

import java.time.LocalTime;
import roomescape.reservationtime.ReservationTimeMapper;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.domain.dto.ReservationTimeReqDto;

public class ReservationTimeFixture {

    public static ReservationTimeReqDto createReqDto(LocalTime time) {
        return new ReservationTimeReqDto(time);
    }

    public static ReservationTime create(LocalTime time) {
        ReservationTimeReqDto reqDto = createReqDto(time);
        return ReservationTimeMapper.toEntity(reqDto);
    }
}
