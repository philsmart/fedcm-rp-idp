
package org.philsmart.demo.fedcm.controller;

import java.util.List;

import org.philsmart.demo.fedcm.model.IdentityProviderAPIConfig;
import org.philsmart.demo.fedcm.model.IdentityProviderAccount;
import org.philsmart.demo.fedcm.model.IdentityProviderAccounts;
import org.philsmart.demo.fedcm.model.IdentityProviderBranding;
import org.philsmart.demo.fedcm.model.IdentityProviderClientMetadata;
import org.philsmart.demo.fedcm.model.IdentityProviderToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * FedCM IdP endpoints
 */
@Controller
public class IdPController {

    /** Logger. */
    private static final Logger log = LoggerFactory.getLogger(IdPController.class);

    /**
     * Endpoint to fetch the API config for this IdP.
     * 
     * @return the API config.
     */
    @GetMapping(path = "/fedcm.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdentityProviderAPIConfig> getIdentityProviderAPIConfig() {
        // Dummy response
        final IdentityProviderAPIConfig config = IdentityProviderAPIConfig.builder()
                .withAccountsEndpoint("/fedcm/accounts").withClientMetadataEndpoint("/fedcm/client_metadata")
                .withIdAssertionEndpoint("/fedcm/assertion")
                .withBranding(IdentityProviderBranding.builder().withBackgroundColor("red").withColor("blue").build())
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
    public ResponseEntity<IdentityProviderAccounts> getAccounts() {
        // Dummy response
        final IdentityProviderAccount account =
                IdentityProviderAccount.builder().withId("1234").withName("John").withEmail("john_doe@idp.example")
                        .withGivenName("John Doe").withApprovedClients(List.of("1234")).build();

        log.info("Built IdentityProviderAccount response: '{}'", account);

        return ResponseEntity.status(HttpStatus.OK).body(new IdentityProviderAccounts(List.of(account)));
    }

    /**
     * Open endpoint for returning client metadata?
     * 
     * @return the IdP's client metadata
     */
    @GetMapping(path = "/fedcm/client_metadata", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdentityProviderClientMetadata> getClientMetadata() {
        // Dummy response
        final IdentityProviderClientMetadata metadata = IdentityProviderClientMetadata.builder()
                .withPrivacyPolicyUrl("https://demo.fedcm.org:8080/privacy.html")
                .withTermsOfServiceUrl("https://demo.fedcm.org:8080/tos.html").build();

        log.info("Built IdentityProviderClientMetadata response: '{}'", metadata);

        return ResponseEntity.status(HttpStatus.OK).body(metadata);
    }

    /**
     * Identity assertion endpoint
     * 
     * @return a dummy assertion for now
     */
    @PostMapping(path = "/fedcm/assertion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdentityProviderToken> getAssertion() {
        // Dummy response
        final String dummyJWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
                + ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ"
                + ".SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        return ResponseEntity.status(HttpStatus.OK).body(new IdentityProviderToken(dummyJWT));
    }

}
