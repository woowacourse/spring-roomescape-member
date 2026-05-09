package roomescape.controller.fixture;

import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import roomescape.web.dto.ReservationTimeRequest;

public class ReservationTimeRequestFixture {

    public static Stream<Arguments> registerFailRequestFixture() {
        return Stream.of(
                Arguments.of(
                        new ReservationTimeRequest(null),
                        "예약 시간 정보는 필수 값입니다."
                )
        );
    }

    public static ReservationTimeRequest registerSuccessRequestFixture() {
        return new ReservationTimeRequest(LocalTime.of(13, 0));
    }
}
