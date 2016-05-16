package org.eshark.neospringem.web.cntlr;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CommonController {
 
	@RequestMapping("/landingpage")
	public ModelAndView landMeHEre() {
 
		System.out.println("Controller: Landing Page" );	
		String message = "<br><div style='text-align:center;'>"
				+ "<h3>Hello World, Spring MVC Tutorial</h3></div><br><br>";
		return new ModelAndView("landingpage", "message", message);
	}
	
	@RequestMapping("/collapsibletree")
	public ModelAndView collapsibleTree() {
 
		System.out.println("Controller: Landing Page" );	
		String message = "<br><div style='text-align:center;'>"
				+ "<h3>D3 Examples...</h3></div><br><br>";
		return new ModelAndView("collapsibletree", "message", message);
	}
	
	@RequestMapping("/tilfordtree")
	public ModelAndView tilfordTree() {
 
		System.out.println("Controller: Landing Page" );	
		String message = "<br><div style='text-align:center;'>"
				+ "<h3>D3 Examples...</h3></div><br><br>";
		return new ModelAndView("tilfordtree", "message", message);
	}

	
	
	
	
}