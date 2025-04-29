package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationRequest(
    @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
    String name,
    Long timeId) {

    public ReservationRequest {
        validateNull(date, name, timeId);
        validateName(name);
    }

    public static Reservation toEntity(ReservationRequest request, ReservationTime time) {
        return new Reservation(null, request.name(), request.date(), time);
    }

    private void validateNull(LocalDate date, String name, Long timeId) {
        if(date == null || name == null || timeId == null) {
            throw new IllegalArgumentException("값을 모두 선택해야 합니다.");
        }
    }

    private void validateName(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("이름은 한 글자 이상이어야 합니다.");
        }
    }
}
