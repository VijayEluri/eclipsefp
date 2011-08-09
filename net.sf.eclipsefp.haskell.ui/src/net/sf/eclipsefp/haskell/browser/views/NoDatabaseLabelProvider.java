package net.sf.eclipsefp.haskell.browser.views;

import net.sf.eclipsefp.haskell.browser.util.ImageCache;
import net.sf.eclipsefp.haskell.ui.internal.util.UITexts;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;


public class NoDatabaseLabelProvider implements ILabelProvider {

  boolean isHoogle;

  public NoDatabaseLabelProvider(final boolean isHoogle) {
    this.isHoogle = isHoogle;
  }

  public Image getImage( final Object element ) {
    return ImageCache.DATABASE;
  }

  public String getText( final Object element ) {
    return isHoogle ? UITexts.scionBrowserNoDatabaseLoadedOrHoogleNotPresent :
      UITexts.scionBrowserNoDatabaseLoaded;
  }

  public void addListener( final ILabelProviderListener listener ) {
    // Do nothing
  }

  public void removeListener( final ILabelProviderListener listener ) {
    // Do nothing
  }

  public void dispose() {
    // Do nothing
  }

  public boolean isLabelProperty( final Object element, final String property ) {
    // Do nothing
    return false;
  }

}
