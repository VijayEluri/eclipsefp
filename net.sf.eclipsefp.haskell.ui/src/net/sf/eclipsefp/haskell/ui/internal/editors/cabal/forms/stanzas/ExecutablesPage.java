package net.sf.eclipsefp.haskell.ui.internal.editors.cabal.forms.stanzas;

import java.util.List;
import net.sf.eclipsefp.haskell.core.cabalmodel.CabalSyntax;
import net.sf.eclipsefp.haskell.core.cabalmodel.PackageDescription;
import net.sf.eclipsefp.haskell.core.cabalmodel.PackageDescriptionStanza;
import net.sf.eclipsefp.haskell.scion.types.Component.ComponentType;
import net.sf.eclipsefp.haskell.ui.internal.util.UITexts;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.forms.editor.FormEditor;


public class ExecutablesPage extends ExecutablesTestSuitePage {

  public ExecutablesPage( final FormEditor editor, final IProject project ) {
    super( editor, project, UITexts.cabalEditor_executables );
  }

  @Override
  public String getMessage( final Messages m ) {
    switch(m) {
      case TITLE:
        return UITexts.cabalEditor_executables;
      case NEW:
        return UITexts.cabalEditor_newExecutableString;
      case BLANK_ERROR:
        return UITexts.cabalEditor_newExecutableBlankError;
      case ALREADY_EXISTS_ERROR:
        return UITexts.cabalEditor_newExecutableAlreadyExistsError;
    }
    return null;
  }

  @Override
  public PackageDescriptionStanza createNewStanza(final PackageDescription desc, final String name) {
    PackageDescriptionStanza stanza = desc.addStanza(
        CabalSyntax.SECTION_EXECUTABLE, name );
    stanza.setIndent( 2 );
    return stanza;
  }

  @Override
  public List<PackageDescriptionStanza> getStanzas(
      final PackageDescription description ) {
    return description.getExecutableStanzas();
  }

  @Override
  public ComponentType getComponentType() {
    return ComponentType.EXECUTABLE;
  }

}
