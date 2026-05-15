package roomescape.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    private final String code;
    private final String message;
    private final String path;
    private final LocalDateTime timestamp;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> errors;

    public ErrorResponse(Builder builder) {
        this.code = builder.code;
        this.message = builder.message;
        this.path = builder.path;
        this.timestamp = builder.timestamp;
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

    public String getPath() {
        return path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public static class ValidationError {
        private final String field;
        private final String message;
        private final Object rejectedValue;

        private ValidationError(Builder builder) {
            this.field = builder.field;
            this.message = builder.message;
            this.rejectedValue = builder.rejectedValue;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .rejectedValue(fieldError.getRejectedValue())
                    .build();
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }

        public Object getRejectedValue() {
            return rejectedValue;
        }

        public static class Builder {
            private String field;
            private String message;
            private Object rejectedValue;

            public Builder field(String field) {
                this.field = field;
                return this;
            }

            public Builder message(String message) {
                this.message = message;
                return this;
            }

            public Builder rejectedValue(Object rejectedValue) {
                this.rejectedValue = rejectedValue;
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
        private String path;
        private LocalDateTime timestamp;

        private List<ValidationError> errors;

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
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
