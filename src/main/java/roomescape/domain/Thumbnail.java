package roomescape.domain;

import java.util.List;
import java.util.regex.Pattern;

public record Thumbnail(String value) {

    private static final List<String> ALLOW_EXTENTIONS = List.of("jpg", "jpeg", "png", "heic");
    public static final String ALLOW_EXTENSIONS_PATTERN = String.join("|", ALLOW_EXTENTIONS);
    private static final Pattern PATTERN = Pattern.compile(
            String.format("^\\S+.(?i)(%s)$", ALLOW_EXTENSIONS_PATTERN));


    public Thumbnail {
        validate(value);
    }

    private void validate(final String url) {
        validateNull(url);
        validateExt(url);
    }

    private void validateNull(final String url) {
        if (url.isBlank()) {
            throw new IllegalArgumentException("");
        }
    }

    private void validateExt(final String url) {
        if (!PATTERN.matcher(url).matches()) {
            throw new IllegalArgumentException(String.format("%s 확장자만 가능합니다.", ALLOW_EXTENSIONS_PATTERN));
        }
    }

    public String asString() {
        return value;
    }
}
