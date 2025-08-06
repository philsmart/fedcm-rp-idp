package uk.ac.jisc.ti.demo.fedcm.rp.saml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.time.Instant;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import javax.annotation.Nonnull;
import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.opensaml.core.config.InitializationException;
import org.opensaml.core.config.InitializationService;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.XMLObjectBuilderFactory;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.core.xml.util.XMLObjectSupport;
import org.opensaml.messaging.context.MessageContext;
import org.opensaml.messaging.encoder.MessageEncodingException;
import org.opensaml.saml.common.SAMLObject;
import org.opensaml.saml.common.messaging.context.SAMLArtifactContext;
import org.opensaml.saml.common.messaging.context.SAMLEndpointContext;
import org.opensaml.saml.common.messaging.context.SAMLPeerEntityContext;
import org.opensaml.saml.common.messaging.context.SAMLSelfEntityContext;
import org.opensaml.saml.common.xml.SAMLConstants;
import org.opensaml.saml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml.saml2.core.AuthnRequest;
import org.opensaml.saml.saml2.core.IDPEntry;
import org.opensaml.saml.saml2.core.IDPList;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.saml.saml2.core.NameID;
import org.opensaml.saml.saml2.core.NameIDPolicy;
import org.opensaml.saml.saml2.core.RequestedAuthnContext;
import org.opensaml.saml.saml2.core.RequesterID;
import org.opensaml.saml.saml2.core.Scoping;
import org.opensaml.saml.saml2.core.Subject;
import org.opensaml.saml.saml2.metadata.Endpoint;
import org.opensaml.saml.saml2.metadata.SingleSignOnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerMapping;
import org.w3c.dom.Element;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.shibboleth.shared.codec.Base64Support;
import net.shibboleth.shared.codec.EncodingException;
import net.shibboleth.shared.component.ComponentInitializationException;
import net.shibboleth.shared.net.URLBuilder;
import net.shibboleth.shared.primitive.StringSupport;
import net.shibboleth.shared.security.IdentifierGenerationStrategy;
import net.shibboleth.shared.security.IdentifierGenerationStrategy.ProviderType;
import net.shibboleth.shared.xml.SerializeSupport;
import net.shibboleth.shared.xml.impl.BasicParserPool;

public class SamlEngine {

	/** Logger. */
	private static final Logger log = LoggerFactory.getLogger(SamlEngine.class);

	private IdentifierGenerationStrategy idGenerator = IdentifierGenerationStrategy.getInstance(ProviderType.UUID);

	private XMLObjectBuilderFactory builderFactory;

	private BasicParserPool parser;

	public SamlEngine() throws ComponentInitializationException {
		parser = new BasicParserPool();
		parser.setMaxPoolSize(100);
		parser.setCoalescing(true);
		parser.setIgnoreComments(true);
		parser.setIgnoreElementContentWhitespace(true);
		parser.setNamespaceAware(true);
		parser.setSecurityManagerAttributeName("http://apache.org/xml/properties/security-manager");

		try {
			InitializationService.initialize();
		} catch (final InitializationException e) {
			throw new ComponentInitializationException("Exception initializing OpenSAML", e);
		}

		builderFactory = XMLObjectProviderRegistrySupport.getBuilderFactory();
	}

//	public void decorateProviderConfigWithSamlRequest(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse,
//				IdentityProviderConfig idpConfig) {
//		
//		String deflatedBase64AuthnRequest = buildAuthnRequest(servletRequest, servletResponse);
//		idpConfig.
//		
//	}

	public String buildAuthnRequest(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse,
			final String authnContextClass) throws Exception {

		final AuthnRequest authnRequest = buildAuthnRequest(servletRequest, authnContextClass);
		authnRequest.setDestination(getDestinationPost(servletRequest, "SSO"));
		final Endpoint endpoint = buildIdpSsoEndpoint(SAMLConstants.SAML2_POST_BINDING_URI,
				authnRequest.getDestination());
		final String spEntityID = getSpEntityId(servletRequest);
		final String idpEntityID = getIdpEntityId(servletRequest);
		final MessageContext messageContext = buildOutboundMessageContext(authnRequest, endpoint, spEntityID,
				idpEntityID);
		// SAMLMessageSecuritySupport.signMessage(messageContext);
		// encodeOutboundMessageContextPost(messageContext, servletResponse);
		
		// FORCE AUTHN Fixed
		//authnRequest.setForceAuthn(true);

		final String messageXML = SerializeSupport
				.nodeToString(marshallMessage(XMLObject.class.cast(messageContext.getMessage())));
		final String encodedMessage = Base64Support.encode(messageXML.getBytes("UTF-8"), Base64Support.UNCHUNKED);
		log.info("AuthnRequest: \n{}", encodedMessage);

		return encodedMessage;
	}

