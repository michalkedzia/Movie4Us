package dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.google.gson.Gson;
import com.movie4us.Connection;
import model.Message;

public class AcceptDialog extends AppCompatDialogFragment {
  private Connection connection;
  private Message message;
  private Gson gson;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    connection = Connection.getConnection();
    gson = new Gson();

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder
        .setTitle("Znaleziono użytkownika")
        .setMessage(
            "Czy chcesz zaakceptować połączenie od użytkownika '"
                + message.getConnectedUser()
                + "'")
        .setNegativeButton(
            "Odrzuć",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                connection
                    .getExecutorService()
                    .execute(
                        () -> {
                          message.setUsername(connection.getUsername());
                          message.setAction("reject");
                          connection.send(gson.toJson(message));
                        });
              }
            })
        .setPositiveButton(
            "Akceptuj",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                connection
                    .getExecutorService()
                    .execute(
                        () -> {
                          message.setUsername(connection.getUsername());
                          message.setAction("connect");
                          connection.send(gson.toJson(message));
                        });
              }
            });

    return builder.create();
  }

  public Message getMessage() {
    return message;
  }

  public void setMessage(Message message) {
    this.message = message;
  }
}
