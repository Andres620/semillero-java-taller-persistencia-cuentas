package com.semillero.entidades;

public abstract class CuentaBancaria {
    private String numeroCuenta;
	private float saldo;
	private String propietario;
	private int cantidadRetiros;
	
    public CuentaBancaria(String numeroCuenta, float saldo, String propietario) {
		this.numeroCuenta = numeroCuenta;
		this.saldo = saldo;
		this.propietario = propietario;
		this.cantidadRetiros = 0;
	}

	public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public int getCantidadRetiros() {
        return cantidadRetiros;
    }

    public void setCantidadRetiros(int cantidadRetiros) {
        this.cantidadRetiros = cantidadRetiros;
    }
}
