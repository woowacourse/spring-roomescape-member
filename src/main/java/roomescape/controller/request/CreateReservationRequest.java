package roomescape.controller.request;

import java.time.LocalDate;
import roomescape.service.param.CreateReservationParam;

public record CreateReservationRequest(
        String name,
        LocalDate date,
        Long timeId
) {

    private static final String ERROR_MESSAGE_FORMAT = "[ERROR]  예약 필수 정보가 누락되었습니다. %s: %s";

    public CreateReservationRequest {
        validateName(name);
        validateDate(date);
        validateTimeId(timeId);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(String.format(ERROR_MESSAGE_FORMAT, "name", name));
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException(String.format(ERROR_MESSAGE_FORMAT, "date", date));
        }
    }

    private void validateTimeId(Long timeId) {
        if (timeId == null) {
            throw new IllegalArgumentException(String.format(ERROR_MESSAGE_FORMAT, "timeId", timeId));
        }
    }

    public CreateReservationParam toServiceParam() {
        return new CreateReservationParam(name, date, timeId);
    }
}
