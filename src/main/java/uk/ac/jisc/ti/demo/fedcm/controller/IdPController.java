
package uk.ac.jisc.ti.demo.fedcm.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderAPIConfig;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderAccount;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderAccounts;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderBranding;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderClientMetadata;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderIcon;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderToken;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderWellKnown;

/**
 * Mock FedCM IdP endpoints
 */
@Controller
public class IdPController {

    /** Logger. */
    private static final Logger log = LoggerFactory.getLogger(IdPController.class);

    /** The hostname to use for certain response fields. */
    private final String hostname;

    /**
     * Constructor.
     * 
     * @param host the hostname
     */
    public IdPController(@Value("${fedcm.idp.hostname}") final String host) {
        hostname = Objects.requireNonNull(host);
    }

    @GetMapping("/idp")
    public String getIdPIndex(final HttpServletRequest req) {
    	// Create a session cookie so you can see it being returned.
    	req.getSession();
        return "idp";
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
                .withAccountsEndpoint("/fedcm/accounts").withClientMetadataEndpoint("/fedcm/client_metadata")
                .withIdAssertionEndpoint("/fedcm/assertion")
                .withBranding(IdentityProviderBranding.builder().withBackgroundColor("red").withColor("0xFFEEAA")
                        .withIcons(List.of(IdentityProviderIcon.builder()
                                .withUrl("https://" + hostname + "/images/logo.ico").withSize(50).build()))
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
    @GetMapping(path = "/fedcm/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdentityProviderAccounts> getAccounts(HttpServletRequest req, 
    		@RequestHeader final Map<String, String> headers,
            @RequestParam final Map<String, String> allRequestParams) {
    	
    	if (req.getCookies() != null) {
    		Arrays.stream(req.getCookies()).forEach(c -> log.info("FedCM Account Cookie: {}:{}", c.getName(), c.getValue()));
    	}

        headers.entrySet().forEach(h -> log.info("FedCM Account Header: {}", h));

        // There should not be any of these.
        allRequestParams.entrySet().forEach(p -> log.info("FedCM Accounts Param: {}", p));

        // Dummy response
        final IdentityProviderAccount account = IdentityProviderAccount.builder().withId("1234").withName("James Kirk")
                .withEmail("james.kirk@idp.example").withGivenName("James").withApprovedClients(List.of("1234"))
                .withPicture("https://" + hostname + "/images/kirk.ico").build();

        log.info("Built IdentityProviderAccount response: '{}'", account);

        return ResponseEntity.status(HttpStatus.OK).body(new IdentityProviderAccounts(List.of(account)));
    }

    /**
     * Endpoint for returning client metadata.
     * 
     * @return the IdP's client metadata
     */
    @GetMapping(path = "/fedcm/client_metadata", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdentityProviderClientMetadata> getClientMetadata() {
        // Dummy response
        final IdentityProviderClientMetadata metadata =
                IdentityProviderClientMetadata.builder().withPrivacyPolicyUrl("https://" + hostname + "/privacy.html")
                        .withTermsOfServiceUrl("https://" + hostname + "/tos.html").build();

        log.info("Built IdentityProviderClientMetadata response: '{}'", metadata);

        return ResponseEntity.status(HttpStatus.OK).body(metadata);
    }

    /**
     * Identity assertion/token endpoint.
     * 
     * @return a dummy assertion for now
     */
    @PostMapping(path = "/fedcm/assertion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdentityProviderToken> getAssertion(HttpServletRequest req,
    		@RequestHeader final Map<String, String> headers,
            @RequestParam final Map<String, String> allRequestParams) {
    	
    	if (req.getCookies() != null) {
    		Arrays.stream(req.getCookies()).forEach(c -> log.info("FedCM Assertion Cookie: {}:{}", c.getName(), c.getValue()));
    	}

        headers.entrySet().forEach(h -> log.info("FedCM Assertion Header: {}", h));

        allRequestParams.entrySet().forEach(p -> log.info("FedCM Assertion Param: {}", p));

        // Dummy response
        final String dummyJWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
                + ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ"
                + ".SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        final IdentityProviderToken token = new IdentityProviderToken(dummyJWT);

        log.info("Built IdentityProviderToken: '{}'", token);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    /**
     * Get the well-known webidentity of this IdP. Which points to the server manifests. See {@link #getManifest()}.
     * 
     * @return the IdentityProviderWellKnown JSON
     */
    @GetMapping(path = ".well-known/web-identity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdentityProviderWellKnown> getWebIdentity() {
        // Dummy response
        final IdentityProviderWellKnown wellKnown = new IdentityProviderWellKnown(List.of("/fedcm.json"));

        return ResponseEntity.status(HttpStatus.OK).body(wellKnown);
    }
    
    private String getCookieValue(HttpServletRequest req, String cookieName) {
        return Arrays.stream(req.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

}
