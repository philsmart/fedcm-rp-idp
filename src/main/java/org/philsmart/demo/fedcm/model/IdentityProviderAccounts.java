
package org.philsmart.demo.fedcm.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * Holder for more than one {@link IdentityProviderAccount}].
 */
public class IdentityProviderAccounts {

    private final List<IdentityProviderAccount> accounts;

    public IdentityProviderAccounts(final List<IdentityProviderAccount> accounts) {
        this.accounts = accounts;
    }

    @JsonGetter("accounts")
    public List<IdentityProviderAccount> getAccounts() {
        return accounts;
    }

}
