package com.semillero.entidades;

public abstract class CuentaBancaria {
    private String numeroCuenta;
	private float saldo;
	private String propietario;
	private int cantidadRetiros;
    private TipoCuenta tipo;

    public CuentaBancaria(String numeroCuenta, float saldo, String propietario, TipoCuenta tipo) {
		this.numeroCuenta = numeroCuenta;
		this.saldo = saldo;
		this.propietario = propietario;
        this.tipo = tipo;
		this.cantidadRetiros = 0;
	}

    public CuentaBancaria(String numeroCuenta, float saldo, String propietario, TipoCuenta tipo, int cantidadRetiros) {
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
        this.propietario = propietario;
        this.tipo = tipo;
        this.cantidadRetiros = cantidadRetiros;
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

    public TipoCuenta getTipo() {
        return tipo;
    }

    public void setTipo(TipoCuenta tipo) {
        this.tipo = tipo;
    }
}
