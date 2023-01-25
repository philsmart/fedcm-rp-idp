
package uk.ac.jisc.ti.demo.fedcm.controller;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * FedCM RP controller demo
 */
@Controller
public class RPController {

    /** Logger. */
    private static final Logger log = LoggerFactory.getLogger(RPController.class);

    /** The hostname to use for certain response fields. */
    private final String hostname;
    
    /** The root domain, where the well-known file is served.*/
    private final String rootDomain;

    /** The clientID. */
    private final String clientId;

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
    }

    @GetMapping("/rp")
    public String getRPIndex(final Model model) {
        model.addAttribute("hostname", hostname);
        model.addAttribute("rootDomain", rootDomain);
        model.addAttribute("clientId", clientId);
        return "rp";
    }

    @GetMapping(path = "/rp/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logout() {
        log.info("Logout request sent from the IdP");
        return ResponseEntity.status(HttpStatus.OK).body("");

    }

}
