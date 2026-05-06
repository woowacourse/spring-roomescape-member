package roomescape.dto;

public record SearchRequest(
        String condition,
        int size
) {
}
