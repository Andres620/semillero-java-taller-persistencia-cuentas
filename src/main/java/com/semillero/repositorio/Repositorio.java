package com.semillero.repositorio;

import java.util.List;

public interface Repositorio {
    public void guardar(Object objeto);

    public void eliminar(String numeroCuenta);

    public boolean actualizar(Object objeto);

    public Object buscar(String numeroCuenta);

    public List<?> listar();
}
