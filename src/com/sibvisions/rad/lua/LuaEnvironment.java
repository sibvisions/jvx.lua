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

import java.util.ArrayList;
import java.util.List;

import javax.rad.genui.UIFactoryManager;
import javax.rad.ui.IComponent;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.luajc.LuaJC;

import com.sibvisions.rad.lua.libs.GuiLib;
import com.sibvisions.rad.lua.libs.JVxLib;
import com.sibvisions.util.type.ExceptionUtil;

/**
 * The {@link LuaEnvironment} provides a simple environment for using JVx
 * components.
 * 
 * @author Robert Zenz
 */
public class LuaEnvironment
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Constants
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The name of the chunk that is being executed. */
	private static final String CHUNKNAME = "SCRIPT";
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link Globals} which are used. */
	private Globals globals = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link LuaEnvironment}.
	 */
	public LuaEnvironment()
	{
		super();
		
		globals = new Globals();
		
		LuaC.install(globals);
		LuaJC.install(globals);
		
		globals.load(new PackageLib());
		
		globals.load(new Bit32Lib());
		globals.load(new StringLib());
		globals.load(new TableLib());
		
		globals.load(new JseBaseLib());
		globals.load(new JseMathLib());
		
		globals.load(new JVxLib());
		
		globals.load(new GuiLib(UIFactoryManager.getFactory()));
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Extracts the Lua-relevant information from the given stack trace.
	 * 
	 * @param pStackTrace the stack trace from which to extract it.
	 * @return a new Lua stack trace. Can be empty.
	 */
	private static final StackTraceElement[] extractLuaStacktrace(StackTraceElement[] pStackTrace)
	{
		if (pStackTrace == null || pStackTrace.length == 0)
		{
			return new StackTraceElement[0];
		}
		
		List<StackTraceElement> luaStackTrace = new ArrayList<>();
		
		for (StackTraceElement stackTraceElement : pStackTrace)
		{
			String fileName = stackTraceElement.getFileName();
			
			if (fileName != null && fileName.startsWith(CHUNKNAME))
			{
				String methodName = stackTraceElement.getMethodName();
				
				int dollarIndex = stackTraceElement.getClassName().indexOf('$');
				
				if (dollarIndex >= 0)
				{
					methodName = stackTraceElement.getClassName().substring(dollarIndex + 1);
				}
				
				StackTraceElement luaStackTraceElement = new StackTraceElement(
						"",
						fileName,
						methodName,
						stackTraceElement.getLineNumber());
				
				luaStackTrace.add(luaStackTraceElement);
			}
		}
		
		return luaStackTrace.toArray(new StackTraceElement[luaStackTrace.size()]);
	}
	
	/**
	 * Gets the {@link Globals} of this environment.
	 * 
	 * @return the {@link Globals} of this environment.
	 */
	public Globals getGlobals()
	{
		return globals;
	}
	
	/**
	 * Executs the given script returns the {@link IComponent}, if any.
	 * 
	 * @param pLuaScript the Lua script to execute.
	 * @return the returned {@link IComponent}, if any.
	 * @throws LuaException if execution of the Lua script failed.
	 */
	public IComponent execute(String pLuaScript) throws LuaException
	{
		try
		{
			LuaValue luaValue = globals.load(pLuaScript, CHUNKNAME).call();
			return (IComponent)CoerceLuaToJava.coerce(luaValue, IComponent.class);
		}
		catch (LuaError e)
		{
			LuaError error = new LuaError(e.getMessage());
			error.setStackTrace(extractLuaStacktrace(e.getStackTrace()));
			
			throw new LuaException(ExceptionUtil.dump(error, false), e);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new LuaException("Failed to execute given script: " + e.getMessage(), e);
		}
	}
	
}	// LuaEnvironment
