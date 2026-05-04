package roomescape.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorStatusMapperTest {

    private final ErrorStatusMapper errorStatusMapper = new ErrorStatusMapper();

    @Test
    void 예약_검증_예외를_bad_request로_매핑한다() {
        assertThat(errorStatusMapper.map(ErrorCode.INVALID_RESERVATION_ID)).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorStatusMapper.map(ErrorCode.INVALID_RESERVATION_NAME)).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorStatusMapper.map(ErrorCode.INVALID_RESERVATION_DATE)).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorStatusMapper.map(ErrorCode.INVALID_RESERVATION_TIME)).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorStatusMapper.map(ErrorCode.INVALID_RESERVATION_TIME_ID)).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorStatusMapper.map(ErrorCode.RESERVATION_ALREADY_HAS_ID)).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(errorStatusMapper.map(ErrorCode.RESERVATION_TIME_ALREADY_HAS_ID)).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
