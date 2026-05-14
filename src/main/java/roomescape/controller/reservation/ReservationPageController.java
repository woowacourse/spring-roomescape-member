package roomescape.controller.reservation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import roomescape.controller.reservation.dto.ReservationResponse;
import roomescape.exception.ApiException;
import roomescape.service.reservation.ReservationService;
import roomescape.controller.reservationtime.dto.ReservationTimeResponse;
import roomescape.service.reservationtime.ReservationTimeService;
import roomescape.controller.theme.dto.ThemeResponse;
import roomescape.service.theme.ThemeService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pages/user/reservations")
public class ReservationPageController {

    private final ReservationService reservationService;
    private final ReservationTimeService reservationTimeService;
    private final ThemeService themeService;

    public ReservationPageController(
            final ReservationService reservationService,
            final ReservationTimeService reservationTimeService,
            final ThemeService themeService
    ) {
        this.reservationService = reservationService;
        this.reservationTimeService = reservationTimeService;
        this.themeService = themeService;
    }

    @GetMapping
    public String getReservationPage(
            @RequestParam(required = false) final String themeId,
            @RequestParam(required = false) final String date,
            @RequestParam(required = false) final String reservationName,
            @RequestParam(defaultValue = "7") final int period,
            @RequestParam(defaultValue = "10") final int limit,
            @RequestParam(required = false) final String errorCode,
            final Model model
    ) {
        Long selectedThemeId = null;
        LocalDate selectedDate = null;
        ThemeResponse selectedTheme = null;
        String resolvedErrorCode = errorCode;

        try {
            selectedThemeId = parseLongValue(themeId);
            selectedDate = parseDate(date);
            selectedTheme = getSelectedTheme(selectedThemeId);
        } catch (ApiException exception) {
            resolvedErrorCode = resolveErrorCode(resolvedErrorCode, exception.getCode());
        }

        model.addAttribute("themes", themeService.getAll().stream()
                .map(ThemeResponse::from)
                .toList());
        model.addAttribute("popularThemes", themeService.getPopularThemes(period, limit).stream()
                .map(ThemeResponse::from)
                .toList());
        model.addAttribute("selectedThemeId", selectedThemeId);
        model.addAttribute("selectedTheme", selectedTheme);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("reservationName", reservationName);
        model.addAttribute("period", period);
        model.addAttribute("limit", limit);
        model.addAttribute("errorCode", resolvedErrorCode);
        model.addAttribute("reservationTimes", reservationTimeService.getAll().stream()
                .map(ReservationTimeResponse::from)
                .toList());

        List<ReservationTimeResponse> availableTimes = List.of();
        if (selectedTheme != null && selectedDate != null) {
            availableTimes = reservationTimeService.findAvailableTimes(selectedDate, selectedThemeId).stream()
                    .map(ReservationTimeResponse::from)
                    .toList();
        }
        model.addAttribute("availableTimes", availableTimes);
        model.addAttribute("myReservations", getMyReservations(reservationName, resolvedErrorCode));

        return "reservation/list";
    }

    @PostMapping
    public String createReservation(
            @RequestParam(required = false) final String name,
            @RequestParam(required = false) final String date,
            @RequestParam(required = false) final String themeId,
            @RequestParam(required = false) final String timeId,
            final RedirectAttributes redirectAttributes
    ) {
        Long parsedThemeId = null;
        Long parsedTimeId = null;
        LocalDate parsedDate = null;

        try {
            parsedThemeId = parseLongValue(themeId);
            parsedTimeId = parseLongValue(timeId);
            parsedDate = parseDate(date);
            reservationService.save(name, parsedDate, parsedThemeId, parsedTimeId);
            addReservationNameAttribute(redirectAttributes, name);
        } catch (ApiException exception) {
            addThemeIdAttribute(redirectAttributes, parsedThemeId);
            addDateAttribute(redirectAttributes, parsedDate);
            addReservationNameAttribute(redirectAttributes, name);
            redirectAttributes.addAttribute("errorCode", exception.getCode());
            return "redirect:/pages/user/reservations";
        } catch (Exception exception) {
            addThemeIdAttribute(redirectAttributes, parsedThemeId);
            addDateAttribute(redirectAttributes, parsedDate);
            addReservationNameAttribute(redirectAttributes, name);
            redirectAttributes.addAttribute("errorCode", "INTERNAL_SERVER_ERROR");
            return "redirect:/pages/user/reservations";
        }

        return "redirect:/pages/user/reservations";
    }

