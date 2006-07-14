package net.sf.eclipsefp.test.util.haskell;

import java.io.StringBufferInputStream;

import net.sf.eclipsefp.common.core.project.ProjectCreationOperation;
import net.sf.eclipsefp.haskell.core.preferences.ICorePreferenceNames;
import net.sf.eclipsefp.haskell.core.project.HaskellProjectCreationOperation;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.Status;

public class TestHaskellProject implements ICorePreferenceNames {

	private IProject fUnderlyingProject;
	private IFolder fSourceFolder;
	
	public TestHaskellProject(final String projectName) throws CoreException {
		ProjectCreationOperation op = new HaskellProjectCreationOperation(
                                              preferences());
		op.setProjectName(projectName);
        try {
			op.run(new NullProgressMonitor());
			fUnderlyingProject = ResourcesPlugin.getWorkspace().getRoot().
			                         getProject(projectName);
			fSourceFolder = fUnderlyingProject.getFolder("src");
		} catch (Exception e) {
			throw new CoreException(Status.OK_STATUS);
		}
	}

	private Preferences preferences() {
		Preferences preferences = new Preferences();
	    preferences.setValue(SELECTED_COMPILER, "null");
	    preferences.setValue(FOLDERS_SRC, "src");		
	    preferences.setValue(FOLDERS_OUT, "out");
	    preferences.setValue(FOLDERS_BIN, "bin");		
	    preferences.setValue(TARGET_BINARY, "theResult");		
	    preferences.setValue(FOLDERS_IN_NEW_PROJECT, true);
		return preferences;
	}

	@SuppressWarnings("deprecation")
	public IFile createSourceFile(String fileName, final String contents) throws CoreException {
		IFile file = fSourceFolder.getFile(fileName);
		file.create(new StringBufferInputStream(contents), true, null);
		return file;
	}
	
	public void destroy() throws CoreException {
		fUnderlyingProject.delete(true, null);
	}

	public IProject getPlatformProject() {
		return fUnderlyingProject;
	}

}
