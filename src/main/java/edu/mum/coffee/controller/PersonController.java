package edu.mum.coffee.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.mum.coffee.domain.Person;
import edu.mum.coffee.service.PersonService;

@Controller
@RequestMapping("/people")
public class PersonController {

	@Autowired
	private PersonService service;

	@GetMapping("/")
	public String get(Model model) {
		model.addAttribute("people", service.getAll());
		return "person/index";
	}
	
	@GetMapping("/add")
	public String add(@ModelAttribute("person") Person person) {
		return "person/add";
	}
	
	@PostMapping({"/add"})
	public String add(@Valid Person person, BindingResult result) {
		if(result.hasErrors()) {
			return "person/add";
		}
		
		service.add(person);
		return "redirect:/people/";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable long id, @ModelAttribute("person") Person person, Model model) {
		model.addAttribute("person", service.get(id));
		return "person/edit";
	}
	
	@PostMapping("/edit/{id}")
	public String edit(@PathVariable long id, @Valid Person person, BindingResult result) {
		if(result.hasErrors()) {
			person.setId(id);
			return "person/edit";
		}
		
		service.edit(id, person);		
		return "redirect:/people/";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable long id) {
		service.delete(id);
		return "redirect:/people/";
	}
}
