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

package com.sibvisions.rad.lua.libs.functions;

import java.lang.reflect.Constructor;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

import com.sibvisions.rad.lua.libs.wrappers.EventHandlerInterceptingWrapper;

/**
 * The {@link ConstructorInvokingFunction} is a {@link VarArgFunction} extension
 * which calls the best fitting constructor.
 * 
 * @author Robert Zenz
 */
public class ConstructorInvokingFunction extends VarArgFunction
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The array of available {@link Constructor}s. */
	private Constructor<?>[] constructors = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link ConstructorInvokingFunction}.
	 *
	 * @param pClazz the {@link Class}.
	 */
	public ConstructorInvokingFunction(Class<?> pClazz)
	{
		super();
		
		try
		{
			constructors = pClazz.getConstructors();
		}
		catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Varargs invoke(Varargs pArgs)
	{
		if (constructors == null || constructors.length == 0)
		{
			throw new LuaError("Object can not be created, no constructors available.");
		}
		
		Object[] parameters = new Object[pArgs.narg()];
		Class<?>[] parameterClasses = new Class[pArgs.narg()];
		
		for (int index = 0; index < pArgs.narg(); index++)
		{
			if (pArgs.isnil(index + 1))
			{
				parameterClasses[index] = Object.class;
				parameters[index] = null;
			}
			else if (pArgs.isnumber(index + 1))
			{
				parameterClasses[index] = int.class;
				parameters[index] = Integer.valueOf(pArgs.toint(index + 1));
			}
			else if (pArgs.isstring(index + 1))
			{
				parameterClasses[index] = String.class;
				parameters[index] = pArgs.tojstring(index + 1);
			}
			else if (pArgs.isuserdata(index + 1))
			{
				parameterClasses[index] = Object.class;
				parameters[index] = CoerceLuaToJava.coerce(pArgs.arg(index + 1), Object.class);
			}
		}
		
		Constructor<?> matchingConstructor = null;
		
		for (Constructor<?> constructor : constructors)
		{
			Class<?>[] parameterTypes = constructor.getParameterTypes();
			
			if (parameterTypes.length == pArgs.narg())
			{
				for (int index = 0; index < parameterTypes.length; index++)
				{
					if (!parameterTypes[index].equals(parameterClasses[index]))
					{
						break;
					}
				}
				
				matchingConstructor = constructor;
			}
		}
		
		if (matchingConstructor == null)
		{
			throw new LuaError("Object can not be created, no fitting constructor available.");
		}
		
		try
		{
			Object instance = matchingConstructor.newInstance(parameters);
			LuaValue luaInstance = new EventHandlerInterceptingWrapper(instance);
			
			return luaInstance;
		}
		catch (Exception e)
		{
			throw new LuaError(e);
		}
	}
	
}	// ConstructorInvokingFunction
