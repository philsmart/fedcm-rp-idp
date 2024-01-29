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

package uk.ac.jisc.ti.demo.fedcm.idp.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.jisc.ti.demo.fedcm.model.CredentialRequestOptions;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderAPIConfig;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderAccount;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderAccounts;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderBranding;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderClientMetadata;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderIcon;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderToken;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderWellKnown;

/**
 * Mock FedCM IdP endpoints.
 */
@Controller
@RequestMapping("/idp")
public class IdPController {

    /** Logger. */
    private static final Logger log = LoggerFactory.getLogger(IdPController.class);

    /** The hostname to use for certain response fields. */
    private final String hostname;
    
    /** The url scheme. */
    private final String scheme;
    
    /** The loaded accounts.*/
    private final IdentityProviderAccounts accounts;

    /**
     * Constructor.
     * 
     * @param host the host
     * @param schemeIn the URL scheme.
     * @param accountsFile the JSON file to load accounts from
     * @throws Exception if the accounts file fails to load
     */
    public IdPController(@Value("${fedcm.idp.hostname}") final String host, 
    		@Value("${fedcm.url.scheme:https://}") final String schemeIn,
    		@Value("${fedcm.idp.idpAccounts}") final Resource accountsFile) throws Exception {
    	log.info("++Started FedCM Identity Provider");
        hostname = Objects.requireNonNull(host);
        scheme = Objects.requireNonNull(schemeIn);
        accounts = loadAccount(accountsFile);
    }
    
    /**
     * Return the personalised login button page.
     * 
     * @return the personalised login button page
     */
    @GetMapping("/login-button")
    public String getLoginButton() {
    	log.info("Getting login button");
    	return "idp/login-button";
    }
    
    /**
     * Load the accounts for the mock user. 
     * 
     * @param accountsFile the idp accounts file
     * @throws Exception if the file can not be read 
     */
    private IdentityProviderAccounts loadAccount(Resource accountsFile) throws Exception {
		ObjectMapper om = new ObjectMapper();
		if (!accountsFile.exists()) {
			throw new IllegalArgumentException("Accounts file does not exist: " + accountsFile);
		}
		try (var stream = accountsFile.getInputStream()){
			IdentityProviderAccounts accounts = 
					om.readValue(stream, IdentityProviderAccounts.class);
			log.info("Loaded IdentityProviderAccounts: {}", accounts);
			return accounts;
		}
	}

    /**
     * Get a basic IdP Page for login and logout.
     * 
     * @param req the HTTP request
     * 
     * @return the idp view
     */
    @GetMapping("")
    public String getIdPIndex(final HttpServletRequest req, Model model) {
    	if (hasSession(req)) {
    		model.addAttribute("signedin", true);
    	} else {
    		model.addAttribute("signedin", false);
    	}
    	
        return "idp/idp";
    }
    
    @GetMapping("/reauth")
    public String getSigninUrlIndex(final HttpServletRequest req, Model model) {    	
        return "idp/reauth";
    }
    
    /**
     * Check if the user has a valid HTTP session. Also check the header to see if an override
     * has been supplied which allows the endpoint to operate if it has no session.
     * 
     * @param req the HTTP request
     * 
     * @return if a session exists for this request or not
     */
    private boolean hasSession(@Nonnull final HttpServletRequest req) {
    	if (req.getHeader("X-FedCM-IdP-NoSession") != null) {
    		return true;
    	}
    	if (req.getSession(false) != null) {
    		log.trace("User has a session", req.getSession(false).getId());
    		return true;
    	} else {
    		log.trace("User does not have a session");
    		return false;
    	}
    }
    
    /**
     * Login to the IdP. Simply create a HTTP Session and set the session cookie, no actual authentication is required. 
     * 
     * 
     * @param req the http request
     * @return the IdP homepage
     */
    @GetMapping("/login")
    public String getLogin(final HttpServletRequest req, final HttpServletResponse resp) {
        // Create a session cookie so you can see it being returned.
        req.getSession();
        resp.setHeader("Set-Login", "logged-in");
        log.info("Login request");
        return "redirect:/idp";
    }
    
    /**
     * Logout of the IdP. Simply remove their session and send browser IdP-SignIn-Status action=signout-all.
     * 
     * 
     * @param req the http request
     * @return the IdP homepage
     */
    @GetMapping("/logout")
    public String getLogout(final HttpServletRequest req, final HttpServletResponse resp) {
        req.getSession().invalidate();
        resp.setHeader("IdP-SignIn-Status", "action=signout-all");
        log.info("Logout event");
        return "redirect:/idp";
    }
    
    /**
     * Logout of the IdP but do not send IdP-SignIn-Status back to the browser. 
     * 
     * 
     * @param req the http request
     * @return the IdP homepage
     */
    @GetMapping("/logoutIdPOnly")
    public String getLogoutIdPOnly(final HttpServletRequest req, final HttpServletResponse resp) {
        req.getSession().invalidate();
        log.info("Invalidate session");
        return "redirect:/idp";
    }

