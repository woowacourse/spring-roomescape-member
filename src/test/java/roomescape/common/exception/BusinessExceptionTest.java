package roomescape.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class BusinessExceptionTest {

    private static final Long THEME_ID = 1L;
    private static final Long RESERVATION_TIME_ID = 2L;
    private static final Long RESERVATION_ID = 3L;
    private static final LocalDate RESERVATION_DATE = LocalDate.of(2026, 5, 10);
    private static final Long DUPLICATED_TIME_ID = 4L;
    private static final Long DUPLICATED_THEME_ID = 5L;
    private static final LocalTime DUPLICATED_START_AT = LocalTime.of(10, 0);
    private static final LocalDateTime NOW = LocalDateTime.of(2026, 5, 17, 15, 30);
    private static final LocalDateTime PAST_REQUEST_DATE_TIME = LocalDateTime.of(2026, 5, 17, 15, 0);

    @Test
    void 존재하지_않는_예외는_응답_메시지와_로그_메시지를_분리한다() {
        // when
        NotFoundException exception = new NotFoundException(DomainType.THEME, THEME_ID);

        // then
        assertThat(exception.getClientMessage()).isEqualTo("존재하지 않는 테마입니다.");
        assertThat(exception.getMessage()).isEqualTo(exception.getClientMessage());
        assertThat(exception.getLogMessage()).contains("domain=THEME", "id=1");
    }

    @Test
    void 사용중인_예외는_응답_메시지와_로그_메시지를_분리한다() {
        // when
        InUseException exception = new InUseException(DomainType.RESERVATION_TIME, RESERVATION_TIME_ID);

        // then
        assertThat(exception.getClientMessage()).isEqualTo("예약이 존재하는 예약 시간을 삭제할 수 없습니다.");
        assertThat(exception.getLogMessage()).contains(
                "domain=RESERVATION_TIME",
                "id=2"
        );
    }

    @Test
    void 사용중인_예외는_도메인명을_이용해_응답_메시지를_만든다() {
        // when
        InUseException exception = new InUseException(DomainType.THEME, RESERVATION_TIME_ID);

        // then
        assertThat(exception.getClientMessage()).isEqualTo("예약이 존재하는 테마을 삭제할 수 없습니다.");
    }

    @Test
    void 권한_예외는_응답_메시지와_로그_메시지를_분리한다() {
        // when
        AccessDeniedException exception = new AccessDeniedException(DomainType.RESERVATION, RESERVATION_ID);

        // then
        assertThat(exception.getClientMessage()).isEqualTo("예약을 처리할 권한이 없습니다.");
        assertThat(exception.getLogMessage()).contains(
                "domain=RESERVATION",
                "id=3"
        );
    }

    @Test
    void 중복_예외는_응답_메시지와_로그_메시지를_분리한다() {
        // when
        DuplicatedException exception = new DuplicatedException(
                DomainType.RESERVATION,
                RESERVATION_DATE,
                DUPLICATED_TIME_ID,
                DUPLICATED_THEME_ID
        );

        // then
        assertThat(exception.getClientMessage()).isEqualTo("이미 존재하는 예약입니다.");
        assertThat(exception.getLogMessage()).contains(
                "domain=RESERVATION",
                "date=2026-05-10",
                "timeId=4",
                "themeId=5"
        );
    }

    @Test
    void 예약시간_중복_예외는_응답_메시지와_로그_메시지를_분리한다() {
        // when
        DuplicatedException exception = new DuplicatedException(
                DomainType.RESERVATION_TIME,
                DUPLICATED_START_AT
        );

        // then
        assertThat(exception.getClientMessage()).isEqualTo("이미 존재하는 예약 시간입니다.");
        assertThat(exception.getLogMessage()).contains(
                "domain=RESERVATION_TIME",
                "time=10:00"
        );
    }

    @Test
    void 과거_시간_예외는_응답_메시지와_로그_메시지를_분리한다() {
        // when
        PastDateTimeException exception = new PastDateTimeException(
                DomainType.RESERVATION,
                NOW,
                PAST_REQUEST_DATE_TIME
        );

        // then
        assertThat(exception.getClientMessage()).isEqualTo("과거 예약은 생성, 변경, 취소할 수 없습니다.");
        assertThat(exception.getLogMessage()).contains(
                "domain=RESERVATION",
                "now=2026-05-17T15:30",
                "request=2026-05-17T15:00"
        );
    }

}
