package com.movie4us;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.google.gson.Gson;
import model.Message;

/**
 * Klasa ConnectDialog tworzy Dialog wyświetlający informację o połączeniu. W zależności od
 * otrzymanej wiadomości z serwera, tworzony Dialog może mieć dwie postaci: informację o
 * zaakceptowaniu połączenia od drugiego użytkownika i informacji o odrzuceniu takiego zaporoszenia.
 */
public class ConnectDialog extends AppCompatDialogFragment {
  /** Informacja o połączeniu z serwerem */
  private Connection connection;
  /** Informacja z wiadomością odnośnie połączenia od serwera. */
  private Message message;
  /** Obiekt gson */
  private Gson gson;
  /** Obiekt klasy intent przechowywujący nowe activity. */
  private Intent intent;

  /**
   * Metoda onCreateDialog tworzy widok Dialog'u za pomocą biblioteki AlertDialog i wbudowanych
   * metod Builder'a do ustawienia odpowiednich parametrów wyświetlanego okna pop-up. W zależności
   * od wartości akcji otrzymanej z wiadomości od serwera, tworzony jest dialog z informacją o
   * akceptacji połączenia z drugim użytkownikiem i uruchamiana jest nowa klasa activity z nowym
   * oknem, bądź w przypadku odrzucenia połączenia, wyświetlana jest tylko informacja o odrzuceniu
   * połączenia.
   *
   * @param savedInstanceState Służy do przekazania informacji o stanie aktywności aplikacji
   *     Android'a.
   * @return builder.create() zwraca wywołanie dialogu
   */
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

  /** @return Metoda zwraca klasę Message. */
  public Message getMessage() {
    return message;
  }

  /** @param message Ustawienie klasy Message w ConnectDialog. */
  public void setMessage(Message message) {
    this.message = message;
  }

  /**
   * @return Metoda zwraca klasę Intent.
   * @see Intent
   */
  public Intent getIntent() {
    return intent;
  }

  /** @param intent - Ustawienie klasy Intent w ConnectDialog. */
  public void setIntent(Intent intent) {
    this.intent = intent;
  }
}
