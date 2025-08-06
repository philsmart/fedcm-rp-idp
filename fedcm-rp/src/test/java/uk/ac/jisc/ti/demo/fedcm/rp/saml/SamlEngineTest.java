package uk.ac.jisc.ti.demo.fedcm.rp.saml;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class SamlEngineTest {

	private SamlEngine engine;
	
	@Test
	public void testAuthnRequest() throws Exception {
		engine = new SamlEngine();
		engine.buildAuthnRequest(new MockHttpServletRequest(), new MockHttpServletResponse());
	}
}
