package dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.movie4us.GenreSelectionActivity;

public class InfoDialog extends AppCompatDialogFragment {

  private String title;
  private String message;
  private GenreSelectionActivity genreSelectionActivity;

  public InfoDialog(String title, String message, GenreSelectionActivity genreSelectionActivity) {
    this.title = title;
    this.message = message;
    this.genreSelectionActivity = genreSelectionActivity;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(title).setMessage(message).setNegativeButton("OK", (dialog, which) -> {this.genreSelectionActivity.finish();});
    return builder.create();
  }
}
