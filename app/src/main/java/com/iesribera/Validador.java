package com.iesribera;

import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Validador {

	public boolean validarEntradasDeDatos(EditText... fields) {
		boolean valid = false;
		String mensajeError = "Este campo no puede estar vacío";
		for (EditText field : fields) {
			valid = false;
			if (field == null || field.getText().toString().isEmpty()) {
				Objects.requireNonNull(field).setError(mensajeError);
			} else {
				valid = true;
			}
		}
		return valid;
	}
}
