package roomescape.domain.vo;

import java.util.Objects;

// TODO: record 만들기?
// TODO: name을 갖는 도메인이 많은데 상속? 그냥 여러 클래스(UserName, ThemeName 등등)
public class Name {

    private final String value;

    public Name(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(value, name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
