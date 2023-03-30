package com.semillero.servicios;

import java.util.List;

import com.semillero.entidades.CuentaAhorros;
import com.semillero.entidades.CuentaBancaria;
import com.semillero.entidades.CuentaCorriente;
import com.semillero.excepciones.RetiroException;
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

    public boolean actualizarCuenta(CuentaBancaria cuenta) {
        return repositorioCuenta.actualizar(cuenta);
    }

    public boolean retirar(CuentaBancaria cuenta, float monto) throws RetiroException{
        if (cuenta instanceof CuentaAhorros) {
            return retirarCuentaAhorros(cuenta, monto); 
        } else if (cuenta instanceof CuentaCorriente) {
            return retirarCuentaCorriente(cuenta, monto);
        }
        return false;
    }

    private boolean retirarCuentaAhorros(CuentaBancaria cuenta, float monto) throws RetiroException{
        if (monto <= 0) {
            throw new RetiroException("El monto a retirar debe ser mayor a cero");
        }
		if (cuenta.getSaldo() < monto) {
			throw new RetiroException("Saldo insuficiente para realizar el retiro.");
		}

        float saldoFinal = cuenta.getSaldo() - monto;

        if (cuenta.getCantidadRetiros() > CuentaAhorros.getMaxNumRetiros()) {
            double comision = monto * CuentaAhorros.getPorcentajeRetiros();
            saldoFinal -= comision;
        }

        cuenta.setSaldo(saldoFinal);
        cuenta.setCantidadRetiros(cuenta.getCantidadRetiros() + 1);
        
        return actualizarCuenta(cuenta);
    }

    private boolean retirarCuentaCorriente(CuentaBancaria cuenta, float monto) throws RetiroException{
        if (monto <= 0) {
            throw new RetiroException("El monto a retirar debe ser mayor a cero");
        }
		if (cuenta.getSaldo() < monto) {
			throw new RetiroException("Saldo insuficiente para realizar el retiro.");
		}
		if (cuenta.getCantidadRetiros() > CuentaCorriente.getMaxNumRetiros()) {
			throw new RetiroException("Se ha alcanzado el máximo de retiros permitidos.");
		}

        float saldoFinal = cuenta.getSaldo() - monto;

        cuenta.setSaldo(saldoFinal);
        cuenta.setCantidadRetiros(cuenta.getCantidadRetiros() + 1);
        
        return actualizarCuenta(cuenta);
    }
    
}
