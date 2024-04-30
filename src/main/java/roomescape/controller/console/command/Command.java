package roomescape.controller.console.command;

import java.util.Objects;

public class Command {
    private static final int START_VALUE = 0;
    private static final int END_VALUE = 6;

    private final int value;

    public Command(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value < START_VALUE || value > END_VALUE) {
            throw new IllegalArgumentException("[ERROR] 올바른 메뉴를 선택해주세요.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Command command = (Command) o;
        return value == command.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
