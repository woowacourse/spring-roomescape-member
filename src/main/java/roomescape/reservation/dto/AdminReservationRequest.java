package roomescape.reservation.dto;

import java.time.LocalDate;

public record AdminReservationRequest(LocalDate date, Long themeId, Long timeId, Long memberId) {

    public AdminReservationRequest {
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] 날짜를 입력해주세요.");
        }
        if (themeId == null) {
            throw new IllegalArgumentException("[ERROR] 테마 id를 입력해주세요.");
        }
        if (timeId == null) {
            throw new IllegalArgumentException("[ERROR] 시간 id를 입력해주세요.");
        }
        if (memberId == null) {
            throw new IllegalArgumentException("[ERROR] 사용자 id를 입력해주세요.");
        }
    }
}
