// Copyright (c) 2003-2005 by Leif Frenzel - see http://leiffrenzel.de
package net.sf.eclipsefp.haskell.ghccompiler.core;

import net.sf.eclipsefp.haskell.core.compiler.CompilerManager;
import net.sf.eclipsefp.haskell.core.compiler.ICompilerManager;
import net.sf.eclipsefp.haskell.core.internal.hsimpl.IHsImplementation;
import net.sf.eclipsefp.haskell.core.util.GHCSyntax;
import net.sf.eclipsefp.haskell.core.util.ResourceUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/** <p>contains common helping functionality.</p>
  *
  * @author Leif Frenzel
  */
public class Util implements IGhcParameters {

  public static String getCompilerExecutable() {
    IPath result = null;

    ICompilerManager msn = CompilerManager.getInstance();
    IHsImplementation impl = msn.getCurrentHsImplementation();
    if( impl != null && impl.getBinDir() != null ) {
      result = new Path( impl.getBinDir() );
      result = result.append( GHCSyntax.GHC );
    }
    return result == null ? GHCSyntax.GHC : result.toOSString();
  }

  public static String constructLibPath( final IFile... files ) {
    StringBuilder sbResult = new StringBuilder();
    /*IImportLibrary[] libs = hsProject.getImportLibraries();

    for( int i = 0; i < libs.length; i++ ) {
      if( i == 0 ) {
        sbResult.append( "-i" ); //$NON-NLS-1$
      } else {
        sbResult.append( File.pathSeparator );
      }
      IPath path = libs[ i ].getPath();
      sbResult.append( path.toOSString() );
    }*/
    for (String s:ResourceUtil.getImportPackages( files )){
      sbResult.append(" -package "); //$NON-NLS-1$
      sbResult.append(s);
    }

    return sbResult.toString();
  }
}