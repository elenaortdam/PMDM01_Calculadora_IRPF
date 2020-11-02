package com.iesribera;

import java.math.BigDecimal;

public class TramosIrpf {

	private BigDecimal minimo;
	private BigDecimal maximo;
	private BigDecimal valor;

	public TramosIrpf(BigDecimal minimo, BigDecimal maximo, BigDecimal valor) {
		this.minimo = minimo;
		this.maximo = maximo;
		this.valor = valor;
	}

	public BigDecimal getMinimo() {
		return minimo;
	}

	public BigDecimal getMaximo() {
		return maximo;
	}

	public BigDecimal getValor() {
		return valor;
	}
}
