package com.semillero.repositorio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.semillero.entidades.CuentaAhorros;
import com.semillero.entidades.CuentaBancaria;
import com.semillero.entidades.CuentaCorriente;
import com.semillero.entidades.TipoCuenta;

public class CuentasDB implements Repositorio{
    private String cadenaConexion;

    public CuentasDB() {
        try {
            DriverManager.registerDriver(new org.sqlite.JDBC());
            cadenaConexion = "jdbc:sqlite:banco.db";
            crearTabla();
        } catch (SQLException e) {
            System.err.println("Error de conexión con la base de datos: " + e);
        }
    }

    private void crearTabla() {
        try (Connection conexion = DriverManager.getConnection(cadenaConexion)) {

            String sql = "CREATE TABLE IF NOT EXISTS Cuentas (\n"
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                    + "tipo TEXT,\n"
                    + "numero_cuenta TEXT NOT NULL UNIQUE,\n"
                    + "saldo REAL NOT NULL,\n"
                    + "propietario TEXT NOT NULL,\n"
                    + "cantidad_retiros,\n"
                    + "cantidad_depositos,\n"
                    + "cantidad_transferencias_corriente_ahorro"
                    + ");";

            Statement sentencia = conexion.createStatement();
            sentencia.execute(sql);

        } catch (SQLException e) {
            System.out.println("Error al crear la tabla Cuentas: " + e.getMessage());
        }
    }

