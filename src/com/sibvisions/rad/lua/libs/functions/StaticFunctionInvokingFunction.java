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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

/**
 * The {@link StaticFunctionInvokingFunction} is a {@link VarArgFunction} which
 * invokes the best fitting static function on an object.
 * 
 * @author Robert Zenz
 */
public class StaticFunctionInvokingFunction extends VarArgFunction
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link Method}s. */
	private List<Method> methods = new ArrayList<>();
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link StaticFunctionInvokingFunction}.
	 *
	 * @param pClass the {@link Class class}.
	 * @param pStaticMethodName the {@link String static method name}.
	 */
	public StaticFunctionInvokingFunction(Class<?> pClass, String pStaticMethodName)
	{
		super();
		
		for (Method method : pClass.getMethods())
		{
			if (method.getName().equals(pStaticMethodName))
			{
				methods.add(method);
			}
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
		
		Method matchingMethod = null;
		
		for (Method method : methods)
		{
			Class<?>[] parameterTypes = method.getParameterTypes();
			
			if (parameterTypes.length == pArgs.narg())
			{
				for (int index = 0; index < parameterTypes.length; index++)
				{
					if (!parameterTypes[index].equals(parameterClasses[index]))
					{
						break;
					}
				}
				
				matchingMethod = method;
			}
		}
		
		if (matchingMethod == null)
		{
			throw new LuaError("No fitting method found.");
		}
		
		try
		{
			return CoerceJavaToLua.coerce(matchingMethod.invoke(null, parameters));
		}
		catch (Exception e)
		{
			throw new LuaError(e);
		}
	}
	
}	// StaticFunctionInvokingFunction
