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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import javax.rad.genui.UIColor;
import javax.rad.genui.UICursor;
import javax.rad.genui.UIDimension;
import javax.rad.genui.UIFont;
import javax.rad.genui.UIImage;
import javax.rad.genui.UIInsets;
import javax.rad.genui.UILayout;
import javax.rad.genui.UIPoint;
import javax.rad.genui.UIRectangle;
import javax.rad.genui.component.UIButton;
import javax.rad.genui.component.UICheckBox;
import javax.rad.genui.component.UIIcon;
import javax.rad.genui.component.UILabel;
import javax.rad.genui.component.UIPasswordField;
import javax.rad.genui.component.UIRadioButton;
import javax.rad.genui.component.UITextArea;
import javax.rad.genui.component.UITextField;
import javax.rad.genui.component.UIToggleButton;
import javax.rad.genui.container.UIDesktopPanel;
import javax.rad.genui.container.UIFrame;
import javax.rad.genui.container.UIGroupPanel;
import javax.rad.genui.container.UIInternalFrame;
import javax.rad.genui.container.UIPanel;
import javax.rad.genui.container.UIScrollPanel;
import javax.rad.genui.container.UISplitPanel;
import javax.rad.genui.container.UITabsetPanel;
import javax.rad.genui.container.UIToolBar;
import javax.rad.genui.container.UIToolBarPanel;
import javax.rad.genui.container.UIWindow;
import javax.rad.genui.control.UICellFormat;
import javax.rad.genui.control.UIChart;
import javax.rad.genui.control.UIEditor;
import javax.rad.genui.control.UITable;
import javax.rad.genui.control.UITree;
import javax.rad.genui.layout.UIBorderLayout;
import javax.rad.genui.layout.UIFlowLayout;
import javax.rad.genui.layout.UIFormLayout;
import javax.rad.genui.layout.UIGridLayout;
import javax.rad.genui.menu.UIMenu;
import javax.rad.genui.menu.UIMenuBar;
import javax.rad.genui.menu.UIMenuItem;
import javax.rad.genui.menu.UIPopupMenu;
import javax.rad.genui.menu.UISeparator;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.sibvisions.rad.lua.libs.functions.ConstructorInvokingFunction;
import com.sibvisions.rad.lua.libs.functions.StaticFunctionInvokingFunction;

/**
 * The {@link JVxLib} provides all JVx GUI elements.
 * 
 * @author Robert Zenz
 */
public class JVxLib extends TwoArgFunction
{
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Initialization
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Creates a new instance of {@link JVxLib}.
	 */
	public JVxLib()
	{
		super();
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
		coerceClass(UIButton.class, pEnv);
		coerceClass(UICheckBox.class, pEnv);
		coerceClass(UIIcon.class, pEnv);
		coerceClass(UILabel.class, pEnv);
		coerceClass(UIPasswordField.class, pEnv);
		coerceClass(UIRadioButton.class, pEnv);
		coerceClass(UITextArea.class, pEnv);
		coerceClass(UITextField.class, pEnv);
		coerceClass(UIToggleButton.class, pEnv);
		
		coerceClass(UIDesktopPanel.class, pEnv);
		coerceClass(UIFrame.class, pEnv);
		coerceClass(UIGroupPanel.class, pEnv);
		coerceClass(UIInternalFrame.class, pEnv);
		coerceClass(UIPanel.class, pEnv);
		coerceClass(UIScrollPanel.class, pEnv);
		coerceClass(UISplitPanel.class, pEnv);
		coerceClass(UITabsetPanel.class, pEnv);
		coerceClass(UIToolBar.class, pEnv);
		coerceClass(UIToolBarPanel.class, pEnv);
		coerceClass(UIWindow.class, pEnv);
		
		coerceClass(UICellFormat.class, pEnv);
		coerceClass(UIChart.class, pEnv);
		coerceClass(UIEditor.class, pEnv);
		coerceClass(UITable.class, pEnv);
		coerceClass(UITree.class, pEnv);
		
		coerceClass(UIBorderLayout.class, pEnv);
		coerceClass(UIFlowLayout.class, pEnv);
		coerceClass(UIFormLayout.class, pEnv);
		coerceClass(UIGridLayout.class, pEnv);
		
		coerceClass(UIMenu.class, pEnv);
		coerceClass(UIMenuBar.class, pEnv);
		coerceClass(UIMenuItem.class, pEnv);
		coerceClass(UIPopupMenu.class, pEnv);
		coerceClass(UISeparator.class, pEnv);
		
		coerceClass(UIColor.class, pEnv);
		coerceClass(UICursor.class, pEnv);
		coerceClass(UIDimension.class, pEnv);
		coerceClass(UIFont.class, pEnv);
		coerceClass(UIImage.class, pEnv);
		coerceClass(UIInsets.class, pEnv);
		coerceClass(UILayout.class, pEnv);
		coerceClass(UIPoint.class, pEnv);
		coerceClass(UIRectangle.class, pEnv);
		
		return LuaValue.NIL;
	}
	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// User-defined methods
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Coerces the given {@link Class} into the given {@link LuaValue
	 * environment}.
	 * 
	 * @param pClass the {@link Class} to coerce.
	 * @param pEnv the target {@link LuaValue environment}.
	 */
	private static final void coerceClass(Class<?> pClass, LuaValue pEnv)
	{
		LuaValue coercedClass = new LuaTable();
		coercedClass.set("new", new ConstructorInvokingFunction(pClass));
		
		coerceStaticMethods(pClass, coercedClass);
		coerceStaticFields(pClass, coercedClass);
		
		pEnv.set(pClass.getSimpleName(), coercedClass);
	}
	
	/**
	 * Adds all static fields of the given {@link Class} to the given
	 * {@link LuaValue target}.
	 * 
	 * @param pClass the {@link Class}.
	 * @param pTarget the {@link LuaValue target}.
	 */
	private static final void coerceStaticFields(Class<?> pClass, LuaValue pTarget)
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
	private static final void coerceStaticMethods(Class<?> pClass, LuaValue pTarget)
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
	
}	// JVxLib
