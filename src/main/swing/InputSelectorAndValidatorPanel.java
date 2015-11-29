package swing;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.function.Function;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class InputSelectorAndValidatorPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4218180059814548156L;

  public static final int MAX_HEIGHT = 35;
  public static final int TEXT_FIELD_HEIGHT = 30;
  public static final int HELP_BUTTON_WIDTH = 40;
  public static final int TEXT_FIELD_MIN_WIDTH = 300;

	private JButton chooseButton;
	private JButton helpButton;
	private FileFilter fileFilter;
	private File defaultDirectory;
	
	private String errorText = "ERROR IN FILE UPLOAD";
	private String helpText;
	private JDefaultTextField textField;

	private Function<File, Boolean> singleArgCallback;

	private File uploadedFile;

  public InputSelectorAndValidatorPanel() {
	  this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		chooseButton = new JButton("");

	  chooseButton.addActionListener((e) -> chooseFile());

		helpButton = new JButton("?");

	  helpButton.addActionListener((e) -> {
			JOptionPane frame = new JOptionPane();
			JOptionPane.showMessageDialog(frame, helpText, chooseButton.getText() + " Help", JOptionPane.INFORMATION_MESSAGE);
	  });

		textField = new JDefaultTextField();
		textField.setEditable(false);
    textField.setMinimumSize(new Dimension(TEXT_FIELD_MIN_WIDTH, TEXT_FIELD_HEIGHT));
    textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, TEXT_FIELD_HEIGHT));
		textField.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				chooseFile();
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});

    helpButton.setPreferredSize(new Dimension(HELP_BUTTON_WIDTH, TEXT_FIELD_HEIGHT));
    helpButton.setMinimumSize(helpButton.getPreferredSize());
    helpButton.setMaximumSize(helpButton.getPreferredSize());

    add(chooseButton);
	  add(textField);
	  add(helpButton);
  }

	private void chooseFile() {
		JFileChooser fileChooser = new JFileChooser(defaultDirectory != null ? defaultDirectory.getAbsolutePath() : "");
		fileChooser.setFileFilter(fileFilter);
		fileChooser.setDialogTitle(chooseButton.getText());
		int returnValue = fileChooser.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			selectFile(fileChooser.getSelectedFile());
		} else if (returnValue == JFileChooser.ERROR_OPTION) {
			textField.setText(errorText);
		} else {
			textField.setText("");
		}
	}


	public InputSelectorAndValidatorPanel withFileFilter(FileFilter f) {
		this.fileFilter = f;
		this.textField.setDefaultText(" (" + f.getDescription() + ")");
		return this;
	}

	public InputSelectorAndValidatorPanel withCallback(Function<File, Boolean> callback) {
		this.singleArgCallback = callback;
		return this;
	}

	public InputSelectorAndValidatorPanel withButtonText(String buttonText) {
		this.chooseButton.setText(buttonText);
		return this;
	}

	public InputSelectorAndValidatorPanel withErrorText(String errorText) {
		this.errorText = errorText;
		return this;
	}

	public InputSelectorAndValidatorPanel withHelpText(String helpText) {
		this.helpText = helpText;
		return this;
	}

	public InputSelectorAndValidatorPanel withButtonWidth(int buttonWidth) {
		chooseButton.setPreferredSize(new Dimension(buttonWidth, TEXT_FIELD_HEIGHT));
		chooseButton.setMinimumSize(chooseButton.getPreferredSize());
		return this;
	}

	public InputSelectorAndValidatorPanel withExtraComponent(Component c) {

		if(c instanceof JTextField) {
			JTextField t = (JTextField)c;
			t.setMinimumSize(new Dimension(TEXT_FIELD_MIN_WIDTH, TEXT_FIELD_HEIGHT));
			t.setMaximumSize(new Dimension(Integer.MAX_VALUE, TEXT_FIELD_HEIGHT));
		}

		remove(helpButton);
		add(c);
		add(helpButton);
		return this;
	}

	public void setDefaultDirectory(File dir) {
		if(dir.isDirectory()) {
			this.defaultDirectory = dir;
		} else {
			this.defaultDirectory = dir.getParentFile();
		}
	}

	public JButton getChooseButton() {
		return chooseButton;
	}

	public boolean selectFile(File f) {
		if(fileFilter != null && ! fileFilter.accept(f)) {
			return false;
		}
		uploadedFile = f;
		if(singleArgCallback.apply(uploadedFile)) {
			textField.setText(uploadedFile.getAbsolutePath());
			return true;
		} else {
			textField.setText(errorText);
			return false;
		}
	}

	public File getSelectedFile() {
		return uploadedFile;
	}

  public Dimension getMinimumSize() {
    return new Dimension(chooseButton.getMinimumSize().width +
                         textField.getMinimumSize().width +
                         helpButton.getMinimumSize().width, TEXT_FIELD_HEIGHT);
  }

  public Dimension getPreferredSize() {
    return new Dimension(chooseButton.getPreferredSize().width +
        textField.getPreferredSize().width +
        helpButton.getPreferredSize().width, MAX_HEIGHT);
  }

	public Dimension getMaximumSize() {
    return new Dimension(Integer.MAX_VALUE, MAX_HEIGHT);
  }
}
