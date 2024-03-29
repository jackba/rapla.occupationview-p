/*--------------------------------------------------------------------------*
 | Copyright (C) 2011 Robert Jordaens                                       |
 |                                                                          |
 | This program is free software; you can redistribute it and/or modify     |
 | it under the terms of the GNU General Public License as published by the |
 | Free Software Foundation. A copy of the license has been included with   |
 | these distribution in the COPYING file, if not go to www.fsf.org         |
 |                                                                          |
 | As a special exception, you are granted the permissions to link this     |
 | program with every library, which license fulfills the Open Source       |
 | Definition as published by the Open Source Initiative (OSI).             |
 *--------------------------------------------------------------------------*/
package org.rapla.plugin.occupationview;

import javax.swing.Icon;

import org.rapla.facade.CalendarModel;
import org.rapla.facade.RaplaComponent;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;
import org.rapla.gui.SwingCalendarView;
import org.rapla.gui.SwingViewFactory;
import org.rapla.gui.images.Images;

public class OccupationFactory extends RaplaComponent implements SwingViewFactory
{
	
    public OccupationFactory( RaplaContext context ) throws RaplaException
    {
        super( context );
        setChildBundleName( OccupationPlugin.RESOURCE_FILE);
    }

    public final static String OCCUPATION_VIEW =  "occupation";

    public SwingCalendarView createSwingView(RaplaContext context, CalendarModel model, boolean editable) throws RaplaException
    {
    	SwingCalendarView viewObject;
    	viewObject = new SwingOccupation( context, model, editable);
    	return viewObject;
    }

    public String getViewId()
    {
        return OCCUPATION_VIEW;
    }

    public String getName()
    {
        return getString(OCCUPATION_VIEW);
    }

    Icon icon;
    public Icon getIcon()
    {
        if ( icon == null) {
            icon = Images.getIcon("/org/rapla/plugin/occupationview/images/table.png");
        }
        return icon;
    }

    public String getMenuSortKey() {
        return "O";
    }

	
}