/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import Controller.LanguageManager;
import java.util.Calendar;
import java.util.Date;
/**
 *
 * @author SONY
 */
public class Review implements EntityInterface{
    private String esID;
    private int reviewID;
    private String date; 
    private String typeOfMeal;
    private int overallRating;
    private float cost;
    private int serviceRating;
    private int atmosphereRating;
    private int foodRating;
    private String comment;
    private String separator=";";
    public static String fomartDateToString(Date d)
    {
        
        //formart mm/dd/yyyy
        Calendar ca=Calendar.getInstance();
        ca.setTime(d);
       String dateStr= ca.get(Calendar.MONTH)+1 + "-"+ ca.get(Calendar.DATE) + "-" + ca.get(Calendar.YEAR);
       return dateStr;
    }
//    public void setDate(String dateWithFormat)
//    {
//        Calendar cal=Calendar.getInstance();
//        String[] dmy=LanguageManager.split(dateWithFormat, "-");
//        cal.set(Calendar.DATE, Integer.valueOf(dmy[0]).intValue());
//        cal.set(Calendar.MONTH, Integer.valueOf(dmy[1]).intValue());
//        cal.set(Calendar.YEAR, Integer.valueOf(dmy[2]).intValue());
//        
//        System.out.print("convertdate ok");
//    }
    
    public String getInformation() {
            return    getDate()+separator
                    +typeOfMeal+separator
                    +overallRating+separator
                    +cost+separator
                    +serviceRating+separator
                    +atmosphereRating+separator
                    +foodRating+separator
                    +comment+separator
                    +esID+separator
                    +reviewID;
    }
    
    public void setInformation(String infor) {
        String[] data=LanguageManager.split(infor, separator);
        setDate(data[0]);
        setTypeOfMeal(data[1]);
        setOverallRating(Integer.valueOf(data[2]).intValue());
        setCost(Float.valueOf(data[3]).floatValue());
        setServiceRating(Integer.valueOf(data[4]).intValue());
        setAtmosphereRating(Integer.valueOf(data[5]).intValue());
        setFoodRating(Integer.valueOf(data[6]).intValue());
        setComment(data[7]);
        setEsID(data[8]);
        setReviewID(Integer.valueOf(data[9]).intValue());
    }
   
    /**
     * @return the date
     */
 

    /**
     * @return the typeOfMeal
     */
    public String getTypeOfMeal() {
        return typeOfMeal;
    }

    /**
     * @param typeOfMeal the typeOfMeal to set
     */
    public void setTypeOfMeal(String typeOfMeal) {
        this.typeOfMeal = typeOfMeal;
    }

    /**
     * @return the overallRating
     */
    public int getOverallRating() {
        return overallRating;
    }

    /**
     * @param overallRating the overallRating to set
     */
    public void setOverallRating(int overallRating) {
        this.overallRating = overallRating;
    }

    /**
     * @return the cost
     */
    public float getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(float cost) {
        this.cost = cost;
    }

    /**
     * @return the serviceRating
     */
    public int getServiceRating() {
        return serviceRating;
    }

    /**
     * @param serviceRating the serviceRating to set
     */
    public void setServiceRating(int serviceRating) {
        this.serviceRating = serviceRating;
    }

    /**
     * @return the atmosphereRating
     */
    public int getAtmosphereRating() {
        return atmosphereRating;
    }

    /**
     * @param atmosphereRating the atmosphereRating to set
     */
    public void setAtmosphereRating(int atmosphereRating) {
        this.atmosphereRating = atmosphereRating;
    }

    /**
     * @return the foodRating
     */
    public int getFoodRating() {
        return foodRating;
    }

    /**
     * @param foodRating the foodRating to set
     */
    public void setFoodRating(int foodRating) {
        this.foodRating = foodRating;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the esID
     */
    public String getEsID() {
        return esID;
    }

    /**
     * @param esID the esID to set
     */
    public void setEsID(String esID) {
        this.esID = esID;
    }

    /**
     * @return the reviewID
     */
    public int getReviewID() {
        return reviewID;
    }

    /**
     * @param reviewID the reviewID to set
     */
    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }
}
