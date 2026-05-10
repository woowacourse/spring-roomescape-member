package roomescape.domain.theme;

public record Theme(long id, String name, String description, String imageUrl) {
    public static Theme from(long id, ReservationThemeCommand command) {
        return new Theme(id, command.name(), command.description(), command.imageUrl());
    }
}
