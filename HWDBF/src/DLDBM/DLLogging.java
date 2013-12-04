package DLDBM;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 *
 * @author Duong89
 */
public class DLLogging {
    DLEnvironmentSetting HW;

    public DLLogging(DLEnvironmentSetting HWSetting) {
        this.HW = HWSetting;
    }

    public  void RunVoidMethod(Method thisMethod, Object[] agrs) {
        try {
            thisMethod.invoke(new Object(), agrs);
        } catch (IllegalAccessException ex) {
          new  DLFile(HW).AppendStringToFile(HW.getLoggingErrorFile(), "an error occurs" + ex.getMessage() + "\t at:" + new Date().toString());
        } catch (IllegalArgumentException ex) {
            new  DLFile(HW).AppendStringToFile(HW.getLoggingErrorFile(), "an error occurs" + ex.getMessage() + "\t at:" + new Date().toString());
        } catch (InvocationTargetException ex) {
          new  DLFile(HW).AppendStringToFile(HW.getLoggingErrorFile(), "an error occurs" + ex.getMessage() + "\t at:" + new Date().toString());
        } catch (NullPointerException ex) {
           new  DLFile(HW).AppendStringToFile(HW.getLoggingErrorFile(), "an error occurs" + ex.getMessage() + "\t at:" + new Date().toString());
        } catch (Exception ex) {
           new  DLFile(HW).AppendStringToFile(HW.getLoggingErrorFile(), "an error occurs" + ex.getMessage() + "\t at:" + new Date().toString());
        }
    }

    public Object RunReturnMethod(Method thisMethod, Object[] agrs) {
        Object result = null;
        try {
            result = thisMethod.invoke(new Object(), agrs);
        } catch (IllegalAccessException ex) {
            new  DLFile(HW).AppendStringToFile(HW.getLoggingErrorFile(), "an error occurs" + ex.getMessage() + "\t at:" + new Date().toString());
        } catch (IllegalArgumentException ex) {
            new  DLFile(HW).AppendStringToFile(HW.getLoggingErrorFile(), "an error occurs" + ex.getMessage() + "\t at:" + new Date().toString());
        } catch (InvocationTargetException ex) {
            new  DLFile(HW).AppendStringToFile(HW.getLoggingErrorFile(), "an error occurs" + ex.getMessage() + "\t at:" + new Date().toString());
        } catch (NullPointerException ex) {
            new  DLFile(HW).AppendStringToFile(HW.getLoggingErrorFile(), "an error occurs" + ex.getMessage() + "\t at:" + new Date().toString());
        } catch (Exception ex) {
            new  DLFile(HW).AppendStringToFile(HW.getLoggingErrorFile(), "an error occurs" + ex.getMessage() + "\t at:" + new Date().toString());

        }
        return result;
    }

    public  void WriteError(String error, String FromClass, String FromMethod) {
        if(HW.isHWLogging()){
        String Er = "\n+An error occurs  at:" + new Date().toString()
                + " From class:" + FromClass + " in method:" + FromMethod;
        Er += "\n|---->Details:" + error;
       new  DLFile(HW).AppendStringToFile(HW.getLoggingErrorFile(), Er);
        }
    }
    public  void ClearLogging()
    {
      new  DLFile(HW).WriteFile(HW.getLoggingErrorFile(), "");
    }
    public  String ViewLogging()
    {
    return   new  DLFile(HW).GetFileContent(HW.getLoggingErrorFile());
    }
}
