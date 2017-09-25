/*
 * Copyright 2017 SIB Visions GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.sibvisions.rad.lua.libs.wrappers;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.AbstractInterceptingJavaInstance;

/**
 * The {@link EventHandlerInterceptingWrapper} is an
 * {@linnk AbstractInterceptingJavaInstance} extension which intercepts calls to
 * {@code event*} functions and injects an
 * {@link AddListenerInterceptingWrapper}.
 * 
 * @author Robert Zenz
 */
public class EventHandlerInterceptingWrapper extends AbstractInterceptingJavaInstance
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The cache. */
	private Map<String, LuaValue> cache = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link EventHandlerInterceptingWrapper}.
	 *
	 * @param pInstance the {@link Object instance}.
	 */
	public EventHandlerInterceptingWrapper(Object pInstance)
	{
		super(pInstance);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LuaValue intercept(LuaValue pKey)
	{
		String name = pKey.tojstring();
		
		if (name.startsWith("event"))
		{
			if (cache == null)
			{
				cache = new HashMap<>();
			}
			
			LuaValue cachedValue = cache.get(name);
			
			if (cachedValue == null)
			{
				try
				{
					Method method = m_instance.getClass().getMethod(name);
					cachedValue = new AddListenerInjectingFunction(method);
					
					cache.put(name, cachedValue);
				}
				catch (Exception e)
				{
					// Maybe we were wrong...let the parent deal with it.
				}
			}
			
			return cachedValue;
		}
		
		return null;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link AddListenerInjectingFunction} is returning a
	 * 
	 * @author Robert Zenz
	 */
	private final class AddListenerInjectingFunction extends ZeroArgFunction
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/** The {@link Method} to invoke. */
		private Method method = null;
		
		/** The cached value to return. */
		private LuaValue value = null;
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Initialization
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * Creates a new instance of {@link AddListenerInjectingFunction}.
		 *
		 * @param pMethod the {@link Method method}.
		 */
		public AddListenerInjectingFunction(Method pMethod)
		{
			super();
			
			method = pMethod;
		}
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public LuaValue call()
		{
			if (value == null)
			{
				try
				{
					value = new AddListenerInterceptingWrapper(method.invoke(m_instance));
				}
				catch (Exception e)
				{
					throw new LuaError(e);
				}
			}
			
			return value;
		}
		
	}	// AddListenerInjectingFunction
	
}	// EventHandlerInterceptingWrapper
