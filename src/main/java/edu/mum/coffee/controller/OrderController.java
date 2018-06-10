package edu.mum.coffee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.mum.coffee.domain.*;
import edu.mum.coffee.service.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private PersonService personService;
	
	@Autowired
	private OrderService orderService;

	@GetMapping({ "/" })
	public String listAll(Model model) {
		String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Person p = personService.getByEmail(email);
		if(p == null) {
			return "redirect:/cart/";
		}
		
		List<Order> orders = orderService.findByPerson(p);
		model.addAttribute("orders", orders);
		return "order/index";
	}
}
