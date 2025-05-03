package roomescape.business.model.vo;

public record Id(
        Long id
) {
    public static Id create(final long id) {
        return new Id(id);
    }

    public static Id nullId() {
        return new Id(null);
    }

    public Long longValue() {
        return id;
    }
}
