package com.semillero.servicios;

import java.util.List;

import com.semillero.entidades.CuentaBancaria;
import com.semillero.repositorio.CuentasDB;
import com.semillero.repositorio.Repositorio;

public class ServicioCuentas {
    private Repositorio repositorioCuenta;

    public ServicioCuentas() {
        repositorioCuenta = new CuentasDB();
    }

    public void guardarCuenta(CuentaBancaria cuenta) {
        repositorioCuenta.guardar(cuenta);
    }

    public CuentaBancaria buscarCuenta(String numeroCuenta) throws Exception {
        Object cuenta = repositorioCuenta.buscar(numeroCuenta);
        if(cuenta == null) {
            throw new Exception("No se encontro la cuenta");
        }
        return (CuentaBancaria) cuenta;
    }

    @SuppressWarnings("unchecked")
    public List<CuentaBancaria> listarCuentas() {
        return (List<CuentaBancaria>) repositorioCuenta.listar();
    }

    public void eliminarCuenta(String numeroCuenta) {
        repositorioCuenta.eliminar(numeroCuenta);
    }

    public void actualizarCuenta(String numeroCuenta, CuentaBancaria cuenta) {
        repositorioCuenta.actualizar(numeroCuenta, cuenta);
    }
    
}
