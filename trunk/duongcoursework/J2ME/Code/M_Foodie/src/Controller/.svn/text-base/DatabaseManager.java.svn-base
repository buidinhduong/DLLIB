/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Entities.Establishment;
import Entities.Review;
import java.util.Vector;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

/**
 *
 * @author SONY
 */
public class DatabaseManager {

    private RecordStore rs;
    private String esTable = "Establishment";
    private String reviewTable = "Review";

    public DatabaseManager() {
    }

    public boolean openRecordStore(String rsName) {
        try {
            rs = RecordStore.openRecordStore(rsName, true);
            return true;
        } catch (RecordStoreException ex) {
            return false;
        }
    }

    public boolean closeRecordStore() {
        try {
            rs.closeRecordStore();
            return true;
        } catch (RecordStoreException ex) {
            return false;
        }
    }

    public boolean addNewEstablishment(Establishment es) {
        try {
            openRecordStore(this.esTable);//open  record store
            byte[] esInfo = es.getInformation().getBytes();
            rs.addRecord(esInfo, 0, esInfo.length);
            closeRecordStore();//close record store
            return true;
        } catch (Exception e) {
            closeRecordStore();
            return false;
        }
    }

    public boolean addReview(Review rv) {
        try {
            openRecordStore(this.reviewTable);//open  record store
            byte[] esInfo = rv.getInformation().getBytes();
            rs.addRecord(esInfo, 0, esInfo.length);
            closeRecordStore();//close record store
            return true;
        } catch (Exception e) {
            closeRecordStore();
            e.printStackTrace();
            return false;
        }
    }

    public Establishment[] getAllEstablishments() {
        try {
            openRecordStore(this.esTable);
            int rsCount = rs.getNumRecords();
            Establishment[] returnList = new Establishment[rsCount];
            for (int i = 1; i <= rsCount; i++) {
                returnList[i - 1] = new Establishment();
                returnList[i - 1].setInformation(new String(rs.getRecord(i)));
            }
            closeRecordStore();
            return returnList;
        } catch (RecordStoreNotOpenException ex) {
            closeRecordStore();
            return null;
        } catch (RecordStoreException ex) {
            closeRecordStore();
            return null;
        }
    }

    public Review[] getAllReviews() {

        try {
            Review[] returnR;
            openRecordStore(this.reviewTable);
            Vector vData = new Vector();
            RecordEnumeration re = rs.enumerateRecords(null, null, false);
            while (re.hasNextElement()) {
                byte[] recordBuffer = re.nextRecord();
                String data = new String(recordBuffer);
                Review newR = new Review();
                newR.setInformation(data);
                vData.addElement(newR);
            }
            rs.closeRecordStore();
            returnR = new Review[vData.size()];
            for (int i = 0; i < vData.size(); i++) {
                returnR[i] = (Review) vData.elementAt(i);
            }
            return returnR;
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        return null;

    }

    public boolean updateEstablishment(Establishment upEs) {
        try {
            openRecordStore(esTable);
            RecordEnumeration re = rs.enumerateRecords(null, null, false);
             byte[]dataU=upEs.getInformation().getBytes();

            if(rs.getNumRecords()==1)
            {
                rs.setRecord(rs.getNextRecordID()-1, dataU, 0, dataU.length);
                return true;
            }
            int id = -1;
            while (re.hasNextElement()) {
                byte[] recordBuffer = re.nextRecord();
                String data = new String(recordBuffer);
                if (data.equalsIgnoreCase(upEs.getInformation())) {
                    if (re.hasPreviousElement()) {
                        re.previousRecordId();
                        id = re.nextRecordId();
                       
                    }
                    else if(re.hasNextElement()){
                        re.nextRecordId();
                        id = re.previousRecordId();
                    }
                    else
                    {
                        id=rs.getNextRecordID()-1;
                    }
                    
                }
            }
            if(id!=-1)
            {
                rs.setRecord(id, dataU, 0, dataU.length);
            }
            return true;
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        } 
        return false;
    }

    public boolean deleteReview(Review r) {
        try {
            openRecordStore(reviewTable);
            RecordEnumeration re = rs.enumerateRecords(null, null, false);
            if (rs.getNumRecords() == 1) {
                closeRecordStore();
                RecordStore.deleteRecordStore(reviewTable);
                return true;
            }
            //find id of record contain this review
            int id = -1;
            while (re.hasNextElement()) {
                byte[] recordBuffer = re.nextRecord();
                String data = new String(recordBuffer);
                if (data.equalsIgnoreCase(r.getInformation())) {
                    if (re.hasPreviousElement()) {
                        re.previousRecordId();
                        id = re.nextRecordId();
                    } else {
                        re.nextRecordId();
                        id = re.previousRecordId();
                    }
                }
            }
            if (id == -1) {
                rs.closeRecordStore();
                return false;
            }
            rs.deleteRecord(id);
            rs.closeRecordStore();
            return true;
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        return true;
    }
}
