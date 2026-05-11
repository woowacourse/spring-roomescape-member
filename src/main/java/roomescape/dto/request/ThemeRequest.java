package roomescape.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record ThemeRequest(String name, String description, MultipartFile file) {
}
