package roomescape.util;

public interface TokenProvider {

    String create(String value);

    String extract(String token);
}
