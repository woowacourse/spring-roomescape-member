package roomescape.business.model.vo;

public class Id {

    private final Long id;

    private Id(final Long id) {
        this.id = id;
    }

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
