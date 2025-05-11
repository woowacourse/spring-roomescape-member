package roomescape.auth.sign.password;

import roomescape.user.domain.Password;

public interface PasswordEncoder {

    Password execute(String rawPassword);
}
