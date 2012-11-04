// Copyright (c) 2003-2005 by Leif Frenzel - see http://leiffrenzel.de
package net.sf.eclipsefp.haskell.core.preferences;

import net.sf.eclipsefp.haskell.core.HaskellCorePlugin;
import net.sf.eclipsefp.haskell.core.cabalmodel.CabalSyntax;
import net.sf.eclipsefp.haskell.util.FileUtil;
import net.sf.eclipsefp.haskell.util.PlatformUtil;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;


/** <p>initializer for the core preferences (declared in the
  * <code>plugin.xml</code>).</p>
  *
  * @author Leif Frenzel
  */
public class CorePreferenceInitializer extends AbstractPreferenceInitializer
                                       implements ICorePreferenceNames {
  @Override
  public void initializeDefaultPreferences() {
    IEclipsePreferences coreNode = DefaultScope.INSTANCE.getNode( HaskellCorePlugin.getPluginId() );
    coreNode.put( SELECTED_COMPILER, "ghcCompiler" ); //$NON-NLS-1$
    coreNode.put( FOLDERS_SRC, FileUtil.DEFAULT_FOLDER_SRC );
   // coreNode.put( FOLDERS_DOC, FileUtil.DEFAULT_FOLDER_DOC );
   // coreNode.put( FOLDERS_OUT, "out" ); //$NON-NLS-1$
   // coreNode.put( TARGET_BINARY, "bin/theResult" ); //$NON-NLS-1$
    coreNode.putBoolean( FOLDERS_IN_NEW_PROJECT, true );

    coreNode.putBoolean( DEBUG_BREAK_ON_ERROR, false );
    coreNode.putBoolean( DEBUG_BREAK_ON_EXCEPTION, false );
    coreNode.putBoolean( DEBUG_PRINT_WITH_SHOW, true );
    coreNode.putInt( RUN_COMMAND_HISTORY_MAX, 20 );

    coreNode.put( TEMPLATE_CABAL,
        CabalSyntax.FIELD_NAME.getCabalName()+":           ${"+TemplateVariables.PROJECT_NAME+"}"+PlatformUtil.NL+  //$NON-NLS-1$ //$NON-NLS-2$
        CabalSyntax.FIELD_VERSION.getCabalName()+":        0.1"+PlatformUtil.NL+  //$NON-NLS-1$
        CabalSyntax.FIELD_CABAL_VERSION.getCabalName()+":  >=1.2"+PlatformUtil.NL+  //$NON-NLS-1$
        CabalSyntax.FIELD_AUTHOR.getCabalName()+":         ${"+TemplateVariables.USER_NAME+"}"+PlatformUtil.NL+  //$NON-NLS-1$ //$NON-NLS-2$
         ""+PlatformUtil.NL+  //$NON-NLS-1$
         "${"+TemplateVariables.LIBRARY+"}"+PlatformUtil.NL+  //$NON-NLS-1$ //$NON-NLS-2$
         "${"+TemplateVariables.EXECUTABLE+"}"+PlatformUtil.NL+  //$NON-NLS-1$ //$NON-NLS-2$
         "");  //$NON-NLS-1$

    coreNode.put( TEMPLATE_CABAL_LIBRARY,
        CabalSyntax.SECTION_LIBRARY.getCabalName()+PlatformUtil.NL+
        "  "+CabalSyntax.FIELD_HS_SOURCE_DIRS.getCabalName()+":    ${"+TemplateVariables.SRC+"}" +PlatformUtil.NL+  //$NON-NLS-1$ //$NON-NLS-2$  //$NON-NLS-3$
        "  "+CabalSyntax.FIELD_BUILD_DEPENDS.getCabalName()+":     base >= 4" +PlatformUtil.NL+  //$NON-NLS-1$ //$NON-NLS-2$
        "  "+CabalSyntax.FIELD_GHC_OPTIONS.getCabalName()+":       -Wall" +PlatformUtil.NL+  //$NON-NLS-1$ //$NON-NLS-2$
        "");  //$NON-NLS-1$

    coreNode.put( TEMPLATE_CABAL_EXE,
         CabalSyntax.SECTION_EXECUTABLE.getCabalName()+" ${"+TemplateVariables.PROJECT_NAME+"}"+PlatformUtil.NL+ //$NON-NLS-1$ //$NON-NLS-2$
         "  "+CabalSyntax.FIELD_HS_SOURCE_DIRS+": ${"+TemplateVariables.SRC+"}" +PlatformUtil.NL+ //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
         "  "+CabalSyntax.FIELD_MAIN_IS+":       Main.hs"+PlatformUtil.NL+ //$NON-NLS-1$ //$NON-NLS-2$
         "  "+CabalSyntax.FIELD_BUILD_DEPENDS+": base >= 4"+PlatformUtil.NL+ //$NON-NLS-1$ //$NON-NLS-2$
         "  "+CabalSyntax.FIELD_GHC_OPTIONS+":   -Wall"+PlatformUtil.NL+ //$NON-NLS-1$ //$NON-NLS-2$
         "");  //$NON-NLS-1$

    coreNode.put( TEMPLATE_CABAL_SETUP,
        "import Distribution.Simple"+PlatformUtil.NL+"main = defaultMain"+PlatformUtil.NL //$NON-NLS-1$ //$NON-NLS-2$
    );

    coreNode.put( TEMPLATE_MODULE,
        "module ${"+TemplateVariables.MODULE_NAME+"} where"+PlatformUtil.NL //$NON-NLS-1$ //$NON-NLS-2$
    );

    coreNode.put( TEMPLATE_MAIN,
        "${"+TemplateVariables.MODULE+"}"+PlatformUtil.NL+ //$NON-NLS-1$ //$NON-NLS-2$
        "main::IO()"+PlatformUtil.NL+ //$NON-NLS-1$
        "main = undefined" //$NON-NLS-1$
    );

    coreNode.put( TEMPLATE_GTK,
        "${"+TemplateVariables.MODULE+"}"+PlatformUtil.NL+ //$NON-NLS-1$ //$NON-NLS-2$
        "import Graphics.UI.Gtk"+PlatformUtil.NL+PlatformUtil.NL+ //$NON-NLS-1$
        "main :: IO ()"+PlatformUtil.NL+ //$NON-NLS-1$
        "main = do"+PlatformUtil.NL+ //$NON-NLS-1$
        "  initGUI"+PlatformUtil.NL+ //$NON-NLS-1$
        "  window <- windowNew"+PlatformUtil.NL+ //$NON-NLS-1$
        "  button <- buttonNew"+PlatformUtil.NL+ //$NON-NLS-1$
        "  set window [ containerBorderWidth := 10,"+PlatformUtil.NL+ //$NON-NLS-1$
        "               containerChild := button ]"+PlatformUtil.NL+ //$NON-NLS-1$
        "  set button [ buttonLabel := \"Hello World\" ]"+PlatformUtil.NL+ //$NON-NLS-1$
        "  onClicked button (putStrLn \"Hello World\")"+PlatformUtil.NL+ //$NON-NLS-1$
        "  onDestroy window mainQuit"+PlatformUtil.NL+ //$NON-NLS-1$
        "  widgetShowAll window"+PlatformUtil.NL+ //$NON-NLS-1$
        "  mainGUI"+PlatformUtil.NL //$NON-NLS-1$
    );
  }
}