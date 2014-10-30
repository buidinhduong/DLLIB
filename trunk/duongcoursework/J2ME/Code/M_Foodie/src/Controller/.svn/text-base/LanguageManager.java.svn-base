/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.InputStream;
import java.util.Vector;

/**
 *
 * @author SONY
 */
public class LanguageManager {
  public String language="English";
  public String data;
  public String[] keys;
  public String[] values;

    public LanguageManager(String langauge) {
        this.language=langauge;
        this.loadLanguage();
        this.convertToKeysAndValues();
    }
   
  public void loadLanguage(){
      //this method will load data from file
      InputStream is = getClass().getResourceAsStream(language+".txt");
      StringBuffer sb = new StringBuffer();
      try{
      int chars, i = 0;
      while ((chars = is.read()) != -1){
      sb.append((char) chars);
      }
      this.data=sb.toString();
      System.out.print("file data:"+this.data);
      }catch (Exception e){
          System.out.print("Can not load language");
      }
  }
  private void convertToKeysAndValues ()
  {
      /*
       * Convert each line into 2 parts: keys and value
       * Store keys and values in 2 arrays
       */
      String[]lines=split(this.data,"\n");
      this.keys=new String[lines.length];
      this.values=new String[lines.length];
      for(int i=0;i<lines.length;i++)
      {
          String[] arr=split(lines[i],"=");
          System.out.println("\nkey:"+arr[0]+" value:"+values[1]);
          this.keys[i]=arr[0];
          this.values[i]=arr[1];
      }
      
  }
  public static String[] split(String original,String separator) {
      /*Duong splits string
       * J2ME does not support for Spliting String, so this method will
       * do spliting any string with specified separator
       */
    Vector nodes = new Vector();
    // Parse nodes into vector
    int index = original.indexOf(separator);
    while(index>=0) 
    {
    nodes.addElement( original.substring(0, index) );
    original = original.substring(index+separator.length());
    index = original.indexOf(separator);
    }
    // take the last node
    nodes.addElement( original );

    // Create splitted string array
    String[] result = new String[ nodes.size() ];
    if( nodes.size()>0 )
    {
    for(int i=0; i<nodes.size(); i++)
        {
    result[i] = (String)nodes.elementAt(i);
    System.out.println(result[i]);
        }
    }
    return result;
}
  public  String getKeyValue(String keyName)
  {
      //This method takes an parepetmet is the key name
      //from KeyName, find the Index of Key Name
      //return Values[Index of Key] because with have key-pair 2 arrays Key and Value
      for(int i=0;i<this.keys.length;i++)
      {
         // System.out.print("key:"+keyName+" value:"+values[i]);
          if(keys[i].equalsIgnoreCase(keyName))
          {
              
              return values[i];
          }
      }
      return "#"+keyName;//# means: not found key name
  }
}
