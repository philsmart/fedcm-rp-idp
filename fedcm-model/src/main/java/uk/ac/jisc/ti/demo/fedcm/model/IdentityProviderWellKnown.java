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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;

import jakarta.annotation.Nonnull;

public class IdentityProviderWellKnown {
	
	private final List<String> providerURLs;
	
	/**
	 * Constructor 
	 * @param urls the provider URLs
	 */
	public IdentityProviderWellKnown(@Nonnull List<String> urls) {
		providerURLs = urls;
	}

	/**
	 * Get the providerURLs.
	 *
	 * @return the providerURLs
	 */
	@JsonGetter("provider_urls")
	public List<String> getProviderURLs() {
		return providerURLs;
	}


}
