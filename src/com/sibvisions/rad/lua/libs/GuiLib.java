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

package com.sibvisions.rad.lua.libs;

import javax.rad.ui.IFactory;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * The {@link GuiLib} provides basic factory functionality.
 * 
 * @author Robert Zenz
 */
public class GuiLib extends TwoArgFunction
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The current {@link IFactory}. */
	private IFactory factory = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link GuiLib}.
	 *
	 * @param pFactory the {@link IFactory}.
	 */
	public GuiLib(IFactory pFactory)
	{
		super();
		
		factory = pFactory;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Overwritten methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LuaValue call(LuaValue pValue, LuaValue pEnv)
	{
		LuaTable gui = new LuaTable();
		gui.set("invokeLater", new invokeLater());
		gui.set("invoke", new invoke());
		
		pEnv.set("Gui", gui);
		
		return LuaValue.NIL;
	}
	
	//****************************************************************
	// Subclass definition
	//****************************************************************
	
	/**
	 * The {@link invoke} function.
	 * 
	 * @author Robert Zenz
	 */
	private final class invoke extends OneArgFunction
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public LuaValue call(LuaValue pArg)
		{
			try
			{
				factory.invokeAndWait(pArg::call);
			}
			catch (Exception e)
			{
				throw new LuaError(e);
			}
			
			return LuaValue.NIL;
		}
	}
	
	/**
	 * The {@link invokeLater} function.
	 * 
	 * @author Robert Zenz
	 */
	private final class invokeLater extends OneArgFunction
	{
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Overwritten methods
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public LuaValue call(LuaValue pArg)
		{
			factory.invokeLater(pArg::call);
			
			return LuaValue.NIL;
		}
	}
	
}	// GuiLib
