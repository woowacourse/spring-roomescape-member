package roomescape.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import roomescape.member.domain.MemberRole;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginMember {

    @NonNull
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @NonNull
    private String password;

    @NonNull
    private MemberRole role;
}
