package roomescape.constant;

public enum CommonKey {

    TOKEN_KEY("token");

    private final String key;

    CommonKey(String key) {
        this.key = key;
    }

    public boolean isTokenKey(String value) {
        return TOKEN_KEY.key.equals(value);
    }

    public String getKey() {
        return key;
    }
}
