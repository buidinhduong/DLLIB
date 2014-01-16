/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DLDBM;

///import java.sql.Connection;

import java.sql.Connection;


/**
 *
 * @author Duong89
 */
public class DLEnvironmentSetting {

    private String DatabaseName = "";
    private String ServerName = "";
    private String Username = "";
    private String Password = "";
    private String ClassesFolder = "";
    private String RootFolder = "";
    private String Encode_Decode_Key = "";
    private String LoggingErrorFile ="";
    private String Language = "";
    private String LanguageFolder = RootFolder + "";
    private Connection MyConnection;
    private String BackupFile = "";
    private boolean HWLogging = false;
    private String DatabaseType = "SQLServer";

    public DLEnvironmentSetting() {
    }

    /**
     * @return the DatabaseName
     */
    public String getDatabaseName() {
        return DatabaseName;
    }

    /**
     * @param DatabaseName the DatabaseName to set
     */
    public void setDatabaseName(String DatabaseName) {
        this.DatabaseName = DatabaseName;
    }

    /**
     * @return the ServerName
     */
    public String getServerName() {
        return ServerName;
    }

    /**
     * @param ServerName the ServerName to set
     */
    public void setServerName(String ServerName) {
        this.ServerName = ServerName;
    }

    /**
     * @return the Username
     */
    public String getUsername() {
        return Username;
    }

    /**
     * @param Username the Username to set
     */
    public void setUsername(String Username) {
        this.Username = Username;
    }

    /**
     * @return the Password
     */
    public String getPassword() {
        return Password;
    }

    /**
     * @param Password the Password to set
     */
    public void setPassword(String Password) {
        this.Password = Password;
    }

    /**
     * @return the ClassesFolder
     */
    public String getClassesFolder() {
        return ClassesFolder;
    }

    /**
     * @param ClassesFolder the ClassesFolder to set
     */
    public void setClassesFolder(String ClassesFolder) {
        this.ClassesFolder = ClassesFolder;
    }

    /**
     * @return the RootFolder
     */
    public String getRootFolder() {
        return RootFolder;
    }

    /**
     * @param RootFolder the RootFolder to set
     */
    public void setRootFolder(String RootFolder) {
        this.RootFolder = RootFolder;
    }

    /**
     * @return the Encode_Decode_Key
     */
    public String getEncode_Decode_Key() {
        return Encode_Decode_Key;
    }

    /**
     * @param Encode_Decode_Key the Encode_Decode_Key to set
     */
    public void setEncode_Decode_Key(String Encode_Decode_Key) {
        this.Encode_Decode_Key = Encode_Decode_Key;
    }

    /**
     * @return the LoggingErrorFile
     */
    public String getLoggingErrorFile() {
        return LoggingErrorFile;
    }

    /**
     * @param LoggingErrorFile the LoggingErrorFile to set
     */
    public void setLoggingErrorFile(String LoggingErrorFile) {
        this.LoggingErrorFile = LoggingErrorFile;
    }

    /**
     * @return the Language
     */
    public String getLanguage() {
        return Language;
    }

    /**
     * @param Language the Language to set
     */
    public void setLanguage(String Language) {
        this.Language = Language;
    }

    /**
     * @return the LanguageFolder
     */
    public String getLanguageFolder() {
        return LanguageFolder;
    }

    /**
     * @param LanguageFolder the LanguageFolder to set
     */
    public void setLanguageFolder(String LanguageFolder) {
        this.LanguageFolder = LanguageFolder;
    }

    /**
     * @return the HWLogging
     */
    public boolean isHWLogging() {
        return HWLogging;
    }

    /**
     * @param HWLogging the HWLogging to set
     */
    public void setHWLogging(boolean HWLogging) {
        this.HWLogging = HWLogging;
    }

    /**
     * @return the MyConnection
     */
    public Connection getMyConnection() {
        return null;
    }

    /**
     * @param MyConnection the MyConnection to set
     */
    public void setMyConnection(Connection MyConnection) {
        this.MyConnection = MyConnection;
    }

    /**
     * @return the BackupFile
     */
    public String getBackupFile() {
        return BackupFile;
    }

    /**
     * @param BackupFile the BackupFile to set
     */
    public void setBackupFile(String BackupFile) {
        this.BackupFile = BackupFile;
    }

    /**
     * @return the DatabaseType
     */
    public String getDatabaseType() {
        return DatabaseType;
    }

    /**
     * @param DatabaseType the DatabaseType to set
     */
    public void setDatabaseType(String DatabaseType) {
        this.DatabaseType = DatabaseType;
    }
}
