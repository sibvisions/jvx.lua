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

package com.sibvisions.rad.lua;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.AbstractInterceptingJavaInstance;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.sibvisions.rad.lua.support.functions.ConstructorInvokingFunction;
import com.sibvisions.rad.lua.support.functions.StaticFunctionInvokingFunction;
import com.sibvisions.rad.lua.support.wrappers.EventHandlerInterceptingWrapper;

/**
 * {@link CoerceClass} is a helper utility which allows to coerce
 * {@link Class}es into a Lua environment and back from it.
 * 
 * @author Robert Zenz
 */
public final class CoerceClass
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * No instance required.
	 */
	private CoerceClass()
	{
		super();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adds all static fields of the given {@link Class} to the given
	 * {@link LuaValue target}.
	 * 
	 * @param pClass the {@link Class}.
	 * @param pTarget the {@link LuaValue target}.
	 */
	public static final void coerceStaticFields(Class<?> pClass, LuaValue pTarget)
	{
		for (Field field : pClass.getFields())
		{
			if ((field.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC
					&& (field.getModifiers() & Modifier.FINAL) == Modifier.FINAL
					&& (field.getModifiers() & Modifier.STATIC) == Modifier.STATIC)
			{
				try
				{
					pTarget.set(field.getName(), CoerceJavaToLua.coerce(field.get(null)));
				}
				catch (IllegalArgumentException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		// Workaround for Java Bug #9050649.
		for (Class<?> interfaze : pClass.getInterfaces())
		{
			coerceStaticFields(interfaze, pTarget);
		}
		
		if (pClass.getSuperclass() != null)
		{
			coerceStaticFields(pClass.getSuperclass(), pTarget);
		}
	}
	
	/**
	 * Adds all static methods of the given {@link Class} to the given
	 * {@link LuaValue target}.
	 * 
	 * @param pClass the {@link Class}.
	 * @param pTarget the {@link LuaValue target}.
	 */
	public static final void coerceStaticMethods(Class<?> pClass, LuaValue pTarget)
	{
		Set<String> staticMethodNames = new HashSet<>();
		
		for (Method method : pClass.getMethods())
		{
			if ((method.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC
					&& (method.getModifiers() & Modifier.STATIC) == Modifier.STATIC)
			{
				staticMethodNames.add(method.getName());
			}
		}
		
		for (String staticMethodName : staticMethodNames)
		{
			pTarget.set(staticMethodName, new StaticFunctionInvokingFunction(pClass, staticMethodName));
		}
	}
	
	/**
	 * Coerces the given {@link Class} into the given {@link LuaValue
	 * environment}.
	 * 
	 * @param pClass the {@link Class} to coerce.
	 * @param pWrapperClass the {@link AbstractInterceptingJavaInstance wrapper
	 *            class} to use.
	 * @param pEnv the target {@link LuaValue environment}.
	 */
	public static final void load(Class<?> pClass, Class<? extends AbstractInterceptingJavaInstance> pWrapperClass, LuaValue pEnv)
	{
		LuaValue coercedClass = new LuaTable();
		coercedClass.set("new", new ConstructorInvokingFunction(pClass, pWrapperClass));
		
		coerceStaticMethods(pClass, coercedClass);
		coerceStaticFields(pClass, coercedClass);
		
		pEnv.set(pClass.getSimpleName(), coercedClass);
	}
	
	/**
	 * Coerces the given {@link Class} into the given {@link LuaValue
	 * environment}.
	 * 
	 * @param pClass the {@link Class} to coerce.
	 * @param pEnv the target {@link LuaValue environment}.
	 */
	public static final void load(Class<?> pClass, LuaValue pEnv)
	{
		load(pClass, EventHandlerInterceptingWrapper.class, pEnv);
	}
	
}	// CoerceClass
