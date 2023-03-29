package com.semillero.gui;

import java.util.List;
import java.util.Scanner;

import com.semillero.entidades.CuentaAhorros;
import com.semillero.entidades.CuentaBancaria;
import com.semillero.servicios.ServicioCuentas;

public class GUI_Cuenta {
    private boolean running = true;
    private ServicioCuentas servicioCuentas;

    public GUI_Cuenta() {
        servicioCuentas = new ServicioCuentas();
    }
    
    public void crearCuenta() { //poner privado
        System.out.println("Crear Cuenta Ahorros");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Numero Cuenta: ");
        String numeroCuenta = scanner.nextLine();
        System.out.println("Saldo: ");
        Float saldo = scanner.nextFloat();
        scanner.nextLine();
        System.out.println("Propietario: ");
        String propietario = scanner.nextLine();

        CuentaBancaria cuenta = new CuentaAhorros(numeroCuenta, saldo, propietario);
        System.out.println("cuentaq: " + cuenta.getSaldo());
        servicioCuentas.guardarCuenta(cuenta);
    }

    public void eliminarCuenta() {
        System.out.println("Eliminar cuenta");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Número de cuenta: ");
        String numeroCuenta = scanner.nextLine();
        try {
            servicioCuentas.eliminarCuenta(numeroCuenta);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    
    public void buscarCuenta() {
        System.out.println("Buscar cuenta");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Número de cuenta: ");
        String numeroCuenta = scanner.nextLine();
        try {
            CuentaBancaria cuenta = servicioCuentas.buscarCuenta(numeroCuenta);
            System.out.println("Cuenta encontrada " + cuenta.getNumeroCuenta() + ": " + cuenta.getPropietario());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void listarCuentas() { //poner privado
        System.out.println("Listando cuentas");
        List<CuentaBancaria> cuentas = servicioCuentas.listarCuentas();

        for (CuentaBancaria cuenta : cuentas) {
            System.out.println("Cuenta " + cuenta.getNumeroCuenta() + ": " + cuenta.getPropietario());
        }
    }

    
}
