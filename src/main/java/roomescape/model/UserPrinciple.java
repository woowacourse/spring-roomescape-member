package roomescape.model;

import java.util.List;

public record UserPrinciple(Long id, String name, String email, List<Role> roles) {
}
