package roomescape.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginWebController {

  @GetMapping
  public String getLoginPage() {
    return "login";
  }
}
