package edu.mum.coffee.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.mum.coffee.domain.*;
import edu.mum.coffee.service.PersonService;

@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private PersonService service;
	
	@GetMapping({"/register"})
	public String register(@ModelAttribute("person") Person person) {
		return "account/register";
	}
	
	@PostMapping({"/register"})
	public String register(@Valid Person person, BindingResult result) {
		if(result.hasErrors()) {
			return "account/register";
		}
		
		service.add(person);
		return "redirect:/login";
	}
	
	@GetMapping({"/update"})
	public String update(@ModelAttribute("person") Person person, Model model) {
		String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Person p = service.getByEmail(email);
		
		if(p == null) {
			return "redirect:/";
		}else {
			model.addAttribute("person", p);
			return "account/update";
		}
	}	
	
	@PostMapping({"/update/{id}"})
	public String update(@PathVariable long id, @Valid Person person, BindingResult result) {
		if(result.hasErrors()) {
			person.setId(id);
			return "account/update";
		}
		
		service.edit(id, person);
		return "redirect:/";
	}
}
