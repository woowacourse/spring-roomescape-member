package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.service.request.ReservationTimeRequest;
import roomescape.support.IntegrationTestSupport;

/*
 * 테스트 데이터베이스 예약 초기 데이터
 * {ID=1, NAME=브라운, DATE=2023-05-04, TIME={ID=1, START_AT="10:00"}, THEME={ID=1, NAME="레벨1 탈출"}}
 * {ID=2, NAME=엘라, DATE=2023-05-04, TIME={ID=2, START_AT="11:00"}, THEME={ID=1, NAME="레벨1 탈출"}}
 * {ID=3, NAME=릴리, DATE=2023-08-05, TIME={ID=2, START_AT="11:00"}, THEME={ID=1, NAME="레벨1 탈출"}}
 *
 * 테스트 데이터베이스 시간 초기 데이터
 * {ID=1, START_AT=10:00}
 * {ID=2, START_AT=11:00}
 * {ID=3, START_AT=13:00}
 */
class ReservationTimeServiceTest extends IntegrationTestSupport {

    @Autowired
    private ReservationTimeService target;

    @Test
    @DisplayName("신규 예약 시간을 생성할 수 있다.")
    void createReservationTime() {
        ReservationTimeRequest request = new ReservationTimeRequest("16:00");

        assertThatCode(() -> target.createReservationTime(request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("중복되는 예약 시간을 생성할 수 없다.")
    void duplicated() {
        ReservationTimeRequest request = new ReservationTimeRequest("10:00");

        assertThatThrownBy(() -> target.createReservationTime(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 예약 시간이 존재합니다.");
    }

    @Test
    @DisplayName("예약 시간을 사용하는 예약이 존재하면, 삭제하지 않는다.")
    void cantDelete() {
        long id = 1L;

        assertThatThrownBy(() -> target.deleteReservationTime(id))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 시간을 사용하고 있는 예약이 존재합니다.");
    }
}
