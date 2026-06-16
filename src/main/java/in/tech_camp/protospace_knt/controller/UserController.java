package in.tech_camp.protospace_knt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_knt.entity.UserEntity;
import in.tech_camp.protospace_knt.repository.UserRepository; 
import in.tech_camp.protospace_knt.service.UserService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService; 

    @GetMapping("/")
    public String index(Model model) {
        List<UserEntity> prototypes = userRepository.findAll();
        model.addAttribute("prototypes", prototypes);
        return "messages/index";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login"; 
    }

    @GetMapping("/signUp")
    public String showSignupForm(Model model) {
        model.addAttribute("userEntity", new UserEntity());
        return "users/signUp"; 
    }

    @PostMapping("/signUp")
    public String registerUser(@Validated @ModelAttribute("userEntity") UserEntity user, 
                               BindingResult bindingResult, 
                               Model model) {
        
        if (bindingResult.hasErrors()) {
            return "users/signUp";
        }

        userService.registerUser(user);
        return "redirect:/login";
    }
}