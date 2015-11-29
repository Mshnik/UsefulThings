package swing;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.function.Supplier;

public class ExtensionFileFilter extends FileFilter {

  private Supplier<String[]> extensions;

  public ExtensionFileFilter(Supplier<String[]> extensions) {
    this.extensions = extensions;
  }

  public ExtensionFileFilter(String... extensions) {
    this.extensions = () -> extensions;
  }

  @Override
  public boolean accept(File f) {
    if (f == null) {
      return false;
    }
    for (String suffix : extensions.get()) {
      if (f.getName().endsWith(suffix)) {
        return true;
      }
    }
    return false;
  }

  public String getDescription() {
    String s = "";
    for (String suffix : extensions.get()) {
      s += "*" + suffix + ", ";
    }
    return s.substring(0, s.length() - 2);
  }
}
