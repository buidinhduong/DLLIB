/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Entities.Establishment;
import Entities.Review;
import java.io.IOException;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Image;

/**
 *
 * @author SONY
 */
public class FormListEstablishment extends Form implements CommandListener {

    private HomeManager parentMidlet;
    private TextField txtSearch;
    private ChoiceGroup choiceFilter;
    private Command cmdAddDetail;
    private Command cmdViewDetail;
    private Command cmdHome;
    private Command cmdSortAphabel;
    private Command cmdViewReviews;
    private  Command cmdupdateEstablishment;
    private Command cmdBestReview;
    private ChoiceGroup esList;
    private Vector displayedEstablishment;

    public FormListEstablishment(String title, HomeManager parent) {
        super("");
        this.parentMidlet = parent;
        setTitle(parentMidlet.getLmanager().getKeyValue("common.frmListEstablishmentTitle"));
        //init components
        txtSearch = new TextField((parentMidlet.getLmanager().getKeyValue("common.search")), "", 50, 0);

        choiceFilter = new ChoiceGroup((parentMidlet.getLmanager().getKeyValue("common.filter")), ChoiceGroup.POPUP);
        choiceFilter.append("All", null);
        choiceFilter.append("Cafe", null);
        choiceFilter.append("Bar", null);
        choiceFilter.append("Restaurant", null);
        esList = new ChoiceGroup("List", ChoiceGroup.EXCLUSIVE);
        //set language value will be displayed
        cmdHome = new Command(parentMidlet.getLmanager().getKeyValue("common.back"), Command.SCREEN, 0);
        cmdViewDetail = new Command(parentMidlet.getLmanager().getKeyValue("frm.ListEstablishment.viewDetail"), Command.SCREEN, 1);
        cmdSortAphabel = new Command(parentMidlet.getLmanager().getKeyValue("frm.ListEstablishment.sortAnphabel"), Command.SCREEN, 1);
        cmdAddDetail = new Command(parentMidlet.getLmanager().getKeyValue("frm.ListEstablishment.addDetail"), Command.SCREEN, 1);
        cmdViewReviews = new Command(parentMidlet.getLmanager().getKeyValue("frm.ListEstablishment.viewAllReviews"), Command.SCREEN, 1);
        cmdBestReview = new Command(parentMidlet.getLmanager().getKeyValue("frm.ListEstablishment.bestReview"), Command.SCREEN, 1);
        cmdupdateEstablishment=new Command("update", Command.SCREEN, 1) ;
        //add componet
        this.append(txtSearch);
        this.append(choiceFilter);
        this.append(esList);
        this.addCommand(cmdHome);
        this.addCommand(cmdViewDetail);
        this.addCommand(cmdSortAphabel);
        this.addCommand(cmdAddDetail);
        this.addCommand(cmdViewReviews);
        this.addCommand(cmdBestReview);
        this.addCommand(cmdupdateEstablishment);
        this.setCommandListener(this);

    }

    public void sortAnphabel() {
        System.out.print("sorted");
        for (int i = 0; i < displayedEstablishment.size() - 1; i++) {
            for (int j = i + 1; j < displayedEstablishment.size(); j++) {
                Establishment tamp;//=(Establishment) displayedEstablishment.elementAt(j);
                if (((Establishment) displayedEstablishment.elementAt(i)).getName().compareTo(((Establishment) displayedEstablishment.elementAt(j)).getName()) > 0) {
                    tamp = (Establishment) displayedEstablishment.elementAt(i);
                    displayedEstablishment.setElementAt(displayedEstablishment.elementAt(j), i);
                    displayedEstablishment.setElementAt(tamp, j);
                }
            }
        }
    }

    public void bindDataToList(Vector data) {
        esList.deleteAll();
        try {
            for (int i = 0; i < data.size(); i++) {
                Image esImg = Image.createImage(getClass().getResourceAsStream("esImage.png"));
                esList.append(((Establishment) data.elementAt(i)).getName(), esImg);

            }
        } catch (IOException ex) {
        }
    }

