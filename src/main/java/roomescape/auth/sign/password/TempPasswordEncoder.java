package roomescape.auth.sign.password;

import org.springframework.stereotype.Component;

// TODO 변경 예정
@Component
public class TempPasswordEncoder implements PasswordEncoder {

    public static final String secretKey = "(단방향) 해시 처리 완료";

    @Override
    public String execute(final String rawPassword) {
        return rawPassword + secretKey;
    }
}
