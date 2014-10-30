/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Entities.Establishment;
import Entities.Review;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;

/**
 *
 * @author SONY
 */
public class FormListReview extends Form implements CommandListener {

    HomeManager parent;
    Command cmdBack;
    Command cmdViewReviewDetail;
    Command cmdDelete;
    ChoiceGroup rvList;
    Vector data;
    Establishment myEsta;

    public FormListReview(HomeManager parent) {
        super("");
        this.parent = parent;
        setTitle(this.parent.getLmanager().getKeyValue("frm.ListEstablishment.viewAllReviews"));

        //init components
        cmdBack = new Command(this.parent.getLmanager().getKeyValue("common.back"), Command.BACK, 0);
        cmdDelete = new Command(this.parent.getLmanager().getKeyValue("common.delete"), Command.SCREEN, 1);
        cmdViewReviewDetail = new Command(parent.getLmanager().getKeyValue("common.detail"), Command.SCREEN, 1);
        rvList = new ChoiceGroup(parent.getLmanager().getKeyValue("frm.ListEstablishment.viewAllReviews"), ChoiceGroup.EXCLUSIVE);

        this.addCommand(cmdBack);
        this.addCommand(cmdViewReviewDetail);
        this.addCommand(cmdDelete);
        this.append(rvList);

        this.setCommandListener(this);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdBack) {
            parent.display(parent.frm_List);
        }
        if (c == cmdViewReviewDetail) {
            if (rvList.getSelectedIndex() == -1) {
                Alert aler = new Alert(parent.getLmanager().getKeyValue("common.info"), "", null, AlertType.WARNING);
                aler.setString(parent.getLmanager().getKeyValue("frm.ListEstablishment.noReview"));
                parent.display(aler);
                return;
            }
            parent.display(parent.frm_reviewDetail);
            parent.frm_reviewDetail.showInfo((Review) this.data.elementAt(rvList.getSelectedIndex()));
        }
        if (c == cmdDelete) {
            if (rvList.getSelectedIndex() == -1) {
                Alert aler = new Alert(parent.getLmanager().getKeyValue("common.info"), "", null, AlertType.WARNING);
                aler.setString(parent.getLmanager().getKeyValue("frm.ListEstablishment.noReview"));
                aler.setTimeout(Alert.FOREVER);
                parent.display(aler);
                return;
            }
            boolean ok = parent.getDbManager().deleteReview((Review) this.data.elementAt(rvList.getSelectedIndex()));
            if (ok) {
                Alert aler = new Alert(parent.getLmanager().getKeyValue("common.info"), "", null, AlertType.INFO);
                aler.setString(parent.getLmanager().getKeyValue("common.success"));
                aler.setTimeout(Alert.FOREVER);
                parent.display(aler);
                parent.loadReviews();
                this.filterReviewOfEstablishment(parent.getReviewData());
                this.refreshListView();
                return;
            } else {
                Alert aler = new Alert(parent.getLmanager().getKeyValue("common.info"), "", null, AlertType.WARNING);
                aler.setString(parent.getLmanager().getKeyValue("common.fail"));
                aler.setTimeout(Alert.FOREVER);
                parent.display(aler);
                return;
            }
        }
    }

    public void filterReviewOfEstablishment(Review[] dataRV) {
        if (this.data == null) {
            this.data = new Vector();
        }
        data.removeAllElements();
        for (int i = 0; i < dataRV.length; i++) {
            if (dataRV[i].getEsID().equalsIgnoreCase(myEsta.getId())) {
                this.data.addElement(dataRV[i]);
            }
        }
    }

    public void refreshListView() {
        rvList.deleteAll();
        try {
            for (int i = 0; i < data.size(); i++) {
                Image esImg = Image.createImage(getClass().getResourceAsStream("esImage.png"));
                rvList.append(((Review) data.elementAt(i)).getDate() + "", esImg);
            }
        } catch (IOException ex) {
        }
    }
}
