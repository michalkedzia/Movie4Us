package com.movie4us;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.google.gson.Gson;
import model.Message;

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
          .setTitle("Connected")
          .setMessage("User '" + message.getConnectedUser() + "' accepted your invite.")
          .setNegativeButton(
              "OK",
              new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  startActivity(intent);
                }
              });
    } else if (message.getAction().equals("reject")) {
      builder
          .setTitle("Rejected")
          .setMessage("User '" + message.getConnectedUser() + "' rejected your invite.")
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
