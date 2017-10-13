package com.sibvisions.rad.lua.support.events;

import javax.rad.ui.event.IActionListener;
import javax.rad.ui.event.UIActionEvent;
import javax.rad.util.IRunnable;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

/**
 * The {@link CallingActionListener} is an {@link IRunnable} implementation wich
 * calls a given {@link LuaValue}.
 */
public class CallingActionListener implements IActionListener
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
	 * Creates a new instance of {@link CallingActionListener}.
	 *
	 * @param pLuaValue the {@link LuaValue lua value}.
	 */
	public CallingActionListener(LuaValue pLuaValue)
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
	public void action(UIActionEvent pEvent) throws Throwable
	{
		luaValue.call(CoerceJavaToLua.coerce(pEvent));
	}
	
}	// CallingRunnable