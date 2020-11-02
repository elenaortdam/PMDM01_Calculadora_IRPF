package com.iesribera;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

	private DatePicker picker;
	private EditText fechaNacimiento;
	private LocalDate fechaSeleccionada;
	private EditText numeroHijos;
	private EditText salarioBruto;
	private EditText retencionTotal;
	private Button calcular;
	Calendar calendario = Calendar.getInstance();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			calcular = (Button) findViewById(R.id.calcularRetencion);
			fechaNacimiento = findViewById(R.id.editTextFechaNacimiento);
			mostrarDatePicker();
			calcular.setOnClickListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void mostrarDatePicker() {
		this.fechaNacimiento.setShowSoftInputOnFocus(false);
		this.fechaNacimiento.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

				DatePickerDialog dialog =
						new DatePickerDialog(MainActivity.this,
											 android.R.style.Theme_Holo_Dialog,
											 MainActivity.this,
											 calendar.get(Calendar.YEAR),
											 calendar.get(Calendar.MONTH),
											 calendar.get(Calendar.DAY_OF_MONTH));
				dialog.show();
			}
		});
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		this.fechaSeleccionada =  LocalDate.of(year, monthOfYear, dayOfMonth);
		if(this.fechaSeleccionada.isAfter(LocalDate.now())){
			this.fechaNacimiento.setError("La fecha de nacimiento no puede ser posterior a la actual");
		}
		this.fechaNacimiento.setText(this.fechaSeleccionada.toString());
	}

	@Override
	public void onClick(View v) {

		numeroHijos = findViewById(R.id.editTextNumeroHijos);
		salarioBruto = findViewById(R.id.editTextSalarioBruto);
		Validador validador = new Validador();
		if (validador.validarEntradasDeDatos(this.numeroHijos, this.fechaNacimiento, this.salarioBruto)) {
			retencionTotal = findViewById(R.id.editTextTotalRetencion);

			CalculadoraIrpf calculadoraIrpf = new CalculadoraIrpf();
			BigDecimal total = calculadoraIrpf.calcularIrpf(fechaSeleccionada,
															new BigDecimal(numeroHijos.getText().toString()),
															new BigDecimal(salarioBruto.getText().toString()));
			System.out.println(retencionTotal);
			retencionTotal.setText(String.valueOf(total));
		}

	}
}