    public void refreshListView() {
        esList.deleteAll();
        try {
            for (int i = 0; i < displayedEstablishment.size(); i++) {
                Image esImg = Image.createImage(getClass().getResourceAsStream("esImage.png"));
                esList.append(((Establishment) displayedEstablishment.elementAt(i)).getName(), esImg);
            }
        } catch (IOException ex) {
        }
    }
    //bind all data to choice list

    public void bindDataToList(Establishment[] data) {
        esList.deleteAll();
        if (displayedEstablishment == null) {
            displayedEstablishment = new Vector();
        }
        displayedEstablishment.removeAllElements();
        try {
            for (int i = 0; i < data.length; i++) {
                Image esImg = Image.createImage(getClass().getResourceAsStream("esImage.png"));
                esList.append(data[i].getName(), esImg);
                displayedEstablishment.addElement(data[i]);
            }
        } catch (IOException ex) {
        }
    }

    public void loadEstablishment() {
        getParentMidlet().loadEstablishment();
        esList.deleteAll();
        if (displayedEstablishment == null) {
            displayedEstablishment = new Vector();
        }
        displayedEstablishment.removeAllElements();
        try {
            for (int i = 0; i < getParentMidlet().getEsData().length; i++) {
                Image esImg = Image.createImage(getClass().getResourceAsStream("esImage.png"));
                esList.append(getParentMidlet().getEsData()[i].getName(), esImg);
                displayedEstablishment.addElement(getParentMidlet().getEsData()[i]);
            }
        } catch (IOException ex) {
        }
    }
    //filter daa by name

    public Vector filerDataByName(String name) {
        if (displayedEstablishment == null) {
            displayedEstablishment = new Vector();
        }
        displayedEstablishment.removeAllElements();
        for (int i = 0; i < getParentMidlet().getEsData().length; i++) {
            Establishment thisE = getParentMidlet().getEsData()[i];
            if (thisE.getName().toLowerCase().indexOf(name.toLowerCase()) != -1) {
                displayedEstablishment.addElement(thisE);
            }
        }
        return displayedEstablishment;
    }
    //filter data by type 

