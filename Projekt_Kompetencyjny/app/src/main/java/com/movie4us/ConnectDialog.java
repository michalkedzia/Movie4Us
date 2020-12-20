package com.movie4us;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.google.gson.Gson;

public class ConnectDialog extends AppCompatDialogFragment {
    private Connection connection;
    private Message message;
    private Gson gson;
    private Intent intent;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        connection = Connection.getConnection();
        gson = new Gson();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    if (message.getAction().equals("connect")) {
      builder
          .setTitle("Połączono")
          .setMessage("Użytkownik '" + message.getConnectedUser() + "' zaakceptował zaproszenie.")
          .setNegativeButton(
              "OK",
              new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(intent);
                }
              });
        } else if(message.getAction().equals("reject")){
        builder
                .setTitle("Odrzucono")
                .setMessage("Użytkownik '" + message.getConnectedUser() + "' odrzucił zaproszenie.")
                .setNegativeButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        });
        }

        return builder.create();
    }
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
