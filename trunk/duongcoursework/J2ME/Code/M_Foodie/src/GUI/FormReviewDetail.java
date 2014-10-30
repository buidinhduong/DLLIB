/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Entities.Review;
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
public class FormReviewDetail extends Form implements CommandListener {
    
    HomeManager parent;
    Command cmdBack;
    StringItem infor;
    private Review reviewInfo;
    
    public FormReviewDetail(HomeManager parent) {
        super("");
        this.parent = parent;
        setTitle(this.parent.getLmanager().getKeyValue("frm.ReviewDetail.Title"));
        infor = new StringItem(this.parent.getLmanager().getKeyValue("frm.ReviewDetail.Title"), "");
        infor.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
        cmdBack = new Command(this.parent.getLmanager().getKeyValue("common.back"), Command.BACK, 0);
        this.addCommand(cmdBack);
        this.append(infor);
        this.setCommandListener(this);
    }
    
    public void commandAction(Command c, Displayable d) {
        if (c == cmdBack) {
            parent.display(parent.frm_reviews);
        }
    }
    
    public void showInfo(Review e) {
        this.reviewInfo = e;
//        String text = parent.getLmanager().getKeyValue("frm.AddReview.label.dateOfReview") + ":" + reviewInfo.getDate()
//                + "\n" + parent.getLmanager().getKeyValue("frm.AddReview.label.MealType") + ":" + reviewInfo.getTypeOfMeal()
//                + "\n" + parent.getLmanager().getKeyValue("frm.AddReview.label.CostOfMeal") + ":" + reviewInfo.getCost()
//                + "\n"
//                + parent.getLmanager().getKeyValue("frm.AddReview.label.OverallRate") + ":" + reviewInfo.getOverallRating() + "/5"
//                + "\n"
//                + parent.getLmanager().getKeyValue("frm.AddReview.label.ServiceRate") + ":" + reviewInfo.getServiceRating() + "/5"
//                + "\n"
//                + parent.getLmanager().getKeyValue("frm.AddReview.label.Atmosphere") + ":" + reviewInfo.getAtmosphereRating() + "/5"
//               + "\n"
//                + parent.getLmanager().getKeyValue("frm.AddReview.label.Food") + ":" + reviewInfo.getAtmosphereRating() + "/5"
//               + "\n" 
//                + parent.getLmanager().getKeyValue("frm.AddReview.label.Comment") + ":\t" + reviewInfo.getComment() + "/5";
        String text = "Date" + ":" + reviewInfo.getDate()
                + "\n" + "Meal Type" + ":" + reviewInfo.getTypeOfMeal()
                + "\n" + "Cost" + ":" + reviewInfo.getCost()
                + "\n"
                + "Overall Rating" + ":" + reviewInfo.getOverallRating() + "/5"
                + "\n"
                + "Service Rating" + ":" + reviewInfo.getServiceRating() + "/5"
                + "\n"
                + "Atmosphere Rating" + ":" + reviewInfo.getAtmosphereRating() + "/5"
                + "\n"
                + "Food Rating" + ":" + reviewInfo.getFoodRating() + "/5"
                + "\n"
                + "Comment" + ":\t" + reviewInfo.getComment();
        infor.setText(text);
    }
}
