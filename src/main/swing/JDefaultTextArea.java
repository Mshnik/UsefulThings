package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class JDefaultTextArea extends JTextArea {

  /**
	 * 
	 */
	private static final long serialVersionUID = -8906055045350304885L;
  private String defaultText;

  public JDefaultTextArea() {
    this("");
  }

  public JDefaultTextArea(String defaultText) {
    super();
    setDefaultText(defaultText);
    addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if(defaultText.equals(getTrueText())) {
          setForeground(Color.BLACK);
          setTrueText("");
        }
      }

      @Override
      public void focusLost(FocusEvent e) {
        if(getTrueText() == null || getTrueText().equals("")) {
          setForeground(Color.GRAY);
          setTrueText(defaultText);
        }
      }
    });

    setForeground(Color.GRAY);
    setTrueText(defaultText);
  }

  public String getDefaultText() {
    return defaultText;
  }

  public void setDefaultText(String defaultText) {
    if(getTrueText() == null || getTrueText().equals(this.defaultText)) {
      setForeground(Color.GRAY);
      setTrueText(defaultText);
    }
    if(defaultText == null) {
      this.defaultText = "";
    } else {
      this.defaultText = defaultText;
    }
  }

  private String getTrueText() {
    return super.getText();
  }

  private void setTrueText(String t) {
    super.setText(t);
  }

  @Override
  public void setText(String t) {
    if(t == null || t.equals("")) {
      super.setText(defaultText);
      setForeground(Color.GRAY);
    } else {
      super.setText(t);
      setForeground(Color.BLACK);
    }
  }

  @Override
  public String getText() {
    if(defaultText.equals(super.getText())) {
      return "";
    } else {
      return super.getText();
    }
  }
}
