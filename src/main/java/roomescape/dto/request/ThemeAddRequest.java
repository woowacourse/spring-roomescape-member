package roomescape.dto.request;

import roomescape.domain.Name;
import roomescape.domain.Theme;

public record ThemeAddRequest(String name, String description, String thumbnail) {

     public ThemeAddRequest {
         validate(name, description, thumbnail);
     }

     private void validate(String name, String description, String thumbnail) {
         if (name == null || name.isBlank()) {
             throw new IllegalArgumentException("잘못된 요청입니다. 테마 이름을 확인해주세요");
         }
         if (description == null || description.isBlank()) {
             throw new IllegalArgumentException("잘못된 요청입니다. 테마 설명을 확인해주세요");
         }
         if (thumbnail == null || thumbnail.isBlank()) {
             throw new IllegalArgumentException("잘못된 요청입니다. 테마 썸네일을 확인해주세요");
         }
     }

    public Theme toTheme() {
        return new Theme(null, new Name(name), description, thumbnail);
    }
}
