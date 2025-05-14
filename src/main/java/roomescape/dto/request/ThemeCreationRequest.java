package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record ThemeCreationRequest(
        @NotBlank(message = "[ERROR] 이름은 빈 값이나 공백값을 허용하지 않습니다.")
        @Length(max = 255, message = "[ERROR] 255자를 초과한 값은 허용하지 않습니다.")
        String name,

        @NotBlank(message = "[ERROR] 설명은 빈 값이나 공백값을 허용하지 않습니다.")
        @Length(max = 255, message = "[ERROR] 255자를 초과한 값은 허용하지 않습니다.")
        String description,
        
        @NotBlank(message = "[ERROR] 썸네일은 빈 값이나 공백값을 허용하지 않습니다.")
        @Length(max = 255, message = "[ERROR] 255자를 초과한 값은 허용하지 않습니다.")
        String thumbnail
) {

}
