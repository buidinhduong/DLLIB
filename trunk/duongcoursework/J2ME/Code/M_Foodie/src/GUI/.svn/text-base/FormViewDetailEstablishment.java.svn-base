/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Entities.Establishment;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;

/**
 *
 * @author SONY
 */
public class FormViewDetailEstablishment extends Form implements CommandListener {

    HomeManager parent;
    Command cmdBack;
    StringItem infor;
    private Establishment es;

    public FormViewDetailEstablishment(HomeManager parent) {
        super("");
        this.parent = parent;
        setTitle(this.parent.getLmanager().getKeyValue("frm.ListEstablishment.viewDetail"));
        infor = new StringItem(this.parent.getLmanager().getKeyValue("frm.ListEstablishment.viewDetail") + "\n", "");
        infor.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
        cmdBack = new Command(this.parent.getLmanager().getKeyValue("common.back"), Command.BACK, 0);
        this.addCommand(cmdBack);
        this.append(infor);
        this.setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdBack) {
            parent.display(parent.frm_List);
        }
    }

    public void showInfo(Establishment e) {
        this.es = e;
        infor.setText(this.es.showInformation());
    }

    /**
     * @return the es
     */
    public Establishment getEs() {
        return es;
    }

    /**
     * @param es the es to set
     */
    public void setEs(Establishment es) {
        this.es = es;
    }
}
