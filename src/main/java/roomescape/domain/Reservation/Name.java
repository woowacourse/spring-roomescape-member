package roomescape.domain.Reservation;

import java.util.regex.Pattern;

public class Name {

    private final String name;

    public Name(String name) {
        validateName(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private void validateName(String name) {
        if (!Pattern.matches("^[a-zA-Zㄱ-ㅎ가-힣]*$", name)) {
            throw new IllegalArgumentException("[ERROR] 이름은 영어 또는 한글만 가능합니다.");
        }

        if (name.length() < 2 || name.length() > 10) {
            throw new IllegalArgumentException("[ERROR] 이름은 2~10자만 가능합니다.");
        }
    }
}
