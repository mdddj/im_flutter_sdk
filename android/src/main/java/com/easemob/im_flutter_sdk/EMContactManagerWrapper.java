package com.easemob.im_flutter_sdk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.json.JSONException;
import org.json.JSONObject;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;


public class EMContactManagerWrapper extends EMWrapper implements MethodCallHandler{

    EMContactManagerWrapper(PluginRegistry.Registrar registrar, String channelName) {
        super(registrar, channelName);
        registerEaseListener();
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {

        JSONObject param = (JSONObject)call.arguments;
        try {
            if (EMSDKMethod.addContact.equals(call.method))
            {
                addContact(param, EMSDKMethod.addContact, result);
            }
            else if(EMSDKMethod.deleteContact.equals(call.method))
            {
                deleteContact(param, EMSDKMethod.deleteContact, result);
            }
            else if(EMSDKMethod.getAllContactsFromServer.equals(call.method))
            {
                getAllContactsFromServer(param, EMSDKMethod.getAllContactsFromServer, result);
            }
            else if(EMSDKMethod.addUserToBlackList.equals(call.method))
            {
                addUserToBlackList(param, EMSDKMethod.getAllContactsFromServer, result);
            }
            else if(EMSDKMethod.removeUserFromBlackList.equals(call.method))
            {
                removeUserFromBlackList(param, EMSDKMethod.getAllContactsFromServer, result);
            }
            else if(EMSDKMethod.getBlackListFromServer.equals(call.method))
            {
                getBlackListFromServer(param, EMSDKMethod.getAllContactsFromServer, result);
            }
            else if(EMSDKMethod.acceptInvitation.equals(call.method))
            {
                acceptInvitation(param, EMSDKMethod.getAllContactsFromServer, result);
            }
            else if(EMSDKMethod.declineInvitation.equals(call.method))
            {
                declineInvitation(param, EMSDKMethod.getAllContactsFromServer, result);
            }
            else if(EMSDKMethod.getSelfIdsOnOtherPlatform.equals(call.method))
            {
                getSelfIdsOnOtherPlatform(param, EMSDKMethod.getAllContactsFromServer, result);
            }
            else  {
                super.onMethodCall(call, result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addContact(JSONObject param, String channelName, Result result) throws JSONException {
        String username = param.getString("username");
        String reason = param.getString("reason");

        asyncRunnable(() -> {
            try {
                EMClient.getInstance().contactManager().addContact(username, reason);
                onSuccess(result, channelName, username);
            }catch (HyphenateException e) {
                onError(result, e);
            }
        });
    }

    private void deleteContact(JSONObject param, String channelName, Result result) throws JSONException {
        String username = param.getString("username");
        boolean keepConversation = param.getBoolean("keepConversation");
        asyncRunnable(() -> {
            try {
                EMClient.getInstance().contactManager().deleteContact(username, keepConversation);
                onSuccess(result, channelName, username);
            }catch (HyphenateException e) {
                onError(result, e);
            }
        });
    }

    private void getAllContactsFromServer(JSONObject params, String channelName,  Result result) throws JSONException {
        asyncRunnable(() -> {
            try {
                List contacts =  EMClient.getInstance().contactManager().getAllContactsFromServer();
                onSuccess(result, channelName, contacts);
            }catch (HyphenateException e) {
                onError(result, e);
            }
        });
    }

    private void addUserToBlackList(JSONObject params, String channelName,  Result result) throws JSONException {
        String username = params.getString("username");
        asyncRunnable(() -> {
            try {
                EMClient.getInstance().contactManager().addUserToBlackList(username, false);
                onSuccess(result, channelName, username);
            }catch (HyphenateException e) {
                onError(result, e);
            }
        });
    }

    private void removeUserFromBlackList(JSONObject params, String channelName,  Result result) throws JSONException {
        String username = params.getString("username");
        asyncRunnable(() -> {
            try {
                EMClient.getInstance().contactManager().removeUserFromBlackList(username);
                onSuccess(result, channelName, username);
            }catch (HyphenateException e) {
                onError(result, e);
            }
        });
    }

    private void getBlackListFromServer(JSONObject params, String channelName,  Result result) throws JSONException {
        asyncRunnable(() -> {
            try {
                List contacts = EMClient.getInstance().contactManager().getBlackListFromServer();
                onSuccess(result, channelName, contacts);
            }catch (HyphenateException e) {
                onError(result, e);
            }
        });
    }

    private void acceptInvitation(JSONObject params, String channelName,  Result result) throws JSONException {
        String username = params.getString("username");
        asyncRunnable(() -> {
            try {
                EMClient.getInstance().contactManager().acceptInvitation(username);
                onSuccess(result, channelName, username);
            }catch (HyphenateException e) {
                onError(result, e);
            }
        });
    }

    private void declineInvitation(JSONObject params, String channelName,  Result result) throws JSONException {
        String username = params.getString("username");
        asyncRunnable(() -> {
            try {
                EMClient.getInstance().contactManager().declineInvitation(username);
                onSuccess(result, channelName, username);
            }catch (HyphenateException e) {
                onError(result, e);
            }
        });
    }

    private void getSelfIdsOnOtherPlatform(JSONObject params, String channelName,  Result result) throws JSONException {
        asyncRunnable(() -> {
            try {
                List platforms = EMClient.getInstance().contactManager().getSelfIdsOnOtherPlatform();
                onSuccess(result, channelName, platforms);
            }catch (HyphenateException e) {
                onError(result, e);
            }
        });

    }

    private void registerEaseListener(){
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String userName) {
                Map<String, Object> data = new HashMap<>();
                data.put("type", "onContactAdded");
                data.put("username", userName);
                post(() -> channel.invokeMethod(EMSDKMethod.onContactChanged, data));

            }

            @Override
            public void onContactDeleted(String userName) {
                Map<String, Object> data = new HashMap<>();
                data.put("type", "onContactDeleted");
                data.put("username", userName);
                post(() -> channel.invokeMethod(EMSDKMethod.onContactChanged, data));
            }

            @Override
            public void onContactInvited(String userName, String reason) {
                Map<String, Object> data = new HashMap<>();
                data.put("type", "onContactInvited");
                data.put("username", userName);
                data.put("reason", reason);
                post(() -> channel.invokeMethod(EMSDKMethod.onContactChanged, data));
            }

            @Override
            public void onFriendRequestAccepted(String userName) {
                Map<String, Object> data = new HashMap<>();
                data.put("type", "onFriendRequestAccepted");
                data.put("username", userName);
                post(() -> channel.invokeMethod(EMSDKMethod.onContactChanged, data));
            }

            @Override
            public void onFriendRequestDeclined(String userName) {
                Map<String, Object> data = new HashMap<>();
                data.put("type", "onFriendRequestDeclined");
                data.put("username", userName);
                post(() -> channel.invokeMethod(EMSDKMethod.onContactChanged, data));
            }
        });
    }
}
