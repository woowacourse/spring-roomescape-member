package roomescape.member.dto;

import jakarta.validation.constraints.Email;

public record MemberRequest(@Email String email, String password, String name) {
}
