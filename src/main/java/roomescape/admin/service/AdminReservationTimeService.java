package roomescape.admin.service;

import java.time.LocalTime;
import org.springframework.stereotype.Service;
import roomescape.admin.dao.AdminReservationTimeDao;
import roomescape.domain.ReservationTime;

@Service
public class AdminReservationTimeService {

    private final AdminReservationTimeDao adminReservationTimeDao;

    public AdminReservationTimeService(AdminReservationTimeDao adminReservationTimeDao) {
        this.adminReservationTimeDao = adminReservationTimeDao;
    }

    public ReservationTime addReservationTime(LocalTime startAt) {
        ReservationTime time = new ReservationTime(startAt);
        return adminReservationTimeDao.insert(time);
    }

    public void deleteById(Long id) {
        adminReservationTimeDao.delete(id);
    }
}
