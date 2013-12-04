/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DLDBM;

/**
 *
 * @author SONY
 */
public interface DLClassInterface {

    public String[] PrimaryKey();

    public String[] ReferTo();

    public String[] ReferBy();

    public String IdentityColumn();

    public String[] OrderColumn();
}