    public Vector filerDataByType(int type) {
        Vector first = filerDataByName(this.txtSearch.getString());
        if (type == 0) {
            displayedEstablishment = first;
            refreshListView();
            return displayedEstablishment;
        }
        type--;
        Vector tampVector = new Vector();
        for (int i = 0; i < first.size(); i++) {
            Establishment thisE = (Establishment) first.elementAt(i);
            if (thisE.getType() == type) {
                tampVector.addElement(thisE);
            }
        }
        displayedEstablishment = tampVector;
        refreshListView();
        return tampVector;
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmdHome) {
            getParentMidlet().display(getParentMidlet().form);
            return;
        }
        if (c == cmdSortAphabel) {
            if (esList == null || esList.getSelectedIndex() == -1) {
                Alert a = new Alert(parentMidlet.getLmanager().getKeyValue("common.info"), parentMidlet.getLmanager().getKeyValue("frm.ListEstablishment.noEstablishment"), null, AlertType.WARNING);
                a.setTimeout(Alert.FOREVER);
                parentMidlet.display(a);
                return;
            }
            sortAnphabel();
            refreshListView();
        }
        if (c == cmdAddDetail) {
            if (esList == null || esList.getSelectedIndex() == -1) {
                Alert a = new Alert(parentMidlet.getLmanager().getKeyValue("common.info"), parentMidlet.getLmanager().getKeyValue("frm.ListEstablishment.noEstablishment"), null, AlertType.WARNING);
                a.setTimeout(Alert.FOREVER);
                parentMidlet.display(a);
                return;
            }
            Establishment e = (Establishment) displayedEstablishment.elementAt(esList.getSelectedIndex());
            parentMidlet.frm_AddDetail.setEs(e);
            parentMidlet.frm_AddDetail.setTitle(parentMidlet.getLmanager().getKeyValue("frm.ListEstablishment.addDetail") + e.getName());
            parentMidlet.loadReviews();
            parentMidlet.display(parentMidlet.frm_AddDetail);
        }
        if (c == cmdViewDetail) {
            if (esList == null || esList.getSelectedIndex() == -1) {
                Alert a = new Alert(parentMidlet.getLmanager().getKeyValue("common.info"), parentMidlet.getLmanager().getKeyValue("frm.ListEstablishment.noEstablishment"), null, AlertType.WARNING);
                a.setTimeout(Alert.FOREVER);
                parentMidlet.display(a);
                return;
            }
            parentMidlet.frm_ViewDetail.showInfo((Establishment) displayedEstablishment.elementAt(esList.getSelectedIndex()));
            parentMidlet.display(parentMidlet.frm_ViewDetail);
        }
        if (c == cmdViewReviews) {
            if (esList == null || esList.getSelectedIndex() == -1) {
                Alert a = new Alert(parentMidlet.getLmanager().getKeyValue("common.info"), parentMidlet.getLmanager().getKeyValue("frm.ListEstablishment.noEstablishment"), null, AlertType.WARNING);
                a.setTimeout(Alert.FOREVER);
                parentMidlet.display(a);
                return;
            }
            parentMidlet.frm_reviews.myEsta = (Establishment) this.displayedEstablishment.elementAt(esList.getSelectedIndex());
            parentMidlet.frm_reviews.rvList.setLabel(parentMidlet.getLmanager().getKeyValue("frm.ListEstablishment.viewAllReviews") + " of:" + parentMidlet.frm_reviews.myEsta.getName());
            parentMidlet.frm_reviews.filterReviewOfEstablishment(parentMidlet.getDbManager().getAllReviews());
            parentMidlet.display(parentMidlet.frm_reviews);
            parentMidlet.frm_reviews.refreshListView();
        }
        if (c == cmdBestReview) {
            if (parentMidlet.getReviewData() == null || parentMidlet.getReviewData().length == 0) {
                Alert aler = new Alert(parentMidlet.getLmanager().getKeyValue("common.info"), "", null, AlertType.WARNING);
                aler.setString(parentMidlet.getLmanager().getKeyValue("frm.ListEstablishment.noReview"));
                parentMidlet.display(aler);
                return;
            }
            if (esList == null || esList.getSelectedIndex() == -1) {
                Alert a = new Alert(parentMidlet.getLmanager().getKeyValue("common.info"), parentMidlet.getLmanager().getKeyValue("frm.ListEstablishment.noEstablishment"), null, AlertType.WARNING);
                a.setTimeout(Alert.FOREVER);
                parentMidlet.display(a);
                return;
            }
            Review rv = parentMidlet.getBestReview((Establishment) displayedEstablishment.elementAt(esList.getSelectedIndex()));
            if (rv == null) {
                Alert a = new Alert(parentMidlet.getLmanager().getKeyValue("common.info"), parentMidlet.getLmanager().getKeyValue("frm.ListEstablishment.noReview"), null, AlertType.WARNING);
                a.setTimeout(Alert.FOREVER);
                parentMidlet.display(a);
                return;
            }
            parentMidlet.display(parentMidlet.frm_reviewDetail);
            parentMidlet.frm_reviewDetail.showInfo(rv);
        }
        if(c==cmdupdateEstablishment)
        {
            
            parentMidlet.frm_update.setOldEs((Establishment)this.displayedEstablishment.elementAt(esList.getSelectedIndex()));
            parentMidlet.display(parentMidlet.frm_update);
        }
    }

    /**
     * @return the parentMidlet
     */
    public HomeManager getParentMidlet() {
        return parentMidlet;
    }

    /**
     * @param parentMidlet the parentMidlet to set
     */
    public void setParentMidlet(HomeManager parentMidlet) {
        this.parentMidlet = parentMidlet;
    }

    /**
     * @return the txtSearch
     */
    public TextField getTxtSearch() {
        return txtSearch;
    }

    /**
     * @return the choiceFilter
     */
    public ChoiceGroup getChoiceFilter() {
        return choiceFilter;
    }
}