	/**
	 * DEFLATE (RFC1951) compresses the given SAML message.
	 * 
	 * @param message SAML message
	 * 
	 * @return DEFLATE compressed message
	 * 
	 * @throws MessageEncodingException thrown if there is a problem compressing the
	 *                                  message
	 */
	@Nonnull
	protected String deflateAndBase64Encode(@Nonnull final SAMLObject message) throws MessageEncodingException {
		log.debug("Deflating and Base64 encoding SAML message");
		try {
			final String messageStr = SerializeSupport.nodeToString(marshallMessage(message));

			try (final ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
					final DeflaterOutputStream deflaterStream = new NoWrapAutoEndDeflaterOutputStream(bytesOut,
							Deflater.DEFLATED)) {

				deflaterStream.write(messageStr.getBytes("UTF-8"));
				deflaterStream.finish();

				return Base64Support.encode(bytesOut.toByteArray(), Base64Support.UNCHUNKED);
			}
		} catch (final IOException | EncodingException e) {
			throw new MessageEncodingException("Unable to DEFLATE and Base64 encode SAML message", e);
		}
	}

	/**
	 * A subclass of {@link DeflaterOutputStream} which defaults in a no-wrap
	 * {@link Deflater} instance and closes it when the stream is closed.
	 */
	private class NoWrapAutoEndDeflaterOutputStream extends DeflaterOutputStream {

		/**
		 * Creates a new output stream with a default no-wrap compressor and buffer
		 * size, and the specified compression level.
		 *
		 * @param os    the output stream
		 * @param level the compression level (0-9)
		 */
		public NoWrapAutoEndDeflaterOutputStream(@Nonnull final OutputStream os, final int level) {
			super(os, new Deflater(level, true));
		}

		/** {@inheritDoc} */
		public void close() throws IOException {
			if (def != null) {
				def.end();
			}
			super.close();
		}

	}

	/**
	 * Helper method that marshalls the given message.
	 * 
	 * @param message message the marshall and serialize
	 * 
	 * @return marshalled message
	 * 
	 * @throws MessageEncodingException thrown if the give message can not be
	 *                                  marshalled into its DOM representation
	 */
	@Nonnull
	protected Element marshallMessage(@Nonnull final XMLObject message) throws MessageEncodingException {
		log.debug("Marshalling message");

		try {
			return XMLObjectSupport.marshall(message);
		} catch (final MarshallingException e) {
			log.error("Error marshalling message: {}", e.getMessage());
			throw new MessageEncodingException("Error marshalling message", e);
		}
	}

	private SingleSignOnService buildIdpSsoEndpoint(final String binding, final String destination) {
		final SingleSignOnService ssoEndpoint = (SingleSignOnService) builderFactory
				.getBuilder(SingleSignOnService.DEFAULT_ELEMENT_NAME)
				.buildObject(SingleSignOnService.DEFAULT_ELEMENT_NAME);
		ssoEndpoint.setBinding(binding);
		ssoEndpoint.setLocation(destination);
		return ssoEndpoint;
	}

	private String getSpEntityId(final HttpServletRequest servletRequest) {

		final String spId = servletRequest.getHeader("host");
		return (spId == null) ? "https://sp.example.org" : "http://" + spId + "/";
	}

	private String getIdpEntityId(final HttpServletRequest servletRequest) {
		// TODO get from config somewhere
		// Sometimes it's useful to return an IdP entityID per SP.
		// final String spId = getSpId(servletRequest);
		// return (spId == null) ? "https://idp.example.org" : "https://idp." + spId +
		// ".example.org";
		return "https://idp.example.org";
	}

