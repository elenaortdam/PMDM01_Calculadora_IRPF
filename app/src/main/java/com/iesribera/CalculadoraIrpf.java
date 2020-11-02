package com.iesribera;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class CalculadoraIrpf {

	/**
	 * Método para calcular el IRPF
	 *
	 * @param fechaNacimiento usada para calcular la edad de la persona
	 * @param numeroHijos     cantidad de hijos que tiene esa persona
	 * @param salario         Salario bruto anual
	 * @return retención del IRPF a practicar con los datos introducidos
	 */
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

	/**
	 * Calcula la base imponible a partir del salario
	 *
	 * @param salario bruto anual
	 * @return base imponible para un salario dado
	 */
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

	/**
	 * Devuelve el mínimo personal a partir de la edad de la persona
	 *
	 * @param fechaNacimiento     usada para calcular la edad
	 * @param tablaMinimoPersonal tabla de deduciones por minimo personal
	 * @return mínimo personal calculado
	 */
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

	/**
	 * Devuelve la deducción por la cantidad de hijos
	 *
	 * @param numeroHijos                 cantidad de hijos de una persona
	 * @param deduccionesPorDescendientes tabla donde aparecen las deducciones según el número de hijos
	 * @return deducción por número de hijos calculada
	 */
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

	/**
	 * Calcula la retención facilitándole una cantidad y la tabla de retenciones
	 *
	 * @param cantidad        de dinero sobre el que se quiere aplicar la retención
	 * @param tramosRetencion Tabla con los porcentajes de retención que se aplica en cada tramo
	 * @return retención practicada para una cantidad concreta
	 */
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
