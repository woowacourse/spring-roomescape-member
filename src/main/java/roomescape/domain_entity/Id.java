package roomescape.domain_entity;

public record Id(long value) {

    public static Id empty() {
        return new Id(0);
    }

    public long value() {
        if (value == 0) {
            throw new IllegalStateException("id값이 존재하지 않습니다.");
        }
        return value;
    }
}
