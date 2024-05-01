package roomescape.domain;

import java.util.List;
import java.util.regex.Pattern;

public class Thumbnail {

    private static final List<String> ALLOW_EXTENTIONS = List.of("jpg", "jpeg", "png", "heic");
    public static final String ALLOW_EXTENSIONS_PATTERN = String.join("|", ALLOW_EXTENTIONS);
    private static final Pattern PATTERN = Pattern.compile(
            String.format("^\\S+.(?i)(%s)$", ALLOW_EXTENSIONS_PATTERN));

    private final String value;

    public Thumbnail(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String url) {
        validateNull(url);
        validateExt(url);
    }

    private void validateNull(String url) {
        if (url.isBlank()) {
            throw new IllegalArgumentException("");
        }
    }

    private void validateExt(String url) {
        if (!PATTERN.matcher(url).matches()) {
            throw new IllegalArgumentException(String.format("%s 확장자만 가능합니다.", ALLOW_EXTENSIONS_PATTERN));
        }
    }
}
