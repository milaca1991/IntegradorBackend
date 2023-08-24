package com.backend.clinica.service.impl;
import com.backend.clinica.dao.IDao;
import com.backend.clinica.entity.Paciente;
import com.backend.clinica.service.impl.IPacienteService;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class PacienteService implements IPacienteService {
    private final IDao<Paciente> pacienteIDao;

    public PacienteService(IDao<Paciente> pacienteIDao) {
        this.pacienteIDao = pacienteIDao;
    }

    public Paciente registrarPaciente(Paciente paciente){
        return pacienteIDao.registrar(paciente);
    }


    public Paciente buscarPacientePorId(int id){
        return pacienteIDao.buscarPorId(id);
    }

    public List<Paciente> listarPacientes(){
        return pacienteIDao.listarTodos();
    }


    public void eliminarPaciente(int id){
        pacienteIDao.eliminar(id);
    }

    @Override
    public Paciente modificarPaciente(Paciente pacienteModificado) {
        return pacienteIDao.modificar(pacienteModificado);
    }
}
