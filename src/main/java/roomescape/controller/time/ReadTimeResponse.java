package roomescape.controller.time;

import roomescape.domain.ReservationTime;

import java.time.format.DateTimeFormatter;

//TODO 더 좋은 이름 생각하기
public record ReadTimeResponse(Long id, String startAt) {

    public static ReadTimeResponse from(final ReservationTime time) {
        return new ReadTimeResponse(
                time.getId(),
                time.getStartAt().format(DateTimeFormatter.ofPattern("HH:mm"))
        );
    }
}
