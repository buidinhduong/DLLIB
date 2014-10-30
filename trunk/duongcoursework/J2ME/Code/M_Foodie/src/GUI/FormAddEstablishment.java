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
public class FormAddEstablishment extends Form implements CommandListener {

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

    public FormAddEstablishment(String title, HomeManager parent) {
        super(title);
        this.parentMidlet = parent;
        //Init Items ,set language
        setTitle(parentMidlet.getLmanager().getKeyValue("common.frmAddEstablishmentTitle"));
        txtID = new TextField(parentMidlet.getLmanager().getKeyValue("frm.AddEstablishment.id"), "", 50, 0);
        txtName = new TextField(parentMidlet.getLmanager().getKeyValue("frm.AddEstablishment.name"), "", 50, 0);
        cmdAdd = new Command(parentMidlet.getLmanager().getKeyValue("common.add"), Command.SCREEN, 1);
        cmdBack = new Command(parentMidlet.getLmanager().getKeyValue("common.back"), Command.BACK, 0);
        choiceType = new ChoiceGroup(parentMidlet.getLmanager().getKeyValue("frm.AddEstablishment.types"), ChoiceGroup.POPUP, new String[]{"Cafe", "Bar", "Restaurant"}, null);
        txtFoodType = new TextField(parentMidlet.getLmanager().getKeyValue("frm.AddEstablishment.food"), "", 50, 0);
        txtLocation = new TextField(parentMidlet.getLmanager().getKeyValue("frm.AddEstablishment.location"), "", 50, 0);
        //Add to form

        this.append(txtID);
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
        String input = txtID.getString() + txtName.getString() + txtFoodType.getString() + txtLocation.getString();
        if (input.indexOf(";") != -1) {
            eror++;
            informInfo += "\n" + eror + ": " + parentMidlet.getLmanager().getKeyValue("frm.AddEstablishment.validator.containSpecialChar");
        }
        if (txtID.getString().equals("")) {
            eror++;
            informInfo += "\n" + eror + ": " + parentMidlet.getLmanager().getKeyValue("frm.AddEstablishment.validator.id");
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
            newEs = new Establishment();
            newEs.setId(this.txtID.getString());
            newEs.setName(this.txtName.getString());
            newEs.setType(choiceType.getSelectedIndex());
            newEs.setFoodType(this.txtFoodType.getString());
            newEs.setLocaltion(txtLocation.getString());

            alertView = new Alert(parentMidlet.getLmanager().getKeyValue("common.info"), "", null, AlertType.CONFIRMATION);
            if (cmdAConfirm == null) {
                cmdAConfirm = new Command(parentMidlet.getLmanager().getKeyValue("common.ok"), Command.OK, 0);
            }
            if (cmdAEdit == null) {
                cmdAEdit = new Command(parentMidlet.getLmanager().getKeyValue("common.edit"), Command.BACK, 0);
            }
            alertView.addCommand(cmdAConfirm);
            alertView.addCommand(cmdAEdit);
            alertView.setCommandListener(this);
            alertView.setTimeout(Alert.FOREVER);
            alertView.setString(newEs.showInformation());
            parentMidlet.display(alertView);
            return;
        }
        if (c == cmdAConfirm) {
            boolean ok = parentMidlet.getDbManager().addNewEstablishment(newEs);
            if (ok) {
                alertView.setString(parentMidlet.getLmanager().getKeyValue("common.success"));
                alertView.setType(AlertType.INFO);
                alertView.removeCommand(cmdAConfirm);
                alertView.removeCommand(cmdAEdit);
                cmdDone = new Command(parentMidlet.getLmanager().getKeyValue("common.done"), Command.OK, 0);
                alertView.addCommand(cmdDone);
            } else {
                alertView.setString(parentMidlet.getLmanager().getKeyValue("common.fail"));
            }
            return;
        }
        if (c == cmdAEdit) {
            parentMidlet.display(this);
            return;
        }
        if (c == cmdDone) {
            parentMidlet.display(parentMidlet.form);
        }
    }

}
