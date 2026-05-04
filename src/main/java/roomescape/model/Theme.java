package roomescape.model;

public class Theme {
    private final String name;
    private final String description;
    private final String url;

    public Theme(String name, String description, String url) {
        this.name = name;
        this.description = description;
        this.url = url;
        validateName();
    }

    private void validateName(){
        if (name.length() < 1 || name.length() > 20) {
            throw new IllegalArgumentException("[ERROR] 테마 이름은 1자 이상 20자 이하입니다.");
        }
    }

    public String getName() {
        return name;
    }
}
