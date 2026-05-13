package roomescape.admin.dto;

public record AdminThemeRequest(
        String name,
        String description,
        String image
) {
}
