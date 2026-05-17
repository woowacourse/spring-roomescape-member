package roomescape.time.controller.dto;

import java.time.format.DateTimeFormatter;

import roomescape.time.domain.ReservationTime;

public record TimeResponseDto(Long id, String startAt, String endAt) {

  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

  public static TimeResponseDto from(ReservationTime time) {
    return new TimeResponseDto(
        time.getId(),
        time.getStartAt().format(TIME_FORMATTER),
        time.getEndAt().format(TIME_FORMATTER)
    );
  }
}

