package DLDBM;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Duong89
 */
public class DLClassTableInfo {

    String ClassName;
    String[] Fields;
    String[] Types;
    private String[] primaryKey;
    private String[] ReferTo;
    private String[] ReferFrom;
    private String IdentityColumn;
    public String getClassName() {
        return ClassName;
    }

    public void setReferFrom(String[] ReferFrom) {
        this.ReferFrom = ReferFrom;
    }

    public String[] getReferFrom() {
        return ReferFrom;
    }

    public String[] getFields() {
        return Fields;
    }

    public DLClassTableInfo() {
    }

    public String[] getTypes() {
        return Types;
    }

    public void setClassName(String ClassName) {
        this.ClassName = ClassName;
    }

    public void setFields(String[] Fields) {
        this.Fields = Fields;
    }

    public void setTypes(String[] Types) {
        this.Types = Types;
    }

    public DLClassTableInfo(String ClassName, String[] Fields, String[] Types) {
        this.ClassName = ClassName;
        this.Fields = Fields;
        this.Types = Types;
    }

    /**
     * @return the primaryKey
     */
    public String[] getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(String[] primaryKey) {
        this.primaryKey=primaryKey;
    }


    /**
     * @return the ReferTo
     */
    public String[] getReferTo() {
        return ReferTo;
    }

    /**
     * @param ReferTo the ReferTo to set
     */
    public void setReferTo(String[] ReferTo) {
        this.ReferTo = ReferTo;
    }

    /**
     * @return the IdentityColumn
     */
    public String getIdentityColumn() {
        return IdentityColumn;
    }

    /**
     * @param IdentityColumn the IdentityColumn to set
     */
    public void setIdentityColumn(String IdentityColumn) {
        this.IdentityColumn = IdentityColumn;
    }

 
}
