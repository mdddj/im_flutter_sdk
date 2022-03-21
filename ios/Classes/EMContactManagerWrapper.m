//
//  EMContactManagerWrapper.m
//  
//
//  Created by 杜洁鹏 on 2019/10/8.
//

#import "EMContactManagerWrapper.h"
#import "EMSDKMethod.h"

@interface EMContactManagerWrapper () <EMContactManagerDelegate>

@end

@implementation EMContactManagerWrapper
- (instancetype)initWithChannelName:(NSString *)aChannelName
                          registrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    if(self = [super initWithChannelName:aChannelName
                           registrar:registrar]) {
       [EMClient.sharedClient.contactManager addDelegate:self delegateQueue:nil];
    }
    return self;
}


#pragma mark - FlutterPlugin

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    
    if ([EMMethodKeyAddContact isEqualToString:call.method]) {
        [self addContact:call.arguments
             channelName:call.method
                  result:result];
    } else if ([EMMethodKeyDeleteContact isEqualToString:call.method]) {
        [self deleteContact:call.arguments
                channelName:call.method
                     result:result];
    } else if ([EMMethodKeyGetAllContactsFromServer isEqualToString:call.method]) {
        [self getAllContactsFromServer:call.arguments
                           channelName:call.method
                                result:result];
    } else if ([EMMethodKeyGetAllContactsFromDB isEqualToString:call.method]) {
        [self getAllContactsFromDB:call.arguments
                       channelName:call.method
                            result:result];
    } else if ([EMMethodKeyAddUserToBlockList isEqualToString:call.method]) {
        [self addUserToBlockList:call.arguments
                     channelName:call.method
                          result:result];
    } else if ([EMMethodKeyRemoveUserFromBlockList isEqualToString:call.method]) {
        [self removeUserFromBlockList:call.arguments
                          channelName:call.method
                               result:result];
    } else if ([EMMethodKeyGetBlockListFromServer isEqualToString:call.method]) {
        [self getBlockListFromServer:call.arguments
                         channelName:call.method
                              result:result];
    } else if ([EMMethodKeyGetBlockListFromDB isEqualToString:call.method]){
        [self getBlockListFromDB:call.arguments
                     channelName:call.method
                          result:result];
    } else if ([EMMethodKeyAcceptInvitation isEqualToString:call.method]) {
        [self acceptInvitation:call.arguments
                   channelName:call.method
                        result:result];
    } else if ([EMMethodKeyDeclineInvitation isEqualToString:call.method]) {
        [self declineInvitation:call.arguments
                    channelName:call.method
                         result:result];
    } else if ([EMMethodKeyGetSelfIdsOnOtherPlatform isEqualToString:call.method]) {
        [self getSelfIdsOnOtherPlatform:call.arguments
                            channelName:call.method
                                 result:result];
    } else {
        [super handleMethodCall:call result:result];
    }
}


#pragma mark - Actions
- (void)addContact:(NSDictionary *)param channelName:(NSString *)aChannelName result:(FlutterResult)result {
    __weak typeof(self)weakSelf = self;
    NSString *username = param[@"username"];
    NSString *reason = param[@"reason"];
    [EMClient.sharedClient.contactManager addContact:username
                                             message:reason
                                          completion:^(NSString *aUsername, EMError *aError)
     {
        [weakSelf wrapperCallBack:result
                      channelName:aChannelName
                            error:aError
                           object:aUsername];
    }];
}

- (void)deleteContact:(NSDictionary *)param channelName:(NSString *)aChannelName result:(FlutterResult)result {
    __weak typeof(self)weakSelf = self;
    NSString *username = param[@"username"];
    BOOL keepConversation = [param[@"keepConversation"] boolValue];
    [EMClient.sharedClient.contactManager deleteContact:username
                                   isDeleteConversation:keepConversation
                                             completion:^(NSString *aUsername, EMError *aError)
     {
        [weakSelf wrapperCallBack:result
                      channelName:aChannelName
                            error:aError
                           object:aUsername];
    }];
}

- (void)getAllContactsFromServer:(NSDictionary *)param channelName:(NSString *)aChannelName result:(FlutterResult)result {
    __weak typeof(self)weakSelf = self;
    [EMClient.sharedClient.contactManager getContactsFromServerWithCompletion:^(NSArray *aList, EMError *aError)
     {
        [weakSelf wrapperCallBack:result
                      channelName:aChannelName
                            error:aError
                           object:aList];
    }];
}

- (void)getAllContactsFromDB:(NSDictionary *)param channelName:(NSString *)aChannelName result:(FlutterResult)result {
    __weak typeof(self)weakSelf = self;
    NSArray *aList = EMClient.sharedClient.contactManager.getContacts;
    [weakSelf wrapperCallBack:result
                  channelName:aChannelName
                        error:nil
                       object:aList];
}


