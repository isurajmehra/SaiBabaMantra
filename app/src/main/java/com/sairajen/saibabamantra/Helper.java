package com.sairajen.saibabamantra;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

/**
 * @author Gmonetix
 */

public class Helper {

    public static void showDialog(Context context, String title, String content, String positiveText, int drawableId) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(content)
                .setIcon(drawableId)
                .setPositiveButton(positiveText,null)
                .show();
    }

    public static void openLink(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(Intent.createChooser(intent,"choose any one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareApp(Context context) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT,"sai mantra app");
        i.putExtra(Intent.EXTRA_TEXT,"https://play.gogle.com/store/apps/details?id=com.sairajen.saihere");
        context.startActivity(Intent.createChooser(i,"choosr any one"));
    }

}
