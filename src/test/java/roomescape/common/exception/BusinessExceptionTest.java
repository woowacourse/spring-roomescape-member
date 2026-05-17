package roomescape.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class BusinessExceptionTest {

    @Test
    void 존재하지_않는_예외는_응답_메시지와_로그_메시지를_분리한다() {
        NotFoundException exception = new NotFoundException(DomainType.THEME, 1L);

        assertThat(exception.getClientMessage()).isEqualTo("존재하지 않는 테마입니다.");
        assertThat(exception.getMessage()).isEqualTo(exception.getClientMessage());
        assertThat(exception.getLogMessage()).contains("domain=THEME", "id=1");
    }

    @Test
    void 사용중인_예외는_응답_메시지와_로그_메시지를_분리한다() {
        InUseException exception = new InUseException(DomainType.RESERVATION_TIME, 2L);

        assertThat(exception.getClientMessage()).isEqualTo("예약이 존재하는 예약 시간을 삭제할 수 없습니다.");
        assertThat(exception.getLogMessage()).contains("domain=RESERVATION_TIME", "id=2");
    }

    @Test
    void 사용중인_예외는_도메인명을_이용해_응답_메시지를_만든다() {
        InUseException exception = new InUseException(DomainType.THEME, 2L);

        assertThat(exception.getClientMessage()).isEqualTo("예약이 존재하는 테마을 삭제할 수 없습니다.");
    }

    @Test
    void 권한_예외는_응답_메시지와_로그_메시지를_분리한다() {
        AccessDeniedException exception = new AccessDeniedException(DomainType.RESERVATION, 3L);

        assertThat(exception.getClientMessage()).isEqualTo("예약을 처리할 권한이 없습니다.");
        assertThat(exception.getLogMessage()).contains("domain=RESERVATION", "id=3");
    }

    @Test
    void 중복_예외는_응답_메시지와_로그_메시지를_분리한다() {
        DuplicatedException exception = new DuplicatedException(
                DomainType.RESERVATION,
                LocalDate.of(2026, 5, 10),
                4L,
                5L
        );

        assertThat(exception.getClientMessage()).isEqualTo("이미 존재하는 예약입니다.");
        assertThat(exception.getLogMessage())
                .contains("domain=RESERVATION", "date=2026-05-10", "timeId=4", "themeId=5");
    }

    @Test
    void 예약시간_중복_예외는_응답_메시지와_로그_메시지를_분리한다() {
        DuplicatedException exception = new DuplicatedException(
                DomainType.RESERVATION_TIME,
                LocalTime.of(10, 0)
        );

        assertThat(exception.getClientMessage()).isEqualTo("이미 존재하는 예약 시간입니다.");
        assertThat(exception.getLogMessage())
                .contains("domain=RESERVATION_TIME", "time=10:00");
    }

    @Test
    void 과거_시간_예외는_응답_메시지와_로그_메시지를_분리한다() {
        LocalDateTime now = LocalDateTime.of(2026, 5, 17, 15, 30);
        LocalDateTime request = LocalDateTime.of(2026, 5, 17, 15, 0);
        PastDateTimeException exception = new PastDateTimeException(DomainType.RESERVATION, now, request);

        assertThat(exception.getClientMessage()).isEqualTo("과거 예약은 생성, 변경, 취소할 수 없습니다.");
        assertThat(exception.getLogMessage())
                .contains("domain=RESERVATION", "now=2026-05-17T15:30", "request=2026-05-17T15:00");
    }

}
