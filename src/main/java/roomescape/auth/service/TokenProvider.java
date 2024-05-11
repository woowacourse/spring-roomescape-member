package roomescape.auth.service;

import roomescape.auth.domain.Payload;

public interface TokenProvider<T> {
    String createAccessToken(T payload);

    Payload<T> getPayload(String token);

    boolean isToken(String token);
}
