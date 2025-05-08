package roomescape.dto;

public record ErrorResponse(String message) {

    public ErrorResponse(Exception ex) {
        this(ex.getMessage());
    }
}
