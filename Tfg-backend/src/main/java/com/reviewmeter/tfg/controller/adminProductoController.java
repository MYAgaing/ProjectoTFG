package com.reviewmeter.tfg.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.reviewmeter.tfg.model.Categoria;
import com.reviewmeter.tfg.model.Producto;
import com.reviewmeter.tfg.repository.categoriaRepository;
import com.reviewmeter.tfg.repository.productoRepository;
import com.reviewmeter.tfg.service.FileStorageService;

@RestController
@RequestMapping("/admin/productos")
@CrossOrigin(origins = "*")
public class adminProductoController {

    @Autowired
    private productoRepository productoRepo;

    @Autowired
    private categoriaRepository categoriaRepo;

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${app.backend-url:http://localhost:8080}")
    private String backendUrl;

    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> getCategorias() {
        return ResponseEntity.ok(categoriaRepo.findAll());
    }

    @PostMapping
    public ResponseEntity<?> crearProducto(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("marca") String marca,
            @RequestParam("fechaLanzamiento") String fechaLanzamiento,
            @RequestParam("idCategoria") Long idCategoria,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen) {

        try {
            Categoria categoria = categoriaRepo.findById(idCategoria)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setMarca(marca);
            producto.setFechaLanzamiento(LocalDate.parse(fechaLanzamiento));
            producto.setCategoria(categoria);
            producto.setValoracion(0);

            if (imagen != null && !imagen.isEmpty()) {
                String fileName = fileStorageService.storeFile(imagen);
                String imageUrl = backendUrl + "/uploads/productos/" + fileName;
                producto.setImageUrl(imageUrl);
            } else {
                // Si no mandan imagen uso un placeholder para que no quede vacío
                producto.setImageUrl("https://via.placeholder.com/400x300?text=Sin+Imagen");
            }

            productoRepo.save(producto);
            return ResponseEntity.ok(producto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear producto: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("marca") String marca,
            @RequestParam("fechaLanzamiento") String fechaLanzamiento,
            @RequestParam("idCategoria") Long idCategoria,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen) {

        try {
            Producto producto = productoRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            Categoria categoria = categoriaRepo.findById(idCategoria)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setMarca(marca);
            producto.setFechaLanzamiento(LocalDate.parse(fechaLanzamiento));
            producto.setCategoria(categoria);

            // Si mandan imagen nueva borro la anterior y guardo la nueva
            if (imagen != null && !imagen.isEmpty()) {
                String oldImageUrl = producto.getImageUrl();
                if (oldImageUrl != null && oldImageUrl.contains("/uploads/productos/")) {
                    String oldFileName = oldImageUrl.substring(oldImageUrl.lastIndexOf("/") + 1);
                    fileStorageService.deleteFile(oldFileName);
                }

                String fileName = fileStorageService.storeFile(imagen);
                String imageUrl = backendUrl + "/uploads/productos/" + fileName;
                producto.setImageUrl(imageUrl);
            }

            productoRepo.save(producto);
            return ResponseEntity.ok(producto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar producto: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            Producto producto = productoRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // Borro la imagen del disco si no es un placeholder externo
            String imageUrl = producto.getImageUrl();
            if (imageUrl != null && imageUrl.contains("/uploads/productos/")) {
                String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                fileStorageService.deleteFile(fileName);
            }

            productoRepo.delete(producto);
            return ResponseEntity.ok("Producto eliminado correctamente");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar producto: " + e.getMessage());
        }
    }
}
