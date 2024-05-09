package roomescape.domain;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import roomescape.exception.InvalidInputException;

public record Thumbnail(String value) {

    private static final List<String> ALLOWED_EXTENSIONS_LIST = List.of("jpg", "jpeg", "png", "heic");
    private static final Function<String, String> EXTENSIONS_JOINER =
            delimiter -> String.join(delimiter, ALLOWED_EXTENSIONS_LIST);
    private static final Pattern ALLOWED_EXTENSIONS_PATTERN =
            Pattern.compile(String.format("^\\S+.(?i)(%s)$", EXTENSIONS_JOINER.apply("|")));

    public Thumbnail {
        validate(value);
    }

    private void validate(final String url) {
        validateExt(url);
    }

    private void validateExt(final String url) {
        if (!ALLOWED_EXTENSIONS_PATTERN.matcher(url).matches()) {
            throw InvalidInputException.of(String.format("thumbnail (%s 확장자만 가능)", EXTENSIONS_JOINER.apply(", ")), url);
        }
    }

    public String asString() {
        return value;
    }
}
