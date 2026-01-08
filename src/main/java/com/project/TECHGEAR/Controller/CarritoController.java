package com.project.TECHGEAR.Controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.TECHGEAR.Model.Carrito;
import com.project.TECHGEAR.Model.CarritoProducto;
import com.project.TECHGEAR.Model.Product;
import com.project.TECHGEAR.Model.User;
import com.project.TECHGEAR.Services.CarritoProductoService;
import com.project.TECHGEAR.Services.CarritoService;
import com.project.TECHGEAR.Services.ProductService;
import com.project.TECHGEAR.Services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class CarritoController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CarritoProductoService carritoProductoService;

    @Autowired
    private ProductService productService;


    @PostMapping("/Carrito/add/{productId}")
    public String addToCart(@PathVariable Integer productId, @RequestParam(defaultValue = "1") Double cantidad) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/Login"; 
        }

        String username = auth.getName();
        User usuario = usuarioService.findByUsername(username);
        if (usuario == null) {
            return "redirect:/Login";
        }

        Carrito carrito = carritoService.obtenerCarritoActivo(usuario.getId());
        if (carrito == null) {
            carrito = new Carrito();
            carrito.setEstado("Activo");
            carrito.setUsuario(usuario);
            carrito = carritoService.guardar(carrito);
        }

        Product prod = productService.get(productId);
        if (prod == null) {
            return "redirect:/Find"; 
        }

        CarritoProducto existente = carritoProductoService.buscarProductoEnCarrito(carrito.getId(), prod.getId());
        if (existente != null) {
            existente.setCantidad(existente.getCantidad() + cantidad);
            carritoProductoService.guardar(existente);
        } else {
            CarritoProducto nuevo = new CarritoProducto();
            nuevo.setCarrito(carrito);
            nuevo.setProduct(prod);
            nuevo.setCantidad(cantidad);
            carritoProductoService.guardar(nuevo);
        }

        return "redirect:/Carrito";
    }
    @GetMapping("/Carrito")
    public String verCarrito(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            model.addAttribute("carrito", null);
            model.addAttribute("productos", List.of());
            model.addAttribute("subtotal", 0.00);
            model.addAttribute("descuento", 0.00);
            model.addAttribute("igv", 0.00);
            model.addAttribute("total", 0.00);
            return "Carrito";
        }

        String username = auth.getName();
        User usuario = usuarioService.findByUsername(username);
        if (usuario == null) {
            return "Carrito";
        }

        Carrito carrito = carritoService.obtenerCarritoActivo(usuario.getId());
        if (carrito == null) {
            model.addAttribute("carrito", null);
            model.addAttribute("productos", List.of());
            model.addAttribute("subtotal", 0.00);
            model.addAttribute("descuento", 0.00);
            model.addAttribute("igv", 0.00);
            model.addAttribute("total", 0.00);
            return "Carrito";
        }

        List<CarritoProducto> productos = carritoProductoService.buscarPorCarrito(carrito.getId());
        if(productos != null) {
            double total = 0.0;
        for (CarritoProducto cp : productos) {
            if (cp.getProduct() != null && cp.getCantidad() != null) {
                total += (cp.getProduct().getPrice() != null ? cp.getProduct().getPrice() : 0.0) * cp.getCantidad();
            }
        }
        double descuento = 0.0; 

        double subtotal = total / 1.18;
        double igv = total - subtotal;
        total = total - descuento;

        model.addAttribute("carrito", carrito);
        model.addAttribute("productos", productos);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("descuento", descuento);
        model.addAttribute("igv", igv);
        model.addAttribute("total", total);

        }
        
        return "Carrito";
    }

    @PostMapping("/Carrito/actualizar-cantidad/{id}")
    public String actualizarCantidad(@PathVariable Integer id, @RequestParam Double cantidad) {
        CarritoProducto cp = carritoProductoService.buscarPorId(id);
        if (cp != null) {
            cp.setCantidad(cantidad);
            carritoProductoService.guardar(cp);
        }
        return "redirect:/Carrito";
    }

    @PostMapping("/Carrito/eliminar/{id}")
    public String eliminarItem(@PathVariable Integer id) {
        carritoProductoService.eliminar(id);
        return "redirect:/Carrito";
    }

    @PostMapping("/Carrito/cancelar/{id}")
    public String cancelarCarrito(@PathVariable Integer id) {
        Carrito carrito = carritoService.buscarPorId(id);
        if (carrito != null) {
            carrito.setEstado("Cancelado");
            carritoService.guardar(carrito);
        }
        return "redirect:/Carrito";
    }


}
