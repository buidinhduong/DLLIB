/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Controller.DatabaseManager;
import Controller.LanguageManager;
import CustomeControl.AboutAuthor;
import CustomeControl.Logo;
import Entities.Establishment;
import Entities.Review;
import java.io.IOException;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.*;

/**
 * @author SONY
 */
public class HomeManager extends MIDlet implements CommandListener, ItemStateListener {

    TextField tf = new TextField("dsa", "", 20, 0);
    public Form form;
    //command
    Command cmdAdd, cmdList, cmdExit, cmdAboutAuthor;
    FormListEstablishment frm_List;
    FormAddEstablishment frm_Add;
    FormAddReview frm_AddDetail;
    FormViewDetailEstablishment frm_ViewDetail;
    FormListReview frm_reviews;
    FormReviewDetail frm_reviewDetail;
    FormUpdatedEstablishment frm_update;
    AboutAuthor author;
    private LanguageManager lmanager;
    private DatabaseManager dbManager;
    private Establishment[] esData;
    private Review[] reviewData;
    private ChoiceGroup languageChoice;
    Logo logo;
    String selectedLanguage = "English";//default

    public void startApp() {
        try {
            //init
            lmanager = new LanguageManager(selectedLanguage);
            this.cmdExit = new Command(getLmanager().getKeyValue("common.exit"), Command.EXIT, 0);
            this.cmdAdd = new Command(getLmanager().getKeyValue("home.addEstablishment"), Command.SCREEN, 1);
            this.cmdList = new Command(getLmanager().getKeyValue("home.list"), Command.SCREEN, 1);
            this.cmdAboutAuthor = new Command("Author", Command.SCREEN, 1);
            form = new Form("              M-Foodie");
            //add componet
            logo = new Logo();
            form.append(logo);
            author = new AboutAuthor(this);
            //
            Image[] langImage = new Image[2];
            langImage[0] = Image.createImage(getClass().getResourceAsStream("en.png"));
            langImage[1] = Image.createImage(getClass().getResourceAsStream("vn.png"));
            languageChoice = new ChoiceGroup("         ", ChoiceGroup.POPUP, new String[]{"English", "Vietnamese"}, langImage);
            form.append(languageChoice);
            form.addCommand(cmdAdd);
            form.addCommand(cmdExit);
            form.addCommand(cmdList);
            form.addCommand(cmdAboutAuthor);
            frm_List = new FormListEstablishment("", this);
            frm_List.setItemStateListener(this);

            frm_Add = new FormAddEstablishment(getLmanager().getKeyValue("home.addEstablishment"), this);

            frm_AddDetail = new FormAddReview(this);
            frm_AddDetail.setItemStateListener(this);
            frm_ViewDetail = new FormViewDetailEstablishment(this);
            frm_update=new FormUpdatedEstablishment("Update", this);
            frm_reviews = new FormListReview(this);
            frm_reviewDetail = new FormReviewDetail(this);
            form.setCommandListener(this);
            form.setItemStateListener(this);

            Display.getDisplay(this).setCurrent(form);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void itemStateChanged(Item item) {
        System.out.println("State change");
        if (item == frm_List.getTxtSearch()) {
            frm_List.filerDataByName(frm_List.getTxtSearch().getString());
            frm_List.refreshListView();
        }
        if (item == frm_List.getChoiceFilter()) {
            frm_List.filerDataByType(frm_List.getChoiceFilter().getSelectedIndex());
        }
        //track state change for form Add Detail
        if (item == frm_AddDetail.gServiceRate || item == frm_AddDetail.gAtmosphere || item == frm_AddDetail.gFood || item == frm_AddDetail.gOverallRate) {
            frm_AddDetail.calculateOverallRating();
        }
        if (item == languageChoice) {
            selectedLanguage = languageChoice.getString(languageChoice.getSelectedIndex());
            startApp();
        }
    }

    public Review getBestReview(Establishment es) {
        Review best = null;
        int start = 0;
        for (int i = 0; i < reviewData.length; i++) {
            if (reviewData[i].getEsID().equalsIgnoreCase(es.getId())) {
                best = reviewData[i];
                start = i;
                break;
            }
        }
        if (best == null) {
            return null;
        }
        for (int i = start; i < reviewData.length; i++) {
            if (reviewData[i].getEsID().equalsIgnoreCase(es.getId()) && reviewData[i].getOverallRating() > best.getOverallRating()) {
                if (reviewData[i].getOverallRating() > best.getOverallRating()) {
                    best = reviewData[i];
                }
            }
        }
        return best;
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdList) {
            loadEstablishment();
            loadReviews();
            frm_List.bindDataToList(esData);
            Display.getDisplay(this).setCurrent(frm_List);
        }
        if (c == cmdAdd) {
            loadEstablishment();
            Display.getDisplay(this).setCurrent(frm_Add);
        }
        if (c == cmdExit) {
            notifyDestroyed();
        }
        if (c == cmdAboutAuthor) {
            author.setStart(true);
            Display.getDisplay(this).setCurrent(author);
        }
    }

    /**
     * @return the lManager
     */
    /**
     * @return the dbManager
     */
    public DatabaseManager getDbManager() {
        return dbManager;
    }

    /**
     * @return the lmanager
     */
    public LanguageManager getLmanager() {
        return lmanager;
    }

    public void doAction(Displayable d) {
        if (d == this.frm_List) {
        }

    }

    public boolean isDuplicateID(String id) {
        for (int i = 0; i < this.getEsData().length; i++) {
            if (getEsData()[i].getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public void display(Displayable screen) {

        Display.getDisplay(this).setCurrent(screen);
    }

    public void loadEstablishment() {
        if (dbManager == null) {
            dbManager = new DatabaseManager();
        }
        esData = this.getDbManager().getAllEstablishments();
    }

    public void loadReviews() {
        if (dbManager == null) {
            dbManager = new DatabaseManager();
        }
        reviewData = getDbManager().getAllReviews();
    }

    /**
     * @return the esData
     */
    public Establishment[] getEsData() {
        return esData;
    }

    /**
     * @return the reviewData
     */
    public Review[] getReviewData() {
        return reviewData;
    }
}
