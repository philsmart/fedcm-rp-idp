package org.philsmart.demo.fedcm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * FedCM RP controller demo
 */
@Controller
public class RPController {
	
	@GetMapping("/rp")
	public String getRPIndex() {
		return "rp";
	}

}
