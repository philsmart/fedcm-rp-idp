package uk.ac.jisc.ti.demo.fedcm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * FedCM RP controller demo
 */
@Controller
public class RPController {
	
	 /** Logger. */
    private static final Logger log = LoggerFactory.getLogger(RPController.class);
	
	@GetMapping("/rp")
	public String getRPIndex() {
		return "rp";
	}
	
	@GetMapping(path = "/rp/logout", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> logout(){
		log.info("Logout request sent from the IdP");
		return ResponseEntity.status(HttpStatus.OK).body("");
		
	}

}
