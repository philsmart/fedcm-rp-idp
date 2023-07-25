/*
 * Copyright 2023 Jisc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under
 * the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
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
