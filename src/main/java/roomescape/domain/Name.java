package roomescape.domain;

public record Name(String value) {
    public Name {
        if (value.isBlank()) {
            throw new IllegalArgumentException("이름은 공백을 제외한 1글자 이상이어야 합니다.");
        }
    }
}
