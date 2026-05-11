package roomescape.time.controller.dto;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import roomescape.reservation.domain.ReservationTime;

public record TimeResponseDto(
    Long id,
    @JsonFormat(pattern = "HH:mm") LocalTime startAt,
    @JsonFormat(pattern = "HH:mm") LocalTime endAt
) {

  public static TimeResponseDto from(ReservationTime time) {
    return new TimeResponseDto(time.getId(), time.getStartAt(), time.getEndAt());
  }
}
