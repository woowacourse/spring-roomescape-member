package roomescape.auth;

public class StubJwtProvider extends JwtProvider {

    private String returnValue;

    @Override
    public String provideToken(final String payload) {
        return returnValue;
    }

    public void stubToken(final String returnValue) {
        this.returnValue = returnValue;
    }
}
