package roomescape.domain.ReservationTheme;

public record ReservationTheme(long id, String name, String description, String imageUrl) {
    public static ReservationTheme from(long id, ReservationThemeCommand command) {
        return new ReservationTheme(id, command.name(), command.description(), command.imageUrl());
    }
}
