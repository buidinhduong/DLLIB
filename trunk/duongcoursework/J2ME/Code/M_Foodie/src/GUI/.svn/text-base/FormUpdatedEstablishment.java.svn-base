/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Entities.Establishment;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

/**
 *
 * @author SONY
 */
public class FormUpdatedEstablishment extends Form implements CommandListener {

    String state = "";
    private HomeManager parentMidlet;
    private TextField txtID;
    private TextField txtName;
    private Command cmdAdd;
    private Command cmdBack;
    private Command cmdDone;
    private Alert alertView;//show alert
    private Command cmdAConfirm;
    private Command cmdAEdit;
    private ChoiceGroup choiceType;//type of establishment
    private TextField txtFoodType;
    private TextField txtLocation;
    private Establishment newEs;
    private Establishment oldEs;

    public FormUpdatedEstablishment(String title, HomeManager parent) {
        super(title);
        this.parentMidlet = parent;
        //Init Items ,set language
        setTitle(parentMidlet.getLmanager().getKeyValue("Update Establishment"));
        txtID = new TextField(parentMidlet.getLmanager().getKeyValue("frm.AddEstablishment.id"), "", 50, 0);
        txtName = new TextField(parentMidlet.getLmanager().getKeyValue("frm.AddEstablishment.name"), "", 50, 0);
        cmdAdd = new Command("add", Command.SCREEN, 1);
        cmdBack = new Command(parentMidlet.getLmanager().getKeyValue("common.back"), Command.BACK, 0);
        choiceType = new ChoiceGroup(parentMidlet.getLmanager().getKeyValue("frm.AddEstablishment.types"), ChoiceGroup.POPUP, new String[]{"Cafe", "Bar", "Restaurant"}, null);
        txtFoodType = new TextField(parentMidlet.getLmanager().getKeyValue("frm.AddEstablishment.food"), "", 50, 0);
        txtLocation = new TextField(parentMidlet.getLmanager().getKeyValue("frm.AddEstablishment.location"), "", 50, 0);
        //Add to form

        // this.append(txtID);
        this.append(txtName);
        this.append(choiceType);
        this.append(txtFoodType);
        this.append(txtLocation);
        this.addCommand(cmdAdd);
        this.addCommand(cmdBack);

        this.setCommandListener(this);
    }

    public boolean checkInputData() {
        //check input data
        String informInfo = "";
        int eror = 0;
        String input = txtName.getString() + txtFoodType.getString() + txtLocation.getString();
        if (input.indexOf(";") != -1) {
            eror++;
            informInfo += "\n" + eror + ": " + parentMidlet.getLmanager().getKeyValue("frm.AddEstablishment.validator.containSpecialChar");
        }

        if (txtName.getString().equals("")) {
            eror++;
            informInfo += "\n" + eror + ": " + parentMidlet.getLmanager().getKeyValue("frm.AddEstablishment.validator.name");
        }
        if (txtFoodType.getString().equals("")) {
            eror++;
            informInfo += "\n" + eror + ": " + parentMidlet.getLmanager().getKeyValue("frm.AddEstablishment.validator.foodType");
        }
        if (txtLocation.getString().equals("")) {
            eror++;
            informInfo += "\n" + eror + ": " + parentMidlet.getLmanager().getKeyValue("frm.AddEstablishment.validator.location");
        }
        if (parentMidlet.isDuplicateID(txtID.getString())) {
            eror++;
            informInfo += "\n" + eror + ": " + parentMidlet.getLmanager().getKeyValue("frm.AddEstablishment.validator.dupicateID");
        }
        if (informInfo.equals("") == false) {
            alertView = new Alert(parentMidlet.getLmanager().getKeyValue("common.info"), informInfo, null, AlertType.WARNING);
            alertView.setTimeout(Alert.FOREVER);
            parentMidlet.display(alertView);
            return false;
        }
        return true;
    }

    public void bindOldInfo() {
        this.txtFoodType.setString(this.getOldEs().getFoodType());
        this.txtLocation.setString(this.getOldEs().getLocaltion());
        this.txtName.setString(getOldEs().getName());
        this.choiceType.setSelectedIndex(getOldEs().getType(), true);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == this.cmdBack) {
            //inform to parent form, this form is finished, back to previous form
            this.parentMidlet.display(parentMidlet.form);
            return;
        }
        if (c == this.cmdAdd) {
            if (checkInputData() == false) {
                return;
            }

            oldEs.setName(this.txtName.getString());
            oldEs.setType(choiceType.getSelectedIndex());
            oldEs.setFoodType(this.txtFoodType.getString());
            oldEs.setLocaltion(txtLocation.getString());

            boolean ok = parentMidlet.getDbManager().updateEstablishment(oldEs);
           Alert  alert = new Alert(parentMidlet.getLmanager().getKeyValue("common.info"),"common.success", null, AlertType.INFO);
            alert.setTimeout(Alert.FOREVER);
            parentMidlet.display(alert);
        }

        if (c == cmdAEdit) {
            parentMidlet.display(this);
            return;
        }
        if (c == cmdDone) {
            parentMidlet.display(parentMidlet.form);
        }
    }

    /**
     * @return the oldEs
     */
    public Establishment getOldEs() {
        return oldEs;
    }

    /**
     * @param oldEs the oldEs to set
     */
    public void setOldEs(Establishment oldEs) {
        this.oldEs = oldEs;
        bindOldInfo();
    }
}
