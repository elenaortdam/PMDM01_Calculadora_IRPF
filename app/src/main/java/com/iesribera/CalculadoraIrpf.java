package com.iesribera;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class CalculadoraIrpf {

	public BigDecimal calcularIrpf(LocalDate fechaNacimiento,
								   BigDecimal numeroHijos, BigDecimal salario) {

		TablasIrpf tablasIrpf = new TablasIrpf();
		BigDecimal baseImponible = calcularBaseImponible(salario).setScale(2, RoundingMode.HALF_UP);
		BigDecimal minimoFamiliar = calcularMinimoPersonal(fechaNacimiento,
														   tablasIrpf.getMinimoPersonal())
				.add(calcularDeduccionDescendientes(numeroHijos, tablasIrpf.getDescendientes()));

		BigDecimal retencionBaseImponible = calcularRetencion(baseImponible,
															  tablasIrpf.getTramosIrpf());
		BigDecimal retencionMinimoFamiliar = calcularRetencion(minimoFamiliar,
															   tablasIrpf.getTramosIrpf());

		return retencionBaseImponible.subtract(retencionMinimoFamiliar).setScale(2,
																				 RoundingMode.HALF_UP);

	}

	private BigDecimal calcularBaseImponible(BigDecimal salario) {
		BigDecimal deduccionesTrabajo = Deducciones.RENDIMIENTOS_TRABAJO;
		BigDecimal deduccionesSeguridadSocial =
				salario.multiply(Deducciones.PORCENTAJE_SEGURIDAD_SOCIAL
										 .movePointLeft(2));
		BigDecimal baseImponible = salario.subtract(deduccionesTrabajo)
										  .subtract(deduccionesSeguridadSocial);
		if (baseImponible.compareTo(BigDecimal.ZERO) < 0) {
			baseImponible = BigDecimal.ZERO;
		}
		return baseImponible;
	}

	private BigDecimal calcularMinimoPersonal(LocalDate fechaNacimiento, Deducciones[] tablaMinimoPersonal) {
		BigDecimal edad = BigDecimal.valueOf(LocalDate.now().getYear() - fechaNacimiento.getYear());
		BigDecimal deduccionMinimoPersonal = BigDecimal.ZERO;

		for (Deducciones minimoPersonal : tablaMinimoPersonal) {
			if (edad.compareTo(minimoPersonal.getMaximo()) < 0) {
				deduccionMinimoPersonal = minimoPersonal.getValor();
				break;
			}
		}
		return deduccionMinimoPersonal;
	}

	private BigDecimal calcularDeduccionDescendientes(BigDecimal numeroHijos,
													  Deducciones[] deduccionesPorDescendientes) {
		BigDecimal totalDeduccion = BigDecimal.ZERO;

		for (Deducciones deduccion : deduccionesPorDescendientes) {
			if (deduccion.getMaximo().compareTo(numeroHijos) <= 0) {
				totalDeduccion =
						totalDeduccion.add(deduccion.getValor());
			}
		}
		return totalDeduccion;
	}

	private BigDecimal calcularRetencion(BigDecimal cantidad, TramosIrpf[] tramosRetencion) {
		BigDecimal cantidadRetenidaTotal = BigDecimal.ZERO;
		BigDecimal cantidadRestante = cantidad;
		for (TramosIrpf retencion : tramosRetencion) {
			BigDecimal cantidadRetenida;
			BigDecimal diferenciaTramo = retencion.getMaximo()
												  .subtract(retencion.getMinimo());
			if (cantidad.compareTo(retencion.getMaximo()) > 0) {
				cantidadRetenida = diferenciaTramo;
			} else {
				cantidadRetenida = cantidadRestante;
			}
			cantidadRetenida = cantidadRetenida.multiply(retencion.getValor()
																  .movePointLeft(2));
			cantidadRetenidaTotal = cantidadRetenidaTotal.add(cantidadRetenida);
			cantidadRestante = cantidadRestante.subtract(diferenciaTramo);
			if (cantidadRestante.compareTo(BigDecimal.ZERO) < 0) {
				break;
			}
		}
		return cantidadRetenidaTotal;
	}
}
