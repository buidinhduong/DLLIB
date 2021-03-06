//
//  Generated by the J2ObjC translator.  DO NOT EDIT!
//  source: /Volumes/Duong's Data/MyMoney/Mobile Software/DLLIB/HWDBF/src/DLDBM/DLEnvironmentSetting.java
//
//  Created by buiduong on 1/17/14.
//

#include "DLDBM/DLEnvironmentSetting.h"

@implementation DLDBMDLEnvironmentSetting

- (id)init {
  if (self = [super init]) {
    DLDBMDLEnvironmentSetting_set_DatabaseName_(self, @"");
    DLDBMDLEnvironmentSetting_set_ServerName_(self, @"");
    DLDBMDLEnvironmentSetting_set_Username_(self, @"");
    DLDBMDLEnvironmentSetting_set_Password_(self, @"");
    DLDBMDLEnvironmentSetting_set_ClassesFolder_(self, @"");
    DLDBMDLEnvironmentSetting_set_RootFolder_(self, @"");
    DLDBMDLEnvironmentSetting_set_Encode_Decode_Key_(self, @"");
    DLDBMDLEnvironmentSetting_set_LoggingErrorFile_(self, @"");
    DLDBMDLEnvironmentSetting_set_Language_(self, @"");
    DLDBMDLEnvironmentSetting_set_LanguageFolder_(self, [NSString stringWithFormat:@"%@", RootFolder_]);
    DLDBMDLEnvironmentSetting_set_BackupFile_(self, @"");
    HWLogging_ = NO;
    DLDBMDLEnvironmentSetting_set_DatabaseType_(self, @"SQLServer");
  }
  return self;
}

- (NSString *)getDatabaseName {
  return DatabaseName_;
}

- (void)setDatabaseNameWithNSString:(NSString *)DatabaseName {
  DLDBMDLEnvironmentSetting_set_DatabaseName_(self, DatabaseName);
}

- (NSString *)getServerName {
  return ServerName_;
}

- (void)setServerNameWithNSString:(NSString *)ServerName {
  DLDBMDLEnvironmentSetting_set_ServerName_(self, ServerName);
}

- (NSString *)getUsername {
  return Username_;
}

- (void)setUsernameWithNSString:(NSString *)Username {
  DLDBMDLEnvironmentSetting_set_Username_(self, Username);
}

- (NSString *)getPassword {
  return Password_;
}

- (void)setPasswordWithNSString:(NSString *)Password {
  DLDBMDLEnvironmentSetting_set_Password_(self, Password);
}

- (NSString *)getClassesFolder {
  return ClassesFolder_;
}

- (void)setClassesFolderWithNSString:(NSString *)ClassesFolder {
  DLDBMDLEnvironmentSetting_set_ClassesFolder_(self, ClassesFolder);
}

- (NSString *)getRootFolder {
  return RootFolder_;
}

- (void)setRootFolderWithNSString:(NSString *)RootFolder {
  DLDBMDLEnvironmentSetting_set_RootFolder_(self, RootFolder);
}

- (NSString *)getEncode_Decode_Key {
  return Encode_Decode_Key_;
}

- (void)setEncode_Decode_KeyWithNSString:(NSString *)Encode_Decode_Key {
  DLDBMDLEnvironmentSetting_set_Encode_Decode_Key_(self, Encode_Decode_Key);
}

- (NSString *)getLoggingErrorFile {
  return LoggingErrorFile_;
}

- (void)setLoggingErrorFileWithNSString:(NSString *)LoggingErrorFile {
  DLDBMDLEnvironmentSetting_set_LoggingErrorFile_(self, LoggingErrorFile);
}

- (NSString *)getLanguage {
  return Language_;
}

- (void)setLanguageWithNSString:(NSString *)Language {
  DLDBMDLEnvironmentSetting_set_Language_(self, Language);
}

- (NSString *)getLanguageFolder {
  return LanguageFolder_;
}

- (void)setLanguageFolderWithNSString:(NSString *)LanguageFolder {
  DLDBMDLEnvironmentSetting_set_LanguageFolder_(self, LanguageFolder);
}

- (BOOL)isHWLogging {
  return HWLogging_;
}

- (void)setHWLoggingWithBoolean:(BOOL)HWLogging {
  self->HWLogging_ = HWLogging;
}

- (id)getMyConnection {
  return nil;
}

- (void)setMyConnectionWithId:(id)MyConnection {
  DLDBMDLEnvironmentSetting_set_MyConnection_(self, MyConnection);
}

