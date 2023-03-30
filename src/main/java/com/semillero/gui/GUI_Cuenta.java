package com.semillero.gui;

import java.util.List;
import java.util.Scanner;

import com.semillero.entidades.CuentaAhorros;
import com.semillero.entidades.CuentaBancaria;
import com.semillero.excepciones.DepositoException;
import com.semillero.excepciones.RetiroException;
import com.semillero.servicios.ServicioCuentas;

public class GUI_Cuenta {
    private boolean running = true;
    private ServicioCuentas servicioCuentas;

    public GUI_Cuenta() {
        servicioCuentas = new ServicioCuentas();
    }

    public void iniciar() {
        boolean cuentaEncontrada = false;
        CuentaBancaria cuentaActual = null;
        Scanner scanner = new Scanner(System.in);
        
        try {
            do {
                System.out.println("\n--- Menú de opciones ---");
                System.out.println("1. Crear cuenta");
                System.out.println("2. Buscar cuenta");
                if (cuentaEncontrada) {
                    System.out.println("3. Retirar");
                    System.out.println("4. Depositar");
                    System.out.println("5. Transferir");
                    System.out.println("6. Ver saldo");
                }
                System.out.println("7. Salir");
            
                int opcion = scanner.nextInt();
                scanner.nextLine();
            
                switch (opcion) {
                    case 1:
                        // crear cuenta
                        crearCuenta();
                        cuentaEncontrada = true;
                        break;
                    case 2:
                        // buscar cuenta
                        System.out.print("Ingrese el número de cuenta: ");
                        String numeroCuenta = scanner.nextLine();
                        cuentaActual = buscarCuenta(numeroCuenta);
                        if (cuentaActual == null) {
                            System.out.println("No se encontró la cuenta con el número " + numeroCuenta);
                        } else {
                            System.out.println("Cuenta encontrada: " + cuentaActual.getNumeroCuenta());
                            cuentaEncontrada = true;
                        }
                        break;
                    case 3:
                        // retirar
                        if (cuentaEncontrada) {
                            System.out.print("Ingrese el monto a retirar: ");
                            Float monto = scanner.nextFloat();
                            scanner.nextLine();
                            retirar(cuentaActual, monto);
                        } else {
                            System.out.println("No se ha encontrado ninguna cuenta");
                        }
                        break;
                    case 4:
                        // depositar
                        if (cuentaEncontrada) {
                            System.out.print("Ingrese el monto a depositar: ");
                            Float monto = scanner.nextFloat();
                            scanner.nextLine();
                            depositar(cuentaActual, monto);
                        } else {
                            System.out.println("No se ha encontrado ninguna cuenta");
                        }
                        break;
                    case 5:
                        // transferir
                        if (cuentaEncontrada) {
                            System.out.print("Ingrese el número de cuenta destino: ");
                            String numeroCuentaDestino = scanner.nextLine();
                            System.out.print("Ingrese el monto a depositar: ");
                            Float monto = scanner.nextFloat();
                            scanner.nextLine();
                            transferir(cuentaActual, numeroCuentaDestino, monto);
                        } else {
                            System.out.println("No se ha encontrado ninguna cuenta");
                        }
                        break;
                    case 6:
                        // ver saldo
                        if (cuentaEncontrada) {
                            System.out.println("Saldo actual: " + consultarSaldo(cuentaActual));
                        } else {
                            System.out.println("No se ha encontrado ninguna cuenta");
                        }
                        break;
                    case 7:
                        // salir
                        salir();
                        break;
                    default:
                        System.out.println("Opción inválida");
                }
            } while (running);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void crearCuenta() { 
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

    private void eliminarCuenta() {
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

    
    private CuentaBancaria buscarCuenta(String numeroCuenta) {
        System.out.println("Buscar cuenta");
        try {
            CuentaBancaria cuenta = servicioCuentas.buscarCuenta(numeroCuenta);
            System.out.println("Cuenta encontrada " + cuenta.getNumeroCuenta() + ": " + cuenta.getPropietario());
            return cuenta;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private void listarCuentas() { //poner privado
        System.out.println("Listando cuentas");
        List<CuentaBancaria> cuentas = servicioCuentas.listarCuentas();

        for (CuentaBancaria cuenta : cuentas) {
            System.out.println("Cuenta " + cuenta.getNumeroCuenta() + ": " + cuenta.getPropietario()+ ": " + cuenta.getSaldo());
        }
    }

    private void retirar(CuentaBancaria cuenta, float monto){
        try {
            servicioCuentas.retirar(cuenta, monto);
        } catch (RetiroException e) {
            e.printStackTrace();
        }
    }

    private void depositar(CuentaBancaria cuenta, float monto){
        try {
            servicioCuentas.depositar(cuenta, monto);
        } catch (DepositoException e) {
            e.printStackTrace();
        }
    }

    private void transferir(CuentaBancaria cuentaOrigen, String numeroCuentaDestino,float monto){
        try {
            servicioCuentas.transferir(cuentaOrigen, numeroCuentaDestino, monto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float consultarSaldo(CuentaBancaria cuenta){
        return cuenta.getSaldo();
    }

    private void salir() {
        System.out.println("Salir");
        running = false;
    }
    
}
