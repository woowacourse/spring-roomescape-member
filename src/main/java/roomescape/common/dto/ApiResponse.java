package roomescape.common.dto;

public class ApiResponse<T> {

    private final boolean ok;
    private final T data;

    public ApiResponse(T data) {
        this.ok = true;
        this.data = data;
    }

    public boolean isOk() {
        return ok;
    }

    public T getData() {
        return data;
    }
}
