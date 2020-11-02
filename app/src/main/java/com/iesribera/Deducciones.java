package com.iesribera;

import java.math.BigDecimal;

public class Deducciones extends TramosIrpf {

	public static final BigDecimal RENDIMIENTOS_TRABAJO = BigDecimal.valueOf(2000);
	public static final BigDecimal PORCENTAJE_SEGURIDAD_SOCIAL = BigDecimal.valueOf(6.35);

	public Deducciones(BigDecimal minimo, BigDecimal maximo, BigDecimal porcentaje) {
		super(minimo, maximo, porcentaje);
	}
}
