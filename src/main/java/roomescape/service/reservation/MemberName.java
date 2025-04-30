package roomescape.service.reservation;

public final class MemberName {

    private final String name;

    public MemberName(final String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank() || name.length() > 5) {
            throw new IllegalArgumentException("예약자명은 최소 1글자, 최대 5글자여야합니다.");
        }
    }

    public String getName() {
        return name;
    }
}
