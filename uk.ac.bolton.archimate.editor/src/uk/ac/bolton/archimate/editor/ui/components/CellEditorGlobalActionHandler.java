/*******************************************************************************
 * Copyright (c) 2011-12 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.ui.components;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;


/**
 * Manages de-activating and restoring of editing type global Action Handlers setting them to null
 * so that they cannot be invoked by the user when editing text in a text cell editor.<p>
 * This ensures that edit key shortcuts are bound to the cell editor, not the application. 
 * 
 * @author Phillip Beauvoir
 */
public class CellEditorGlobalActionHandler {

    private IActionBars fActionBars;
    
    // These are the global actions to set to null
    private String[] actionIds = new String[] {
            ActionFactory.CUT.getId(),
            ActionFactory.COPY.getId(),
            ActionFactory.PASTE.getId(),
            ActionFactory.DELETE.getId(),
            ActionFactory.SELECT_ALL.getId(),
            ActionFactory.FIND.getId(),
            ActionFactory.RENAME.getId(),
            ActionFactory.UNDO.getId(),
            ActionFactory.REDO.getId()
    };
    
    private IAction[] actions = new IAction[actionIds.length];

    public CellEditorGlobalActionHandler() {
        // Get Action Bars from Active Part
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if(window != null && window.getActivePage() != null && window.getActivePage().getActivePart() != null) {
            IWorkbenchPartSite site = window.getActivePage().getActivePart().getSite();
            if(site instanceof IEditorSite) {
                fActionBars = ((IEditorSite)site).getActionBars();
            }
            else if(site instanceof IViewSite) {
                fActionBars = ((IViewSite)site).getActionBars();
            }
        }
    }
    
    /**
     * Clear the Global Action Handlers for the Active Part
     */
    public void clearGlobalActions() {
        if(fActionBars != null) {
            // Save Actions
            for(int i = 0; i < actionIds.length; i++) {
                actions[i] = fActionBars.getGlobalActionHandler(actionIds[i]);
            }
            
            // Null them
            for(int i = 0; i < actionIds.length; i++) {
                fActionBars.setGlobalActionHandler(actionIds[i], null);
            }
            
            // Update
            fActionBars.updateActionBars();
        }
    }
    
    /**
     * Restore the Global Action Handlers that were set to null
     */
    public void restoreGlobalActions() {
        if(fActionBars != null) {
            // Restore Actions
            for(int i = 0; i < actionIds.length; i++) {
                fActionBars.setGlobalActionHandler(actionIds[i], actions[i]);
            }
            
            // Update
            fActionBars.updateActionBars();
        }
    }
}