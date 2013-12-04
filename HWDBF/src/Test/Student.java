/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Test;

import DLDBM.DLClassInterface;

/**
 *
 * @author buiduong
 */
public class Student implements DLClassInterface{
    String name;
    int age;
    String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] PrimaryKey() {
        return null;
    }

    public String[] ReferTo() {
        return null;
    }

    public String[] ReferBy() {
        return null;
    }

    public String IdentityColumn() {
        return "";
    }

    public String[] OrderColumn() {
        return new String[]{"name","age","email"};
    }
    
}
