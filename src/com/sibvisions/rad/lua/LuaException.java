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

/**
 * The {@link LuaException} is an {@link Exception} extension which is thrown by
 * the Lua environment.
 * 
 * @author Robert Zenz
 */
public class LuaException extends Exception
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link LuaException}.
	 */
	public LuaException()
	{
		super();
	}
	
	/**
	 * Creates a new instance of {@link LuaException}.
	 *
	 * @param pMessage the {@link String message}.
	 * @param pCause the {@link Throwable cause}.
	 */
	public LuaException(String pMessage, Throwable pCause)
	{
		super(pMessage, pCause);
	}
	
	/**
	 * Creates a new instance of {@link LuaException}.
	 *
	 * @param pMessage the {@link String message}.
	 */
	public LuaException(String pMessage)
	{
		super(pMessage);
	}
	
	/**
	 * Creates a new instance of {@link LuaException}.
	 *
	 * @param pCause the {@link Throwable cause}.
	 */
	public LuaException(Throwable pCause)
	{
		super(pCause);
	}
	
}	// LuaException
