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

package com.sibvisions.rad.lua.support.wrappers;

import javax.rad.util.EventHandler;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.AbstractInterceptingJavaInstance;

import com.sibvisions.rad.lua.support.events.CallingRunnable;

/**
 * The {@link AddListenerInterceptingWrapper}.
 *
 * @author Robert Zenz
 */
public class AddListenerInterceptingWrapper extends AbstractInterceptingJavaInstance
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link LuaValue} to return instead of the addListener method. */
	private LuaValue detour = null;
	
	/** The {@link EventHandler} of this instance. */
	private EventHandler<?> eventHandler = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link AddListenerInterceptingWrapper}.
	 *
	 * @param pInstance the {@link Object instance}.
	 */
	public AddListenerInterceptingWrapper(Object pInstance)
	{
		super(pInstance);
		
		eventHandler = ((EventHandler<?>)m_instance);
		detour = new ReroutingFunction();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LuaValue intercept(LuaValue pKey)
	{
		String name = pKey.tojstring();
		
		if (name.equals("addListener"))
		{
			return detour;
		}
		
		return null;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link ReroutingFunction} is an {@link VarArgFunction} which reroutes
	 * the given objects as correct listeners.
	 * 
	 * @author Robert Zenz
	 */
	private final class ReroutingFunction extends VarArgFunction
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Varargs invoke(Varargs pArgs)
		{
			if (pArgs.narg() == 2 && pArgs.isfunction(2))
			{
				eventHandler.addListener(new CallingRunnable(pArgs.arg(2)));
			}
			
			return LuaValue.NIL;
		}
	}
	
}	// AddListenerInterceptingWrapper
