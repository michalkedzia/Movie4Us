package dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.google.gson.Gson;
import com.movie4us.Connection;
import model.Message;

/**
 * Klasa AcceptDialog tworzy dialog z informacją o zaproszeniu od innego użytkownika. Klasa jest
 * wywoływana z poziomu MainActivity i wykorzystywana do komunikacji z serwerem w celu potwierdzenia
 * bądź odrzucenia komunikacji/połączenia między dwoma użytkownikami.
 */
public class AcceptDialog extends AppCompatDialogFragment {

  /** Pole zawierające informacje o połączeniu z serwerem */
  private Connection connection;
  /** Pole klasy message zawierające informacje o przychodzącej wiadomości od serwera */
  private Message message;
  /** Obiekt gson do wysłania wiadomości dla serwera */
  private Gson gson;

  /**
   * Metoda onCreateDialog tworzy nowy dialog poprzez wbudowanego buildera w bibliotekach
   * AlertDialog. Dialog wyświetla informację o chęci połączenia od innego użytkownika, wyświetlając
   * jego nazwę, wyciąganą z wiadomości od serwera (message). Metoda implementuje dwa przyciski, do
   * akceptacji i odrzucenia połączenia. W zależności od wybranej opcji z pozycji tej klasy
   * AcceptDialogu wysyłana jest informacja do serwera z zaistniałą interakcją (accept/reject).
   *
   * @param savedInstanceState
   * @return
   */
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    connection = Connection.getConnection();
    gson = new Gson();

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder
        .setTitle("User requested connection")
        .setMessage("Do you want to connect with user: '" + message.getConnectedUser() + "'")
        .setNegativeButton(
            "Reject",
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
            "Accept",
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
