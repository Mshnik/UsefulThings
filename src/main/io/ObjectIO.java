package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ObjectIO {

  private ObjectIO(){}

  private static final String EXTENSION = ".ser";  //The default extension for serialized objects

  /**
   * Creates a serialized Object file for input Object for later reading and editing
   *
   * @param toWrite   - the object to write to memory.
   * @param filePathToWrite - the filePath to write with this object
   */
  public static void write(Object toWrite, String filePathToWrite) throws IOException {
    FileOutputStream fileOut = null;
    ObjectOutputStream out = null;
    try {
      fileOut = new FileOutputStream(filePathToWrite);
      out = new ObjectOutputStream(fileOut);
      out.writeObject(toWrite);

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      //Make sure resources are closed
      if (out != null)
        out.close();
      if (fileOut != null)
        fileOut.close();
    }
  }

  /**
   * Creates a serialized Object file for input Object for later reading and editing
   *
   * @param toWrite   - the object to write to memory.
   * @param directory - the location to write the object to
   * @param name      - the name to give to the serialized version of this object.
   *                  Must not contain the '.' or the '/' characters.
   */
  public static void write(Object toWrite, String directory, String name)
      throws IllegalArgumentException, IOException {

    if (name.contains(".") || name.contains("/"))
      throw new IllegalArgumentException("Input name contains a '.' or a '/'");

    write(toWrite, directory + "/" + name + EXTENSION);
  }

  @SuppressWarnings("rawtypes")
  /** Prompts the user to select a serialized object from memory to read.
   * @param objectClass - The class of the object that the user is expected to return
   * @param objectFileToLoad - The file containing the object to be loaded
   * @return - The selected object, loaded from memory
   * @throws RuntimeException - If the user selects the wrong class of object, or if null somehow results
   */
  public static Object read(Class objectClass, File objectFileToLoad) throws RuntimeException, IOException {
    Object g = null;
    FileInputStream fileIn = null;
    ObjectInputStream in = null;
    try {
      fileIn = new FileInputStream(objectFileToLoad);
      in = new ObjectInputStream(fileIn);

      g = in.readObject();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      //Make sure resources are closed
      if (fileIn != null)
        fileIn.close();
      if (in != null)
        in.close();
    }

    if (objectClass.isInstance(g))
      return g;

    throw new RuntimeException("Invalid Object Selection - found instance of " + (g == null ? "null " : g.getClass())
        + " required " + objectClass);
  }

  /** Prompts the user to select a serialized object from memory to read.
   * @param objectClass - The class of the object that the user is expected to return
   * @param directory - The location the user is prompted to select from, though they can navigate to other locations.
   * @return - The selected object, loaded from memory
   * @throws RuntimeException - If the user selects the wrong class of object.
   */
  public static Object chooseAndRead(Class objectClass, String directory) throws RuntimeException, IOException {
    JFileChooser chooser = new JFileChooser(directory);
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    FileNameExtensionFilter filter = new FileNameExtensionFilter(objectClass.getSimpleName() + " Objects", EXTENSION);
    chooser.setFileFilter(filter);
    int returnVal = chooser.showOpenDialog(null);

    Object g = null;
    FileInputStream fileIn = null;
    ObjectInputStream in = null;

    if (returnVal == JFileChooser.APPROVE_OPTION) {
      return read(objectClass, chooser.getSelectedFile());
    } else {
      return null;
    }
  }
}
