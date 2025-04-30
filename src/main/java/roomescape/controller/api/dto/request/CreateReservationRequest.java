package roomescape.controller.api.dto.request;

import java.time.LocalDate;

import roomescape.service.dto.request.CreateReservationServiceRequest;

public record CreateReservationRequest(
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {

    public CreateReservationRequest {
        if (name == null) {
            throw new IllegalArgumentException("이름은 필수로 입력해야 합니다.");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("이름은 비어 있을 수 없습니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("날짜는 필수로 입력해야 합니다.");
        }
        if (timeId == null) {
            throw new IllegalArgumentException("시간은 필수로 입력해야 합니다.");
        }
    }

    public CreateReservationServiceRequest toCommand() {
        return new CreateReservationServiceRequest(name, date, timeId, themeId);
    }
}
