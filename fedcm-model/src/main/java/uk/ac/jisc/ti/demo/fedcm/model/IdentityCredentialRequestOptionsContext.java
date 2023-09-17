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

public enum IdentityCredentialRequestOptionsContext {
	
	@JsonProperty("signin") SIGNIN("signin"),
	@JsonProperty("signup") SIGNUP("signup"),
	@JsonProperty("use") USE("use"),
	@JsonProperty("continue") CONTINUE("continue");

	private final String name;
	
	private IdentityCredentialRequestOptionsContext(final String value) {
		name = value;
	}
	
	public String getName(){
		return name;
	}
}
