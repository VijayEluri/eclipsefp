package net.sf.eclipsefp.haskell.browser.views;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Content provider used when the database or Hoogle haven't been loaded yet.
 * @author Alejandro Serrano
 *
 */
public class NoDatabaseContentProvider implements ITreeContentProvider {

  @Override
  public void dispose() {
    // Do nothing
  }

  @Override
  public void inputChanged( final Viewer viewer, final Object oldInput, final Object newInput ) {
    // Do nothing
  }

  @Override
  public Object[] getElements( final Object inputElement ) {
    return new Object[] { NoDatabaseRoot.ROOT };
  }

  @Override
  public Object[] getChildren( final Object parentElement ) {
    return new Object[0];
  }

  @Override
  public Object getParent( final Object element ) {
    return null;
  }

  @Override
  public boolean hasChildren( final Object element ) {
    return false;
  }

}