    /**
     * Endpoint to fetch the manifest that holds the API config for this IdP.
     * 
     * @return the API config.
     */
    @GetMapping(path = "/fedcm.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdentityProviderAPIConfig> getManifest() {
        // Dummy response
        final IdentityProviderAPIConfig config = IdentityProviderAPIConfig.builder()
                .withAccountsEndpoint("/idp/accounts").withClientMetadataEndpoint("/idp/client_metadata")
                .withIdAssertionEndpoint("/idp/assertion")
                .withLoginUrl("/idp/reauth")
                .withBranding(IdentityProviderBranding.builder().withBackgroundColor("red").withColor("0xFFEEAA")
                        .withIcons(List.of(IdentityProviderIcon.builder()
                                .withUrl(scheme + hostname + "/images/logo.ico").withSize(50).build()))
                        .build())
                .build();
        log.info("Built IdentityProviderAPIConfig response: '{}'", config);

        return ResponseEntity.status(HttpStatus.OK).body(config);
    }

    /**
     * Endpoint to fetch signed-in accounts as identified by the cookie session identifier.
     * 
     * @return the signed-in accounts for the identified sesssion
     */
    @GetMapping(path = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdentityProviderAccounts> getAccounts(final HttpServletRequest req,
            @RequestHeader final Map<String, String> headers,
            @RequestParam final Map<String, String> allRequestParams) {
        
        checkRequestValidity(req, headers);
        logCookiesAndHeaders(req, headers, allRequestParams);
        
        if (!hasSession(req)) {
        	// Dummy response with no values	
	        log.info("No session and no accounts");      
	        return ResponseEntity.status(HttpStatus.OK).body(new IdentityProviderAccounts(Collections.emptyList()));
        	
        } else {
	        // Dummy response	
	        log.info("IdentityProviderAccount response: '{}'", accounts);        
	
	        return ResponseEntity.status(HttpStatus.OK).body(accounts);
        }
    }
    
    /**
     * Log the cookies, headers, and any params in the request.
     * 
     * @param req the http request
     * @param headers the http headers
     * @param allRequestParams the http params
     */
    private void logCookiesAndHeaders(@Nonnull final HttpServletRequest req, 
    		@Nonnull final Map<String, String> headers,
    		@Nonnull final Map<String, String> allRequestParams) {
    	
        if (req.getCookies() != null) {
            Arrays.stream(req.getCookies())
                    .forEach(c -> log.info("FedCM Account Cookie: {}:{}", c.getName(), c.getValue()));
        }

        headers.entrySet().forEach(h -> log.info("FedCM Account Header: {}", h));        

        allRequestParams.entrySet().forEach(p -> log.info("FedCM Accounts Param: {}", p));
    }
    
    /**
     * Is the HTTP request complaint with the FedCM specification.
     * 
     * @param req the HTTP request
     * @param headers the HTTP headers
     */
    private void checkRequestValidity(@Nonnull final HttpServletRequest req,
    		@Nonnull final Map<String, String> headers) {
    	
    	if (!"webidentity".equals(headers.get("sec-fetch-dest"))) {
    		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sec-Fetch-Dest header is not webidentity");
    	}
    }

    /**
     * Endpoint for returning client metadata.
     * 
     * @param req the HTTP request
     * @param headers the HTTP headers
     * 
     * @return the IdP's client metadata
     */
    @GetMapping(path = "/client_metadata", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdentityProviderClientMetadata> getClientMetadata(final HttpServletRequest req,
            @RequestHeader final Map<String, String> headers) {
    	
    	checkRequestValidity(req, headers);
        // Dummy response
        final IdentityProviderClientMetadata metadata =
                IdentityProviderClientMetadata.builder().withPrivacyPolicyUrl("https://" + hostname + "/privacy.html")
                        .withTermsOfServiceUrl(scheme + hostname + "/tos.html").build();

        log.info("Built IdentityProviderClientMetadata response: '{}'", metadata);

        return ResponseEntity.status(HttpStatus.OK).body(metadata);
    }

    /**
     * Identity assertion/token endpoint.
     *      
     * @param req the HTTP request
     * @param headers the HTTP headers 
     * @param allRequestParams the request parameters
     * 
     * @return a dummy assertion for now
     */
    @PostMapping(path = "/assertion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdentityProviderToken> getAssertion(final HttpServletRequest req,
            @RequestHeader final Map<String, String> headers,
            @RequestParam final Map<String, String> allRequestParams) {
    	
    	logCookiesAndHeaders(req, headers, allRequestParams);
    	
    	if (!hasSession(req)) {
    		log.info("No session and not assertion");
    		final IdentityProviderToken token =
	                IdentityProviderToken.builder().withToken("").build();
    		return ResponseEntity.status(HttpStatus.OK).body(token);
    	} else {
	        // Dummy response
	        final String dummyJWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
	                + ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ"
	                + ".SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
	        final IdentityProviderToken token =
	                IdentityProviderToken.builder().withToken(dummyJWT).build();
	
	        log.info("Built IdentityProviderToken: '{}'", token);
	        return ResponseEntity.status(HttpStatus.OK).body(token);
    	}
    }


    /**
     * Get the value of a cookie.
     * 
     * @param req the http request
     * @param cookieName the cookie name
     * @return the cookie value
     */
    private String getCookieValue(@Nonnull final HttpServletRequest req, @Nonnull final String cookieName) {
        return Arrays.stream(req.getCookies()).filter(c -> c.getName().equals(cookieName)).findFirst()
                .map(Cookie::getValue).orElse(null);
    }

}
