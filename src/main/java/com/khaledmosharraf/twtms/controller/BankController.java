package com.khaledmosharraf.twtms.controller;

import com.khaledmosharraf.twtms.dto.BankDTO;
import com.khaledmosharraf.twtms.dto.BankDTO;
import com.khaledmosharraf.twtms.dto.BankRequestDTO;
import com.khaledmosharraf.twtms.mapper.BankMapper;
import com.khaledmosharraf.twtms.mapper.BankRequestMapper;
import com.khaledmosharraf.twtms.service.BankService;
import com.khaledmosharraf.twtms.utils.PageStatus;
import jakarta.validation.Valid;
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
public class BankController {

    @Autowired
    BankRequestMapper bankRequestMapper;
    @Autowired
    BankService bankService;

    @GetMapping("banks")
    public String showBankList(Model model){
        List<BankDTO> banks = bankService.getAll();
        model.addAttribute("banks",banks);
        model.addAttribute("pageTitle", "Bank Page");
        String username = getLoggedUsername();
        model.addAttribute("username",getLoggedUsername());
        model.addAttribute("totalPages",16);

        return "adminPanel/bank/list";
      //   return  "bank/bank_list";
    }
    @GetMapping("banks2")
    public String showBankList2(Model model){
        List<BankDTO> banks = bankService.getAll();
        model.addAttribute("banks",banks);
        model.addAttribute("pageTitle", "Bank Page");
        String username = getLoggedUsername();
        model.addAttribute("username",getLoggedUsername());
        model.addAttribute("totalPages",16);

        return "bank/bank_list";
    }


    @GetMapping("create-bank")
    public String showBankForm(Model model ) {
        BankDTO bankDTO  = new  BankDTO();
        model.addAttribute("bank",bankDTO);
        model.addAttribute("pageTopic","Create New Bank");
        model.addAttribute("username",getLoggedUsername());
        model.addAttribute("pageStatus", PageStatus.CREATE);
        model.addAttribute("pageStatus_tag", PageStatus.CREATE_TAG);

        return "adminPanel/bank/create";

      //  return  "bank/create_bank";
    }

    @PostMapping("create-bank")
    public String submitBankForm(@Valid @ModelAttribute("bank") BankRequestDTO bankRequestDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            model.addAttribute("pageTopic","Create New Bank");
            model.addAttribute("username",getLoggedUsername());
            model.addAttribute("pageStatus", PageStatus.CREATE);
            model.addAttribute("pageStatus_tag", PageStatus.CREATE_TAG);
            return "adminPanel/bank/create";
           // return  "bank/create_bank";
        }
        BankDTO bankDTO = bankRequestMapper.toBankDTO(bankRequestDTO);
        bankService.add(bankDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Added Successfully. Thank You.");
        return "redirect:/banks";
    }
    @GetMapping("delete-bank")
    public String deleteTodo(@RequestParam long id , RedirectAttributes redirectAttributes) {
        bankService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Deleted Successfully. Thank You.");
        return "redirect:/banks";
    }
    @GetMapping("update-bank")
    public String showUpdateBankForm( @RequestParam Long id , Model model) {

        BankDTO bankDTO = bankService.get(id);
        model.addAttribute("bank",bankDTO);

        model.addAttribute("pageTopic","Edit Bank");
        model.addAttribute("username",getLoggedUsername());
        model.addAttribute("pageStatus", PageStatus.UPDATE);
        model.addAttribute("pageStatus_tag", PageStatus.UPDATE_TAG);
        return "adminPanel/bank/create";
      //  return  "bank/create_bank";
    }


    @PostMapping("update-bank")
    public String submitUpdateBankForm(@Valid @ModelAttribute("bank") BankRequestDTO bankRequestDTO, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            model.addAttribute("pageTopic","Edit Bank");
            model.addAttribute("username",getLoggedUsername());
            model.addAttribute("pageStatus", PageStatus.UPDATE);
            model.addAttribute("pageStatus_tag", PageStatus.UPDATE_TAG);
            return "adminPanel/bank/create";
         //   return  "bank/create_bank";
        }
        BankDTO bankDTO = bankRequestMapper.toBankDTO(bankRequestDTO);
        bankService.update(bankDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Updated Successfully. Thank You.");
        return "redirect:/banks";
    }
    @GetMapping("view-bank")
    public String showViewBankForm( @RequestParam Long id , Model model) {

        BankDTO bankDTO = bankService.get(id);
        model.addAttribute("bank",bankDTO);

        model.addAttribute("pageTopic","View Bank");
        model.addAttribute("username",getLoggedUsername());
        model.addAttribute("pageStatus", PageStatus.VIEW);
        model.addAttribute("pageStatus_tag", PageStatus.VIEW_TAG);
        return "adminPanel/bank/create";
      //  return  "bank/create_bank";
    }
    private String getLoggedUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

}
