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
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.jisc.ti.demo.fedcm.model.CredentialRequestOptions;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityCredentialRequestOptions;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderConfig;
import uk.ac.jisc.ti.demo.fedcm.rp.model.IdPSelection;
import uk.ac.jisc.ti.demo.fedcm.rp.saml.SamlEngine;

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

    /** The globally loaded IdP configuration information.*/
    private final CredentialRequestOptions credRequestOptions;
    
    /** A SAML processing engine.*/
    private final SamlEngine samlEngine;

    /**
     * Constructor.
     * 
     * @param host the hostname
     * @param idpConfig a preselected set of IdPs to build requests for. If null, one must be selected through the UI.
     * @throws Exception on error loading the idp config
     */
    public RPController(@Value("${fedcm.rp.clientid:https://rp.localhost:8080/}") final String clientIdentifier,
            @Value("${fedcm.rp.idpConfig:#{null}}") final Resource idpConfig) throws Exception {
       
        clientId = Objects.requireNonNull(clientIdentifier);
        
        if (idpConfig != null) {
        	// Load config. Fail if config fails
        	credRequestOptions = loadIdPConfig(idpConfig);
        } else {
        	credRequestOptions = null;
        }

        samlEngine = new SamlEngine();

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
    
    @GetMapping("/rp/ds")
    public String getStartDiscovery(@RequestParam(name="target", required=false) final String target) {
    	return "redirect:https://service.seamlessaccess.org/ds/?return=http://rp.localhost:8081/rp/ds-end&target="+target;
    }
    
    @GetMapping("/rp/ds-end")
    public String getEndDiscovery(@RequestParam(name="entityID", required=false) final String entityId,
    		final HttpServletRequest req) {
    	log.info("Retrieved IdP from DS '{}'", entityId);
    	req.getSession().setAttribute("idpSelection", new IdPSelection(entityId));
    	return "redirect:/";
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
    public String getRPIndex(final Model model, final HttpServletRequest req, final HttpServletResponse resp) {
        try {
        	Object idpSelectionObj = req.getSession().getAttribute("idpSelection");
        	//TODO REMOVE fix to CU for now
        	idpSelectionObj = new IdPSelection("https://proxy.idp.cardiff.ac.uk/shibboleth");
        	Object authenticated = req.getSession().getAttribute("authenticated");;
        	Object authenticatedPrincipal = req.getSession().getAttribute("principal");
        	Object samlResponse = req.getSession().getAttribute("samlresponse");
        	
        	// If we have a json config, just augment it with saml requests
        	if (credRequestOptions != null) {
        		augmentCredentialRequestOptions(credRequestOptions, req, resp);
        		final String credOptions = mapper.writeValueAsString(credRequestOptions);
        		model.addAttribute("credentialRequestOptionsSingle", credOptions);
	            log.info("Created CredentialsRequestOptions: {}", credOptions);
	            model.addAttribute("idpSelected", "true");
        	} else {        	
	        	CredentialRequestOptions opts;
	        	if (idpSelectionObj instanceof IdPSelection idp) {
	        		log.info("IdP selected as '{}'", idp.getEntityID());
	        		opts = buildCredentialRequestOptions(req, resp, idp);
	        		model.addAttribute("idpSelected", "true");
	        		URL entityURL = new URL(idp.getEntityID());
	        		model.addAttribute("selectedIdP", entityURL.getHost());
	        	} else {
	        		//TODO maybe build nothing if no IdP selected
	        		opts = buildCredentialRequestOptions(null, null, null);
	        		model.addAttribute("idpSelected", "false");
	        		
	        	}
	            final String credOptionsSingle = mapper.writeValueAsString(opts);
	            model.addAttribute("credentialRequestOptionsSingle", credOptionsSingle);
	            log.info("Created CredentialsRequestOptions: {}", credOptionsSingle);
        	}
            
            if (authenticated instanceof Boolean authn) {
            	model.addAttribute("authenticated",authn);
            } else {
            	model.addAttribute("authenticated","false");
            }
            
            if (authenticatedPrincipal instanceof String principal) {
            	model.addAttribute("principal",principal);
            }
            if (samlResponse instanceof String response) {
            	model.addAttribute("samlresponse",response);
            }
            
        } catch (final Exception e) {
            log.error("Unable to write CredentialsRequestOptions to JSON string", e);
        }
        //model.addAttribute("rootDomain", rootDomain);
        return "/rp";
    }
    
    /**
     * Add a SAML request to each of the IdPs in the config.
     * 
     * @param options the credential to augment
     * @throws Exception on error
     */
    private void augmentCredentialRequestOptions(final CredentialRequestOptions options, 
    		@Nullable final HttpServletRequest req, @Nullable final HttpServletResponse resp) throws Exception {
		for (var provider : options.getIdentity().getProviders()) {
    		String authnRequest = samlEngine.buildAuthnRequest(req, resp, null);
    		provider.getParams().put("SAMLRequest", authnRequest);
		}
		
	}

	@PostMapping("/rp/token-callback")
    public String tokenCallback(final Model model, @RequestParam("tokenpost") final String token,
    		final HttpServletRequest req) throws JsonMappingException, JsonProcessingException {
    	log.info("Have token '{}'", token);
    	var splitToken = token.split("\\.");
    	if (splitToken.length == 3) {
    		byte[] payloadAsBytes = Base64Utils.decodeFromUrlSafeString(splitToken[1]);
    		String payload = new String(payloadAsBytes);
    		ObjectMapper om = new ObjectMapper();
    		Map<String,String> payloadMap = om.readValue(payload, new TypeReference<Map<String,String>>() {});
    		req.getSession().setAttribute("principal", payloadMap.get("name"));
    		req.getSession().setAttribute("authenticated", true);
    	} else {
    		// assume SAML, but add to try so we can switch to a nice error if not
    		try {
	    		byte[] payloadAsBytes = Base64Utils.decodeFromString(token);
	    		String payload = new String(payloadAsBytes);
	    		if (payload.contains("AuthnFailed") || payload.contains("error")) {
	    			//TODO just say authenticated, but give wrong principal to stop looping for now
	    			req.getSession().setAttribute("authenticated", true);
	    			req.getSession().setAttribute("principal", "SAML Authn Failed");
	    			req.getSession().setAttribute("samlresponse", SamlEngine.prettyFormat(payload));
	    		} else {
	    			req.getSession().setAttribute("authenticated", true);
	    			req.getSession().setAttribute("principal", "SAML Authn Success");
	    			req.getSession().setAttribute("samlresponse", SamlEngine.prettyFormat(payload));
	    		}
    		} catch (Exception e) {
    			req.getSession().setAttribute("authenticated", true);
    			req.getSession().setAttribute("principal", "SAML Authn Failed");
    		}
    		
    	}
    	
    	log.info("User authenticated!");
    	return "redirect:/";
    }

    /**
     * Logout endpoint of the RP.
     * 
     * @return Logout success or failure.
     */
    //TODO not implemented
//    @GetMapping(path = "/rp/logout", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<String> logout() {
//        log.info("Logout request sent from the IdP");
//        return ResponseEntity.status(HttpStatus.OK).body("");
//
//    }
    
    @GetMapping(path = "/rp/logout")
	public String logout(final HttpServletRequest req) {
		log.info("Logout request");
		req.getSession().setAttribute("authenticated", false);
		req.getSession().setAttribute("idpSelection", null);
		req.getSession().setAttribute("samlresponse", null);
		return "redirect:/";

	}

    /** Build a credential request options. 
     * @throws IOException */
    private CredentialRequestOptions buildCredentialRequestOptions(@Nullable final HttpServletRequest req, @Nullable final HttpServletResponse resp, 
    		@Nullable final IdPSelection selection) throws Exception {
    	if (selection == null) {
    		return credRequestOptions;
    	} else {
    		IdentityProviderConfig idpConfig = loadIdPConfig(selection.getEntityID());
    		String authnRequest = samlEngine.buildAuthnRequest(req, resp, null);
    		idpConfig.getParams().put("SAMLRequest", authnRequest);
    		return CredentialRequestOptions.builder().withIdentity(IdentityCredentialRequestOptions.builder()
    				.withProviders(List.of(idpConfig)).withMode("active").build()).build();
    	}
    }

	private IdentityProviderConfig loadIdPConfig(String entityID) throws IOException {
		URL url = new URL(entityID);
		String hostname = url.getHost();
		Resource metadataLocation = new ClassPathResource("metadata/"+hostname+".json");
		ObjectMapper om = new ObjectMapper();
		try (var stream = metadataLocation.getInputStream()){
			IdentityProviderConfig config = om.readValue(stream, new TypeReference<IdentityProviderConfig>() {});
			log.info("Read IdP Config: {}", config);
			return config;
		}
	}


}
