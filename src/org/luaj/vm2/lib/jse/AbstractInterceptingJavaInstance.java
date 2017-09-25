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

package org.luaj.vm2.lib.jse;

import org.luaj.vm2.LuaValue;

/**
 * The {@link AbstractInterceptingJavaInstance} is an abstract extension of
 * {@link JavaInstance} which allows to intercept {@code get} operations.
 * 
 * @author Robert Zenz
 */
public abstract class AbstractInterceptingJavaInstance extends JavaInstance
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link AbstractInterceptingJavaInstance}.
	 *
	 * @param pInstance the {@link Object instance}.
	 */
	protected AbstractInterceptingJavaInstance(Object pInstance)
	{
		super(pInstance);
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Abstract methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Allows to intercept the current {@code get} call.
	 * 
	 * @param pKey the current {@link LuaValue key}.
	 * @return the intercepted value. {@code null} if it should not be
	 *         intercepted.
	 */
	protected abstract LuaValue intercept(LuaValue pKey);
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LuaValue get(LuaValue pKey)
	{
		LuaValue interceptedValue = intercept(pKey);
		
		if (interceptedValue != null)
		{
			return interceptedValue;
		}
		else
		{
			return super.get(pKey);
		}
	}
	
}	// AbstractInterceptingJavaInstance
