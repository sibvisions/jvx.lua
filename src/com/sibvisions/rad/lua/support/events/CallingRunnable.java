package com.sibvisions.rad.lua.support.events;

import javax.rad.util.IRunnable;

import org.luaj.vm2.LuaValue;

/**
 * The {@link CallingRunnable} is an {@link IRunnable} implementation wich
 * calls a given {@link LuaValue}.
 */
public class CallingRunnable implements IRunnable
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Class members
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/** The {@link LuaValue} to call. */
	private LuaValue luaValue = null;
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link CallingRunnable}.
	 *
	 * @param pLuaValue the {@link LuaValue lua value}.
	 */
	public CallingRunnable(LuaValue pLuaValue)
	{
		super();
		
		luaValue = pLuaValue;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Interface implementation
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void run() throws Throwable
	{
		luaValue.call();
	}
	
}	// CallingRunnable