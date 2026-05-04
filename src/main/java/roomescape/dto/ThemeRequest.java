package roomescape.dto;

public class ThemeRequest {
    private  String name;
    private  String description;
    private  String url;

    public ThemeRequest(String name, String description, String url) {
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

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}
