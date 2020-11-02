package com.iesribera;

import java.math.BigDecimal;

public class TablasIrpf {

	private final TramosIrpf[] tramosIrpf = completarTablaIRPF();
	private final Deducciones[] minimoPersonal = completarMinimoPersonal();
	private final Deducciones[] descendientes = completarTablaDescendientes();

	private TramosIrpf[] completarTablaIRPF() {
		return new TramosIrpf[]{
				new TramosIrpf(BigDecimal.valueOf(0),
							   BigDecimal.valueOf(12450),
							   BigDecimal.valueOf(19)),
				new TramosIrpf(BigDecimal.valueOf(12450),
							   BigDecimal.valueOf(20200),
							   BigDecimal.valueOf(24)),
				new TramosIrpf(BigDecimal.valueOf(20200),
							   BigDecimal.valueOf(35200),
							   BigDecimal.valueOf(30)),
				new TramosIrpf(BigDecimal.valueOf(35200),
							   BigDecimal.valueOf(60000),
							   BigDecimal.valueOf(37)),
				new TramosIrpf(BigDecimal.valueOf(60000),
							   BigDecimal.valueOf(Integer.MAX_VALUE),
							   BigDecimal.valueOf(45))

		};
	}

	private Deducciones[] completarMinimoPersonal() {
		return new Deducciones[]{
				new Deducciones(BigDecimal.valueOf(0),
								BigDecimal.valueOf(65),
								BigDecimal.valueOf(5550)),
				new Deducciones(BigDecimal.valueOf(66),
								BigDecimal.valueOf(75),
								BigDecimal.valueOf(6468)),
				new Deducciones(BigDecimal.valueOf(76),
								BigDecimal.valueOf(1000),
								BigDecimal.valueOf(7590)),

		};

	}

	private Deducciones[] completarTablaDescendientes() {
		return new Deducciones[]{
				new Deducciones(BigDecimal.valueOf(0),
								BigDecimal.valueOf(1),
								BigDecimal.valueOf(2400)),
				new Deducciones(BigDecimal.valueOf(1),
								BigDecimal.valueOf(2),
								BigDecimal.valueOf(2700)),
				new Deducciones(BigDecimal.valueOf(2),
								BigDecimal.valueOf(3),
								BigDecimal.valueOf(4000)),
				new Deducciones(BigDecimal.valueOf(3),
								BigDecimal.valueOf(4),
								BigDecimal.valueOf(4500)),

		};
	}


	public Deducciones[] getMinimoPersonal() {
		return minimoPersonal;
	}

	public Deducciones[] getDescendientes() {
		return descendientes;
	}

	public TramosIrpf[] getTramosIrpf() {
		return tramosIrpf;
	}
}
