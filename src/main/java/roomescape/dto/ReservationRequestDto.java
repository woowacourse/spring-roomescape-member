package roomescape.dto;

public record ReservationRequestDto(String name, String date, Long timeId) {
    public ReservationRequestDto {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름을 입력하여야 합니다.");
        }
        if (date == null || date.isBlank()) {
            throw new IllegalArgumentException("날짜를 선택하여야 합니다.");
        }
    }
}
