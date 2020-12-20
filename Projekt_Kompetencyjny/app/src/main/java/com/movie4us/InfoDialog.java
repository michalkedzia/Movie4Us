package com.movie4us;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;

public class InfoDialog extends AppCompatDialogFragment {

  private String title;
  private String message;

  public InfoDialog(String title, String message) {
    this.title = title;
    this.message = message;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(title).setMessage(message).setNegativeButton("OK", (dialog, which) -> {});
    return builder.create();
  }
}
