//
//  Generated by the J2ObjC translator.  DO NOT EDIT!
//  source: /Volumes/Duong's Data/MyMoney/Mobile Software/DLLIB/HWDBF/src/DLDBM/DLEnvironmentSetting.java
//
//  Created by buiduong on 1/17/14.
//

#ifndef _DLDBMDLEnvironmentSetting_H_
#define _DLDBMDLEnvironmentSetting_H_

#import "JreEmulation.h"

@interface DLDBMDLEnvironmentSetting : NSObject {
 @public
  NSString *DatabaseName_;
  NSString *ServerName_;
  NSString *Username_;
  NSString *Password_;
  NSString *ClassesFolder_;
  NSString *RootFolder_;
  NSString *Encode_Decode_Key_;
  NSString *LoggingErrorFile_;
  NSString *Language_;
  NSString *LanguageFolder_;
  id MyConnection_;
  NSString *BackupFile_;
  BOOL HWLogging_;
  NSString *DatabaseType_;
}

- (id)init;
- (NSString *)getDatabaseName;
- (void)setDatabaseNameWithNSString:(NSString *)DatabaseName;
- (NSString *)getServerName;
- (void)setServerNameWithNSString:(NSString *)ServerName;
- (NSString *)getUsername;
- (void)setUsernameWithNSString:(NSString *)Username;
- (NSString *)getPassword;
- (void)setPasswordWithNSString:(NSString *)Password;
- (NSString *)getClassesFolder;
- (void)setClassesFolderWithNSString:(NSString *)ClassesFolder;
- (NSString *)getRootFolder;
- (void)setRootFolderWithNSString:(NSString *)RootFolder;
- (NSString *)getEncode_Decode_Key;
- (void)setEncode_Decode_KeyWithNSString:(NSString *)Encode_Decode_Key;
- (NSString *)getLoggingErrorFile;
- (void)setLoggingErrorFileWithNSString:(NSString *)LoggingErrorFile;
- (NSString *)getLanguage;
- (void)setLanguageWithNSString:(NSString *)Language;
- (NSString *)getLanguageFolder;
- (void)setLanguageFolderWithNSString:(NSString *)LanguageFolder;
- (BOOL)isHWLogging;
- (void)setHWLoggingWithBoolean:(BOOL)HWLogging;
- (id)getMyConnection;
- (void)setMyConnectionWithId:(id)MyConnection;
- (NSString *)getBackupFile;
- (void)setBackupFileWithNSString:(NSString *)BackupFile;
- (NSString *)getDatabaseType;
- (void)setDatabaseTypeWithNSString:(NSString *)DatabaseType;
- (void)copyAllFieldsTo:(DLDBMDLEnvironmentSetting *)other;
@end

J2OBJC_FIELD_SETTER(DLDBMDLEnvironmentSetting, DatabaseName_, NSString *)
J2OBJC_FIELD_SETTER(DLDBMDLEnvironmentSetting, ServerName_, NSString *)
J2OBJC_FIELD_SETTER(DLDBMDLEnvironmentSetting, Username_, NSString *)
J2OBJC_FIELD_SETTER(DLDBMDLEnvironmentSetting, Password_, NSString *)
J2OBJC_FIELD_SETTER(DLDBMDLEnvironmentSetting, ClassesFolder_, NSString *)
J2OBJC_FIELD_SETTER(DLDBMDLEnvironmentSetting, RootFolder_, NSString *)
J2OBJC_FIELD_SETTER(DLDBMDLEnvironmentSetting, Encode_Decode_Key_, NSString *)
J2OBJC_FIELD_SETTER(DLDBMDLEnvironmentSetting, LoggingErrorFile_, NSString *)
J2OBJC_FIELD_SETTER(DLDBMDLEnvironmentSetting, Language_, NSString *)
J2OBJC_FIELD_SETTER(DLDBMDLEnvironmentSetting, LanguageFolder_, NSString *)
J2OBJC_FIELD_SETTER(DLDBMDLEnvironmentSetting, MyConnection_, id)
J2OBJC_FIELD_SETTER(DLDBMDLEnvironmentSetting, BackupFile_, NSString *)
J2OBJC_FIELD_SETTER(DLDBMDLEnvironmentSetting, DatabaseType_, NSString *)

#endif // _DLDBMDLEnvironmentSetting_H_