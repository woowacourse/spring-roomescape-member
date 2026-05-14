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
            @RequestParam(defaultValue = "7") final int period,
            @RequestParam(defaultValue = "10") final int limit,
            @RequestParam(required = false) final String errorCode,
            final Model model
    ) {
        Long selectedThemeId = null;
        LocalDate selectedDate = null;
        String resolvedErrorCode = errorCode;

        try {
            selectedThemeId = parseLongValue(themeId);
            selectedDate = parseDate(date);
        } catch (ApiException exception) {
            resolvedErrorCode = resolveErrorCode(resolvedErrorCode, exception.getCode());
        }

        model.addAttribute("reservations", reservationService.getAll().stream()
                .map(ReservationResponse::from)
                .toList());
        model.addAttribute("themes", themeService.getAll().stream()
                .map(ThemeResponse::from)
                .toList());
        model.addAttribute("popularThemes", themeService.getPopularThemes(period, limit).stream()
                .map(ThemeResponse::from)
                .toList());
        model.addAttribute("selectedThemeId", selectedThemeId);
        model.addAttribute("selectedTheme", getSelectedTheme(selectedThemeId));
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("period", period);
        model.addAttribute("limit", limit);
        model.addAttribute("errorCode", resolvedErrorCode);

        List<ReservationTimeResponse> availableTimes = List.of();
        if (selectedThemeId != null && selectedDate != null) {
            availableTimes = reservationTimeService.findAvailableTimes(selectedDate, selectedThemeId).stream()
                    .map(ReservationTimeResponse::from)
                    .toList();
        }
        model.addAttribute("availableTimes", availableTimes);

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
        } catch (ApiException exception) {
            addThemeIdAttribute(redirectAttributes, parsedThemeId);
            addDateAttribute(redirectAttributes, parsedDate);
            redirectAttributes.addAttribute("errorCode", exception.getCode());
            return "redirect:/pages/user/reservations";
        } catch (Exception exception) {
            addThemeIdAttribute(redirectAttributes, parsedThemeId);
            addDateAttribute(redirectAttributes, parsedDate);
            redirectAttributes.addAttribute("errorCode", "INTERNAL_SERVER_ERROR");
            return "redirect:/pages/user/reservations";
        }

        return "redirect:/pages/user/reservations";
    }

    @PostMapping("/{id}/delete")
    public String deleteReservation(@PathVariable final Long id) {
        reservationService.deleteById(id);
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
}
