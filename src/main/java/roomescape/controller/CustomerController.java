package roomescape.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.customer.LoginRequestDto;
import roomescape.service.CustomerService;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("login")
    public void userLogin(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        customerService.verifyLogin(loginRequestDto);
    }
}
