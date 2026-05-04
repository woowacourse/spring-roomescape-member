package roomescape.time.controller.dto;

import roomescape.reservation.domain.ReservationTime;

public class TimeResponseDto {
  private final Long id;
  private final String startAt;

  public TimeResponseDto(Long id, String startAt) {
    this.id = id;
    this.startAt = startAt;
  }

  public static TimeResponseDto from(ReservationTime time) {
    return new TimeResponseDto(time.getId(), time.getStartAt());
  }

  public Long getId() {
    return id;
  }

  public String getStartAt() {
    return startAt;
  }
}

