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

package com.sibvisions.rad.lua.support.functions;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.AbstractInterceptingJavaInstance;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

import com.sibvisions.rad.lua.CoerceEnum;

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
	
	/** The {@link Class} to use as wrapper. */
	private Class<? extends AbstractInterceptingJavaInstance> wrapperClass = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link ConstructorInvokingFunction}.
	 *
	 * @param pClazz the {@link Class}.
	 * @param pWrapperClass the {@link Class} to use as wrapper.
	 */
	public ConstructorInvokingFunction(Class<?> pClazz, Class<? extends AbstractInterceptingJavaInstance> pWrapperClass)
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
		
		wrapperClass = pWrapperClass;
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
		
		List<Constructor<?>> remainingConstructors = new ArrayList<>();
		
		for (Constructor<?> constructor : constructors)
		{
			if (constructor.getParameterCount() == pArgs.narg())
			{
				remainingConstructors.add(constructor);
			}
		}
		
		for (int index = 0; index < pArgs.narg(); index++)
		{
			Class<?> currentClass = null;
			
			if (pArgs.isnil(index + 1))
			{
				currentClass = Object.class;
				parameters[index] = null;
			}
			else if (pArgs.isnumber(index + 1))
			{
				currentClass = int.class;
				parameters[index] = Integer.valueOf(pArgs.toint(index + 1));
			}
			else if (pArgs.isstring(index + 1))
			{
				currentClass = String.class;
				parameters[index] = pArgs.tojstring(index + 1);
			}
			else if (pArgs.isuserdata(index + 1))
			{
				parameters[index] = CoerceLuaToJava.coerce(pArgs.arg(index + 1), Object.class);
				
				if (parameters[index] != null)
				{
					currentClass = parameters[index].getClass();
				}
				else
				{
					currentClass = Object.class;
				}
			}
			
			List<Constructor<?>> fittingConstructors = new ArrayList<>();
			
			for (Constructor<?> constructor : remainingConstructors)
			{
				Class<?> expectedClass = constructor.getParameterTypes()[index];
				
				if (expectedClass.isAssignableFrom(currentClass)
						|| (expectedClass.equals(long.class) && currentClass.equals(int.class))
						|| (expectedClass.equals(double.class) && currentClass.equals(float.class)))
				{
					fittingConstructors.add(constructor);
				}
				else if (Enum.class.isAssignableFrom(expectedClass) && currentClass.equals(String.class))
				{
					@SuppressWarnings("unchecked")
					Object value = CoerceEnum.luaToJava(pArgs.arg(index + 1), null, (Class<Enum<?>>)expectedClass);
					
					if (value != null)
					{
						parameters[index] = value;
						fittingConstructors.add(constructor);
					}
				}
			}
			
			remainingConstructors = fittingConstructors;
		}
		
		if (remainingConstructors.isEmpty())
		{
			throw new LuaError("Object can not be created, no fitting constructor available.");
		}
		else if (remainingConstructors.size() > 1)
		{
			throw new LuaError("Multiple matching constructors found.");
		}
		
		try
		{
			Object instance = remainingConstructors.get(0).newInstance(parameters);
			LuaValue luaInstance = wrapperClass.getConstructor(Object.class).newInstance(instance);
			
			return luaInstance;
		}
		catch (Exception e)
		{
			throw new LuaError(e);
		}
	}
	
}	// ConstructorInvokingFunction
