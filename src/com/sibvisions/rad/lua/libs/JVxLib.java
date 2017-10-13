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

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import com.sibvisions.rad.lua.CoerceClass;

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
		CoerceClass.load(UIButton.class, pEnv);
		CoerceClass.load(UICheckBox.class, pEnv);
		CoerceClass.load(UIIcon.class, pEnv);
		CoerceClass.load(UILabel.class, pEnv);
		CoerceClass.load(UIPasswordField.class, pEnv);
		CoerceClass.load(UIRadioButton.class, pEnv);
		CoerceClass.load(UITextArea.class, pEnv);
		CoerceClass.load(UITextField.class, pEnv);
		CoerceClass.load(UIToggleButton.class, pEnv);
		
		CoerceClass.load(UIDesktopPanel.class, pEnv);
		CoerceClass.load(UIFrame.class, pEnv);
		CoerceClass.load(UIGroupPanel.class, pEnv);
		CoerceClass.load(UIInternalFrame.class, pEnv);
		CoerceClass.load(UIPanel.class, pEnv);
		CoerceClass.load(UIScrollPanel.class, pEnv);
		CoerceClass.load(UISplitPanel.class, pEnv);
		CoerceClass.load(UITabsetPanel.class, pEnv);
		CoerceClass.load(UIToolBar.class, pEnv);
		CoerceClass.load(UIToolBarPanel.class, pEnv);
		CoerceClass.load(UIWindow.class, pEnv);
		
		CoerceClass.load(UICellFormat.class, pEnv);
		CoerceClass.load(UIChart.class, pEnv);
		CoerceClass.load(UIEditor.class, pEnv);
		CoerceClass.load(UITable.class, pEnv);
		CoerceClass.load(UITree.class, pEnv);
		
		CoerceClass.load(UIBorderLayout.class, pEnv);
		CoerceClass.load(UIFlowLayout.class, pEnv);
		CoerceClass.load(UIFormLayout.class, pEnv);
		CoerceClass.load(UIGridLayout.class, pEnv);
		
		CoerceClass.load(UIMenu.class, pEnv);
		CoerceClass.load(UIMenuBar.class, pEnv);
		CoerceClass.load(UIMenuItem.class, pEnv);
		CoerceClass.load(UIPopupMenu.class, pEnv);
		CoerceClass.load(UISeparator.class, pEnv);
		
		CoerceClass.load(UIColor.class, pEnv);
		CoerceClass.load(UICursor.class, pEnv);
		CoerceClass.load(UIDimension.class, pEnv);
		CoerceClass.load(UIFont.class, pEnv);
		CoerceClass.load(UIImage.class, pEnv);
		CoerceClass.load(UIInsets.class, pEnv);
		CoerceClass.load(UILayout.class, pEnv);
		CoerceClass.load(UIPoint.class, pEnv);
		CoerceClass.load(UIRectangle.class, pEnv);
		
		return LuaValue.NIL;
	}
	
}	// JVxLib
