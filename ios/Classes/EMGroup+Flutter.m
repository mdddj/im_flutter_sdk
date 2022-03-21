//
//  EMGroup+Flutter.m
//  im_flutter_sdk
//
//  Created by 杜洁鹏 on 2020/9/28.
//

#import "EMGroup+Flutter.h"

@implementation EMGroup (Flutter)


- (NSDictionary *)toJson {
    NSMutableDictionary *ret = [NSMutableDictionary dictionary];
    ret[@"groupId"] = self.groupId;
    ret[@"name"] = self.groupName;
    ret[@"desc"] = self.description;
    ret[@"owner"] = self.owner;
    ret[@"announcement"] = self.announcement;
    ret[@"memberCount"] = @(self.occupantsCount);
    ret[@"memberList"] = self.memberList;
    ret[@"adminList"] = self.adminList;
    ret[@"blockList"] = self.blacklist;
    ret[@"muteList"] = self.muteList;
    ret[@"noticeEnable"] = @(self.isPushNotificationEnabled);
    ret[@"messageBlocked"] = @(self.isBlocked);
    ret[@"isAllMemberMuted"] = @(self.isMuteAllMembers);
    ret[@"options"] = [self.setting toJson];
    ret[@"permissionType"] = @([EMGroup premissionTypeToInt:self.permissionType]);
    
    return ret;
}

+ (int)premissionTypeToInt:(EMGroupPermissionType)type {
    int ret = -1;
    switch (type) {
        case EMGroupPermissionTypeNone:
        {
            ret = -1;
        }
            break;
        case EMGroupPermissionTypeMember:
        {
            ret = 0;
        }
            break;
        case EMGroupPermissionTypeAdmin:
        {
            ret = 1;
        }
            break;
        case EMGroupPermissionTypeOwner:
        {
            ret = 2;
        }
            break;
        default:
            break;
    }
    return ret;
}

+ (EMGroupPermissionType)premissionTypeFromInt:(int)type {
    EMGroupPermissionType ret = EMGroupPermissionTypeMember;
    switch (type) {
        case -1:
        {
            ret = EMGroupPermissionTypeNone;
        }
            break;
        case 0:
        {
            ret = EMGroupPermissionTypeMember;
        }
            break;
        case 1:
        {
            ret = EMGroupPermissionTypeAdmin;
        }
            break;
        case 2:
        {
            ret = EMGroupPermissionTypeOwner;
        }
            break;
        default:
            break;
    }
    return ret;
}

@end

@implementation EMGroupOptions (Flutter)
- (NSDictionary *)toJson {
    NSMutableDictionary *ret = [NSMutableDictionary dictionary];
    ret[@"maxCount"] = @(self.maxUsersCount);
    ret[@"ext"] = self.ext;
    ret[@"style"] = @([EMGroupOptions styleToInt:self.style]);
    ret[@"inviteNeedConfirm"] = @(self.IsInviteNeedConfirm);
    return ret;
}

+ (EMGroupOptions *)formJson:(NSDictionary *)dict {
    EMGroupOptions *options = [[EMGroupOptions alloc] init];
    options.maxUsersCount = [dict[@"maxCount"] intValue];
    options.ext = dict[@"ext"];
    options.IsInviteNeedConfirm = [dict[@"inviteNeedConfirm"] boolValue];
    options.style = [EMGroupOptions styleFromInt:[dict[@"style"] intValue]];
    return options;
}

+ (EMGroupStyle)styleFromInt:(int)style {
    EMGroupStyle ret = EMGroupStylePrivateOnlyOwnerInvite;
    switch (style) {
        case 0:{
            ret = EMGroupStylePrivateOnlyOwnerInvite;
        }
            break;
        case 1:{
            ret = EMGroupStylePrivateMemberCanInvite;
        }
            break;
        case 2:{
            ret = EMGroupStylePublicJoinNeedApproval;
        }
            break;
        case 3:{
            ret = EMGroupStylePublicOpenJoin;
        }
            break;
        default:
            break;
    }
    
    return ret;
}

+ (int)styleToInt:(EMGroupStyle)style {
    int ret = 0;
    switch (style) {
        case EMGroupStylePrivateOnlyOwnerInvite:{
            ret = 0;
        }
            break;
        case EMGroupStylePrivateMemberCanInvite:{
            ret = 1;
        }
            break;
        case EMGroupStylePublicJoinNeedApproval:{
            ret = 2;
        }
            break;
        case EMGroupStylePublicOpenJoin:{
            ret = 3;
        }
            break;
        default:
            break;
    }
    
    return ret;
}

@end


@implementation EMGroupSharedFile (Flutter)
- (NSDictionary *)toJson {
    NSMutableDictionary *data = [NSMutableDictionary dictionary];
    data[@"fileId"] = self.fileId;
    data[@"name"] = self.fileName;
    data[@"owner"] = self.fileOwner;
    data[@"createTime"] = @(self.createTime);
    data[@"fileSize"] = @(self.fileSize);
    return data;
}

@end
