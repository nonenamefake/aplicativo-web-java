package com.project.TECHGEAR.Controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.project.TECHGEAR.Model.Empleado;
import com.project.TECHGEAR.Services.EmpleadoService;
import jakarta.validation.Valid;

@Controller
public class EmpleadoController {
    List<String> listarol = Arrays.asList("Administrador","ventas","tecnico","contavilidad");

    @Autowired
    private EmpleadoService empleadoService;
    
    @GetMapping("/empleado")
    public String empleado(Model model) {
        model.addAttribute("lista", empleadoService.get());
        return "empleado/verempleado";
    }

    @PostMapping("/empleado/guardar")
    public String Guardaremp(@Valid @ModelAttribute("empleado") Empleado empleado, 
                           BindingResult result, 
                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("empleado", empleado);
            model.addAttribute("areas", listarol);
            model.addAttribute("error", "Por favor, corrija los errores en el formulario");
            return "empleado/nuevoempleado";
        }
        empleadoService.save(empleado);
        return "redirect:/empleado";
    }
    
    @GetMapping("/empleado/new")
    public String Nuevoemp(Model model) {
        Empleado emp = new Empleado();
        model.addAttribute("empleado", emp);
        model.addAttribute("areas", listarol);
        return "empleado/nuevoempleado";
    }
    
    @GetMapping("/empleado/editar/{id}")
    public String Editaremp(@PathVariable Integer id, Model modelo) {
        modelo.addAttribute("empleado", empleadoService.get(id));
        modelo.addAttribute("areas", listarol);
        return "empleado/editarempleado";
    }

    @GetMapping("/empleado/eliminar/{id}")
    public String eliminaremp(@PathVariable Integer id) {
        empleadoService.delete(id);
        return "redirect:/empleado";
    }

    @PostMapping("/empleado/actualizar/{id}")
    public String updateempleado(@PathVariable Integer id, 
                                 @Valid @ModelAttribute("empleado") Empleado empleado, 
                                 BindingResult result, 
                                 Model model) {
        if (result.hasErrors()) {
            empleado.setIdempleado(id);
            model.addAttribute("empleado", empleado);
            model.addAttribute("areas", listarol);
            model.addAttribute("error", "Por favor, corrija los errores en el formulario");
            return "empleado/editarempleado";
        }
        Empleado now = empleadoService.get(id);
        now.setIdempleado(id);
        now.setNombres(empleado.getNombres());
        now.setUsuario(empleado.getUsuario());
        now.setRol(empleado.getRol());
        empleadoService.update(now);
        return "redirect:/empleado";
    }
}
