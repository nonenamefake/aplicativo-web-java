package com.project.TECHGEAR.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.sql.Timestamp;
import java.time.Instant;
import com.project.TECHGEAR.Services.ConsultaService;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConsultaController {
    @Autowired
    ConsultaService consultaService;

    @GetMapping("/Consultas")
    public String verConsultas(Model model) {
        model.addAttribute("listaConsultas", consultaService.listar());
        return "Consulta/VerConsultas";
    }
        @GetMapping("/Contacto")
    public String contacto(Model model) {
        com.project.TECHGEAR.Model.Consulta c = new com.project.TECHGEAR.Model.Consulta();
        model.addAttribute("consulta", c);
        return "Contacto";
    }

    @PostMapping("/Contacto/save")
    public String submitConsulta(@Valid @ModelAttribute("consulta") com.project.TECHGEAR.Model.Consulta consulta,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("consulta", consulta);
            return "Contacto";
        }

        consulta.setFecha(Timestamp.from(Instant.now()));
        consultaService.save(consulta);
        redirectAttributes.addFlashAttribute("success", "Gracias — tu mensaje ha sido recibido y será respondido pronto.");
        return "redirect:/Contacto";
    }
}