    @PostMapping("/{id}/delete")
    public String deleteReservation(
            @PathVariable final Long id,
            @RequestParam(required = false) final String reservationName,
            final RedirectAttributes redirectAttributes
    ) {
        try {
            reservationService.deleteByIdAndName(id, reservationName);
        } catch (ApiException exception) {
            addReservationNameAttribute(redirectAttributes, reservationName);
            redirectAttributes.addAttribute("errorCode", exception.getCode());
            return "redirect:/pages/user/reservations";
        } catch (Exception exception) {
            addReservationNameAttribute(redirectAttributes, reservationName);
            redirectAttributes.addAttribute("errorCode", "INTERNAL_SERVER_ERROR");
            return "redirect:/pages/user/reservations";
        }

        addReservationNameAttribute(redirectAttributes, reservationName);
        return "redirect:/pages/user/reservations";
    }

    @PostMapping("/{id}/update")
    public String updateReservation(
            @PathVariable final Long id,
            @RequestParam(required = false) final String reservationName,
            @RequestParam(required = false) final String date,
            @RequestParam(required = false) final String timeId,
            final RedirectAttributes redirectAttributes
    ) {
        Long parsedTimeId = null;
        LocalDate parsedDate = null;

        try {
            parsedTimeId = parseLongValue(timeId);
            parsedDate = parseDate(date);
            reservationService.updateByIdAndName(id, reservationName, parsedDate, parsedTimeId);
        } catch (ApiException exception) {
            addReservationNameAttribute(redirectAttributes, reservationName);
            redirectAttributes.addAttribute("errorCode", exception.getCode());
            return "redirect:/pages/user/reservations";
        } catch (Exception exception) {
            addReservationNameAttribute(redirectAttributes, reservationName);
            redirectAttributes.addAttribute("errorCode", "INTERNAL_SERVER_ERROR");
            return "redirect:/pages/user/reservations";
        }

        addReservationNameAttribute(redirectAttributes, reservationName);
        return "redirect:/pages/user/reservations";
    }

    private ThemeResponse getSelectedTheme(final Long themeId) {
        if (themeId == null) {
            return null;
        }

        return ThemeResponse.from(themeService.getById(themeId));
    }

    private void addDateAttribute(final RedirectAttributes redirectAttributes, final LocalDate date) {
        if (date == null) {
            return;
        }

        redirectAttributes.addAttribute("date", date.toString());
    }

    private void addThemeIdAttribute(final RedirectAttributes redirectAttributes, final Long themeId) {
        if (themeId == null) {
            return;
        }

        redirectAttributes.addAttribute("themeId", themeId);
    }

    private void addReservationNameAttribute(final RedirectAttributes redirectAttributes, final String reservationName) {
        if (reservationName == null || reservationName.isBlank()) {
            return;
        }

        redirectAttributes.addAttribute("reservationName", reservationName);
    }

    private Long parseLongValue(final String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException exception) {
            throw new roomescape.exception.InvalidInputException(
                    "INVALID_TYPE_VALUE",
                    "숫자 형식의 값이 필요합니다."
            );
        }
    }

    private LocalDate parseDate(final String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException exception) {
            throw new roomescape.exception.InvalidInputException(
                    "INVALID_DATE_FORMAT",
                    "날짜 형식이 올바르지 않습니다. yyyy-MM-dd 형식이어야 합니다."
            );
        }
    }

    private String resolveErrorCode(final String currentErrorCode, final String fallbackErrorCode) {
        if (currentErrorCode != null) {
            return currentErrorCode;
        }

        return fallbackErrorCode;
    }

    private List<ReservationResponse> getMyReservations(final String reservationName, final String errorCode) {
        if (reservationName == null || reservationName.isBlank()) {
            return List.of();
        }

        if ("RESERVATION_NAME_REQUIRED".equals(errorCode)) {
            return List.of();
        }

        return reservationService.getAllByName(reservationName).stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
