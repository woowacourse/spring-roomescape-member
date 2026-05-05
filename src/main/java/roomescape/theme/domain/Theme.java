package roomescape.theme.domain;

public class Theme {
    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnailUrl;
    private final boolean isActive;

    private Theme(Long id, String name, String description, String thumbnailUrl, boolean isActive ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.isActive = isActive;
    }

    public static Theme create(String name, String description, String thumbnailUrl){
        validate(name, description, thumbnailUrl);
        return new Theme(null, name, description, resolveThumbnailUrl(thumbnailUrl), false);
    }

    private static void validate(String name, String description, String thumbnailUrl){
        validateName(name);
        validateDescription(description);
        validateThumbnailUrl(thumbnailUrl);
    }

    private static void validateName(String name) {
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("테마 이름은 필수입니다.");
        }
    }

    private static void validateThumbnailUrl(String thumbnailUrl) {
        if(thumbnailUrl == null){
            throw new IllegalArgumentException("테마 썸네일 URL은 필수입니다.");
        }
    }

    private static void validateDescription(String description) {
        if(description == null || description.isBlank()){
            throw new IllegalArgumentException("테마 설명은 필수입니다.");
        }
    }

    private static String resolveThumbnailUrl(String thumbnailUrl){
        if(thumbnailUrl.isBlank()){
            return "dommy-url"; //todo: default thumbnail url
        }
        return thumbnailUrl;
    }

    public Long id(){
        return id;
    }

    public String name(){
        return name;
    }

    public String description(){
        return description;
    }

    public String thumbnailUrl(){
        return thumbnailUrl;
    }

    public boolean isActive(){
        return isActive;
    }
}
