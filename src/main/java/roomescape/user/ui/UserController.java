package roomescape.user.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.common.uri.UriFactory;
import roomescape.user.application.UserWebFacade;
import roomescape.user.ui.dto.CreateUserWebRequest;
import roomescape.user.ui.dto.UserWebResponse;

import java.net.URI;

import static roomescape.user.ui.UserController.BASE_PATH;

@Controller
@RequiredArgsConstructor
@RequestMapping(BASE_PATH)
public class UserController {

    public static final String BASE_PATH = "/users";

    private final UserWebFacade userWebFacade;

    @PostMapping
    public ResponseEntity<UserWebResponse> create(@RequestBody final CreateUserWebRequest request) {
        final UserWebResponse response = userWebFacade.create(request);
        final URI location = UriFactory.buildPath(BASE_PATH, String.valueOf(response.userId()));

        return ResponseEntity.created(location)
                .body(response);
    }
}
