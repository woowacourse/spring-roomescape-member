package roomescape.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> errors;

    public ErrorResponse(Builder builder) {
        this.code = builder.code;
        this.message = builder.message;
        this.errors = builder.errors;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public static class ValidationError {
        private final String field;
        private final String message;

        private ValidationError(Builder builder) {
            this.field = builder.field;
            this.message = builder.message;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }

        public static class Builder {
            private String field;
            private String message;

            public Builder field(String field) {
                this.field = field;
                return this;
            }

            public Builder message(String message) {
                this.message = message;
                return this;
            }

            public ValidationError build() {
                return new ValidationError(this);
            }
        }
    }

    public static class Builder {
        private String code;
        private String message;

        private List<ValidationError> errors;

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder errors(List<ValidationError> errors) {
            this.errors = errors;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }
}
