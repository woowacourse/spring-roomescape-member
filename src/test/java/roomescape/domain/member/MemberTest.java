package roomescape.domain.member;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class MemberTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"@woo.com", "sfd@w", "@woo", "no.com"})
    void 이메일_형식에_맞지_않으면_예외가_발생한다(String email) {
        Member invalidMember = new Member(new MemberName("lemone"),
                email, "lemone1234", MemberRole.ADMIN);
        Set<ConstraintViolation<Member>> violation = validator.validate(invalidMember);

        violation.forEach(error -> {
            assertEquals("이메일 형식에 맞게 입력해 주세요.", error.getMessage());
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"123", "01234567890123456789"})
    void 비밀번호가_4글자_미만이거나_20글자_초과이면_예외가_발생한다(String invalidPassword) {
        String validEmail = "lemone@wooteco.com";
        Member invalidMember = new Member(new MemberName("lemone"), validEmail, invalidPassword, MemberRole.ADMIN);

        Set<ConstraintViolation<Member>> violation = validator.validate(invalidMember);

        violation.forEach(error -> {
            assertEquals("비밀번호는 최소 4글자, 최대 20글자로 작성해주세요.", error.getMessage());
        });
    }
}
