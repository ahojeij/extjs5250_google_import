/*
 * Copyright (C) 2010,  Tomislav Milkovic
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 */
package hr.ws4is.websocket;

import hr.ws4is.JsonDecoder;
import hr.ws4is.WS4ISConstants;
import hr.ws4is.cdi.BeanManagerUtil;
import hr.ws4is.cdi.IDestructibleBeanInstance;
import hr.ws4is.ext.ExtJSDirectRequest;
import hr.ws4is.ext.ExtJSDirectResponse;
import hr.ws4is.ext.ExtJSResponse;
import hr.ws4is.ext.annotations.ExtJSActionLiteral;
import hr.ws4is.ext.annotations.ExtJSDirect;
import hr.ws4is.ext.annotations.ExtJSMethod;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PreDestroy;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class WebSocketOperations<T> {
	
	final static String [] allPaths = {"*"};
		
	@Inject 
	protected BeanManagerUtil beanManagerUtil;	

	@PreDestroy
	private void preDestroy()
	{
		beanManagerUtil = null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ExtJSDirectResponse<T> process(ExtJSDirectRequest<T> request, HttpSession session, String uri)
	{
		ExtJSDirectResponse<T> directResponse = null;
		ExtJSResponse response = null;
	
		try {		
			
			Bean<?> bean = findBean(request);
			Class<?> beanClass = bean.getBeanClass();

			AnnotatedType annType = beanManagerUtil.getBeanManager().createAnnotatedType(beanClass);
			AnnotatedMethod selectedMethod = findMethod(request, annType);
			ExtJSDirect direct = beanClass.getAnnotation(ExtJSDirect.class);
			
			boolean error = checkForError(annType, selectedMethod, direct, session, uri);	
			if(error) {
				response = new ExtJSResponse(false,WS4ISConstants.DIRECT_SERVICE_NOT_FOUND);				
			} else {
				List<AnnotatedParameter<?>> paramList = selectedMethod.getParameters();
				Object params [] = fillParams(request, paramList);			
				response = executeBean(bean, selectedMethod, params);	
			}
						
		} catch (Exception e) {
			response = new ExtJSResponse(e, e.getMessage());	
		} finally {
			directResponse = new ExtJSDirectResponse<T>(request, response);		
		}
		
		return directResponse;		
	}

	/*
	 * PRIVATE SECTION
	 */
	private boolean checkForError(AnnotatedType<?> annType, AnnotatedMethod<?> selectedMethod, ExtJSDirect direct, HttpSession session, String uri){
		boolean error = false;
		
		//check for path			
		if(direct==null) {
			error = true;				
		} else if(!checkPath(uri,direct.paths())) {
			error = true;				
		}  else if(!isValidHttpSession(session)) {
			error = true;
		} else if(selectedMethod == null) {
			error = true;
		}
		return error;
	}
	
	private boolean checkPath(String uri , String[] paths){
		boolean result = false;
		for(String path : paths) {
			
			if("*".equals(path)) {
				result = true;
				break;
			}

			int idx = uri.indexOf(path);
			if(idx==0 || idx==1){
				result = true;
				break;
			}
			
		}		
		return result;
	}
	
	private boolean isValidHttpSession(HttpSession httpSession) {
		if(httpSession==null) return false;
		String attr = (String)httpSession.getAttribute(WS4ISConstants.HTTP_SEESION_STATUS);		
		return "true".equalsIgnoreCase(attr);
	}
	

	private Bean<?> findBean(final ExtJSDirectRequest<?> request){
		ExtJSActionLiteral literal = new ExtJSActionLiteral(request.getNamespace(), request.getAction());
		Iterator<Bean<?>> it = beanManagerUtil.getBeanManager().getBeans(Object.class, literal).iterator();
		return it.next();
	}	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private AnnotatedMethod<?> findMethod(final ExtJSDirectRequest<T> request, final AnnotatedType annType)	{

		AnnotatedMethod<?> selectedMethod = null;				
		Set<AnnotatedMethod<?>> aMethods = annType.getMethods();
		for(AnnotatedMethod<?> aMethod : aMethods) {
			ExtJSMethod annMethod = aMethod.getAnnotation(ExtJSMethod.class);
			if(annMethod==null){
				continue;
			};
			
			if(annMethod.value().equals(request.getMethod())) {
				selectedMethod = aMethod;
				break;
			}
		}
		
		return selectedMethod;
	}
	
	private Object[] fillParams(final ExtJSDirectRequest<T> request, final List<AnnotatedParameter<?>> methodParams) throws IOException {
		int paramSize = methodParams.size();
		int incomingParamsSize = request.getData() == null ? 0 : request.getData().size();
		
		Object params [] = new Object[paramSize];
		for(int i = 0; i < paramSize; i++) {
			
				if(i<incomingParamsSize) {
					Object paramData = request.getData().get(i);
					if(paramData instanceof JsonNode) {
						JsonNode jnode = (JsonNode) paramData;
						if(jnode!=null) {	
							Class<?> jType = (Class<?>) methodParams.get(i).getBaseType();
							ObjectMapper mapper = JsonDecoder.getJSONEngine();
							params[i] = mapper.treeToValue(jnode, jType);
							//params[i] = JsonDecoder.getJSONEngine().readValue(jnode, jType);							
						}					

					} else {
						params[i] = paramData;
					}
				}
		}
		
		return params;
	}
	
	private ExtJSResponse executeBean(final Bean<?> bean, final AnnotatedMethod<?> method, final Object[] params ) {
		ExtJSResponse response = null;
		IDestructibleBeanInstance<?> di = null;
		
		try{
			di = beanManagerUtil.getDestructibleBeanInstance(bean);
			Object beanInstance = di.getInstance();
			
			Method javaMethod = method.getJavaMember();				
			if(javaMethod.isAccessible()) {
				javaMethod.setAccessible(true);
			}
			
			response = (ExtJSResponse) javaMethod.invoke(beanInstance, params);
			
		} catch (Exception e) {
			response = new ExtJSResponse(e, e.getMessage());
		} finally {
			if(di!=null) di.destroy();
		}
		
		return response;
	}
	
}
