/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Entities.Establishment;
import Entities.Review;
import java.util.Date;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.TextField;

/**
 *
 * @author SONY
 */
public class FormAddReview extends Form implements CommandListener {

    private Establishment es;
    private Command cmdBack;
    private Command cmdAdd;
    private HomeManager parentMidlet;
    private DateField dateOfReview;
    public TextField txtMealType, txtCostOfMeal, txtComment;
    public Gauge gOverallRate, gServiceRate, gAtmosphere, gFood;
    public Alert alertView;

    public FormAddReview(HomeManager parent) {
        super("Add Details");
        parentMidlet = parent;
        setTitle(parentMidlet.getLmanager().getKeyValue("frm.ListEstablishment.addDetail"));
        cmdBack = new Command(parentMidlet.getLmanager().getKeyValue("common.back"), Command.SCREEN, 0);
        cmdAdd = new Command(parentMidlet.getLmanager().getKeyValue("common.add"), Command.SCREEN, 1);
        this.addCommand(cmdBack);
        this.addCommand(cmdAdd);
        //init component
        dateOfReview = new DateField(parentMidlet.getLmanager().getKeyValue("frm.AddReview.label.dateOfReview"), DateField.DATE);
        dateOfReview.setDate(new Date());
        txtMealType = new TextField(parentMidlet.getLmanager().getKeyValue("frm.AddReview.label.MealType"), "", 50, 0);
        txtCostOfMeal = new TextField(parentMidlet.getLmanager().getKeyValue("frm.AddReview.label.CostOfMeal"), "0", 50, 0);

        int maxValue = 5;
        int initialValue = 0;
        gOverallRate = new Gauge(parentMidlet.getLmanager().getKeyValue("frm.AddReview.label.OverallRate"), true, maxValue, initialValue);
        gServiceRate = new Gauge(parentMidlet.getLmanager().getKeyValue("frm.AddReview.label.ServiceRate"), true, maxValue, initialValue);
        gAtmosphere = new Gauge(parentMidlet.getLmanager().getKeyValue("frm.AddReview.label.Atmosphere"), true, maxValue, initialValue);
        gFood = new Gauge(parentMidlet.getLmanager().getKeyValue("frm.AddReview.label.Food"), true, maxValue, initialValue);
        txtComment = new TextField(parentMidlet.getLmanager().getKeyValue("frm.AddReview.label.Comment"), "", 50, 0);
        //append to form
        this.append(dateOfReview);
        this.append(txtMealType);
        this.append(txtCostOfMeal);
        this.append(txtComment);
        this.append(gOverallRate);
        this.append(gServiceRate);
        this.append(gAtmosphere);
        this.append(gFood);

        this.setCommandListener(this);
    }

    public int calculateOverallRating() {
        int everage = (this.gAtmosphere.getValue() + this.gServiceRate.getValue() + gFood.getValue()) / 3;
        this.gOverallRate.setValue(everage);
        return everage;
    }

    public boolean checkInput() {
        int error = 0;
        String erorInfor = "";
        String input = txtMealType.getString() + txtCostOfMeal.getString() + txtComment.getString();
        if (input.indexOf(";") != -1) {
            error++;
            erorInfor += "\n" + error + ": " + parentMidlet.getLmanager().getKeyValue("frm.AddEstablishment.validator.containSpecialChar");
        }
        if (txtMealType.getString().equals("")) {
            //if need
        }
        if (txtCostOfMeal.getString().equals("")) {
            txtCostOfMeal.setString("0");
        }
        if (txtCostOfMeal.getString().length() > 0) {
            //implement if need
            try {
                Integer.valueOf(txtCostOfMeal.getString());
            } catch (Exception e) {
                error++;
                erorInfor += "\n" + error + ": " + parentMidlet.getLmanager().getKeyValue("frm.AddReview.validator.cost");
            }

        }
        if (txtComment.getString().equals("")) {
            //implement of need
        }
        if (error > 0) {
            alertView = new Alert(parentMidlet.getLmanager().getKeyValue("common.info"), erorInfor, null, AlertType.WARNING);
            alertView.setTimeout(10000);
            alertView.setString(erorInfor);
            parentMidlet.display(alertView);
            return false;
        }
        return true;
    }

    public int autogenerateReviewID() {
        int id = 0;
        for (int i = 0; i < parentMidlet.getReviewData().length; i++) {
            //find max ID
            if (parentMidlet.getReviewData()[i].getReviewID() > i) {
                id = parentMidlet.getReviewData()[i].getReviewID();
            }
        }
        //Max ID + 1 is the correct ID
        id++;
        return id;
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdBack) {
            parentMidlet.display(parentMidlet.frm_List);
        }
        if (c == cmdAdd) {
            //check input
            if (checkInput() == false) {
                return;
            }
            //fill data to object
            Review newR = new Review();
            newR.setDate(Review.fomartDateToString(dateOfReview.getDate()));
           
            newR.setEsID(getEs().getId());
            newR.setReviewID(autogenerateReviewID());
            newR.setTypeOfMeal(txtMealType.getString());
            newR.setComment(txtComment.getString());
            newR.setCost(Integer.valueOf(txtCostOfMeal.getString()).intValue());
            newR.setFoodRating(gFood.getValue());
            newR.setServiceRating(gServiceRate.getValue());
            newR.setAtmosphereRating(gAtmosphere.getValue());
            newR.setOverallRating(calculateOverallRating());
            //save to database
            boolean ok = parentMidlet.getDbManager().addReview(newR);
            if (ok) {
                if (alertView == null) {
                    alertView = new Alert(parentMidlet.getLmanager().getKeyValue("common.info"));
                }
                alertView.setString(parentMidlet.getLmanager().getKeyValue("common.success"));
                alertView.setType(AlertType.INFO);
                parentMidlet.display(alertView);
                parentMidlet.loadReviews();
            } else {
                alertView.setString(parentMidlet.getLmanager().getKeyValue("common.fail"));
                alertView.setType(AlertType.WARNING);
                parentMidlet.display(alertView);
            }
        }
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
    /**
     * @return the esID
     */
}
