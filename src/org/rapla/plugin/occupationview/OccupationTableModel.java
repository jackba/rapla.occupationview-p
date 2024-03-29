/*--------------------------------------------------------------------------*
 | Copyright (C) 2011 Bob Jordaens                                          |
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

import java.util.Date;
import javax.swing.table.DefaultTableModel;

import org.rapla.components.util.DateTools;
import org.rapla.components.xmlbundle.I18nBundle;

public class OccupationTableModel extends DefaultTableModel
{
    private static final long serialVersionUID = 1L;

	static final int CALENDAR_SEQUENCE_NUMBER   = 0;
	static final int CALENDAR_RESOURCE			= 1;
	static final int CALENDAR_CHECK 		    = 2;
	static final int CALENDAR_IN_DAYS			= 3;
	static final int CALENDAR_OUT_DAYS 			= 4;
	static final int CALENDAR_EVENTS			= 5;
	
    private I18nBundle i18n;
    Object occupationTable[][];
    private int freeSlot= 0;
    private Date calendarStartDate;
    private int todayColumn = 0;
    private int rowCount = 0;
    private int columnCount = 0;
    
    public OccupationTableModel(I18nBundle i18n, int maxRowCount, int maxColumnCount, Date date) {
        this.calendarStartDate = date;
        this.i18n = i18n;
        this.rowCount = 0; // must be calculated
        this.columnCount = CALENDAR_OUT_DAYS + 1; // must be calculated
        occupationTable = new Object[maxRowCount][maxColumnCount]; 
    }
        
    public void firstFit() {
    	if(freeSlot==0)
    		return;
    	int freeCount=0;
    	Object occupation = null;
    	OccupationCell occCell = null;
    	for (int r=0; r<getRowCount();r++) {
    		freeCount=0;
    		for (int c=CALENDAR_EVENTS; c<getColumnCount();c++) {
    	        occupation = getValueAt(r, c);
    	        if (occupation instanceof OccupationCell ) {
    	        	occCell = (OccupationCell) occupation;
    	        	if(occCell.getTypeId() == 0) {
    	        		freeCount++;
    	        		if(freeCount == freeSlot) {
    	        			setFreeSlots(r,c);
    	        			freeCount=0;
    	        			break;
    	        		}
    	        	}
    	        	else
    	        		freeCount=0;
    	        }
    		}
    	}
    	super.fireTableDataChanged();
    	return;
    }
    
    private void setFreeSlots(int r, int c) {
        char leftBound  = '['; // set for starting
        char rightBound = ' '; 
        OccupationCell occCell = null;
        for( int i=0; i<=freeSlot - 1; i++) {
        	if (i==freeSlot - 1 ) {
        		rightBound = ']';
        	}
            occCell = new OccupationCell(leftBound,-2,rightBound, null);
    		setValueAt( occCell, r, c - freeSlot + i + 1);
    		leftBound  = ' ';
        }
    }

    public int findStartSlot(int row, int col, Object type) {
		int c = col;
    	for (; c>=CALENDAR_EVENTS ;c--)
    		if(!getValueAt(row,c).equals(type))
    			break;
    	return c + 1;
    }

    public Date getColumnDate(int c) {
        return DateTools.addDays(calendarStartDate, c - CALENDAR_EVENTS);
    }

    public Date getcalendarStartDate() {
        return calendarStartDate;
    }

    public void setTodayColumn(int c) {
        this.todayColumn = c;
    }
    
    public int getTodayColumn() {
        return todayColumn;
    }
    
    public int getFreeSlot() {
        return freeSlot;
    }
    
    public boolean isCellEditable(int row, int column) {
        return false;
    }
  
    public int getRowCount() {
    	return rowCount;
    }

    public void setColumnCount(int columnCount) 
    {
    	this.columnCount = columnCount;
    	return;  
    }    
    
    public int getColumnCount() 
    {
        return columnCount;
    }
    
    public int addRow(int incr) {
    	return rowCount+=incr;
    }

    public int addColumn(int incr) {
    	//System.out.println("ColumnCount= " + (columnCount + incr));
    	return columnCount+=incr;
    }
    
    public Object getValueAt( int rowIndex, int columnIndex )
    {

        switch ( columnIndex )
        {
          	case CALENDAR_CHECK: 
          		return occupationTable[rowIndex][columnIndex];
          	case CALENDAR_IN_DAYS: 
          		if(occupationTable[rowIndex][columnIndex] == null) 
          			return null;
          	case CALENDAR_OUT_DAYS: 
          		if(occupationTable[rowIndex][columnIndex] == null) 
          			return null;
        }
        
        return occupationTable[rowIndex][columnIndex];
    }

    public void setValueAt( Object value, int rowIndex, int columnIndex )
    {
    	
        switch ( columnIndex )
        {     
      		case CALENDAR_IN_DAYS: 
      			if(value == null) 
      				occupationTable[rowIndex][columnIndex] = value;
      		case CALENDAR_OUT_DAYS: 
      			if(value == null) 
      				occupationTable[rowIndex][columnIndex] = value;
          	default: 
          			occupationTable[rowIndex][columnIndex] = value;
        }
        return;
    }
    
    public String getColumnName( int columnIndex ) 
    {
        switch ( columnIndex )
        {
        	case CALENDAR_RESOURCE: 		return i18n.getString("resources_persons");
        	case CALENDAR_CHECK: 			return i18n.getString("event_check");
        	case CALENDAR_SEQUENCE_NUMBER: 	return "#";
        	case CALENDAR_IN_DAYS: 			return i18n.getString("daysin");
        	case CALENDAR_OUT_DAYS: 		return i18n.getString("daysout");
        	
            default: return "getcolumnName";
        }
    }

    public Class getColumnClass(int columnIndex) {
    	
        switch ( columnIndex )
        {
            default: return String.class;
        }
    }

	public void setFreeSlot(int freeSlot) {
		this.freeSlot = freeSlot;	
	}

	public void calcSlotGaps() {
    	int totalFreeGaps=0;
    	int lastFreeGap=0;
    	for (int r=0; r<getRowCount();r++) {
    		totalFreeGaps=0;
    		lastFreeGap=0;
    		for (int c=CALENDAR_EVENTS; c<getColumnCount();c++) {
    			Object cell = getValueAt(r,c);
    			if(cell.equals(" ") || cell.equals("="))
    				lastFreeGap++;
    			else {
    				totalFreeGaps+=lastFreeGap;
    				lastFreeGap=0;
    			}	
    		}
    		setValueAt((Object) totalFreeGaps, r, 1);
    	}
    	//super.fireTableDataChanged();
    	return;
	}

	public void showFree() {
    	return;
    }	

	public int getRowCount(int c) {
		int count = 0;
		for ( int r = 0; r < getRowCount();r++){
			Object occCell = getValueAt(r, c);
			if(occCell instanceof OccupationCell) {
				if(((OccupationCell) occCell).getTypeId() > 0)
					count++;
			}	
		}
		return count;
	}	
}
