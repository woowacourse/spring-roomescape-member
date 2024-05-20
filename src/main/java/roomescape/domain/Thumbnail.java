package roomescape.domain;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import roomescape.exception.CustomBadRequest;

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
            throw new CustomBadRequest(
                    String.format("thumbnail(%s)이 유효하지 않습니다. (가능한 확장자: %s)", url, EXTENSIONS_JOINER.apply(", "))
            );
        }
    }

    public String asString() {
        return value;
    }
}
