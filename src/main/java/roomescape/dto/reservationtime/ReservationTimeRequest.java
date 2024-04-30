package roomescape.dto.reservationtime;

import java.time.LocalTime;
import java.util.Objects;
import java.util.regex.Pattern;

import roomescape.application.dto.ReservationTimeCreationRequest;

public record ReservationTimeRequest(String startAt) {
    private static final Pattern FORMAT = Pattern.compile("^\\d{2}:\\d{2}$");

    public ReservationTimeRequest {
        if (!FORMAT.matcher(startAt).matches()) {
            throw new IllegalArgumentException("시작 시간은 HH:mm 형식이어야 합니다.");
        }

        try {
            LocalTime.parse(startAt);
        } catch (Exception e) {
            throw new IllegalArgumentException("시간이 올바르지 않습니다.");
        }

        Objects.requireNonNull(startAt, "시작 시간은 필수입니다.");
    }



    public ReservationTimeCreationRequest toReservationCreationRequest() {
        return new ReservationTimeCreationRequest(LocalTime.parse(startAt));
    }
}
