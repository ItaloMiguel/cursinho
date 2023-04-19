package br.com.blog.cursinho.account.controller;

import br.com.blog.cursinho.account.dto.AccountLoginRequestDTO;
import br.com.blog.cursinho.account.dto.AccountRegisterRequestDTO;
import br.com.blog.cursinho.account.service.RegisterAccountService;
import br.com.blog.cursinho.account.service.impl.CheckRegisterParameters;
import br.com.blog.cursinho.account.service.impl.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class AuthController {

    private final List<String> errorMessages = new ArrayList<>();

    private final RegisterAccountService registerAccountService;
    private final CheckRegisterParameters checkRegisterParameters;

    public AuthController(RegisterAccountService registerAccountService, CheckRegisterParameters checkRegisterParameters) {
        this.registerAccountService = registerAccountService;
        this.checkRegisterParameters = checkRegisterParameters;
    }

    @GetMapping("/authenticate")
    public ModelAndView geAuthenticateView() {
        ModelAndView modelAndView = new ModelAndView("auth/authenticate");

        AccountRegisterRequestDTO accountRegister = new AccountRegisterRequestDTO();
        modelAndView.addObject("accountRegister", accountRegister);

        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView sendRegisterView(@ModelAttribute @Valid AccountRegisterRequestDTO accountDTO, BindingResult bindingResult) {

        System.out.println(accountDTO);

        errorMessages.addAll(checkRegisterParameters.execute(accountDTO, bindingResult));

        if (!errorMessages.isEmpty()) {
            ModelAndView modelAndView = new ModelAndView("redirect:/authenticate");

            modelAndView.addObject("errorMessages", errorMessages);
            modelAndView.addObject("account", accountDTO);

            return modelAndView;
        }

        registerAccountService.createAccount(accountDTO);

        return new ModelAndView("redirect:/");
    }

}
