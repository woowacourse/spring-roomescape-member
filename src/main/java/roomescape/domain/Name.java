package roomescape.domain;

public record Name(String name) {

    public Name {
        validate(name);
    }

    private void validate(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException(String.format("%s 는 유효하지 않은 값입니다.", name));
        }
    }
}
