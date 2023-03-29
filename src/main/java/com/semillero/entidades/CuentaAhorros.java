package com.semillero.entidades;

public class CuentaAhorros extends CuentaBancaria{
	private static final int MAX_NUM_RETIROS = 3;
    private static final double PORCENTAJE_RETIROS = 0.01;
	private static final double PORCENTAJE_DEPOSITO = 0.005;
    private static final double MAX_NUM_DEPOSITOS = 2;
    private static final double PORCENTAJE_COBRO_TRANSFERENCIA_CORRIENTE = 0.015;
    private int cantidadDepositos;
    
    public CuentaAhorros(String numeroCuenta, float saldo, String propietario) {
        super(numeroCuenta, saldo, propietario);
        cantidadDepositos = 0;
    }

    public static double getPorcentajeCobroTransferenciaCorriente() {
        return PORCENTAJE_COBRO_TRANSFERENCIA_CORRIENTE;
    }

    public static double getMaxNumDepositos() {
        return MAX_NUM_DEPOSITOS;
    }
    
    public static double getPorcentajeRetiros() {
        return PORCENTAJE_RETIROS;
    }

    public static int getMaxNumRetiros() {
        return MAX_NUM_RETIROS;
    }

    public static double getPorcentajeDeposito() {
        return PORCENTAJE_DEPOSITO;
    }

    public int getCantidadDepositos() {
        return cantidadDepositos;
    }

    public void setCantidadDepositos(int numDepositos) {
        this.cantidadDepositos = numDepositos;
    }



    
}
