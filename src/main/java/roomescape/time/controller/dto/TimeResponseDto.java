package roomescape.time.controller.dto;

import roomescape.reservation.domain.ReservationTime;

public class TimeResponseDto {
  private final Long id;
  private final String startAt;
  private final String endAt;

  public TimeResponseDto(Long id, String startAt, String endAt) {
    this.id = id;
    this.startAt = startAt;
    this.endAt = endAt;
  }

  public static TimeResponseDto from(ReservationTime time) {
    return new TimeResponseDto(time.getId(), time.getStartAt(), time.getEndAt());
  }

  public Long getId() {
    return id;
  }

  public String getStartAt() {
    return startAt;
  }

  public String getEndAt() {
    return endAt;
  }
}

