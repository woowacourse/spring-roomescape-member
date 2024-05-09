package roomescape.dto.request;

import roomescape.domain.Email;

public record TokenRequest(String email, String name) {
}
