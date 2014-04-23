package hr.ws4is;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JacksonConfig implements ContextResolver<ObjectMapper> {
	  
  private static ObjectMapper apiMapper ;
	 
  public JacksonConfig() {
		super();
		apiMapper = JsonDecoder.getJSONEngine();
		apiMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}


	@Override
  public ObjectMapper getContext(final Class<?> type) {
		return apiMapper;
  }
  
}