- (void)addUserToBlockList:(NSDictionary *)param channelName:(NSString *)aChannelName result:(FlutterResult)result {
    __weak typeof(self)weakSelf = self;
    NSString *username = param[@"username"];
    [EMClient.sharedClient.contactManager addUserToBlackList:username
                                                  completion:^(NSString *aUsername, EMError *aError)
     {
        [weakSelf wrapperCallBack:result
                      channelName:aChannelName
                            error:aError
                           object:aUsername];
    }];
}

- (void)removeUserFromBlockList:(NSDictionary *)param channelName:(NSString *)aChannelName result:(FlutterResult)result {
    __weak typeof(self)weakSelf = self;
    NSString *username = param[@"username"];
    [EMClient.sharedClient.contactManager removeUserFromBlackList:username
                                                       completion:^(NSString *aUsername, EMError *aError)
     {
        [weakSelf wrapperCallBack:result
                      channelName:aChannelName
                            error:aError
                           object:aUsername];
    }];
    
}

- (void)getBlockListFromServer:(NSDictionary *)param channelName:(NSString *)aChannelName result:(FlutterResult)result {
    __weak typeof(self)weakSelf = self;
    [EMClient.sharedClient.contactManager getBlackListFromServerWithCompletion:^(NSArray *aList, EMError *aError)
     {
        [weakSelf wrapperCallBack:result
                      channelName:aChannelName
                            error:aError
                           object:aList];
    }];
}

- (void)getBlockListFromDB:(NSDictionary *)param channelName:(NSString *)aChannelName result:(FlutterResult)result {
    
    NSArray * list = [EMClient.sharedClient.contactManager getBlackList];
    [self wrapperCallBack:result channelName:aChannelName error:nil object:list];
}

- (void)acceptInvitation:(NSDictionary *)param channelName:(NSString *)aChannelName result:(FlutterResult)result {
    __weak typeof(self)weakSelf = self;
    NSString *username = param[@"username"];
    [EMClient.sharedClient.contactManager approveFriendRequestFromUser:username
                                                            completion:^(NSString *aUsername, EMError *aError)
     {
        [weakSelf wrapperCallBack:result
                      channelName:aChannelName
                            error:aError
                           object:aUsername];
    }];
}

- (void)declineInvitation:(NSDictionary *)param channelName:(NSString *)aChannelName result:(FlutterResult)result {
    __weak typeof(self)weakSelf = self;
    NSString *username = param[@"username"];
    [EMClient.sharedClient.contactManager declineFriendRequestFromUser:username
                                                            completion:^(NSString *aUsername, EMError *aError)
     {
        [weakSelf wrapperCallBack:result
                      channelName:aChannelName
                            error:aError
                           object:aUsername];
    }];
}

- (void)getSelfIdsOnOtherPlatform:(NSDictionary *)param channelName:(NSString *)aChannelName result:(FlutterResult)result {
    __weak typeof(self)weakSelf = self;
    [EMClient.sharedClient.contactManager getSelfIdsOnOtherPlatformWithCompletion:^(NSArray *aList, EMError *aError)
     {
        [weakSelf wrapperCallBack:result
                      channelName:aChannelName
                            error:aError
                           object:aList];
    }];
}


#pragma mark - EMContactManagerDelegate

- (void)friendshipDidAddByUser:(NSString *)aUsername {
    NSDictionary *map = @{
        @"type":@"onContactAdded",
        @"username":aUsername
    };
    [self.channel invokeMethod:EMMethodKeyOnContactChanged
                     arguments:map];
}

- (void)friendshipDidRemoveByUser:(NSString *)aUsername {
    NSDictionary *map = @{
        @"type":@"onContactDeleted",
        @"username":aUsername
    };
    [self.channel invokeMethod:EMMethodKeyOnContactChanged
                     arguments:map];
}

- (void)friendRequestDidReceiveFromUser:(NSString *)aUsername
                                message:(NSString *)aMessage {
    NSDictionary *map = @{
        @"type":@"onContactInvited",
        @"username":aUsername,
        @"reason":aMessage
    };
    [self.channel invokeMethod:EMMethodKeyOnContactChanged
                     arguments:map];
}

- (void)friendRequestDidApproveByUser:(NSString *)aUsername {
    NSDictionary *map = @{
        @"type":@"onFriendRequestAccepted",
        @"username":aUsername
    };
    [self.channel invokeMethod:EMMethodKeyOnContactChanged
                     arguments:map];
}

- (void)friendRequestDidDeclineByUser:(NSString *)aUsername {
    NSDictionary *map = @{
        @"type":@"onFriendRequestDeclined",
        @"username":aUsername
    };
    [self.channel invokeMethod:EMMethodKeyOnContactChanged
                     arguments:map];
}

@end
