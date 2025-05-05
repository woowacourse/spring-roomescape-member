package roomescape.reservation.domain;

import roomescape.reservation.domain.exception.ReserverNameEmptyException;

public class ReserverName {

    private final String name;

    public ReserverName(String name) {
        this.name = validateBlank(name);
    }

    private String validateBlank(String reserverName) {
        if (reserverName == null || reserverName.isBlank()) {
            throw new ReserverNameEmptyException("[ERROR] 예약자 이름은 필수입니다.");
        }
        return reserverName;
    }

    public String getName() {
        return name;
    }
}
