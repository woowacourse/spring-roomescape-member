package roomescape.jwt;

import java.util.Date;
import roomescape.domain.MemberRoleType;

public record JwtRequest(String name, String email, MemberRoleType memberRoleType, Date issuedAt) {
}
