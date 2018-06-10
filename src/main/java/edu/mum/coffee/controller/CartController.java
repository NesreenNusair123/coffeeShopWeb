package edu.mum.coffee.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.mum.coffee.domain.*;
import edu.mum.coffee.service.OrderService;
import edu.mum.coffee.service.PersonService;
import edu.mum.coffee.service.ProductService;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private OrderService orderService;

	@GetMapping({ "/" })
	public String list(Model model, HttpServletRequest request) {
		model.addAttribute("cart", getProducts(request));
		return "cart/index";
	}

	private List<Orderline> getProducts(HttpServletRequest request) {
		List<Orderline> cart = null;

		try {
			cart = (List<Orderline>)request.getSession().getAttribute("cart");
		} catch (Exception e) {
		}
		
		if(cart == null) {
			return new ArrayList<Orderline>();
		}else {
			return cart;
		}
	}

	@GetMapping({ "/add/{id}" })
	public String add(@PathVariable int id, HttpServletRequest request) {
		Product product = productService.get(id);
		addProduct(request, product);
		return "redirect:/";
	}

	private void addProduct(HttpServletRequest request, Product product) {
		List<Orderline> cart = getProducts(request);
		
		Orderline line = cart.stream().filter(c -> c.getProduct().getId() == product.getId()).findFirst().orElse(null);
		
		if(line == null) {
			line = new Orderline();
			line.setProduct(product);
			line.setQuantity(1);
			cart.add(line);
		}else {
			line.setQuantity(line.getQuantity() + 1);
		}
		
		request.getSession().setAttribute("cart", cart);
	}
	
	@GetMapping({"/delete/{id}"})
	public String delete(@PathVariable int id, HttpServletRequest request) {
		List<Orderline> cart = getProducts(request);
		
		Orderline line = cart.stream().filter(c -> c.getProduct().getId() == id).findFirst().orElse(null);
		if(line != null) {
			cart.remove(line);
		}

		request.getSession().setAttribute("cart", cart);
		return "redirect:/cart/";
	}
	
	@GetMapping({"/placeOrder"})
	public String placeOrder(HttpServletRequest request) {
		String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Person p = personService.getByEmail(email);
		if(p == null) {
			return "redirect:/cart/";
		}

		Order order = new Order();
		order.setOrderDate(new Date());
		order.setPerson(p);

		List<Orderline> cart = getProducts(request);		
		for(Orderline line : cart) {
			order.addOrderLine(line);
		}
		
		orderService.add(order);
		request.getSession().setAttribute("cart", new ArrayList<Orderline>());
		return "redirect:/";
	}
}
