package roomescape.time.controller.dto;

import roomescape.reservation.domain.ReservationTime;

public record TimeResponseDto(Long id, String startAt, String endAt) {

  public static TimeResponseDto from(ReservationTime time) {
    return new TimeResponseDto(time.getId(), time.getStartAt(), time.getEndAt());
  }
}

