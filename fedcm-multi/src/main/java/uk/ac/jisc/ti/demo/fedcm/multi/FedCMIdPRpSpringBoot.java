package uk.ac.jisc.ti.demo.fedcm.multi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import uk.ac.jisc.ti.demo.fedcm.idp.controller.IdPController;
import uk.ac.jisc.ti.demo.fedcm.rp.controller.RPController;

@SpringBootApplication(scanBasePackageClasses = {RPController.class, IdPController.class})
public class FedCMIdPRpSpringBoot {
	
	public static void main(String[] args) {
		SpringApplication.run(FedCMIdPRpSpringBoot.class, args);
	}

}
