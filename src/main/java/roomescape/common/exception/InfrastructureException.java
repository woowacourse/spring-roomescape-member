package roomescape.common.exception;

public class InfrastructureException extends RuntimeException {

    private final ErrorPolicy errorPolicy;

    public InfrastructureException(ErrorPolicy errorPolicy) {
        super(errorPolicy.message());
        this.errorPolicy = errorPolicy;
    }

}
