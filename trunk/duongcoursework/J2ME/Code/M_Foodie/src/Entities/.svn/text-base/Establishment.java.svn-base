/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Controller.LanguageManager;

/**
 *
 * @author SONY
 */
public class Establishment implements EntityInterface{
   private String id;
   private String name;
   private int type;
   private String foodType;
   private String localtion;

    public Establishment() {
    }

 
   
   /*this one is use to convert data from record store. it is  not related to information of Establishment*/ 
   private String separator=";";
    

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the foodType
     */
  

    /**
     * @return the localtion
     */
    public String getLocaltion() {
        return localtion;
    }

    /**
     * @param localtion the localtion to set
     */
    public void setLocaltion(String localtion) {
        this.localtion = localtion;
    }
    
    public String getInformation()
    {
        return this.getId()+separator
                +this.getName()+separator
                +this.getType()+separator
                +this.getFoodType()+separator
                +this.getLocaltion();
    }
    public String showInformation()
    {
        String[] types={"Cafe","Bar","Restaurant"};
        return  "ID: "+this.getId()+
                "\nName: "+this.getName()+
                "\nType: "+types[this.type]+
                "\nFood: "+this.getFoodType()+
                "\nLocation:"+this.getLocaltion();
    }
    public void setInformation(String info)
    {
        String[] pieces=LanguageManager.split(info, separator);
        this.setId(pieces[0]);
        this.setName(pieces[1]);
        this.setType(Integer.valueOf(pieces[2]).intValue());
        this.setFoodType(pieces[3]);
        this.setLocaltion(pieces[4]);
        
    }

    /**
     * @return the foodType
     */
    public String getFoodType() {
        return foodType;
    }

    /**
     * @param foodType the foodType to set
     */
    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }
}
