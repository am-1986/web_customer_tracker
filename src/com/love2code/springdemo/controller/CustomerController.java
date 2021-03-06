package com.love2code.springdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.love2code.springdemo.entity.Customer;
import com.love2code.springdemo.service.CustomerService;
import com.love2code.springdemo.util.SortUtils;

@Controller
@RequestMapping("/customer")
public class CustomerController {
	
	// inject customer service
	@Autowired
	private CustomerService customerService;

	@GetMapping("/list")
	public String listCustomers(Model model, @RequestParam(required=false) String sort) {
		
		// get customers from the customer service
		List<Customer> theCustomers = null;
		
		// check for sort field
		if(sort != null) {
			
			int sortField = Integer.parseInt(sort);
			
			theCustomers = customerService.getCustomers(sortField);
			
		} else {
			
			// no sort field provided. default to sorting by last name
			theCustomers = customerService.getCustomers(SortUtils.LAST_NAME);
		}
		
		// add the customers to the model
		model.addAttribute("customers", theCustomers);
		
		return "list-customers";
	}
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model model) {
		
		// create model attribute to bind form data
		Customer theCustomer = new Customer();
		
		model.addAttribute("customer", theCustomer);
		
		return "customer-form";
	}
	
	@PostMapping("/saveCustomer")
	public String saveCustomer(@ModelAttribute("customer") Customer theCustomer) {
		
		// save the customer using service
		customerService.saveCustomer(theCustomer);
		
		return "redirect:/customer/list";
	}
	
	@GetMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("customerId") int id, Model model) {
		
		// get customer from the customer service
		Customer theCustomer = customerService.getCustomer(id);
		
		// set customer as model attribute to pre-populate the form
		model.addAttribute("customer", theCustomer);
		
		// send over to the form
		return "customer-form";
	}
	
	@GetMapping("delete")
	public String deleteCustomer(@RequestParam("customerId") int id) {
		
		// delete the customer
		customerService.deleteCustomer(id);
		
		return "redirect:/customer/list";
	}
	
	@GetMapping("/search")
	public String searchCustomers(@RequestParam("theSearchName") String searchName, Model model) {
		
		// search customers from the service
		List<Customer> theCustomers = customerService.searchCustomers(searchName);
		
		// Add the customers to the model
		model.addAttribute("customers", theCustomers);
		
		return "list-customers";
		
	}		
}
