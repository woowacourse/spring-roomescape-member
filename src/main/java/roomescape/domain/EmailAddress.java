package roomescape.domain;

import java.util.regex.Pattern;
import roomescape.exception.ParameterException;

public class EmailAddress {
    private static final Pattern EMAIL_ADDRESS_REGEX = Pattern.compile("^\\w+@\\w+\\.\\w+$");

    private final String address;

    public EmailAddress(String address) {
        validatePattern(address);
        this.address = address;
    }

    private void validatePattern(String address) {
        if (!EMAIL_ADDRESS_REGEX.matcher(address).matches()) {
            throw new ParameterException("올바르지 않은 이메일입니다.");
        }
    }

    public String getAddress() {
        return address;
    }
}
