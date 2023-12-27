package com.zero07.rssb;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class MyTask extends AsyncTask<Void, Void, Void> {
    ProgressDialog dialog;
    Context context;
    public MyTask(Context context)
    {
        this.context=context;
        //constructor for this class
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }

    protected void onPreExecute() {
        //create the progress dialog as
        dialog=new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.show();
    }

    protected void onPostExecute(Void unused) {
        //dismiss the progressdialog
      //  dialog.dismiss();
    }
}
