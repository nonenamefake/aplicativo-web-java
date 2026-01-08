package com.project.TECHGEAR.Services;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.TECHGEAR.Model.Consulta;
import com.project.TECHGEAR.Repository.Consultarepositorio;

@Service
public class ConsultaService {
    @Autowired
    private Consultarepositorio consultarepositorio;

    public List<Consulta> listar(){
        return consultarepositorio.findAll();
    }

    public Consulta save(Consulta c) {
        return consultarepositorio.save(c);
    }
}
