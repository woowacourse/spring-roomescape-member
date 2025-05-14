package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.global.exception.RoomEscapeException.BadRequestException;
import roomescape.global.exception.RoomEscapeException.ResourceNotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.CreateReservationRequest;
import roomescape.reservation.dto.response.AdminReservationPageResponse;
import roomescape.reservation.dto.response.AdminReservationPageResponse.AdminReservationPageElementResponse;
import roomescape.reservation.dto.response.AdminReservationPageResponse.AdminReservationPageElementResponse.AdminReservationPageMemberElementResponse;
import roomescape.reservation.dto.response.AdminReservationPageResponse.AdminReservationPageElementResponse.AdminReservationPageThemeElementResponse;
import roomescape.reservation.dto.response.AdminReservationPageResponse.AdminReservationPageElementResponse.AdminReservationPageTimeElementResponse;
import roomescape.reservation.dto.response.CreateReservationResponse;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeDao;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeDao;
import roomescape.user.domain.User;
import roomescape.user.repository.UserDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final UserDao userDao;

    public ReservationService(ReservationDao reservationDao,
                              ReservationTimeDao reservationTimeDao,
                              ThemeDao themeDao, UserDao userDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.userDao = userDao;
    }

    public CreateReservationResponse addReservation(CreateReservationRequest createReservationRequest) {
        ReservationTime reservationTime = reservationTimeDao.findById(createReservationRequest.timeId())
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 시간입니다."));
        Theme theme = themeDao.findById(createReservationRequest.themeId())
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 테마입니다."));
        User user = userDao.findById(createReservationRequest.memberId())
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 사용자입니다."));
        LocalDate reservationDate = createReservationRequest.date();
        if (!reservationDate.isAfter(LocalDate.now())) {
            throw new BadRequestException("하루 전 까지 예약 가능합니다.");
        }
        if (reservationDao.isExistByThemeIdAndTimeIdAndDate(
                createReservationRequest.themeId(),
                createReservationRequest.timeId(),
                createReservationRequest.date())
        ) {
            throw new BadRequestException("이미 해당 시간에 예약이 존재합니다.");
        }
        Reservation reservation = new Reservation(
                null,
                user,
                createReservationRequest.date(),
                reservationTime,
                theme
        );
        Reservation savedReservation = reservationDao.save(reservation);
        return CreateReservationResponse.fromEntity(savedReservation);
    }

    public void deleteReservation(Long id) {
        boolean isDeleted = reservationDao.deleteById(id);
        if (!isDeleted) {
            throw new ResourceNotFoundException("해당하는 id가 없습니다");
        }
    }

    public AdminReservationPageResponse getReservationsByPage(int page,
                                                              Long userId,
                                                              Long themeId,
                                                              LocalDate dateFrom,
                                                              LocalDate dateTo) {
        if (dateFrom != null && dateTo != null && dateTo.isBefore(dateFrom)) {
            throw new BadRequestException();
        }
        int totalReservations = reservationDao.countTotalReservation(userId, themeId, dateFrom, dateTo);
        int totalPage = totalReservations % 10 == 0 ?
                totalReservations / 10 : (totalReservations / 10) + 1;
        if (totalReservations != 0 && (page < 1 || page > totalPage)) {
            throw new ResourceNotFoundException("해당하는 페이지가 없습니다");
        }
        int start = (page - 1) * 10 + 1;
        int end = start + 10 - 1;
        List<AdminReservationPageElementResponse> briefReservations = reservationDao.findReservationsWithPage(
                        start,
                        end,
                        userId,
                        themeId,
                        dateFrom,
                        dateTo
                )
                .stream()
                .map(reservation -> new AdminReservationPageElementResponse(
                        reservation.getId(),
                        new AdminReservationPageMemberElementResponse(
                                reservation.getUser().getId(),
                                reservation.getUser().getName()
                        ),
                        new AdminReservationPageThemeElementResponse(
                                reservation.getTheme().getId(),
                                reservation.getTheme().getName()
                        ),
                        new AdminReservationPageTimeElementResponse(
                                reservation.getReservationTime().getId(),
                                reservation.getReservationTime().getStartAt()
                        ),
                        reservation.getDate()
                ))
                .toList();

        return new AdminReservationPageResponse(totalPage, briefReservations);
    }
}
