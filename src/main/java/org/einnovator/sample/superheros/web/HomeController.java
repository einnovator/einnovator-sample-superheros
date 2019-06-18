package org.einnovator.sample.superheros.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController extends ControllerBase {
	
	@GetMapping("/")
	public String home(Model model, Principal principal, HttpServletRequest request) {
		return redirect("/superhero");
	}

}
