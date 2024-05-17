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

package uk.ac.jisc.ti.demo.fedcm.rp.controller;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.ac.jisc.ti.demo.fedcm.model.CredentialRequestOptions;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityCredentialRequestOptions;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityCredentialRequestOptionsContext;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderConfig;

/**
 * FedCM RP controller demo
 */
@Controller
public class RPController {

    /** Logger. */
    private static final Logger log = LoggerFactory.getLogger(RPController.class);

    /** The clientID. */
    private final String clientId;

    /** JSON Object Mapper. */
    private final ObjectMapper mapper;

    /** The loaded IdP configuration information.*/
    private final CredentialRequestOptions credRequestOptions;
    
    /**
     * Constructor.
     * 
     * @param host the hostname
     * @throws Exception on error loading the idp config
     */
    public RPController(@Value("${fedcm.rp.clientid:https://rp.localhost:8080/}") final String clientIdentifier,
            @Value("${fedcm.rp.idpConfig:classpath:/identity-provider-config.json}") final Resource idpConfig) throws Exception {
       
        clientId = Objects.requireNonNull(clientIdentifier);
        
        // Load config. Fail if config fails
        credRequestOptions = loadIdPConfig(idpConfig);

        mapper = new ObjectMapper();
        log.info("++Started FedCM Relying Party");
    }

    /**
     * Load the idpConfig. 
     * 
     * @param idpConfig the idp config file
     * @throws Exception if the file can not be read 
     */
    private CredentialRequestOptions loadIdPConfig(Resource idpConfig) throws Exception {
		ObjectMapper om = new ObjectMapper();
		if (!idpConfig.exists()) {
			throw new IllegalArgumentException("IdPConfig file does not exist: " + idpConfig);
		}
		log.info("Loading IdP config: {}", idpConfig);
		try (var stream = idpConfig.getInputStream()){
			CredentialRequestOptions config = 
					om.readValue(stream, CredentialRequestOptions.class);
			log.info("Read IdentityProviderConfig: {}", config);
			return config;
		}
	}

	/**
     * Simple redirect to the main RP page.
     * 
     * @return a redirect to the RP page.
     */
    @GetMapping("/")
    public String getRPIndex() {
        return "redirect:rp";
    }

    /**
     * Display the main RP page.
     * 
     * @param model
     * @return
     */
    @GetMapping("/rp")
    public String getRPIndex(final Model model) {
        try {
            final String credOptionsSingle = mapper.writeValueAsString(buildCredentialRequestOptions());
            model.addAttribute("credentialRequestOptionsSingle", credOptionsSingle);
            log.info("Created CredentialsRequestOptions: {}", credOptionsSingle);
        } catch (final JsonProcessingException e) {
            log.error("Unable to write CredentialsRequestOptions to JSON string", e);
        }
        //model.addAttribute("rootDomain", rootDomain);
        return "rp";
    }

    /**
     * Logout endpoint of the RP.
     * 
     * @return Logout success or failure.
     */
    //TODO not implemented
    @GetMapping(path = "/rp/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logout() {
        log.info("Logout request sent from the IdP");
        return ResponseEntity.status(HttpStatus.OK).body("");

    }

    /** Build a credential request options. */
    private CredentialRequestOptions buildCredentialRequestOptions() {
        return credRequestOptions;
    }


}
