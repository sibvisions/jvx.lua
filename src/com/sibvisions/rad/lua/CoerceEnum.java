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

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

/**
 * {@link CoerceEnum} is a helper utility which allows to coerce {@link Enum}s
 * into a Lua environment and back from it.
 * 
 * @author Robert Zenz
 */
public final class CoerceEnum
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * No instance required.
	 */
	private CoerceEnum()
	{
		super();
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Coerce the given {@link Enum} value into a Lua value.
	 * 
	 * @param pEnumValue the {@link Enum} value to coerce.
	 * @return the coerced value. {@link LuaValue#NIL} if the given value is
	 *         {@code null}.
	 */
	public static final <ENUM extends Enum<?>> LuaValue javaToLua(ENUM pEnumValue)
	{
		if (pEnumValue == null)
		{
			return LuaValue.NIL;
		}
		
		return LuaValue.valueOf(pEnumValue.name());
	}
	
	/**
	 * Loads the given {@link Enum} class into the given {@link LuaValue
	 * environment}.
	 * 
	 * @param pEnumClass the {@link Enum} to load.
	 * @param pEnv the {@link LuaValue environment}.
	 */
	public static final <ENUM extends Enum<?>> void load(Class<ENUM> pEnumClass, LuaValue pEnv)
	{
		LuaTable coercedEnum = new LuaTable();
		
		for (ENUM value : pEnumClass.getEnumConstants())
		{
			coercedEnum.set(value.toString(), value.toString());
		}
		
		pEnv.set(pEnumClass.getSimpleName(), coercedEnum);
	}
	
	/**
	 * Coerces the given {@link LuaValue} into the given {@link Enum}.
	 * 
	 * @param pLuaValue the {@link LuaValue} to coerce.
	 * @param pDefaultValue the default value to return.
	 * @param pEnumClass the {@link Class} of the {@link Enum} to which to
	 *            coerce.
	 * @return the coerced Java value.
	 */
	public static final <ENUM extends Enum<?>> ENUM luaToJava(LuaValue pLuaValue, ENUM pDefaultValue, Class<ENUM> pEnumClass)
	{
		if (pLuaValue == null || pLuaValue.isnil() || !pLuaValue.isstring())
		{
			return pDefaultValue;
		}
		
		String stringValue = pLuaValue.tojstring();
		
		for (ENUM value : pEnumClass.getEnumConstants())
		{
			if (value.toString().equals(stringValue))
			{
				return value;
			}
		}
		
		return pDefaultValue;
	}
	
}	// CoerceEnum
