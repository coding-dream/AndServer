package com.yanzhenjie.andserver.sample.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by wl on 2018/12/26.
 */
public class CopyHelper {

    public static void copy(Context context, String content) {
        ClipboardManager cbm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("myLabel", content);
        cbm.setPrimaryClip(clipData);
        Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
    }

    public static String getClipContent(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = cm.getPrimaryClip();
        ClipData.Item item = null;
        if (data != null) {
            item = data.getItemAt(0);
        }
        if (item != null) {
            return item.getText().toString();
        }
        return "";
    }
}