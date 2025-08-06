package uk.ac.jisc.ti.demo.fedcm.idp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IdPControllerLW {
	
	@GetMapping("/idp/idplw")
	public String getLoginButton() {
		return "idp/idplw";
	}

}
