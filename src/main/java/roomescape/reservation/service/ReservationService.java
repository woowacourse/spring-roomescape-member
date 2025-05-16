package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.global.exception.RoomEscapeException.BadRequestException;
import roomescape.global.exception.RoomEscapeException.ResourceNotFoundException;
import roomescape.global.pagination.PaginationUtil;
import roomescape.global.pagination.PaginationUtil.PageInfo;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.CreateReservationRequest;
import roomescape.reservation.dto.response.AdminReservationPageResponse;
import roomescape.reservation.dto.response.AdminReservationPageResponse.AdminReservationPageElementResponse;
import roomescape.reservation.dto.response.CreateReservationResponse;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;
import roomescape.user.domain.User;
import roomescape.user.service.UserService;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final UserService userService;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationService(ReservationDao reservationDao,
                              UserService userService,
                              ReservationTimeService reservationTimeService,
                              ThemeService themeService) {
        this.reservationDao = reservationDao;
        this.userService = userService;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    public CreateReservationResponse addReservation(CreateReservationRequest createReservationRequest) {
        ReservationTime reservationTime = reservationTimeService
                .getReservationTimeById(createReservationRequest.timeId());
        Theme theme = themeService.getThemeById(createReservationRequest.themeId());
        User user = userService.getUserById(createReservationRequest.memberId());

        validateNewReservation(createReservationRequest);

        Reservation reservation = new Reservation(
                user,
                createReservationRequest.date(),
                reservationTime,
                theme
        );
        Reservation saved = reservationDao.save(reservation);
        return CreateReservationResponse.from(saved);
    }

    private void validateNewReservation(CreateReservationRequest createReservationRequest) {
        if (!createReservationRequest.date().isAfter(LocalDate.now())) {
            throw new BadRequestException("하루 전 까지 예약 가능합니다.");
        }

        if (reservationDao.isExistByThemeIdAndTimeIdAndDate(
                createReservationRequest.themeId(),
                createReservationRequest.timeId(),
                createReservationRequest.date())
        ) {
            throw new BadRequestException("이미 해당 시간에 예약이 존재합니다.");
        }
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
        PageInfo pageInfo = PaginationUtil.calculatePageInfo(page, totalReservations);

        List<AdminReservationPageElementResponse> reservationResponses = reservationDao.findReservationsWithPage(
                        pageInfo.startIdx(),
                        pageInfo.endIdx(),
                        userId,
                        themeId,
                        dateFrom,
                        dateTo
                ).stream()
                .map(AdminReservationPageElementResponse::from)
                .toList();

        return new AdminReservationPageResponse(pageInfo.totalPage(), reservationResponses);
    }
}