	private MessageContext buildOutboundMessageContext(final SAMLObject message, final Endpoint endpoint,
			final String spEntityId, final String idpEntityId) {
		final MessageContext messageContext = new MessageContext();
		messageContext.setMessage(message);

		final SAMLSelfEntityContext selfContext = messageContext.getSubcontext(SAMLSelfEntityContext.class, true);
		selfContext.setEntityId(spEntityId);

		final SAMLPeerEntityContext peerContext = messageContext.getSubcontext(SAMLPeerEntityContext.class, true);
		peerContext.setEntityId(idpEntityId);

		if (endpoint != null) {
			final SAMLEndpointContext endpointContext = peerContext.getSubcontext(SAMLEndpointContext.class, true);
			endpointContext.setEndpoint(endpoint);
		}

		final SAMLArtifactContext artifactContext = messageContext.getSubcontext(SAMLArtifactContext.class, true);
		artifactContext.setSourceArtifactResolutionServiceEndpointIndex(1);

//		final SignatureSigningParameters signingParameters = new SignatureSigningParameters();
//		signingParameters.setSigningCredential(spCredential);
//		signingParameters.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);
//		
//		signingParameters.setSignatureCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
//		final SecurityParametersContext secParamsContext = messageContext.getSubcontext(SecurityParametersContext.class, true);
//		secParamsContext.setSignatureSigningParameters(signingParameters);

		return messageContext;
	}

	
	public static String prettyFormat(String input, int indent) {
	    try {
	        Source xmlInput = new StreamSource(new StringReader(input));
	        StringWriter stringWriter = new StringWriter();
	        StreamResult xmlOutput = new StreamResult(stringWriter);
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        transformerFactory.setAttribute("indent-number", indent);
	        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
	        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
	        Transformer transformer = transformerFactory.newTransformer(); 
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.transform(xmlInput, xmlOutput);
	        return xmlOutput.getWriter().toString();
	    } catch (Exception e) {
	        throw new RuntimeException(e); // simple exception handling, please review it
	    }
	}

	public static String prettyFormat(String input) {
	    return prettyFormat(input, 2);
	}

	private String getDestinationPost(final HttpServletRequest servletRequest, final String profile) {
		// TODO servlet context
		final String destinationPath = "https://fedcm.jisc.ac.uk:8443/idp/profile/fedcm/assertion";
		return destinationPath;

	}

	private String getBaseUrl(final HttpServletRequest servletRequest) {
		final String requestUrl = servletRequest.getRequestURL().toString();
		try {
			final URLBuilder urlBuilder = new URLBuilder(requestUrl);
			urlBuilder.setUsername(null);
			urlBuilder.setPassword(null);
			urlBuilder.setPath(null);
			urlBuilder.getQueryParams().clear();
			urlBuilder.setFragment(null);
			return urlBuilder.buildURL();
		} catch (final MalformedURLException e) {
			log.error("Couldn't parse request URL, reverting to internal default base URL: {}", requestUrl);
			return "http://localhost:8080";
		}

	}

	/**
	 * Get the SP id as a path variable, or <code>null</code> if not present.
	 * 
	 * @param servletRequest the servlet request
	 * @return the SP id or <code>null</code>
	 */
	private String getSpId(final HttpServletRequest servletRequest) {
		final Object attr = servletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		if (attr != null && attr instanceof Map) {
			final Map pathVariables = (Map) attr;
			final Object spId = pathVariables.get("spId");
			log.trace("Found spID '{}'", spId);
			if (spId != null) {
				return spId.toString();
			}
		}
		return null;
	}

	private String getAcsUrl(final HttpServletRequest servletRequest) {
		// TODO servlet context
		final String spId = getSpId(servletRequest);
		final String acsPath = (spId == null) ? "/sp/SAML2/POST/ACS" : "/sp/" + spId + "/SAML2/POST/ACS";
		final String baseUrl = getBaseUrl(servletRequest);
		try {
			final URLBuilder urlBuilder = new URLBuilder(baseUrl);
			urlBuilder.setPath(acsPath);
			return urlBuilder.buildURL();
		} catch (final MalformedURLException e) {
			log.error("Couldn't parse base URL, reverting to internal default ACS: {}", baseUrl);
			return "http://localhost:8080" + acsPath;
		}
	}

