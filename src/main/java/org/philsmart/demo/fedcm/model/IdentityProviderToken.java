
package org.philsmart.demo.fedcm.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * Token response type.
 */
public class IdentityProviderToken {

    private final String token;

    public IdentityProviderToken(final String theToken) {
        token = Objects.requireNonNull(theToken, "The Token can not be null");
    }

    @JsonGetter("token")
    public String getToken() {
        return token;
    }

}
