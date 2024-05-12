package roomescape.domain;

import java.util.regex.Pattern;

public class MemberName {

    private static final int MAX_NAME_LENGTH = 20;
    private static final Pattern NAME_FORMAT = Pattern.compile("^[a-zA-Z가-힣ㄱ-ㅎ0-9]*$");

    private final String memberName;

    public MemberName(String memberName) {
        validate(memberName);
        this.memberName = memberName;
    }

    private void validate(String memberName) {
        if (memberName.isBlank() || memberName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("예약자명은 1글자 이상, " + MAX_NAME_LENGTH + "글자 이하로 작성해야 합니다.");
        }
        if (!NAME_FORMAT.matcher(memberName).matches()) {
            throw new IllegalArgumentException("예약자명은 한글, 영문, 숫자만 허용합니다.");
        }
    }

    public String getMemberName() {
        return memberName;
    }
}
