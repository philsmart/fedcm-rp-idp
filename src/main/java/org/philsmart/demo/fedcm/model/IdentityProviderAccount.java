
package org.philsmart.demo.fedcm.model;

import java.util.Collections;
import java.util.List;

public class IdentityProviderAccount {

    private final String id;

    private final String name;

    private final String email;

    /** Optional. */
    private final String givenName;

    private final List<String> approvedClients;

    @Override
    public String toString() {
        return "IdentityProviderAccount [id=" + id + ", name=" + name + ", email=" + email + ", givenName=" + givenName
                + ", approvedClients=" + approvedClients + "]";
    }

    /**
     * @return the id
     */
    public final String getId() {
        return id;
    }

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @return the email
     */
    public final String getEmail() {
        return email;
    }

    /**
     * @return the givenName
     */
    public final String getGivenName() {
        return givenName;
    }

    /**
     * @return the approvedClients
     */
    public final List<String> getApprovedClients() {
        return approvedClients;
    }

    private IdentityProviderAccount(final Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.givenName = builder.givenName;
        this.approvedClients = builder.approvedClients;
    }

    public static IIdStage builder() {
        return new Builder();
    }

    public interface IIdStage {
        public INameStage withId(String id);
    }

    public interface INameStage {
        public IEmailStage withName(String name);
    }

    public interface IEmailStage {
        public IBuildStage withEmail(String email);
    }

    public interface IBuildStage {
        public IBuildStage withGivenName(String givenName);

        public IBuildStage withApprovedClients(List<String> approvedClients);

        public IdentityProviderAccount build();
    }

    public static final class Builder implements IIdStage, INameStage, IEmailStage, IBuildStage {
        private String id;

        private String name;

        private String email;

        private String givenName;

        private List<String> approvedClients = Collections.emptyList();

        private Builder() {
        }

        @Override
        public INameStage withId(final String id) {
            this.id = id;
            return this;
        }

        @Override
        public IEmailStage withName(final String name) {
            this.name = name;
            return this;
        }

        @Override
        public IBuildStage withEmail(final String email) {
            this.email = email;
            return this;
        }

        @Override
        public IBuildStage withGivenName(final String givenName) {
            this.givenName = givenName;
            return this;
        }

        @Override
        public IBuildStage withApprovedClients(final List<String> approvedClients) {
            this.approvedClients = approvedClients;
            return this;
        }

        @Override
        public IdentityProviderAccount build() {
            return new IdentityProviderAccount(this);
        }
    }

}
