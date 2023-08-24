package com.backend.clinica.dao.impl;


import com.backend.clinica.entity.Odontologo;
import com.backend.clinica.dao.H2Connection;
import com.backend.clinica.dao.IDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class OdontologoDaoH2 implements IDao<Odontologo> {
    private final Logger LOGGER = LoggerFactory.getLogger(OdontologoDaoH2.class);
    private final String CTE_ERROR = "Ha ocurrido un error al intentar cerrar la bdd. {}";

    @Override
    public Odontologo registrar(Odontologo odontologo) {
        Connection connection = null;
        Odontologo odontologo1 = null;
        try {
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement ps = connection.prepareStatement("INSERT INTO ODONTOLOGOS (MATRICULA, NOMBRE, APELLIDO) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, odontologo.getMatricula());
            ps.setString(2, odontologo.getNombre());
            ps.setString(3, odontologo.getApellido());
            ps.execute();

            odontologo1 = new Odontologo(odontologo.getMatricula(), odontologo.getNombre(), odontologo.getApellido());
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                odontologo1.setId(rs.getInt(1));
            }

            connection.commit();
            LOGGER.info("Se ha registrado el odontologo: {}", odontologo1);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                    System.out.println("Tuvimos un problema");
                    e.printStackTrace();
                } catch (SQLException exception) {
                    LOGGER.error(exception.getMessage());
                    exception.printStackTrace();
                }
            }
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                LOGGER.error(CTE_ERROR, e.getMessage());
                e.printStackTrace();
            }
        }

        return odontologo1;
    }

    @Override
    public List<Odontologo> listarTodos() {
        Connection connection = null;
        List<Odontologo> odontologos = new ArrayList<>();

        try {
            connection = H2Connection.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM ODONTOLOGOS");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Odontologo odontologo = crearObjetoOdontologo(rs);
                odontologos.add(odontologo);
            }

            LOGGER.info("Listado de todos los odontologos: {}", odontologos);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();

        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                LOGGER.error(CTE_ERROR, ex.getMessage());
                ex.printStackTrace();
            }
        }
        return odontologos;
    }

    @Override
    public Odontologo modificar(Odontologo odontologo) {
        return null;
    }

    @Override
    public void eliminar(int id) {
        Connection connection = null;
        try {
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement ps = connection.prepareStatement("DELETE FROM ODONTOLOGOS WHERE ID = ?");
            ps.setInt(1, id);
            ps.execute();
            connection.commit();
            LOGGER.info("Se ha eliminado el odontologo con id: {}", id);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                    System.out.println("Tuvimos un problema");
                    e.printStackTrace();
                } catch (SQLException exception) {
                    LOGGER.error(exception.getMessage());
                    exception.printStackTrace();
                }
            }
        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                LOGGER.error(CTE_ERROR, ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    @Override
    public Odontologo buscarPorId(int id) {
        Connection connection = null;
        Odontologo odontologo = null;
        try {
            connection = H2Connection.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM ODONTOLOGOS WHERE ID = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                odontologo = crearObjetoOdontologo(rs);
            }
            LOGGER.info("Se ha encontrado el odontologo con id {}: {}", id, odontologo);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                LOGGER.error(CTE_ERROR, ex.getMessage());
                ex.printStackTrace();
            }
        }
        return odontologo;
    }

    private Odontologo crearObjetoOdontologo(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String nombre = resultSet.getString("nombre");
        String apellido = resultSet.getString("apellido");
        String matricula = resultSet.getString("matricula");

        return new Odontologo(id, matricula, nombre, apellido);
    }
}
