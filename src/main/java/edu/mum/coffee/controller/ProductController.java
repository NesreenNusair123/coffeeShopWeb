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
import edu.mum.coffee.domain.Product;
import edu.mum.coffee.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService service;

	@GetMapping("/")
	public String get(Model model) {
		model.addAttribute("products", service.getAll());
		return "product/index";
	}
	
	@GetMapping("/add")
	public String add(@ModelAttribute("product") Product product) {
		return "product/add";
	}
	
	@PostMapping({"/add"})
	public String add(@Valid Product product, BindingResult result) {
		if(result.hasErrors()) {
			return "product/add";
		}
		
		service.add(product);
		return "redirect:/products/";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, @ModelAttribute("product") Product product, Model model) {
		model.addAttribute("product", service.get(id));
		return "product/edit";
	}
	
	@PostMapping("/edit/{id}")
	public String edit(@PathVariable int id, @Valid Product product, BindingResult result) {
		if(result.hasErrors()) {
			product.setId(id);
			return "product/edit";
		}
		
		service.edit(id, product);		
		return "redirect:/products/";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable int id) {
		service.delete(id);
		return "redirect:/products/";
	}
}
