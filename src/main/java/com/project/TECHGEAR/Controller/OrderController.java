package com.project.TECHGEAR.Controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.TECHGEAR.Model.Carrito;
import com.project.TECHGEAR.Model.CarritoProducto;
import com.project.TECHGEAR.Model.Order;
import com.project.TECHGEAR.Model.OrderItem;
import com.project.TECHGEAR.Model.User;
import com.project.TECHGEAR.Services.CarritoProductoService;
import com.project.TECHGEAR.Services.CarritoService;
import com.project.TECHGEAR.Services.OrderService;
import com.project.TECHGEAR.Services.UsuarioService;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemController orderItemController;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CarritoProductoService carritoProductoService;

    @Autowired
    private com.project.TECHGEAR.Services.OrderItemService orderItemService;

    // Create order from current user's active cart. This is exposed as POST /orders/create
    @PostMapping("/orders/create")
    public String createFromCart(
                                 Model model) {

        // Basic validation
        

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/Login";
        }

        String username = auth.getName();
        User usuario = usuarioService.findByUsername(username);
        if (usuario == null) return "redirect:/Login";

        Carrito carrito = carritoService.obtenerCarritoActivo(usuario.getId());
        if (carrito == null) {
            model.addAttribute("error", "No hay carrito activo para procesar el pago.");
            return "redirect:/Carrito";
        }

        List<CarritoProducto> productos = carritoProductoService.buscarPorCarrito(carrito.getId());
        if (productos == null || productos.isEmpty()) {
            model.addAttribute("error", "El carrito está vacío.");
            return "redirect:/Carrito";
        }

        double total = 0.0;
        for (CarritoProducto cp : productos) {
            if (cp.getProduct() != null && cp.getCantidad() != null) {
                total += (cp.getProduct().getPrice() != null ? cp.getProduct().getPrice() : 0.0) * cp.getCantidad();
            }
        }

        Order order = new Order();
        order.setStatus("Aprobado");
        order.setTotal(total);
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        order.setUser(usuario);
        order = orderService.guardar(order);

        List<OrderItem> items = new ArrayList<>();
        for (CarritoProducto cp : productos) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(cp.getProduct());
            oi.setUnitPrice(cp.getProduct() != null ? cp.getProduct().getPrice() : 0.0);
            int qty = cp.getCantidad() == null ? 1 : (int) Math.round(cp.getCantidad());
            oi.setQuantity(qty);
            // Delegate saving to OrderItemController
            OrderItem saved = orderItemController.guardarItem(oi);
            items.add(saved);
            // remove carrito item
            if (cp.getIdCarritoProducto() != null) {
                try {
                    carritoProductoService.eliminar(cp.getIdCarritoProducto());
                } catch (Exception ex) {
                    // log or ignore — continue removing other items
                }
            }
        }

        order.setItems(items.stream().collect(Collectors.toList()));
        orderService.guardar(order);

        // Remove carrito items already deleted above; now delete the carrito itself
        try {
            if (carrito.getId() != null) {
                carritoService.eliminar(carrito.getId());
            }
        } catch (Exception ex) {
            // if deletion fails, set status as Completado as fallback
            carrito.setEstado("Completado");
            carritoService.guardar(carrito);
        }

        model.addAttribute("order", order);
        return "Order/OrderConfirmation";
    }

    // Show payment page and populate cart summary
    @GetMapping("/PasarelaPagos")
    public String showPaymentPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            // Not authenticated: show empty summary or redirect to login
            model.addAttribute("carrito", null);
            model.addAttribute("productos", List.of());
            model.addAttribute("subtotal", 0.00);
            model.addAttribute("descuento", 0.00);
            model.addAttribute("igv", 0.00);
            model.addAttribute("total", 0.00);
            return "PasarelaPagos";
        }

        String username = auth.getName();
        User usuario = usuarioService.findByUsername(username);
        if (usuario == null) {
            model.addAttribute("carrito", null);
            model.addAttribute("productos", List.of());
            model.addAttribute("subtotal", 0.00);
            model.addAttribute("descuento", 0.00);
            model.addAttribute("igv", 0.00);
            model.addAttribute("total", 0.00);
            return "PasarelaPagos";
        }

        Carrito carrito = carritoService.obtenerCarritoActivo(usuario.getId());
        if (carrito == null) {
            model.addAttribute("carrito", null);
            model.addAttribute("productos", List.of());
            model.addAttribute("subtotal", 0.00);
            model.addAttribute("descuento", 0.00);
            model.addAttribute("igv", 0.00);
            model.addAttribute("total", 0.00);
            return "PasarelaPagos";
        }

        List<CarritoProducto> productos = carritoProductoService.buscarPorCarrito(carrito.getId());

        double total = 0.0;
        if (productos != null) {
            for (CarritoProducto cp : productos) {
                if (cp.getProduct() != null && cp.getCantidad() != null) {
                    total += (cp.getProduct().getPrice() != null ? cp.getProduct().getPrice() : 0.0) * cp.getCantidad();
                }
            }
        }

        double descuento = 0.0;
        double subtotal = total / 1.18; // derive subtotal before IGV
        double igv = total - subtotal;

        model.addAttribute("carrito", carrito);
        model.addAttribute("productos", productos != null ? productos : List.of());
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("descuento", descuento);
        model.addAttribute("igv", igv);
        model.addAttribute("total", total - descuento);

        return "PasarelaPagos";
    }

    // List orders for the authenticated user
    @GetMapping("/Orders")
    public String listUserOrders(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/Login";
        }
        String username = auth.getName();
        User usuario = usuarioService.findByUsername(username);
        if (usuario == null) return "redirect:/Login";

        java.util.List<Order> orders = orderService.buscarPorUsuario(usuario.getId());
        model.addAttribute("orders", orders);
        return "Order/Orders";
    }

    // Show order details and items for a specific order (only owner can view)
    @GetMapping("/orders/{id}")
    public String showOrderDetails(@PathVariable("id") Integer id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/Login";
        }
        String username = auth.getName();
        User usuario = usuarioService.findByUsername(username);
        if (usuario == null) return "redirect:/Login";

        Order order = orderService.buscarPorId(id);
        if (order == null) {
            model.addAttribute("error", "Pedido no encontrado");
            return "Orders";
        }
        // ensure the logged user is the owner
        if (order.getUser() == null || order.getUser().getId() == null || !order.getUser().getId().equals(usuario.getId())) {
            return "redirect:/Orders"; // forbidden for non owners
        }

        java.util.List<OrderItem> items = orderItemService.buscarPorOrden(id);
        model.addAttribute("order", order);
        model.addAttribute("items", items);
        return "Order/OrderItems";
    }

    // Admin: listar todas las órdenes (interfaz administrativa)
    @GetMapping("/Verorder")
    public String adminListOrders(Model model) {
        java.util.List<Order> orders = orderService.listar();
        model.addAttribute("orders", orders);
        return "Order/VerOrder";
    }

    // Admin: ver items de un pedido específico
    @GetMapping("/Verorder/{id}")
    public String adminViewOrderItems(@PathVariable("id") Integer id, Model model) {
        Order order = orderService.buscarPorId(id);
        if (order == null) {
            model.addAttribute("error", "Pedido no encontrado");
            return "Order/VerOrder";
        }
        java.util.List<OrderItem> items = orderItemService.buscarPorOrden(id);
        model.addAttribute("order", order);
        model.addAttribute("items", items);
        return "Order/VerOrderitems";
    }

}