	private AuthnRequest buildAuthnRequest(final HttpServletRequest servletRequest, final String authnContextClass) {
		final AuthnRequest authnRequest = (AuthnRequest) builderFactory.getBuilder(AuthnRequest.DEFAULT_ELEMENT_NAME)
				.buildObject(AuthnRequest.DEFAULT_ELEMENT_NAME);

		authnRequest.setID(idGenerator.generateIdentifier());
		authnRequest.setIssueInstant(Instant.now());
		// authnRequest.setAssertionConsumerServiceURL(getAcsUrl(servletRequest));
		authnRequest.setProtocolBinding("urn:oasis:names:tc:SAML:2.0:bindings:HTTP-FEDCM");

		final Issuer issuer = (Issuer) builderFactory.getBuilder(Issuer.DEFAULT_ELEMENT_NAME)
				.buildObject(Issuer.DEFAULT_ELEMENT_NAME);
		issuer.setValue(getSpEntityId(servletRequest));
		authnRequest.setIssuer(issuer);

		final NameIDPolicy nameIDPolicy = (NameIDPolicy) builderFactory.getBuilder(NameIDPolicy.DEFAULT_ELEMENT_NAME)
				.buildObject(NameIDPolicy.DEFAULT_ELEMENT_NAME);
		nameIDPolicy.setAllowCreate(true);
		authnRequest.setNameIDPolicy(nameIDPolicy);

		String param = StringSupport.trimOrNull(servletRequest.getParameter("nameIDFormat"));
		if (param != null) {
			nameIDPolicy.setFormat(param);
		}

		param = StringSupport.trimOrNull(servletRequest.getParameter("spNameQualifier"));
		if (param != null) {
			nameIDPolicy.setSPNameQualifier(param);
		}

		param = StringSupport.trimOrNull(servletRequest.getParameter("subjectID"));
		if (param != null) {
			final NameID nameID = (NameID) builderFactory.getBuilder(NameID.DEFAULT_ELEMENT_NAME)
					.buildObject(NameID.DEFAULT_ELEMENT_NAME);
			nameID.setValue(param);
			param = StringSupport.trimOrNull(servletRequest.getParameter("subjectIDFormat"));
			if (param != null) {
				nameID.setFormat(param);
			}
			final Subject subject = (Subject) builderFactory.getBuilder(Subject.DEFAULT_ELEMENT_NAME)
					.buildObject(Subject.DEFAULT_ELEMENT_NAME);
			subject.setNameID(nameID);
			authnRequest.setSubject(subject);
		}

		param = authnContextClass; // StringSupport.trimOrNull(servletRequest.getParameter("classRef"));
		if (param != null) {
			final AuthnContextClassRef ref = (AuthnContextClassRef) builderFactory
					.getBuilder(AuthnContextClassRef.DEFAULT_ELEMENT_NAME)
					.buildObject(AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
			ref.setURI(param);
			final RequestedAuthnContext rac = (RequestedAuthnContext) builderFactory
					.getBuilder(RequestedAuthnContext.DEFAULT_ELEMENT_NAME)
					.buildObject(RequestedAuthnContext.DEFAULT_ELEMENT_NAME);
			rac.getAuthnContextClassRefs().add(ref);
			authnRequest.setRequestedAuthnContext(rac);
		}

		param = StringSupport.trimOrNull(servletRequest.getParameter("requesters"));
		if (param != null) {
			final String[] requesters = param.split(",");
			if (requesters != null && requesters.length > 0) {
				for (final String req : requesters) {
					final RequesterID requesterID = (RequesterID) builderFactory
							.getBuilder(RequesterID.DEFAULT_ELEMENT_NAME).buildObject(RequesterID.DEFAULT_ELEMENT_NAME);
					requesterID.setURI(req);
					getScoping(authnRequest).getRequesterIDs().add(requesterID);
				}
			}
		}

		param = StringSupport.trimOrNull(servletRequest.getParameter("idplist"));
		if (param != null) {
			final String[] idplist = param.split(",");
			if (idplist != null && idplist.length > 0) {
				final IDPList obj = (IDPList) builderFactory.getBuilder(IDPList.DEFAULT_ELEMENT_NAME)
						.buildObject(IDPList.DEFAULT_ELEMENT_NAME);
				for (final String idp : idplist) {
					final IDPEntry entry = (IDPEntry) builderFactory.getBuilder(IDPEntry.DEFAULT_ELEMENT_NAME)
							.buildObject(IDPEntry.DEFAULT_ELEMENT_NAME);
					entry.setProviderID(idp);
					obj.getIDPEntrys().add(entry);
				}
				getScoping(authnRequest).setIDPList(obj);
			}
		}

		param = StringSupport.trimOrNull(servletRequest.getParameter("proxycount"));
		if (param != null) {
			getScoping(authnRequest).setProxyCount(Integer.valueOf(param));
		}

		return authnRequest;
	}

	private Scoping getScoping(final AuthnRequest request) {
		if (request.getScoping() == null) {
			final Scoping scoping = (Scoping) builderFactory.getBuilder(Scoping.DEFAULT_ELEMENT_NAME)
					.buildObject(Scoping.DEFAULT_ELEMENT_NAME);
			request.setScoping(scoping);
		}

		return request.getScoping();
	}

}
