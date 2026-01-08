package com.project.TECHGEAR.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.TECHGEAR.Model.Product;
import com.project.TECHGEAR.Model.ProductImage;
import com.project.TECHGEAR.Services.ProductService;
import com.project.TECHGEAR.Services.ProductImageService;
import jakarta.validation.Valid;


@Controller
public class ProductoController {
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductImageService productImageService;
    
    private static final String UPLOAD_DIR = "src/main/resources/static/images/";
    
    List<String> listarol = Arrays.asList("Tarjeta de video","Procesador","Placa Madre","Fuente de poder","Memoria RAM","Almacenamiento","Gabinete","Refrigeracion");

    @GetMapping("/Find")
    public String buscar(@RequestParam(required = false) String buscarProducto,
                         @RequestParam(required = false) String categoria,
                         Model model) {
        List<Product> productos;
        
        // Si hay un término de búsqueda, filtrar productos
        if ((buscarProducto != null && !buscarProducto.trim().isEmpty()) && (categoria != null && !categoria.trim().isEmpty())) {
            productos = productService.buscarPorNombreYCategoria(buscarProducto, categoria);
        } else if (buscarProducto != null && !buscarProducto.trim().isEmpty()) {
            productos = productService.buscarPorNombre(buscarProducto);
        } else if (categoria != null && !categoria.trim().isEmpty()) {
            productos = productService.buscarPorCategoria(categoria);
        } else {
            productos = productService.get();
        }
        
        // Cargar imágenes para cada producto
        for (Product producto : productos) {
            List<ProductImage> imagenes = productImageService.getByProductId(producto.getId());
            producto.setImages(imagenes);
        }
        model.addAttribute("lista", productos);
        model.addAttribute("terminoBusqueda", buscarProducto != null ? buscarProducto : "");
        model.addAttribute("categorias", listarol);
        model.addAttribute("categoriaSeleccionada", categoria != null ? categoria : "");
        return "Buscar";
    }
    
    @GetMapping({"/Verproducto/{id}"})
    public String verproducto(@PathVariable Integer id, Model model) {
        Product prod = productService.get(id);
        // Load images for the carousel
        List<ProductImage> imagenes = productImageService.getByProductId(id);
        prod.setImages(imagenes);
        model.addAttribute("prod", prod);
        model.addAttribute("imagenes", imagenes);
        return "seeProduct";
    }
    
    @GetMapping("/producto")
    public String producto(Model model) {
        model.addAttribute("lista", productService.get());
        return "Producto/Verproducto";
    }

    @PostMapping("/producto/guardar")
    public String Guardarproduct(@Valid @ModelAttribute("prod") Product product, 
                                BindingResult result, 
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("prod", product);
            model.addAttribute("error", "Por favor, corrija los errores en el formulario");
            return "Producto/nuevoproducto";
        }
        product.setUpdatedAt(Timestamp.from(Instant.now()));
        product.setCreatedAt(Timestamp.from(Instant.now()));
        productService.save(product);
        return "redirect:/producto";
    }
    
    @GetMapping("/producto/new")
    public String Nuevoproduct(Model model) {
        Product product = new Product();
        model.addAttribute("cat", listarol);
        model.addAttribute("prod", product);
        return "Producto/nuevoproducto";
    }
    
    @GetMapping("/producto/editar/{id}")
    public String Editarproduct(@PathVariable Integer id, Model modelo) {
        modelo.addAttribute("prod", productService.get(id));
        modelo.addAttribute("cat", listarol);
        return "Producto/Editarproducto";
    }

    @GetMapping("/producto/eliminar/{id}")
    public String eliminarproduct(@PathVariable Integer id) {
        productService.delete(id);
        return "redirect:/producto";
    }

    @PostMapping("/producto/actualizar/{id}")
    public String updateproduct(@PathVariable Integer id, 
                               @Valid @ModelAttribute("prod") Product product, 
                               BindingResult result, 
                               Model model) {
        if (result.hasErrors()) {
            product.setId(id);
            model.addAttribute("prod", product);
            model.addAttribute("error", "Por favor, corrija los errores en el formulario");
            return "Producto/Editarproducto";
        }
        Product now = productService.get(id);
        now.setId(id);
        now.setName(product.getName());
        now.setPrice(product.getPrice());
        now.setStock(product.getStock());
        now.setDescription(product.getDescription());
        now.setCategory(product.getCategory());
        now.setUpdatedAt(Timestamp.from(Instant.now()));
        productService.update(now);
        return "redirect:/producto";
    }
    
    @GetMapping("/producto/imagenes/{id}")
    public String verImagenesProducto(@PathVariable Integer id, Model model) {
        Product product = productService.get(id);
        List<ProductImage> imagenes = productImageService.getByProductId(id);
        model.addAttribute("producto", product);
        model.addAttribute("imagenes", imagenes);
        return "Producto/VerImagenesProducto";
    }
    
    @PostMapping("/producto/imagenes/{id}/subir")
    public String subirImagen(@PathVariable Integer id,
                              @RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Por favor, seleccione una imagen");
            return "redirect:/producto/imagenes/" + id;
        }
        
        try {
            Product product = productService.get(id);
            
            // Generar nombre único para la imagen
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                : ".jpg";
            String newFilename = UUID.randomUUID().toString() + extension;
            
            // Crear directorio si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Guardar archivo
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Guardar URL en la base de datos
            String imageUrl = "/images/" + newFilename;
            ProductImage productImage = new ProductImage();
            productImage.setUrl(imageUrl);
            productImage.setProduct(product);
            
            // Obtener el número de imágenes existentes para establecer el orden
            List<ProductImage> existingImages = productImageService.getByProductId(id);
            productImage.setDisplayOrder(existingImages.size() + 1);
            
            productImageService.save(productImage);
            
            redirectAttributes.addFlashAttribute("success", "Imagen subida exitosamente");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error al subir la imagen: " + e.getMessage());
        }
        
        return "redirect:/producto/imagenes/" + id;
    }
    
    @GetMapping("/producto/imagenes/{productId}/eliminar/{imageId}")
    public String eliminarImagen(@PathVariable Integer productId,
                                 @PathVariable Integer imageId,
                                 RedirectAttributes redirectAttributes) {
        try {
            ProductImage image = productImageService.get(imageId);
            
            // Eliminar archivo físico
            String url = image.getUrl();
            if (url != null && url.startsWith("/images/")) {
                String filename = url.substring("/images/".length());
                Path filePath = Paths.get(UPLOAD_DIR + filename);
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }
            }
            
            // Eliminar de la base de datos
            productImageService.delete(imageId);
            
            redirectAttributes.addFlashAttribute("success", "Imagen eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la imagen: " + e.getMessage());
        }
        
        return "redirect:/producto/imagenes/" + productId;
    }
}