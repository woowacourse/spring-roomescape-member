package roomescape.domain;

import java.util.List;
import java.util.regex.Pattern;
import roomescape.exception.InvalidInputException;

public record Thumbnail(String value) {

    private static final List<String> EXTENSIONS = List.of("jpg", "jpeg", "png", "heic");
    private static final Pattern ALLOW_EXTENSIONS = Pattern.compile(
            String.format("^\\S+.(?i)(%s)$", String.join("|", EXTENSIONS)));


    public Thumbnail {
        validate(value);
    }

    private void validate(final String url) {
        validateExt(url);
    }

    private void validateExt(final String url) {
        if (!ALLOW_EXTENSIONS.matcher(url).matches()) {
            throw InvalidInputException.of(String.format("thumbnail (%s 확장자만 가능)", String.join(", ", EXTENSIONS)), url);
        }
    }

    public String asString() {
        return value;
    }
}
