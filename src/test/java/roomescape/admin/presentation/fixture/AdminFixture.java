package roomescape.admin.presentation.fixture;

import java.time.LocalDate;
import roomescape.admin.presentation.dto.AdminReservationRequest;

public class AdminFixture {

    public AdminReservationRequest createAdminReservation(String date, String themeId, String timeId, String memberId) {
        return new AdminReservationRequest(LocalDate.parse(date), Long.valueOf(themeId), Long.valueOf(timeId),
                Long.valueOf(memberId));
    }

}
