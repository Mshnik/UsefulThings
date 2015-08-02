package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ObjectIO{

	private static final String EXTENSION = ".ser";	//The default extension for serialized objects

	/**Creates a serialized Object file for input Object for later reading and editing
	 * @param toWrite - the object to write to memory.
	 * @param directory - the location to write the object to
	 * @param name - the name to give to the serialized version of this object. 
	 * 			Must not contain the '.' or the '/' characters. */
	public static void write(Object toWrite, String directory, String name) 
			throws IllegalArgumentException, IOException{

		if(name.contains(".") || name.contains("/"))
			throw new IllegalArgumentException("Input name contains a '.' or a '/'");

		FileOutputStream fileOut = null;
		ObjectOutputStream out = null;
		try{
			fileOut = new FileOutputStream(directory + "/" + name + EXTENSION);
			out = new ObjectOutputStream(fileOut);
			out.writeObject(toWrite);

		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			//Make sure resources are closed
			if(out != null)
				out.close();
			if(fileOut != null)
				fileOut.close();
		}
	}

	@SuppressWarnings("rawtypes")
	/** Prompts the user to select a serialized object from memory to load.
	 * @param objectClass - The class of the object that the user is expected to return
	 * @param directory - The location the user is prompted to select from, though they can navigate to other locations.
	 * @return - The selected object, loaded from memory
	 * @throws RuntimeException - If the user selects the wrong class of object.
	 */
	public static Object load(Class objectClass , String directory) throws RuntimeException, IOException{
		JFileChooser chooser = new JFileChooser(directory);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(objectClass.getSimpleName() + " Objects", EXTENSION);
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(null);

		Object g = null;
		FileInputStream fileIn = null;
		ObjectInputStream in = null;

		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();
			try{
				fileIn = new FileInputStream(f);
				in = new ObjectInputStream(fileIn);

				g = in.readObject();

			} catch (Exception e) {
				e.printStackTrace();
			} 
			finally{
				//Make sure resources are closed
				if(fileIn != null)
					fileIn.close();
				if(in != null)
					in.close();
			}

		}

		if(objectClass.isInstance(g))
			return g;

		throw new RuntimeException("Invalid Object Selection - found instance of " + g.getClass() + " required " + objectClass);
	}

}
