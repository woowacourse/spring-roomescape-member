package roomescape.member.domain;

import java.util.Objects;
import roomescape.global.exception.exceptions.InvalidInputException;

public class UserName {

    private static final String NAME_FORMAT = "^[a-zA-Z가-힣ㄱ-ㅎ0-9]*$";

    private final String userName;

    public UserName(String userName) {
        validate(userName);
        this.userName = userName;
    }

    private void validate(String userName) {
        if (userName == null || userName.isBlank()) {
            throw new InvalidInputException("예약자명이 입력되지 않았습니다. 1글자 이상 20글자 이하로 입력해주세요.");
        }
        if (userName.length() > 20) {
            throw new InvalidInputException(userName + "은 유효하지 않은 예약자명입니다. 20글자 이하로 입력해주세요.");
        }
        if (!userName.matches(NAME_FORMAT)) {
            throw new InvalidInputException(userName + "은 유효하지 않은 예약자명입니다. 한글, 영문, 숫자로만 입력해주세요.");
        }
    }

    public String getValue() {
        return userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserName userName1 = (UserName) o;
        return Objects.equals(userName, userName1.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }

    @Override
    public String toString() {
        return "UserName{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
