package roomescape.domain;

public class UserName {

    private static final int MAX_NAME_LENGTH = 20;
    private static final String NAME_FORMAT = "^[a-zA-Z가-힣ㄱ-ㅎ0-9]*$";

    private final String userName;

    public UserName(String userName) {
        validate(userName);
        this.userName = userName;
    }

    private void validate(String userName) {
        if (userName.isBlank() || userName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("예약자명은 1글자 이상, " + MAX_NAME_LENGTH + "글자 이하로 작성해야 합니다.");
        }
        if (!userName.matches(NAME_FORMAT)) {
            throw new IllegalArgumentException("예약자명은 한글, 영문, 숫자만 허용합니다.");
        }
    }

    public String getUserName() {
        return userName;
    }
}
