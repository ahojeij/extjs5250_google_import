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
package hr.ws4is.cdi;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BeanManagerUtil 
{
    @Inject @Default
    private volatile BeanManager beanManager;

    @SuppressWarnings("unchecked")
    public <T> IDestructibleBeanInstance<T> getDestructibleBeanInstance(final Class<T> type, final Annotation... qualifiers) {
    	Set<Bean<?>> beansF = new HashSet<Bean<?>>(); 
    	Set<Bean<?>> beans = beanManager.getBeans(Object.class, qualifiers);
    	
    	Iterator<Bean<?>> it = beans.iterator();
		while(it.hasNext()) {
			Bean<?> bean = it.next();
			
	    	if(type.isInterface()) {
	    		Class<?>[] intfs = bean.getBeanClass().getInterfaces();
	    		for(Class<?> intf : intfs) {
	    			if(type.equals(intf)) {
	    				beansF.add(bean);
	    			}
	    		}
	    	} else {
	    		if(bean.getBeanClass().equals(type)) {
	    			beansF.add(bean);
	    		}
	    	}			
		}
    	
        Bean<T> bean = (Bean<T>) beanManager.resolve(beansF);
        return getDestructibleBeanInstance(bean);
    }    
    
    public <T> IDestructibleBeanInstance<T> getDestructibleBeanInstance(final Bean<T> bean) {
    	IDestructibleBeanInstance<T> result = null;
        if (bean != null) {
            CreationalContext<T> creationalContext = beanManager.createCreationalContext(bean);
            if (creationalContext != null) {
                T instance = bean.create(creationalContext);
                result = new DestructibleBeanInstance<T>(instance, bean, creationalContext);
            }
        }    	
        return result;
    }
    
    public BeanManager getBeanManager(){
    	return beanManager;
    }
    
    public void setBeanManager(BeanManager beanManager)	{
    	this.beanManager = beanManager;
    }
    
    @Override
    public String toString() {
    	return "BeanManagerUtil [beanManager=" + beanManager + ", getBeanManager()=" + getBeanManager() + "]";
    }	
}

