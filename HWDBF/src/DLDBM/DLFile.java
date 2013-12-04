/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DLDBM;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Duong89
 */
public class DLFile {

    DLEnvironmentSetting HW;

    public DLFile(DLEnvironmentSetting HW) {
        this.HW = HW;
    }

    public void WriteFile(String FilePath, String Body) {
        try {
            FileWriter FW = new FileWriter(FilePath);
            FW.write(Body);
            FW.close();
        } catch (IOException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLFile.class.getName(), "WriteFile()");
            ex.printStackTrace();
        }
    }

    public String GetFileContent(String filepath) {
        FileReader FR = null;
        String body = "";
        BufferedReader BR = null;
        try {
            FR = new FileReader(filepath);
            BR = new BufferedReader(FR);
            try {
                FR = new FileReader(filepath);
                String s;
                while ((s = BR.readLine()) != null) {
                    body += "\n" + s;
                }
                return body;
            } catch (Exception ex) {
                new DLLogging(HW).WriteError(ex.getMessage(), DLFile.class.getName(), "getFileContent()");
            }
        } catch (FileNotFoundException ex) {
            new DLLogging(HW).WriteError(ex.getMessage(), DLFile.class.getName(), "getFileContent()");
        } finally {
            try {
                FR.close();
                BR.close();
            } catch (IOException ex) {
                new DLLogging(HW).WriteError(ex.getMessage(), DLFile.class.getName(), "getFileContent()");
            }
        }
        return body;
    }

    public  void AppendStringToFile(String file, String Append) {
        try {
            // Create file
            FileWriter fstream = new FileWriter(file, true);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("\n" + Append);
            //Close the output stream
            out.close();
        } catch (IOException e) {//Catch exception if any
            new DLLogging(HW).WriteError(e.getMessage(), "HWFileProcessor", "AppendStringToFile()");
        }
    }
}
