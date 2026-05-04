package roomescape.time.exception;

public class TimeNotFoundException extends RuntimeException {
  private final Long id;

  public TimeNotFoundException(Long id) {
    super("예약 시간이 존재하지 않습니다. id=" + id);
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}

