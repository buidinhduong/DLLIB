/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DLDBM;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Duong89
 */
public class DLLanguage {

    DLEnvironmentSetting HW;

    public DLLanguage(DLEnvironmentSetting HW) {
        this.HW = HW;
    }

    public DLLanguage() {
        //load from langage file
        // LoadLanguge(DLEnvironmentSetting.LanguageFolder+DLEnvironmentSetting.Language);
    }

    public String[] LoadLanguge(String langFile) {
        ArrayList<String> MyLanguage = new ArrayList<String>();
        String[] ReturnLang = null;
        FileReader F = null;
        try {
            F = new FileReader(langFile);
            BufferedReader B;
            try {
                F = new FileReader(langFile);
                B = new BufferedReader(F);
                String ThisLine = "";
                while ((ThisLine = B.readLine()) != null) {
                    String[] spl = ThisLine.split("=");
                    if (spl.length == 2) {
                        String Key = spl[0];
                        String Value = spl[1];
                        MyLanguage.add(Value);
                    }

                }
                ReturnLang = new String[MyLanguage.size()];
                ReturnLang = MyLanguage.toArray(ReturnLang);
            } catch (Exception ex) {
                new DLLogging(HW).WriteError(ex.getMessage(), DLLanguage.class.getName(), "LoadLanguage()");
                ex.printStackTrace();
            }
        } catch (FileNotFoundException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLLanguage.class.getName(), "LoadLanguage()");
            ex.printStackTrace();
        } finally {
            try {
                F.close();
            } catch (IOException ex) {
                new DLLogging(HW).WriteError(ex.getMessage(), DLLanguage.class.getName(), "LoadLanguage()");
                ex.printStackTrace();
            }
        }
        return ReturnLang;
    }
}
