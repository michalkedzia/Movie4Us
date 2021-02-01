package dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.movie4us.GenreSelectionActivity;

/**
 * Klasa InfoDialog tworzy Dialog wyświetlający ustawione informacje. W zależności od ustawienia
 * parametrów, możemy wyświetlić tytuł dialogu, informację jaką dany dialog ma zawierać, oraz polę
 * klasy GenreSelectionActivity, w której jest ten dialog tworzony.
 */
public class InfoDialog extends AppCompatDialogFragment {

  /** Tytuł wyświetlanego dialogu */
  private String title;
  /** Tekst wyświetlanej wiadomości */
  private String message;
  /** Obiekt klasy GenreSelectionActivity w której jest wyświetlany dialog */
  private GenreSelectionActivity genreSelectionActivity;

  public InfoDialog(String title, String message, GenreSelectionActivity genreSelectionActivity) {
    this.title = title;
    this.message = message;
    this.genreSelectionActivity = genreSelectionActivity;
  }

  /**
   * Metoda tworząca za pomocą bilbitoeki AlertDialog, dialog z ustawioną informacją dla
   * użytkownika. W przypadku klasy GenreSelectionActivity, gdzie dialog jest wyświetlany jest to
   * informacja z wyjściem drugiego użytkownika z aktualnego okna.
   *
   * <p>Po potwierdzeniu wyświetlanego dialogu z informacją o odrzuceniu przez jej zatwierdzenie,
   * następuje wyjście z activity w którym ten dialog został uruchomiony. W tym przypadku jest to
   * GenreSelectionActivity.
   *
   * @param savedInstanceState Służy do przekazania informacji o stanie aktywności aplikacji
   *     Android'a.
   * @return builder.create() zwraca wywołanie dialogu
   */
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton(
            "OK",
            (dialog, which) -> {
              this.genreSelectionActivity.finish();
            });
    return builder.create();
  }
}