
package uk.ac.jisc.ti.demo.fedcm.controller;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.ac.jisc.ti.demo.fedcm.model.CredentialRequestOptions;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityCredentialRequestOptions;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderConfig;

/**
 * FedCM RP controller demo
 */
@Controller
public class RPController {

    /** Logger. */
    private static final Logger log = LoggerFactory.getLogger(RPController.class);

    /** The hostname to use for certain response fields. */
    private final String hostname;

    /** The root domain, where the well-known file is served. */
    private final String rootDomain;

    /** The clientID. */
    private final String clientId;

    /** JSON Object Mapper. */
    private final ObjectMapper mapper;

    /**
     * Constructor.
     * 
     * @param host the hostname
     */
    public RPController(@Value("${fedcm.idp.hostname}") final String host,
            @Value("${fedcm.idp.rootdomain}") final String root,
            @Value("${fedcm.rp.clientid:https://test.rp.org/}") final String clientIdentifier) {
        hostname = Objects.requireNonNull(host);
        clientId = Objects.requireNonNull(clientIdentifier);
        rootDomain = Objects.requireNonNull(root);

        mapper = new ObjectMapper();
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
            log.info("Created single CredentialsRequestOptions: {}", credOptionsSingle);

            final String credOptionsMultiple =
                    mapper.writeValueAsString(buildCredentialRequestOptionsMultipleProviders());
            model.addAttribute("credentialRequestOptionsMultiple", credOptionsMultiple);
            log.info("Created multiple CredentialsRequestOptions: {}", credOptionsMultiple);
        } catch (final JsonProcessingException e) {
            log.error("Unable to write CredentialsRequestOptions to JSON string", e);
        }
        model.addAttribute("rootDomain", rootDomain);
        return "rp";
    }

    @GetMapping(path = "/rp/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logout() {
        log.info("Logout request sent from the IdP");
        return ResponseEntity.status(HttpStatus.OK).body("");

    }

    /** Build a credential request options. */
    private CredentialRequestOptions buildCredentialRequestOptions() {
        final List<IdentityProviderConfig> configs =
                List.of(IdentityProviderConfig.builder().withConfigURL("https://" + hostname + "/fedcm.json")
                        .withClientId(clientId).withNonce(UUID.randomUUID().toString()).build());

        return CredentialRequestOptions.builder()
                .withIdentity(IdentityCredentialRequestOptions.builder().withProviders(configs).build()).build();
    }

    /** Build a credential request options for multiple providers. */
    private CredentialRequestOptions buildCredentialRequestOptionsMultipleProviders() {
        final List<IdentityProviderConfig> configs = List.of(
                IdentityProviderConfig.builder().withConfigURL("https://" + hostname + "/fedcm.json")
                        .withClientId(clientId).withNonce(UUID.randomUUID().toString()).build(),
                IdentityProviderConfig.builder().withConfigURL("https://fedcm-idp-demo.glitch.me/fedcm.json")
                        .withClientId("'https://fedcm-rp-demo.glitch.me").withNonce(UUID.randomUUID().toString())
                        .build());

        return CredentialRequestOptions.builder()
                .withIdentity(IdentityCredentialRequestOptions.builder().withProviders(configs).build()).build();
    }

}
