package roomescape.dto;

public record Response(int status, String errorMessage, Object data) {
    public static Response from(int status, String errorMessage) {
        return new Response(status, errorMessage, null);
    }

    public static Response from(int status, Object data) {
        return new Response(status, null, data);
    }
}
