package com.project.TECHGEAR.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import java.util.List;

import com.project.TECHGEAR.Model.Product;
import com.project.TECHGEAR.Services.ProductService;
import com.project.TECHGEAR.Services.ProductImageService;
import org.springframework.web.bind.annotation.GetMapping;




@Controller

public class Control {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageService productImageService;
    @GetMapping("")
    public String index(Model model) {
        // Populate two small product lists for the homepage sections
        List<Product> tarjetasVideo = productService.buscarPorCategoria("Tarjeta de video");
        List<Product> gabinetes = productService.buscarPorCategoria("Gabinete");

        // Load images for items (if available)
        tarjetasVideo.forEach(p -> p.setImages(productImageService.getByProductId(p.getId())));
        gabinetes.forEach(p -> p.setImages(productImageService.getByProductId(p.getId())));

        model.addAttribute("tarjetasVideo", tarjetasVideo);
        model.addAttribute("gabinetes", gabinetes);
        return "Inicio";
    }
    
    @GetMapping("/Login")
    public String login() {
        return "Login";
    }

    @GetMapping("/loginemp")
    public String loginEmpleado() {
        return "loginemp";
    }   

    @GetMapping("/TerminosCondiciones")
    public String condiciones() {
        return "TerminosCondiciones";
    }
    @GetMapping("/politicas")
    public String politica() {
        return "PoliticaPrivacidad";
    }

}
