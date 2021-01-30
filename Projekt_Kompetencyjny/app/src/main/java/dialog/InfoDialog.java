package dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.movie4us.GenreSelectionActivity;

/**
 * Klasa tworząca Dialog z informacja o zdarzeniu i wy śweitla go uzytkownikowi.
 */
public class InfoDialog extends AppCompatDialogFragment {

  /**
   * Tytuł okna
   */
 private String title;
  /**
   * treść wiadomości dla użytkownika
   */
 private String message;
  /**
   * Activity na którym ma zostac wyświetlony Dialog
   */
 private GenreSelectionActivity genreSelectionActivity;

  public InfoDialog(String title, String message, GenreSelectionActivity genreSelectionActivity) {
    this.title = title;
    this.message = message;
    this.genreSelectionActivity = genreSelectionActivity;
  }

  /**
   * Builder Dialogu
   * @param savedInstanceState
   * @return
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
