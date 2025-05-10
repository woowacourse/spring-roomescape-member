package roomescape.domain.regex;

public class BaseFormat {

    public final static String EMAIL = "[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+";
    public final static String PASSWORD = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$";
}
