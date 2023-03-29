package com.semillero.entidades;

public class CuentaCorriente extends CuentaBancaria{    
    private static final int MAX_NUM_RETIROS = 5;
    private static final int MAX_NUM_TRANSFERENCIAS_AHORRO = 2;
    private static final double PORCENTAJE_COBRO_TRANSFERENCIA = 0.02;
    private int cantidadTransferenciasAhorros;

    public CuentaCorriente(String numeroCuenta, float saldo, String propietario) {
        super(numeroCuenta, saldo, propietario);
        cantidadTransferenciasAhorros = 0;
    }
    
    public static int getMaxNumRetiros() {
        return MAX_NUM_RETIROS;
    }

    public static int getMaxNumTransferenciasAhorro() {
        return MAX_NUM_TRANSFERENCIAS_AHORRO;
    }
    public static double getPorcentajeCobroTransferencia() {
        return PORCENTAJE_COBRO_TRANSFERENCIA;
    }

    public int getCantidadTransferenciasAhorros() {
        return cantidadTransferenciasAhorros;
    }

    public void setCantidadTransferenciasAhorros(int cantidadTransferencias) {
        this.cantidadTransferenciasAhorros = cantidadTransferencias;
    }
}
