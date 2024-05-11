package roomescape.member.security.crypto;

import java.util.Date;
import java.util.Map;

public interface TokenProvider {

    String createToken(String email, String name, Date issuedAt);

    Map<String, String> getPayload(String token);

    boolean validateToken(String token);
}
