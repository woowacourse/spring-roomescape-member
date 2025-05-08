package roomescape.jwt;

import java.util.Date;
import roomescape.domain.MemberRoleType;

public record JwtRequest(long id, String name, String email, MemberRoleType memberRoleType, Date issuedAt) {
}
