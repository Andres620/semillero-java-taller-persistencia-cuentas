package com.semillero.repositorio;

import java.sql.*;
import java.util.List;

public class CuentaAhorrosDB implements Repositorio{
    private String cadenaConexion;

    public CuentaAhorrosDB() {
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'guardar'");
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
