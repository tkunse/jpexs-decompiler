/*
 *  Copyright (C) 2010-2014 JPEXS
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jpexs.decompiler.flash.gui.generictageditors;

import com.jpexs.helpers.Helper;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.lang.reflect.Field;
import javax.swing.JTextArea;
import jsyntaxpane.util.StringUtils;

/**
 *
 * @author JPEXS
 */
public class StringEditor extends JTextArea implements GenericTagEditor {

    private final Object obj;
    private final Field field;
    private final int index;
    private final Class<?> type;
    private String fieldName;

    public StringEditor(String fieldName,Object obj, Field field, int index, Class<?> type) {
        setLineWrap(true);
        this.obj = obj;
        this.field = field;
        this.index = index;
        this.type = type;     
        this.fieldName = fieldName;
        try {
            setText((String) ReflectionTools.getValue(obj, field, index));
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            // ignore
        }
    }

    @Override
    public void save() {
        try {
            ReflectionTools.setValue(obj, field, index, getText());
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            // ignore
        }
    }
    
    @Override
    public void addChangeListener(final ChangeListener l) {
        final GenericTagEditor t = this;
        addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                l.change(t);
            }

        });
    }

    @Override
    public Object getChangedValue() {
        return getText();
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
    
    public Field getField() {
        return field;
    }
    
    @Override
    public String getReadOnlyValue() {
        return Helper.escapeHTML(getChangedValue().toString());
    }    
   
}