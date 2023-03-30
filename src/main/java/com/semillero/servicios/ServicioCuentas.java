package com.semillero.servicios;

import java.util.List;

import com.semillero.entidades.CuentaAhorros;
import com.semillero.entidades.CuentaBancaria;
import com.semillero.entidades.CuentaCorriente;
import com.semillero.excepciones.DepositoException;
import com.semillero.excepciones.RetiroException;
import com.semillero.excepciones.TransferenciaException;
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

    public boolean depositar(CuentaBancaria cuenta, float monto) throws DepositoException{
        if (cuenta instanceof CuentaAhorros) {
            return depositarCuentaAhorros(cuenta, monto); 
        } else if (cuenta instanceof CuentaCorriente) {
            return depositarCuentaCorriente(cuenta, monto);
        }
        return false;
    }

    private boolean depositarCuentaAhorros(CuentaBancaria cuenta, float monto) throws DepositoException{
        if (monto <= 0) {
            throw new DepositoException("El monto a depositar debe ser mayor a cero");
        }
		if (((CuentaAhorros) cuenta).getCantidadDepositos() < CuentaAhorros.getMaxNumDepositos()) {
			monto += monto * CuentaAhorros.getPorcentajeDeposito();
		}
		float saldoFinal = cuenta.getSaldo() + monto;
        cuenta.setSaldo(saldoFinal);
        ((CuentaAhorros)cuenta).setCantidadDepositos(((CuentaAhorros)cuenta).getCantidadDepositos() + 1);
        return actualizarCuenta(cuenta);
    }

    private boolean depositarCuentaCorriente(CuentaBancaria cuenta, float monto) throws DepositoException{
        if (monto <= 0) {
            throw new DepositoException("El monto a depositar debe ser mayor a cero");
        }
		float saldoFinal = cuenta.getSaldo() + monto;
        cuenta.setSaldo(saldoFinal);
        ((CuentaAhorros)cuenta).setCantidadDepositos(((CuentaAhorros)cuenta).getCantidadDepositos() + 1);
        return actualizarCuenta(cuenta);
    }

    public boolean transferir(CuentaBancaria cuentaOrigen, String numeroCuentaDestino, float monto) throws Exception{
        if (cuentaOrigen instanceof CuentaAhorros) {
            return transferirDeAhorros(cuentaOrigen, numeroCuentaDestino, monto); 
        } else if (cuentaOrigen instanceof CuentaCorriente) {
            return transferirDeCorriente(cuentaOrigen, numeroCuentaDestino, monto);
        }
        return false;
    }

    private boolean transferirDeAhorros(CuentaBancaria cuentaOrigen, String numeroCuentaDestino, float monto) throws Exception {
        if (monto <= 0) {
            throw new TransferenciaException("El monto a transferir debe ser mayor a cero");
        }
        CuentaBancaria cuentaDestino = buscarCuenta(numeroCuentaDestino);
        float saldoFinalOrigen = 0;
        float saldoFinalDestino = 0;

        if (cuentaDestino instanceof CuentaAhorros) {
            saldoFinalOrigen = cuentaOrigen.getSaldo() - monto;
            saldoFinalDestino = cuentaDestino.getSaldo() + monto;  
        } else if (cuentaDestino instanceof CuentaCorriente) {  
            saldoFinalOrigen = (float) (cuentaOrigen.getSaldo() - (monto + monto * CuentaAhorros.getPorcentajeCobroTransferenciaCorriente()));
            saldoFinalDestino = cuentaDestino.getSaldo() + monto;
        }
        cuentaOrigen.setSaldo(saldoFinalOrigen);
        cuentaDestino.setSaldo(saldoFinalDestino);
        actualizarCuenta(cuentaOrigen);
        actualizarCuenta(cuentaDestino);
        return true;
    }

    private boolean transferirDeCorriente(CuentaBancaria cuentaOrigen, String numeroCuentaDestino, float monto) throws Exception {
        if (monto <= 0) {
            throw new TransferenciaException("El monto a transferir debe ser mayor a cero");
        }
        CuentaBancaria cuentaDestino = buscarCuenta(numeroCuentaDestino);
        float saldoFinalOrigen = 0;
        float saldoFinalDestino = 0;

        if (cuentaDestino instanceof CuentaAhorros) {
            if (((CuentaCorriente)cuentaOrigen).getCantidadTransferenciasAhorros() > CuentaCorriente.getMaxNumTransferenciasAhorro()){
                return false;
            }
        }
        saldoFinalOrigen = (float) (cuentaOrigen.getSaldo() - (monto + monto * CuentaCorriente.getPorcentajeCobroTransferencia()));
        saldoFinalDestino = cuentaDestino.getSaldo() + monto;
        cuentaOrigen.setSaldo(saldoFinalOrigen);
        cuentaDestino.setSaldo(saldoFinalDestino);
        actualizarCuenta(cuentaOrigen);
        actualizarCuenta(cuentaDestino);
        return true;
    }
}
