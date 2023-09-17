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

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = IdentityProviderAccount.Builder.class)
@JsonInclude(Include.NON_NULL)
public class IdentityProviderAccount {

	private final String id;

	private final String name;

	private final String email;

	/** Optional. */
	private final String givenName;

	private final List<String> approvedClients;

	private final String picture;
	
	private final List<String> loginHints;

	private IdentityProviderAccount(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.email = builder.email;
		this.givenName = builder.givenName;
		this.approvedClients = builder.approvedClients;
		this.picture = builder.picture;
		this.loginHints = builder.loginHints;
	}

	@Override
	public String toString() {
		return "IdentityProviderAccount [id=" + id + ", name=" + name + ", email=" + email + ", givenName=" + givenName
				+ ", approvedClients=" + approvedClients + "]";
	}

	/**
	 * @return the id
	 */
	@JsonGetter("id")
	public final String getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	@JsonGetter("name")
	public final String getName() {
		return name;
	}

	/**
	 * @return the email
	 */
	@JsonGetter("email")
	public final String getEmail() {
		return email;
	}

	/**
	 * @return the givenName
	 */
	@JsonGetter("given_name")
	public final String getGivenName() {
		return givenName;
	}
	
	/**
	 * @return the givenName
	 */
	@JsonGetter("login_hints")
	public final List<String> getLoginHints() {
		return loginHints;
	}

	/**
	 * @return the approvedClients
	 */
	@JsonGetter("approved_clients")
	public final List<String> getApprovedClients() {
		return approvedClients;
	}
	
	@JsonGetter("picture")
	public final String getPicture() {
		return picture;
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

		public IBuildStage withPicture(String picture);
		
		public IBuildStage withLoginHints(List<String> loginHints);

		public IdentityProviderAccount build();
	}
	
	@JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
	public static final class Builder implements IIdStage, INameStage, IEmailStage, IBuildStage {
		private String id;
		private String name;
		private String email;
		private String givenName;
		private List<String> approvedClients = Collections.emptyList();
		private String picture;
		private List<String> loginHints;

		private Builder() {
		}

		@Override
		public INameStage withId(String id) {
			this.id = id;
			return this;
		}

		@Override
		public IEmailStage withName(String name) {
			this.name = name;
			return this;
		}

		@Override
		public IBuildStage withEmail(String email) {
			this.email = email;
			return this;
		}

		@Override
		@JsonProperty("given_name")
		public IBuildStage withGivenName(String givenName) {
			this.givenName = givenName;
			return this;
		}

		@Override
		@JsonProperty("approved_clients")
		public IBuildStage withApprovedClients(List<String> approvedClients) {
			this.approvedClients = approvedClients;
			return this;
		}
		
		@Override
		@JsonProperty("login_hints")
		public IBuildStage withLoginHints(List<String> loginHints) {
			this.loginHints = loginHints;
			return this;
		}

		@Override
		public IBuildStage withPicture(String picture) {
			this.picture = picture;
			return this;
		}

		@Override
		public IdentityProviderAccount build() {
			return new IdentityProviderAccount(this);
		}
	}

}
