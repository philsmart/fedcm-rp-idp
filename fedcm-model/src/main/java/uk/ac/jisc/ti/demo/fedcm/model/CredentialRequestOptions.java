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

package uk.ac.jisc.ti.demo.fedcm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = CredentialRequestOptions.Builder.class)
public class CredentialRequestOptions {

    private final IdentityCredentialRequestOptions identity;

    /**
     * @return the identity
     */
    @JsonProperty("identity")
    public final IdentityCredentialRequestOptions getIdentity() {
        return identity;
    }

    private CredentialRequestOptions(final Builder builder) {
        this.identity = builder.identity;
    }

    public static IIdentityStage builder() {
        return new Builder();
    }

    public interface IIdentityStage {
        public IBuildStage withIdentity(IdentityCredentialRequestOptions identity);
    }

    public interface IBuildStage {
        public CredentialRequestOptions build();
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static final class Builder implements IIdentityStage, IBuildStage {
        private IdentityCredentialRequestOptions identity;

        private Builder() {
        }

        @Override
        public IBuildStage withIdentity(final IdentityCredentialRequestOptions identity) {
            this.identity = identity;
            return this;
        }

        @Override
        public CredentialRequestOptions build() {
            return new CredentialRequestOptions(this);
        }
    }

	@Override
	public String toString() {
		return "CredentialRequestOptions [identity=" + identity + "]";
	}

}
