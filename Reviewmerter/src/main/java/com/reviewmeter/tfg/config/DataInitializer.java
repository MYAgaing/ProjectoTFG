package com.reviewmeter.tfg.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.reviewmeter.tfg.model.*;
import com.reviewmeter.tfg.repository.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final rolRepository rolRepository;
    private final usuarioRepository usuarioRepository;
    private final categoriaRepository categoriaRepository;
    private final productoRepository productoRepository;
    private final resenaRepository resenaRepository;
    private final suscripcionRepository suscripcionRepository;
    private final pagoRepository pagoRepository;
    private final comentarioRepository comentarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            rolRepository rolRepository,
            usuarioRepository usuarioRepository,
            categoriaRepository categoriaRepository,
            productoRepository productoRepository,
            resenaRepository resenaRepository,
            suscripcionRepository suscripcionRepository,
            pagoRepository pagoRepository,
            comentarioRepository comentarioRepository,
            PasswordEncoder passwordEncoder) {

        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.resenaRepository = resenaRepository;
        this.suscripcionRepository = suscripcionRepository;
        this.pagoRepository = pagoRepository;
        this.comentarioRepository = comentarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (usuarioRepository.count() > 0) {
            System.out.println("Base de datos ya inicializada.");
            return;
        }

        System.out.println("Inicializando datos...");

        // ==========================
        // 1️⃣ ROLES
        // ==========================
        Rol rolUser = new Rol();
        rolUser.setNombre("USER");
        rolRepository.save(rolUser);

        Rol rolAdmin = new Rol();
        rolAdmin.setNombre("ADMIN");
        rolRepository.save(rolAdmin);

        // ==========================
        // 2️⃣ USUARIOS
        // ==========================
        Usuario admin = new Usuario();
        admin.setNombre("Administrador");
        admin.setEmail("admin@mail.com");
        admin.setPassword(passwordEncoder.encode("1234"));
        admin.setEstado(true);
        admin.setFechaRegistro(LocalDate.now());
        admin.setRol(rolAdmin);
        usuarioRepository.save(admin);

        for (int i = 1; i <= 10; i++) {
            Usuario user = new Usuario();
            user.setNombre("Usuario" + i);
            user.setEmail("user" + i + "@mail.com");
            user.setPassword(passwordEncoder.encode("1234"));
            user.setEstado(true);
            user.setFechaRegistro(LocalDate.now().minusDays(i));
            user.setRol(rolUser);
            usuarioRepository.save(user);
        }

        List<Usuario> usuarios = usuarioRepository.findAll();

        // ==========================
        // 3️⃣ CATEGORÍAS
        // ==========================
        Categoria tech = new Categoria();
        tech.setNombre("Tecnología");
        categoriaRepository.save(tech);

        Categoria gaming = new Categoria();
        gaming.setNombre("Gaming");
        categoriaRepository.save(gaming);

        Categoria hogar = new Categoria();
        hogar.setNombre("Hogar");
        categoriaRepository.save(hogar);

        Categoria juegos = new Categoria();
        juegos.setNombre("Juegos");
        categoriaRepository.save(juegos);

        Categoria tecnologia = new Categoria();
        tecnologia.setNombre("Tecnología");
        categoriaRepository.save(tecnologia);

        Categoria libros = new Categoria();
        libros.setNombre("Libros");
        categoriaRepository.save(libros);

        Categoria musica = new Categoria();
        musica.setNombre("Música");
        categoriaRepository.save(musica);

        Categoria deportes = new Categoria();
        deportes.setNombre("Deportes");
        categoriaRepository.save(deportes);

        Categoria comida = new Categoria();
        comida.setNombre("Comida");
        categoriaRepository.save(comida);

        Categoria ropa = new Categoria();
        ropa.setNombre("Ropa");
        categoriaRepository.save(ropa);

        Categoria belleza = new Categoria();
        belleza.setNombre("Belleza");
        categoriaRepository.save(belleza);

        Categoria juguetes = new Categoria();
        juguetes.setNombre("Juguetes");
        categoriaRepository.save(juguetes);

        Categoria cine = new Categoria();
        cine.setNombre("Cine");
        categoriaRepository.save(cine);

        Categoria fotografia = new Categoria();
        fotografia.setNombre("Fotografía");
        categoriaRepository.save(fotografia);

        Categoria bricolaje = new Categoria();
        bricolaje.setNombre("Bricolaje");
        categoriaRepository.save(bricolaje);

        Categoria herramientas = new Categoria();
        herramientas.setNombre("Herramientas");
        categoriaRepository.save(herramientas);

        Categoria viajes = new Categoria();
        viajes.setNombre("Viajes");
        categoriaRepository.save(viajes);

        Categoria animales = new Categoria();
        animales.setNombre("Animales");
        categoriaRepository.save(animales);

        Categoria salud = new Categoria();
        salud.setNombre("Salud");
        categoriaRepository.save(salud);

        Categoria coches = new Categoria();
        coches.setNombre("Coches");
        categoriaRepository.save(coches);

        Categoria videojuegos = new Categoria();
        videojuegos.setNombre("Videojuegos");
        categoriaRepository.save(videojuegos);

        Categoria educacion = new Categoria();
        educacion.setNombre("Educación");
        categoriaRepository.save(educacion);

        Categoria instrumentos = new Categoria();
        instrumentos.setNombre("Instrumentos musicales");
        categoriaRepository.save(instrumentos);

        Categoria electrónica = new Categoria();
        electrónica.setNombre("Electrónica");
        categoriaRepository.save(electrónica);

        Categoria jardineria = new Categoria();
        jardineria.setNombre("Jardinería");
        categoriaRepository.save(jardineria);

        Categoria moda = new Categoria();
        moda.setNombre("Moda");
        categoriaRepository.save(moda);
        
        List<Categoria> categorias = categoriaRepository.findAll();

        // ==========================
        // 4️⃣ PRODUCTOS
        // ==========================
        for (int i = 1; i <= 20; i++) {
            Producto p = new Producto();
            p.setNombre("Producto " + i);
            p.setDescripcion("Descripción del producto " + i);
            p.setMarca("Marca " + (i % 5));
            p.setFechaLanzamiento(LocalDate.now().minusMonths(i));
            p.setCategoria(categorias.get(i % categorias.size()));
            productoRepository.save(p);
        }

        List<Producto> productos = productoRepository.findAll();
        Random random = new Random();

        // ==========================
        // 5️⃣ RESEÑAS
        // ==========================
        for (int i = 0; i < 50; i++) {
            Resena r = new Resena();
            r.setTitulo("Reseña " + i);
            r.setComentario("Comentario de prueba número " + i);
            r.setPuntuacion(random.nextInt(5) + 1);
            r.setFecha(LocalDate.now().minusDays(random.nextInt(30)));
            r.setUsuario(usuarios.get(random.nextInt(usuarios.size())));
            r.setProducto(productos.get(random.nextInt(productos.size())));
            resenaRepository.save(r);
        }

        List<Resena> resenas = resenaRepository.findAll();

        // ==========================
        // 6️⃣ SUSCRIPCIONES
        // ==========================
        for (Usuario u : usuarios) {
            Suscripcion s = new Suscripcion();
            s.setTipo("PREMIUM");
            s.setPrecio(new BigDecimal("9.99"));
            s.setFechaInicio(LocalDate.now().minusMonths(1));
            s.setFechaFin(LocalDate.now().plusMonths(1));
            s.setEstado(true);
            s.setUsuario(u);
            suscripcionRepository.save(s);

            // ==========================
            // 7️⃣ PAGOS
            // ==========================
            Pago pago = new Pago();
            pago.setImporte(new BigDecimal("9.99"));
            pago.setFechaPago(LocalDate.now().minusDays(5));
            pago.setMetodoPago("TARJETA");
            pago.setEstado("COMPLETADO");
            pago.setUsuario(u);
            pago.setSuscripcion(s);
            pagoRepository.save(pago);
        }

        // ==========================
        // 8️⃣ COMENTARIOS
        // ==========================
        for (int i = 0; i < 30; i++) {
            Comentario c = new Comentario();
            c.setContenido("Comentario en reseña " + i);
            c.setFecha(LocalDate.now());
            c.setResena(resenas.get(random.nextInt(resenas.size())));
            comentarioRepository.save(c);
        }

        System.out.println("Datos inicializados correctamente 🚀");
    }
}
