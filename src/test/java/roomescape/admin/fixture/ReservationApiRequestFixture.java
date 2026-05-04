package roomescape.admin.fixture;

import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import roomescape.service.command.ReservationCommand;

public class ReservationApiRequestFixture {

    public static Stream<Arguments> reserveFailRequestFixture() {
        return Stream.of(
                Arguments.of(
                        new ReservationCommand(null, LocalDate.now().plusDays(1), 1L),
                        "예약자 이름 정보는 비어있을 수 없습니다."
                ),
                Arguments.of(
                        new ReservationCommand("", LocalDate.now().plusDays(1), 1L),
                        "예약자 이름 정보는 비어있을 수 없습니다."
                ),
                Arguments.of(
                        new ReservationCommand(" ", LocalDate.now().plusDays(1), 1L),
                        "예약자 이름 정보는 비어있을 수 없습니다."
                ),
                Arguments.of(
                        new ReservationCommand("이프", null, 1L),
                        "예약 날짜 정보는 필수 값입니다."
                ),
                Arguments.of(
                        new ReservationCommand("이프", LocalDate.now().minusDays(1), 1L),
                        "이미 지난 날짜는 예약할 수 없습니다."
                ),
                Arguments.of(
                        new ReservationCommand("이프", LocalDate.now(), null),
                        "예약 시간 식별자는 필수 값입니다."
                ),
                Arguments.of(
                        new ReservationCommand("이프", LocalDate.now(), 0L),
                        "예약 시간 식별자는 식별 가능한 양수입니다."
                )
        );
    }

    public static ReservationCommand reserveSuccessRequestFixture() {
        return new ReservationCommand("이프", LocalDate.now().plusDays(1), 1L);
    }
}
