package roomescape.controller.request;

import jakarta.validation.constraints.NotNull;

public record ReservationTimeWebRequest(@NotNull(message = "잘못된 시간 형식을 입력하셨습니다.") String startAt) {

}
