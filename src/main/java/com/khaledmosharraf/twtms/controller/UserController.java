package com.khaledmosharraf.twtms.controller;

import com.khaledmosharraf.twtms.dto.*;
import com.khaledmosharraf.twtms.exception.IncorrectPasswordException;
import com.khaledmosharraf.twtms.mapper.UserRequestMapper;
import com.khaledmosharraf.twtms.service.DistrictService;
import com.khaledmosharraf.twtms.service.SubDistrictService;
import com.khaledmosharraf.twtms.service.UserService;
import com.khaledmosharraf.twtms.utils.PageStatus;
import com.khaledmosharraf.twtms.utils.UrlConstants;
import com.khaledmosharraf.twtms.validations.UserValidator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@SessionAttributes("username")
public class UserController {

    @Autowired
    UserRequestMapper userRequestMapper;
    @Autowired
    UserService userService;
    @Autowired
    UserValidator userValidator;
    @Autowired
    SubDistrictService subDistrictService;
    @Autowired
    DistrictService districtService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping(UrlConstants.User.LIST)
    public String showUserList(Model model){
        List<UserDTO> users = userService.getAll();
        List<DistrictDTO> districts = districtService.getAll();
        model.addAttribute("districts",districts);

        List<SubDistrictDTO> subDistricts = subDistrictService.getAll();
        model.addAttribute("subDistricts",subDistricts);
        model.addAttribute("users",users);
        model.addAttribute("pageTitle", "User Page");
        String username = getLoggedUsername();
        model.addAttribute("username",getLoggedUsername());
        model.addAttribute("activePage","/users");
        model.addAttribute("totalPages",16);

        return "adminPanel/user/list";
      //   return  "user/user_list";
    }
    @GetMapping(UrlConstants.User.CREATE)
    public String showUserForm(Model model ) {
        UserRequestDTO userRequestDTO  = new UserRequestDTO();
        model.addAttribute("user",userRequestDTO);
        List<SubDistrictDTO> subDistricts= subDistrictService.getAll();
        model.addAttribute("subDistricts", subDistricts);
        model.addAttribute("pageTopic","Create New User");
        model.addAttribute("username",getLoggedUsername());
        model.addAttribute("pageStatus", PageStatus.CREATE);
        model.addAttribute("pageStatus_tag", PageStatus.CREATE_TAG);

        return "adminPanel/user/create";

      //  return  "user/create_user";
    }

    @PostMapping(UrlConstants.User.CREATE)
    public String submitUserForm(@Valid @ModelAttribute("user") UserRequestDTO userRequestDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        userValidator.validate(userRequestDTO, bindingResult);
        if(bindingResult.hasErrors()){
            List<SubDistrictDTO> subDistricts= subDistrictService.getAll();
            model.addAttribute("subDistricts", subDistricts);
            model.addAttribute("pageTopic","Create New User");
            model.addAttribute("username",getLoggedUsername());
            model.addAttribute("pageStatus", PageStatus.CREATE);
            model.addAttribute("pageStatus_tag", PageStatus.CREATE_TAG);
            return "adminPanel/user/create";
           // return  "user/create_user";
        }
        userService.add(userRequestMapper.toUserDTO(userRequestDTO));
        redirectAttributes.addFlashAttribute("successMessage", "Added Successfully. Thank You.");
        return "redirect:"+UrlConstants.User.LIST;
    }
    @GetMapping(UrlConstants.User.DELETE)
    public String deleteTodo(@RequestParam long id , RedirectAttributes redirectAttributes) {
        userService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Deleted Successfully. Thank You.");
        return "redirect:"+UrlConstants.User.LIST;
    }
    @GetMapping(UrlConstants.User.UPDATE)
    public String showUpdateUserForm( @RequestParam Long id , Model model) {

        UserDTO userDTO = userService.get(id);
        UserRequestDTO userRequestDTO = userRequestMapper.toUserRequestDTO(userDTO);
        model.addAttribute("user",userRequestDTO);
        List<SubDistrictDTO> subDistricts= subDistrictService.getAll();
        model.addAttribute("subDistricts", subDistricts);
        model.addAttribute("pageTopic","Edit User");
        model.addAttribute("username",getLoggedUsername());
        model.addAttribute("pageStatus", PageStatus.UPDATE);
        model.addAttribute("pageStatus_tag", PageStatus.UPDATE_TAG);
        return "adminPanel/user/create";
      //  return  "user/create_user";
    }


    @PostMapping(UrlConstants.User.UPDATE)
    public String submitUpdateUserForm(@Valid @ModelAttribute("user") UserRequestDTO userRequestDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(error -> {
                logger.error("Validation error: {}", error.getDefaultMessage());
            });
            model.addAttribute("user",userRequestDTO);
            List<SubDistrictDTO> subDistricts= subDistrictService.getAll();
            model.addAttribute("subDistricts", subDistricts);
            model.addAttribute("pageTopic","Edit User");
            model.addAttribute("username",getLoggedUsername());
            model.addAttribute("pageStatus", PageStatus.UPDATE);
            model.addAttribute("pageStatus_tag", PageStatus.UPDATE_TAG);
            return "adminPanel/user/create";
         //   return  "user/create_user";
        }
        UserDTO userDTO= userRequestMapper.toUserDTO(userRequestDTO);
        userService.update(userDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Updated Successfully. Thank You.");
        return "redirect:"+UrlConstants.User.LIST;
    }
    @GetMapping(UrlConstants.User.VIEW)
    public String showViewUserForm( @RequestParam Long id , Model model) {

        UserDTO userDTO = userService.get(id);
        UserRequestDTO userRequestDTO = userRequestMapper.toUserRequestDTO(userDTO);
        model.addAttribute("user",userRequestDTO);
        List<SubDistrictDTO> subDistricts= subDistrictService.getAll();
        model.addAttribute("subDistricts", subDistricts);
        model.addAttribute("pageTopic","View User");
        model.addAttribute("username",getLoggedUsername());
        model.addAttribute("pageStatus", PageStatus.VIEW);
        model.addAttribute("pageStatus_tag", PageStatus.VIEW_TAG);
        return "adminPanel/user/create";

    }
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("oldPassword") String username, @RequestParam("oldPassword") String oldPassword,
                                @RequestParam("newPassword") String newPassword,
                                Model model) {
        try {
            userService.resetPasswordWithOldPassword(username,oldPassword,newPassword);
            return "redirect:/logout";
        } catch (IncorrectPasswordException e) {
            model.addAttribute("errorMessage", "Incorrect old password");
            return "reset-password";
        }
    }
    private String getLoggedUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}
