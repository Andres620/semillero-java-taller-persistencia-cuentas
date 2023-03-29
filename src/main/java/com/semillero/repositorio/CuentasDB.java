package com.semillero.repositorio;

import java.sql.*;
import java.util.List;

import com.semillero.entidades.CuentaAhorros;
import com.semillero.entidades.CuentaBancaria;
import com.semillero.entidades.CuentaCorriente;

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
                    + "tipo TEXT\n"
                    + "numero_cuenta TEXT NOT NULL,\n"
                    + "saldo REAL NOT NULL,\n"
                    + "propietario TEXT,\n"
                    + "cantidad_retiros\n"
                    + "cantidad_depositos\n"
                    + "cantidad_transferencias_corrite_ahorro"
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
            + "cantidad_retiros, cantidad_depositos, cantidad_transferencias_corrite_ahorro) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = conexion.prepareStatement(sentenciaSql);
            statement.setString(1, cuenta.getTipo().toString());
            statement.setString(2, cuenta.getNumeroCuenta());
            statement.setFloat(3, cuenta.getSaldo());
            statement.setString(4, cuenta.getPropietario());
            statement.setInt(5, cuenta.getCantidadRetiros());

            if (cuenta instanceof CuentaAhorros) {
                statement.setDouble(6, ((CuentaAhorros) cuenta).getCantidadDepositos());
                statement.setNull(7, Types.INTEGER);
            } else if (cuenta instanceof CuentaCorriente) {
                statement.setNull(6, Types.INTEGER);
                statement.setDouble(7, ((CuentaCorriente) cuenta).getCantidadTransferenciasAhorros());
            }

            Statement sentencia = conexion.createStatement();
            sentencia.execute(sentenciaSql);
        } catch (SQLException e) {
            System.err.println("Error al agregar la cuenta:" + e);
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    @Override
    public void eliminar(String identificador) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminar'");
    }

    @Override
    public void actualizar(Object objeto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizar'");
    }

    @Override
    public Object buscar(String identificador) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscar'");
    }

    @Override
    public List<?> listar() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listar'");
    }

    
}
