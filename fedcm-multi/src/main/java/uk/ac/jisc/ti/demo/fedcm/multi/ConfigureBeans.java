package uk.ac.jisc.ti.demo.fedcm.multi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.ac.jisc.ti.demo.fedcm.idp.controller.IdPController;

@Configuration
public class ConfigureBeans {
	
	/** Logger. */
    private static final Logger log = LoggerFactory.getLogger(ConfigureBeans.class);
	
	
	@Bean
    HttpExchangeRepository httpTraceRepository() {
		log.info("Configuring httptrace beans");
        return new InMemoryHttpExchangeRepository();
    }

}