- (NSString *)getBackupFile {
  return BackupFile_;
}

- (void)setBackupFileWithNSString:(NSString *)BackupFile {
  DLDBMDLEnvironmentSetting_set_BackupFile_(self, BackupFile);
}

- (NSString *)getDatabaseType {
  return DatabaseType_;
}

- (void)setDatabaseTypeWithNSString:(NSString *)DatabaseType {
  DLDBMDLEnvironmentSetting_set_DatabaseType_(self, DatabaseType);
}

- (void)dealloc {
  DLDBMDLEnvironmentSetting_set_DatabaseType_(self, nil);
  DLDBMDLEnvironmentSetting_set_BackupFile_(self, nil);
  DLDBMDLEnvironmentSetting_set_MyConnection_(self, nil);
  DLDBMDLEnvironmentSetting_set_LanguageFolder_(self, nil);
  DLDBMDLEnvironmentSetting_set_Language_(self, nil);
  DLDBMDLEnvironmentSetting_set_LoggingErrorFile_(self, nil);
  DLDBMDLEnvironmentSetting_set_Encode_Decode_Key_(self, nil);
  DLDBMDLEnvironmentSetting_set_RootFolder_(self, nil);
  DLDBMDLEnvironmentSetting_set_ClassesFolder_(self, nil);
  DLDBMDLEnvironmentSetting_set_Password_(self, nil);
  DLDBMDLEnvironmentSetting_set_Username_(self, nil);
  DLDBMDLEnvironmentSetting_set_ServerName_(self, nil);
  DLDBMDLEnvironmentSetting_set_DatabaseName_(self, nil);
  [super dealloc];
}

- (void)copyAllFieldsTo:(DLDBMDLEnvironmentSetting *)other {
  [super copyAllFieldsTo:other];
  DLDBMDLEnvironmentSetting_set_BackupFile_(other, BackupFile_);
  DLDBMDLEnvironmentSetting_set_ClassesFolder_(other, ClassesFolder_);
  DLDBMDLEnvironmentSetting_set_DatabaseName_(other, DatabaseName_);
  DLDBMDLEnvironmentSetting_set_DatabaseType_(other, DatabaseType_);
  DLDBMDLEnvironmentSetting_set_Encode_Decode_Key_(other, Encode_Decode_Key_);
  other->HWLogging_ = HWLogging_;
  DLDBMDLEnvironmentSetting_set_Language_(other, Language_);
  DLDBMDLEnvironmentSetting_set_LanguageFolder_(other, LanguageFolder_);
  DLDBMDLEnvironmentSetting_set_LoggingErrorFile_(other, LoggingErrorFile_);
  DLDBMDLEnvironmentSetting_set_MyConnection_(other, MyConnection_);
  DLDBMDLEnvironmentSetting_set_Password_(other, Password_);
  DLDBMDLEnvironmentSetting_set_RootFolder_(other, RootFolder_);
  DLDBMDLEnvironmentSetting_set_ServerName_(other, ServerName_);
  DLDBMDLEnvironmentSetting_set_Username_(other, Username_);
}

+ (J2ObjcClassInfo *)__metadata {
  static J2ObjcMethodInfo methods[] = {
    { "getDatabaseName", NULL, "LNSString", 0x1, NULL },
    { "getServerName", NULL, "LNSString", 0x1, NULL },
    { "getUsername", NULL, "LNSString", 0x1, NULL },
    { "getPassword", NULL, "LNSString", 0x1, NULL },
    { "getClassesFolder", NULL, "LNSString", 0x1, NULL },
    { "getRootFolder", NULL, "LNSString", 0x1, NULL },
    { "getEncode_Decode_Key", NULL, "LNSString", 0x1, NULL },
    { "getLoggingErrorFile", NULL, "LNSString", 0x1, NULL },
    { "getLanguage", NULL, "LNSString", 0x1, NULL },
    { "getLanguageFolder", NULL, "LNSString", 0x1, NULL },
    { "isHWLogging", NULL, "Z", 0x1, NULL },
    { "getMyConnection", NULL, "LNSObject", 0x1, NULL },
    { "getBackupFile", NULL, "LNSString", 0x1, NULL },
    { "getDatabaseType", NULL, "LNSString", 0x1, NULL },
  };
  static J2ObjcClassInfo _DLDBMDLEnvironmentSetting = { "DLEnvironmentSetting", "DLDBM", NULL, 0x1, 14, methods, 0, NULL, 0, NULL};
  return &_DLDBMDLEnvironmentSetting;
}

@end
