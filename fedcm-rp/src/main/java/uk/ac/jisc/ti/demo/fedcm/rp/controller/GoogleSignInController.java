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

package uk.ac.jisc.ti.demo.fedcm.rp.controller;

import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Google Sign-In controller demo: https://developers.google.com/identity/sign-in/web/sign-in
 */
@Controller
public class GoogleSignInController {

    /** Logger. */
    private static final Logger log = LoggerFactory.getLogger(GoogleSignInController.class);

   
	/**
     * Simple redirect to the main RP page.
     * 
     * @return a redirect to the RP page.
     */
    @GetMapping("/google-sign-in")
    public String getRPIndex() {
        return "rp-google-sign-in";
    }

    @PostMapping("/google-sign-in-callback")
    public String getSigninCallback(final HttpServletRequest req,
            @RequestHeader final Map<String, String> headers,
            @RequestParam final Map<String, String> allRequestParams) {
    	logCookiesAndHeaders(req, headers, allRequestParams);
    	return "rp-google-sign-in";
    }
    
    /**
     * Log the cookies, headers, and any params in the request.
     * 
     * @param req the http request
     * @param headers the http headers
     * @param allRequestParams the http params
     */
    private void logCookiesAndHeaders(@Nonnull final HttpServletRequest req, 
    		@Nonnull final Map<String, String> headers,
    		@Nonnull final Map<String, String> allRequestParams) {
    	
        if (req.getCookies() != null) {
            Arrays.stream(req.getCookies())
                    .forEach(c -> log.info("Google SignIn Account Cookie: {}:{}", c.getName(), c.getValue()));
        }

        headers.entrySet().forEach(h -> log.info("Google SignIn Account Header: {}", h));        

        allRequestParams.entrySet().forEach(p -> log.info("Google SignIn Accounts Param: {}", p));
    }

}
