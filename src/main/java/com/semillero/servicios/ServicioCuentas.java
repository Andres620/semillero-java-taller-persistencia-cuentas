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

    @SuppressWarnings("unchecked")
    public List<CuentaBancaria> listarCuentas() {
        return (List<CuentaBancaria>) repositorioCuenta.listar();
    }
}
