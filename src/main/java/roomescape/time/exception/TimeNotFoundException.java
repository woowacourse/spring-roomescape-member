package roomescape.time.exception;

import roomescape.error.ErrorCode;
import roomescape.error.NotFoundException;

public class TimeNotFoundException extends NotFoundException {
  private final Long id;

  public TimeNotFoundException(Long id) {
    super(ErrorCode.TIME_NOT_FOUND, "예약 시간이 존재하지 않습니다. id=" + id);
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}

