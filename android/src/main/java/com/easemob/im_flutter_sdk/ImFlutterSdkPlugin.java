package com.easemob.im_flutter_sdk;

import android.os.Handler;
import android.os.Looper;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.util.EMLog;

import io.flutter.plugin.common.JSONMethodCodec;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.common.MethodChannel.Result;

import java.util.HashMap;
import java.util.Map;



/**
 * ImFlutterSdkPlugin
 */
public class ImFlutterSdkPlugin {

    static final Handler handler = new Handler(Looper.getMainLooper());

    private ImFlutterSdkPlugin() {
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        new EMClientWrapper(registrar, "em_client");
    }

    /*
    private static void registerChatManagerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL_PREFIX + "/em_chat_manager", JSONMethodCodec.INSTANCE);
        channel.setMethodCallHandler(new EMChatManagerWrapper(channel));
    }

    private static void registerContactManagerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL_PREFIX + "/em_contact_manager", JSONMethodCodec.INSTANCE);
        channel.setMethodCallHandler(new EMContactManagerWrapper(channel));
    }

    private static void registerConversationWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL_PREFIX + "/em_conversation", JSONMethodCodec.INSTANCE);
        channel.setMethodCallHandler(new EMConversationWrapper());
    }

    private static void registerEMChatRoomManagerWrapper(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL_PREFIX + "/em_chat_room_manager", JSONMethodCodec.INSTANCE);
        channel.setMethodCallHandler(new EMChatRoomManagerWrapper(channel));
    }

    private static void registerGroupManagerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL_PREFIX + "/em_group_manager", JSONMethodCodec.INSTANCE);
        channel.setMethodCallHandler(new EMGroupManagerWrapper(channel));
    }

    private static void registerPushManagerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL_PREFIX + "/em_push_manager", JSONMethodCodec.INSTANCE);
        channel.setMethodCallHandler(new EMPushManagerWrapper());
    }
    */
}


class EMWrapperCallBack implements EMCallBack {

    EMWrapperCallBack(Result result, String channelName, Object object) {
        this.result = result;
        this.channelName = channelName;
        this.object = object;
    }

    Result result;
    String channelName;
    Object object;

    void post(Runnable runnable) {
        ImFlutterSdkPlugin.handler.post(runnable);
    }

    @Override
    public void onSuccess() {
        post(() -> {
            Map<String, Object> data = new HashMap<>();
            if (object != null) {
                data.put(channelName, object);
            }
            result.success(data);
        });
    }

    @Override
    public void onError(int code, String desc) {
        post(() -> {
            Map<String, Object> data = new HashMap<>();
            data.put("error", EMErrorHelper.toJson(code, desc));
            EMLog.e("callback", "onError");
            result.success(data);
        });
    }

    @Override
    public void onProgress(int progress, String status) {
        // no need
    }
}


class EMValueWrapperCallBack<T> implements EMValueCallBack<T> {

    EMValueWrapperCallBack(MethodChannel.Result result)
    {
        this.result = result;
    }

    private MethodChannel.Result result;

    private void post(Runnable runnable) {
        ImFlutterSdkPlugin.handler.post(runnable);
    }

    @Override
    public void onSuccess(Object value) {
//        post(new Runnable() {
//                 @Override
//                 public void run() {
//                     Map<String, Object> data = new HashMap<String, Object>();
//                     data.put("success", Boolean.TRUE);
//                     if (value.getClass().getSimpleName().equals("ArrayList")) {
//                         if (((List) value).size() > 0) {
//                             Object o = ((List) value).get(0);
//                             if (o.getClass().getSimpleName().equals("EMGroup")) {
//                                 List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
//                                 for (EMGroup emGroup : (List<EMGroup>) value) {
//                                     list.add(EMHelper.convertEMGroupToStringMap(emGroup));
//                                 }
//                                 data.put("value", list);
//                             }
//
//                             if (o.getClass().getSimpleName().equals("String")) {
//                                 data.put("value", value);
//                             }
//
//                             if (o.getClass().getSimpleName().equals("EMMucSharedFile")) {
//                                 List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
//                                 for (EMMucSharedFile file : (List<EMMucSharedFile>) value) {
//                                     list.add(EMHelper.convertEMMucSharedFileToStringMap(file));
//                                 }
//                                 data.put("value", list);
//                             }
//                         }
//                     }
//
//                     if (value.getClass().getSimpleName().equals("EMGroup")) {
//                         data.put("value", EMHelper.convertEMGroupToStringMap((EMGroup) value));
//                     }
//
//                     if (value.getClass().getSimpleName().equals("EMCursorResult")) {
//                         data.put("value", EMHelper.convertEMCursorResultToStringMap((EMCursorResult) value));
//                     }
//
//                     if (value.getClass().getSimpleName().equals("EMPageResult")) {
//                         EMPageResult result = (EMPageResult) value;
//                         if (((List) (result.getData())).get(0).getClass().getSimpleName().equals("EMChatRoom")) {
//                             data.put("value", EMHelper.convertEMPageResultToStringMap(result));
//                         }
//                     }
//
//                     if (value.getClass().getSimpleName().equals("EMChatRoom")) {
//                         data.put("value", EMHelper.convertEMChatRoomToStringMap((EMChatRoom) value));
//                     }
//
//                     if (value.getClass().getSimpleName().equals("HashMap")) {
//                         List<String> dataList = new LinkedList<String>();
//                         for (Map.Entry<String, Long> m : ((Map<String, Long>) value).entrySet()) {
//                             dataList.add(m.getKey());
//                         }
//                         data.put("value", dataList);
//                     }
//
//                     if (value.getClass().getSimpleName().equals("String")) {
//                         data.put("value", value);
//                     }
//
//                     result.success(data);
//                 }
//             }
//        );
    }

    @Override
    public void onError(int code, String desc) {
        post(() -> {
            Map<String, Object> data = new HashMap<>();
            data.put("error", EMErrorHelper.toJson(code, desc));
            EMLog.e("callback", "onError");
            result.success(data);
        });
    }
}