    @Override
    public void guardar(Object objeto) {
        try (Connection conexion = DriverManager.getConnection(cadenaConexion)) {
            CuentaBancaria cuenta = (CuentaBancaria) objeto;

            String sentenciaSql = "INSERT INTO Cuentas (tipo, numero_cuenta, saldo, propietario, " 
            + "cantidad_retiros, cantidad_depositos, cantidad_transferencias_corriente_ahorro) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
            sentencia.setString(1, cuenta.getTipo().toString());
            sentencia.setString(2, cuenta.getNumeroCuenta());
            sentencia.setFloat(3, cuenta.getSaldo());
            sentencia.setString(4, cuenta.getPropietario());
            sentencia.setInt(5, cuenta.getCantidadRetiros());

            if (cuenta instanceof CuentaAhorros) {
                sentencia.setInt(6, ((CuentaAhorros) cuenta).getCantidadDepositos());
                sentencia.setNull(7, Types.INTEGER);
            } else if (cuenta instanceof CuentaCorriente) {
                sentencia.setNull(6, Types.INTEGER);
                sentencia.setInt(7, ((CuentaCorriente) cuenta).getCantidadTransferenciasAhorros());
            }

            sentencia.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al agregar la cuenta:" + e);
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    @Override
    public void eliminar(String numeroCuenta) {
        try (Connection conexion = DriverManager.getConnection(cadenaConexion)) {
            String sentenciaSql = "DELETE FROM cuentas WHERE numero_cuenta = ?;";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);

            sentencia.setString(1, numeroCuenta);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar la cuenta: " + e.getMessage());
        }
    }

    @Override
    public boolean actualizar(Object cuentaActualizada) {
        try (Connection conexion = DriverManager.getConnection(cadenaConexion)) {
            CuentaBancaria cuenta = (CuentaBancaria) cuentaActualizada;
            String sentenciaSql = "UPDATE cuentas SET "
                         + "tipo = ?, "
                         + "saldo = ?, "
                         + "propietario = ?, "
                         + "cantidad_retiros = ?, "
                         + "cantidad_depositos = ?, "
                         + "cantidad_transferencias_corriente_ahorro = ? "
                         + "WHERE numero_cuenta = ?;";
                         
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
            
            sentencia.setString(1, cuenta.getTipo().toString());
            sentencia.setDouble(2, cuenta.getSaldo());
            sentencia.setString(3, cuenta.getPropietario());
            sentencia.setInt(4, cuenta.getCantidadRetiros());
            
            if (cuenta instanceof CuentaAhorros) {
                sentencia.setInt(5, ((CuentaAhorros) cuenta).getCantidadDepositos());
                sentencia.setNull(6, Types.INTEGER);
            } else if (cuenta instanceof CuentaCorriente) {
                sentencia.setNull(5, Types.INTEGER);
                sentencia.setInt(6, ((CuentaCorriente) cuenta).getCantidadTransferenciasAhorros());
            }
            sentencia.setString(7, cuenta.getNumeroCuenta());
            sentencia.executeUpdate();
            return true;
            
        } catch (SQLException e) {
            System.out.println("Error al actualizar la cuenta"  + e.getMessage());
        }
        System.out.println("se metio al false");
        return false;
    }

    @Override
    public Object buscar(String numCuenta) {
        try (Connection conexion = DriverManager.getConnection(cadenaConexion)) {
            String sentenciaSQL = "SELECT * FROM cuentas WHERE numero_cuenta = ?";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSQL);
            sentencia.setString(1, numCuenta);
            ResultSet respuesta = sentencia.executeQuery();
            if (respuesta != null && respuesta.next()) {
                CuentaBancaria cuenta = null;
                TipoCuenta tipo =  TipoCuenta.valueOf(respuesta.getString("tipo")); 
                String numeroCuenta = respuesta.getString("numero_cuenta"); 
                Float saldo = respuesta.getFloat("saldo"); 
                String propietario = respuesta.getString("propietario");
                int cantidadRetiros = respuesta.getInt("cantidad_retiros");

                if (tipo.equals(TipoCuenta.AHORROS)) {
                    int cantidadDeposito = respuesta.getInt("cantidad_depositos");
                    cuenta = new CuentaAhorros(numeroCuenta, saldo, propietario, tipo, cantidadRetiros, cantidadDeposito);
                } else if (tipo.equals(TipoCuenta.CORRIENTE)) {
                    int cantidadTransferenciasAhorros = respuesta.getInt("cantidad_transferencias_corriente_ahorro");
                    cuenta = new CuentaCorriente(numeroCuenta, saldo, propietario, tipo, cantidadRetiros, cantidadTransferenciasAhorros);
                }
                return cuenta;
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar la cuenta: " + e);
        }
        return null;
    }

    @Override
    public List<?> listar() {
        List<CuentaBancaria> cuentas = new ArrayList<CuentaBancaria>();

        try(Connection conexion = DriverManager.getConnection(cadenaConexion)){
            String sentenciaSql = "SELECT * FROM cuentas";
            PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql);
            ResultSet respuesta = sentencia.executeQuery();
            
            if(respuesta != null){
                while(respuesta.next()){
                    CuentaBancaria cuenta = null;
                    TipoCuenta tipo =  TipoCuenta.valueOf(respuesta.getString("tipo")); 
                    String numeroCuenta = respuesta.getString("numero_cuenta"); 
                    Float saldo = respuesta.getFloat("saldo"); 
                    String propietario = respuesta.getString("propietario");
                    int cantidadRetiros = respuesta.getInt("cantidad_retiros");

                    if (tipo.equals(TipoCuenta.AHORROS)) {
                        int cantidadDeposito = respuesta.getInt("cantidad_depositos");
                        cuenta = new CuentaAhorros(numeroCuenta, saldo, propietario, tipo, cantidadRetiros, cantidadDeposito);
                    } else if (tipo.equals(TipoCuenta.CORRIENTE)) {
                        int cantidadTransferenciasAhorros = respuesta.getInt("cantidad_transferencias_corriente_ahorro");
                        cuenta = new CuentaCorriente(numeroCuenta, saldo, propietario, tipo, cantidadRetiros, cantidadTransferenciasAhorros);
                    }
                    cuentas.add(cuenta);
                }
                return cuentas;
            }

        }catch (SQLException e) {
            System.err.println("Error al listar las cuentas: " + e);
        }
        return null;
    }

    
}
