package uk.ac.jisc.ti.demo.fedcm.idp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ResponseStatusException;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import uk.ac.jisc.ti.demo.fedcm.model.IdentityProviderWellKnown;

/** Controller that serves the root of the domain .*/
@Controller
public class RootController {
	
	/**
     * Get the well-known webidentity of this IdP. Which points to the server manifests. See {@link #getManifest()}.
     * 
     * @param req the HTTP request
     * @param headers the HTTP headers
     * @return the IdentityProviderWellKnown JSON
     */
    @GetMapping(path = ".well-known/web-identity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IdentityProviderWellKnown> getWebIdentity(final HttpServletRequest req,
            @RequestHeader final Map<String, String> headers) {
    	
    	checkRequestValidity(req, headers);
    	
        // Dummy response
        final IdentityProviderWellKnown wellKnown = new IdentityProviderWellKnown(List.of("/fedcm.json"));

        return ResponseEntity.status(HttpStatus.OK).body(wellKnown);
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

}
