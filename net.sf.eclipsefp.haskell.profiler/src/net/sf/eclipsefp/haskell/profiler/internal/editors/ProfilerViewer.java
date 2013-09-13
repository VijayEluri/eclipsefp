/**
 * (c) 2011, Alejandro Serrano
 * Released under the terms of the EPL.
 */
package net.sf.eclipsefp.haskell.profiler.internal.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * Viewer for profiling output generated by GHC.
 * Uses the BIRT Charting engine.
 * @author Alejandro Serrano
 *
 */
public class ProfilerViewer extends EditorPart {
	private ProfileViewerImpl impl;
	

	public ProfilerViewer() {
		super();
		try {
			// class in birt model
			this.getClass().getClassLoader().loadClass("org.eclipse.birt.chart.model.Chart");
			// extension class to make sure we have the extension plugin loaded
			this.getClass().getClassLoader().loadClass("org.eclipse.birt.chart.extension.render.Area");
			impl=new ProfileViewerBirtImpl() {
				
				@Override
				public void setPartName(String name) {
					ProfilerViewer.this.setPartName(name);
				}
			};
		} catch (Throwable t){
			impl=new ProfileViewerImpl() {
				
				@Override
				public void setPartName(String name) {
					ProfilerViewer.this.setPartName(name);
				}
			};
		}
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		impl.init(site, input);
	}

	@Override
	public void createPartControl(Composite parent) {
		
		impl.createPartControl(parent);
	}

	

	@Override
	public void setFocus() {
		// Do nothing
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// Do nothing: the .hp files cannot be changed
	}

	@Override
	public void doSaveAs() {
		impl.doSaveAs(getSite().getShell(), getPartName());
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return impl.isSaveAsAllowed();
	}

}