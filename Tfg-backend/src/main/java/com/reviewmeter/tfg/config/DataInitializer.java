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

	public DataInitializer(rolRepository rolRepository, usuarioRepository usuarioRepository,
			categoriaRepository categoriaRepository, productoRepository productoRepository,
			resenaRepository resenaRepository, suscripcionRepository suscripcionRepository,
			pagoRepository pagoRepository, comentarioRepository comentarioRepository, PasswordEncoder passwordEncoder) {
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
		// 2️⃣ USUARIOS (30 usuarios)
		// ==========================
		Usuario admin = new Usuario();
		admin.setNombre("Administrador");
		admin.setEmail("admin@mail.com");
		admin.setPassword(passwordEncoder.encode("1234"));
		admin.setEstado(true);
		admin.setFechaRegistro(LocalDate.now());
		admin.setRol(rolAdmin);
		usuarioRepository.save(admin);

		String[] nombres = {
			"Carlos García", "María López", "Juan Martínez", "Ana Sánchez", "Pedro Rodríguez",
			"Laura González", "Miguel Fernández", "Sofía Torres", "David Díaz", "Elena Ruiz",
			"Alejandro Moreno", "Isabel Jiménez", "Roberto Álvarez", "Carmen Romero", "Francisco Navarro",
			"Patricia Suárez", "Manuel Serrano", "Cristina Molina", "Antonio Blanco", "Lucía Castro",
			"Javier Ortega", "Natalia Ramos", "Sergio Vargas", "Pilar Iglesias", "Alberto Herrero",
			"Marta Medina", "Raúl Delgado", "Silvia Flores", "Rubén Morales", "Andrea Vega"
		};

		for (int i = 0; i < nombres.length; i++) {
			Usuario user = new Usuario();
			user.setNombre(nombres[i]);
			user.setEmail("user" + (i + 1) + "@mail.com");
			user.setPassword(passwordEncoder.encode("1234"));
			user.setEstado(true);
			user.setFechaRegistro(LocalDate.now().minusDays(i * 3L));
			user.setRol(rolUser);
			usuarioRepository.save(user);
		}

		List<Usuario> usuarios = usuarioRepository.findAll();

		// ==========================
		// 3️⃣ CATEGORÍAS
		// ==========================
		Categoria tecnologia = new Categoria(); 
		tecnologia.setNombre("Tecnología"); 
		tecnologia.setImageUrl("https://images.unsplash.com/photo-1518770660439-4636190af475?q=80&w=800");
		categoriaRepository.save(tecnologia);

		Categoria videojuegos = new Categoria(); 
		videojuegos.setNombre("Videojuegos"); 
		videojuegos.setImageUrl("https://images.unsplash.com/photo-1538481199705-c710c4e965fc?q=80&w=800");
		categoriaRepository.save(videojuegos);

		Categoria libros = new Categoria(); 
		libros.setNombre("Libros"); 
		libros.setImageUrl("https://images.unsplash.com/photo-1495446815901-a7297e633e8d?q=80&w=800");
		categoriaRepository.save(libros);

		Categoria electronica = new Categoria(); 
		electronica.setNombre("Electrónica"); 
		electronica.setImageUrl("https://images.unsplash.com/photo-1550009158-9ebf69173e03?q=80&w=800");
		categoriaRepository.save(electronica);

		Categoria moda = new Categoria(); 
		moda.setNombre("Moda"); 
		moda.setImageUrl("https://images.unsplash.com/photo-1445205170230-053b83016050?q=80&w=800");
		categoriaRepository.save(moda);

		Categoria gaming = new Categoria(); 
		gaming.setNombre("Gaming"); 
		gaming.setImageUrl("https://images.unsplash.com/photo-1542751371-adc38448a05e?q=80&w=800");
		categoriaRepository.save(gaming);

		Categoria salud = new Categoria(); 
		salud.setNombre("Salud"); 
		salud.setImageUrl("https://images.unsplash.com/photo-1505751172876-fa1923c5c528?q=80&w=800");
		categoriaRepository.save(salud);

		Categoria fotografia = new Categoria(); 
		fotografia.setNombre("Fotografía"); 
		fotografia.setImageUrl("https://images.unsplash.com/photo-1516035069371-29a1b244cc32?q=80&w=800");
		categoriaRepository.save(fotografia);

		Categoria coches = new Categoria(); 
		coches.setNombre("Coches"); 
		coches.setImageUrl("https://images.unsplash.com/photo-1492144534655-ae79c964c9d7?q=80&w=800");
		categoriaRepository.save(coches);

		Categoria deportes = new Categoria(); 
		deportes.setNombre("Deportes"); 
		deportes.setImageUrl("https://images.unsplash.com/photo-1461896836934-ffe607ba8211?q=80&w=800");
		categoriaRepository.save(deportes);

		Categoria instrumentos = new Categoria(); 
		instrumentos.setNombre("Instrumentos musicales"); 
		instrumentos.setImageUrl("https://images.unsplash.com/photo-1511379938547-c1f69419868d?q=80&w=800");
		categoriaRepository.save(instrumentos);

		Categoria hogar = new Categoria(); 
		hogar.setNombre("Hogar"); 
		hogar.setImageUrl("https://images.unsplash.com/photo-1484154218962-a197022b5858?q=80&w=800");
		categoriaRepository.save(hogar);

		Categoria musica = new Categoria(); 
		musica.setNombre("Música"); 
		musica.setImageUrl("https://images.unsplash.com/photo-1470225620780-dba8ba36b745?q=80&w=800");
		categoriaRepository.save(musica);

		Categoria cine = new Categoria(); 
		cine.setNombre("Cine"); 
		cine.setImageUrl("https://images.unsplash.com/photo-1485846234645-a62644f84728?q=80&w=800");
		categoriaRepository.save(cine);

		Categoria viajes = new Categoria(); 
		viajes.setNombre("Viajes"); 
		viajes.setImageUrl("https://images.unsplash.com/photo-1488085061387-422e29b40080?q=80&w=800");
		categoriaRepository.save(viajes);

		Categoria educacion = new Categoria(); 
		educacion.setNombre("Educación"); 
		educacion.setImageUrl("https://images.unsplash.com/photo-1523050335456-adeba884a0ad?q=80&w=800");
		categoriaRepository.save(educacion);

		Categoria belleza = new Categoria(); 
		belleza.setNombre("Belleza"); 
		belleza.setImageUrl("https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?q=80&w=800");
		categoriaRepository.save(belleza);

		Categoria animales = new Categoria(); 
		animales.setNombre("Animales"); 
		animales.setImageUrl("https://images.unsplash.com/photo-1450778869180-41d0601e046e?q=80&w=800");
		categoriaRepository.save(animales);

		Categoria jardineria = new Categoria(); 
		jardineria.setNombre("Jardinería"); 
		jardineria.setImageUrl("https://images.unsplash.com/photo-1416879595882-3373a0480b5b?q=80&w=800");
		categoriaRepository.save(jardineria);

		Categoria comida = new Categoria(); 
		comida.setNombre("Comida"); 
		comida.setImageUrl("https://images.unsplash.com/photo-1504674900247-0877df9cc836?q=80&w=800");
		categoriaRepository.save(comida);

		// ==========================
		// 4️⃣ PRODUCTOS
		// ==========================

		// --- TECNOLOGÍA ---
		crearProducto("iPhone 15 Pro", "Smartphone Apple con chip A17 Pro y cámara de 48MP", "Apple", tecnologia,
				"https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=400", 5);
		crearProducto("iPhone 14", "Smartphone Apple con modo acción en cámara", "Apple", tecnologia,
				"https://images.unsplash.com/photo-1663499482523-1c0c1bae4ce1?w=400", 4);
		crearProducto("Samsung Galaxy S24 Ultra", "Smartphone Android con S-Pen integrado", "Samsung", tecnologia,
				"https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=400", 5);
		crearProducto("Google Pixel 8 Pro", "Smartphone con IA de Google y cámara excepcional", "Google", tecnologia,
				"https://images.unsplash.com/photo-1598327105854-c8674faddf79?w=400", 4);
		crearProducto("MacBook Air M3", "Portátil ultradelgado con chip M3 de Apple", "Apple", tecnologia,
				"https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400", 5);
		crearProducto("MacBook Pro 16", "Portátil profesional con chip M3 Max", "Apple", tecnologia,
				"https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=400", 5);
		crearProducto("Dell XPS 15", "Portátil premium con pantalla OLED 4K", "Dell", tecnologia,
				"https://images.unsplash.com/photo-1593642632559-0c6d3fc62b89?w=400", 4);
		crearProducto("Microsoft Surface Pro 9", "Tablet con teclado convertible en portátil", "Microsoft", tecnologia,
				"https://images.unsplash.com/photo-1617791160588-241658ad5a1e?w=400", 3);

		// --- VIDEOJUEGOS ---
		crearProducto("The Legend of Zelda: Tears of the Kingdom", "Aventura épica en el reino de Hyrule", "Nintendo", videojuegos,
				"https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?w=400", 5);
		crearProducto("PlayStation 5", "Consola de última generación de Sony con SSD ultrarrápido", "Sony", videojuegos,
				"https://images.unsplash.com/photo-1607853202273-797f1c22a38e?w=400", 5);
		crearProducto("Xbox Series X", "La consola más potente de Microsoft con Game Pass", "Microsoft", videojuegos,
				"https://images.unsplash.com/photo-1621259182978-fbf93132d53d?w=400", 4);
		crearProducto("Nintendo Switch OLED", "Consola híbrida con pantalla OLED vibrante", "Nintendo", videojuegos,
				"https://images.unsplash.com/photo-1578303512597-81e6cc155b3e?w=400", 5);
		crearProducto("Elden Ring", "RPG de acción en un vasto mundo abierto", "FromSoftware", videojuegos,
				"https://images.unsplash.com/photo-1552820728-8b83bb6b773f?w=400", 5);
		crearProducto("God of War Ragnarök", "Épica aventura nórdica con Kratos y Atreus", "Sony Santa Monica", videojuegos,
				"https://images.unsplash.com/photo-1538481199705-c710c4e965fc?w=400", 5);
		crearProducto("Hogwarts Legacy", "RPG ambientado en el universo de Harry Potter", "Avalanche Software", videojuegos,
				"https://images.unsplash.com/photo-1560419015-7c427e8ae5ba?w=400", 4);
		crearProducto("Cyberpunk 2077", "RPG de mundo abierto en una ciudad futurista", "CD Projekt Red", videojuegos,
				"https://images.unsplash.com/photo-1542751371-adc38448a05e?w=400", 4);

		// --- LIBROS ---
		crearProducto("Clean Code", "Guía para escribir código limpio y mantenible", "Robert C. Martin", libros,
				"https://images.unsplash.com/photo-1532012197267-da84d127e765?w=400", 5);
		crearProducto("El Quijote", "La obra cumbre de la literatura española", "Miguel de Cervantes", libros,
				"https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=400", 5);
		crearProducto("Sapiens", "Breve historia de la humanidad por Yuval Noah Harari", "Yuval Noah Harari", libros,
				"https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=400", 5);
		crearProducto("Harry Potter y la piedra filosofal", "El inicio de la saga mágica de Hogwarts", "J.K. Rowling", libros,
				"https://images.unsplash.com/photo-1600189261867-30e5ffe7b8da?w=400", 5);
		crearProducto("1984", "Novela distópica sobre el totalitarismo y la vigilancia", "George Orwell", libros,
				"https://images.unsplash.com/photo-1495640388908-05fa85288e61?w=400", 5);
		crearProducto("El Señor de los Anillos", "La épica fantasía de Tolkien en la Tierra Media", "J.R.R. Tolkien", libros,
				"https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400", 5);
		crearProducto("Atomic Habits", "Técnicas para crear buenos hábitos y eliminar los malos", "James Clear", libros,
				"https://images.unsplash.com/photo-1554415707-6e8cfc93fe23?w=400", 4);

		// --- ELECTRÓNICA ---
		crearProducto("Sony WH-1000XM5", "Auriculares inalámbricos con cancelación de ruido líder", "Sony", electronica,
				"https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400", 5);
		crearProducto("AirPods Pro 2", "Auriculares Apple con cancelación activa de ruido", "Apple", electronica,
				"https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?w=400", 5);
		crearProducto("iPad Pro 12.9", "Tablet profesional Apple con chip M2 y pantalla Liquid Retina", "Apple", electronica,
				"https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=400", 5);
		crearProducto("Samsung Galaxy Tab S9", "Tablet Android premium con pantalla AMOLED", "Samsung", electronica,
				"https://images.unsplash.com/photo-1561154464-82e9adf32764?w=400", 4);
		crearProducto("Apple Watch Series 9", "Smartwatch con sensor de temperatura y ECG", "Apple", electronica,
				"https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=400", 5);
		crearProducto("Kindle Paperwhite", "Lector de libros electrónicos con pantalla sin reflejos", "Amazon", electronica,
				"https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=400", 4);

		// --- MODA ---
		crearProducto("Nike Air Max 90", "Las zapatillas icónicas de running con Air Unit visible", "Nike", moda,
				"https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400", 4);
		crearProducto("Nike Air Force 1", "Las zapatillas más vendidas de la historia de Nike", "Nike", moda,
				"https://images.unsplash.com/photo-1600185365483-26d7a4cc7519?w=400", 5);
		crearProducto("Levi's 501", "El vaquero original de corte recto desde 1873", "Levi's", moda,
				"https://images.unsplash.com/photo-1542272604-787c3835535d?w=400", 4);
		crearProducto("Adidas Ultraboost 23", "Zapatillas de running con tecnología Boost", "Adidas", moda,
				"https://images.unsplash.com/photo-1608231387042-66d1773070a5?w=400", 4);
		crearProducto("Ray-Ban Wayfarer", "Las gafas de sol más icónicas del siglo XX", "Ray-Ban", moda,
				"https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=400", 5);
		crearProducto("Zara Trench Coat", "Gabardina clásica para todas las estaciones", "Zara", moda,
				"https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=400", 3);

		// --- GAMING ---
		crearProducto("Razer DeathAdder V3", "Ratón gaming ergonómico de alto rendimiento 30K DPI", "Razer", gaming,
				"https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=400", 4);
		crearProducto("Logitech G Pro X Superlight", "Ratón gaming ultraligero para eSports profesionales", "Logitech", gaming,
				"https://images.unsplash.com/photo-1563297007-0686b7370b0b?w=400", 5);
		crearProducto("SteelSeries Arctis Nova Pro", "Auriculares gaming premium con cancelación activa", "SteelSeries", gaming,
				"https://images.unsplash.com/photo-1599669454699-248893623440?w=400", 5);
		crearProducto("Corsair K70 RGB", "Teclado mecánico gaming con switches Cherry MX", "Corsair", gaming,
				"https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=400", 4);
		crearProducto("Monitor LG 27GP950", "Monitor gaming 4K 144Hz Nano IPS", "LG", gaming,
				"https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400", 5);
		crearProducto("NVIDIA RTX 4080", "Tarjeta gráfica de alta gama para gaming y creación", "NVIDIA", gaming,
				"https://images.unsplash.com/photo-1591488320449-011701bb6704?w=400", 5);

		// --- SALUD ---
		crearProducto("Fitbit Charge 6", "Pulsera de actividad con GPS y sensor de estrés", "Fitbit", salud,
				"https://images.unsplash.com/photo-1575311373937-040b8e1fd5b6?w=400", 4);
		crearProducto("Garmin Forerunner 965", "Reloj GPS premium para corredores avanzados", "Garmin", salud,
				"https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1?w=400", 5);
		crearProducto("Withings Body Scan", "Báscula inteligente con análisis corporal completo", "Withings", salud,
				"https://images.unsplash.com/photo-1584467735867-4297ae2ebcee?w=400", 4);
		crearProducto("Oura Ring Gen 3", "Anillo inteligente con monitoreo de salud 24/7", "Oura", salud,
				"https://images.unsplash.com/photo-1515377905703-c4788e51af15?w=400", 4);

		// --- FOTOGRAFÍA ---
		crearProducto("Canon EOS R50", "Cámara mirrorless compacta ideal para principiantes", "Canon", fotografia,
				"https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=400", 4);
		crearProducto("Sony A7 IV", "Cámara mirrorless de fotograma completo para profesionales", "Sony", fotografia,
				"https://images.unsplash.com/photo-1617005082133-548c4dd27f35?w=400", 5);
		crearProducto("Fujifilm X-T5", "Cámara mirrorless con sensor APS-C de 40MP y estética retro", "Fujifilm", fotografia,
				"https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=400", 5);
		crearProducto("DJI Mini 4 Pro", "Dron compacto con cámara 4K/60fps y obstáculos omnidireccionales", "DJI", fotografia,
				"https://images.unsplash.com/photo-1473968512647-3e447244af8f?w=400", 5);

		// --- COCHES ---
		crearProducto("Tesla Model 3", "Sedán eléctrico con autopilot y 500km de autonomía", "Tesla", coches,
				"https://images.unsplash.com/photo-1560958089-b8a1929cea89?w=400", 5);
		crearProducto("Tesla Model Y", "SUV eléctrico más vendido del mundo", "Tesla", coches,
				"https://images.unsplash.com/photo-1619767886558-efdc259cde1a?w=400", 5);
		crearProducto("BMW Serie 3", "El sedán deportivo de referencia del segmento premium", "BMW", coches,
				"https://images.unsplash.com/photo-1555215695-3004980ad54e?w=400", 4);
		crearProducto("Volkswagen Golf GTI", "El hatchback deportivo más icónico de todos los tiempos", "Volkswagen", coches,
				"https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?w=400", 4);

		// --- DEPORTES ---
		crearProducto("Raqueta Wilson Pro Staff RF97", "La raqueta de Roger Federer para jugadores avanzados", "Wilson", deportes,
				"https://images.unsplash.com/photo-1617083934555-ac4b7a5e3041?w=400", 5);
		crearProducto("Bicicleta Trek Domane SL 6", "Bicicleta de carretera endurance con IsoSpeed", "Trek", deportes,
				"https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400", 5);
		crearProducto("Zapatillas Asics Gel-Nimbus 25", "Zapatillas de running con máxima amortiguación", "Asics", deportes,
				"https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400", 4);
		crearProducto("Pesas Ajustables Bowflex 552", "Mancuernas ajustables de 2 a 24kg para home gym", "Bowflex", deportes,
				"https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=400", 4);

		// --- INSTRUMENTOS MUSICALES ---
		crearProducto("Guitarra Fender Stratocaster", "La guitarra eléctrica más icónica del rock", "Fender", instrumentos,
				"https://images.unsplash.com/photo-1510915361894-db8b60106cb1?w=400", 5);
		crearProducto("Piano Yamaha P-145", "Piano digital compacto para principiantes y estudiantes", "Yamaha", instrumentos,
				"https://images.unsplash.com/photo-1520523839897-bd0b52f945a0?w=400", 4);
		crearProducto("Batería Roland TD-17KVX", "Batería electrónica silenciosa con mallas de doble capa", "Roland", instrumentos,
				"https://images.unsplash.com/photo-1519892300165-cb5542fb47c7?w=400", 4);
		crearProducto("Bajo Fender Jazz Bass", "El bajo eléctrico preferido de los músicos profesionales", "Fender", instrumentos,
				"https://images.unsplash.com/photo-1558098329-a11cff621064?w=400", 5);

		// --- HOGAR ---
		crearProducto("Roomba j7+", "Robot aspirador con vaciado automático y esquiva obstáculos", "iRobot", hogar,
				"https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400", 4);
		crearProducto("Dyson V15 Detect", "Aspirador sin cable con láser que detecta el polvo", "Dyson", hogar,
				"https://images.unsplash.com/photo-1558618047-3c8c76ca7d13?w=400", 5);
		crearProducto("Thermomix TM6", "Robot de cocina todo en uno con 22 funciones", "Vorwerk", hogar,
				"https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=400", 5);
		crearProducto("Nespresso Vertuo Next", "Cafetera de cápsulas con tecnología Centrifusion", "Nespresso", hogar,
				"https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=400", 4);
		crearProducto("Phillips Hue Starter Kit", "Kit de iluminación inteligente RGB con puente", "Philips", hogar,
				"https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400", 4);

		// --- BELLEZA ---
		crearProducto("Dyson Airwrap", "Moldeador multifunción de pelo con flujo de aire Coanda", "Dyson", belleza,
				"https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?w=400", 5);
		crearProducto("Foreo Luna 4", "Dispositivo de limpieza facial con masaje microcorriente", "Foreo", belleza,
				"https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?w=400", 4);
		crearProducto("Oral-B iO Series 9", "Cepillo eléctrico con IA y pantalla de colores", "Oral-B", belleza,
				"https://images.unsplash.com/photo-1607613009820-a29f7bb81c04?w=400", 4);

		// --- MÚSICA ---
		crearProducto("Sonos Era 300", "Altavoz espacial con sonido Dolby Atmos Music", "Sonos", musica,
				"https://images.unsplash.com/photo-1545454675-3531b543be5d?w=400", 5);
		crearProducto("Marshall Stanmore III", "Altavoz Bluetooth con el sonido clásico de Marshall", "Marshall", musica,
				"https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=400", 4);
		crearProducto("Bose SoundLink Flex", "Altavoz portátil resistente al agua con sonido 360°", "Bose", musica,
				"https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=400", 4);

		// --- ANIMALES ---
		crearProducto("Comedero automático PetSafe", "Dispensador de comida programable para mascotas", "PetSafe", animales,
				"https://images.unsplash.com/photo-1601758125946-6ec2ef64daf8?w=400", 4);
		crearProducto("GPS Tractive para perros", "Localizador GPS en tiempo real para tu mascota", "Tractive", animales,
				"https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=400", 4);
		crearProducto("Cámara Furbo 360", "Cámara para mascotas con dispensador de premios", "Furbo", animales,
				"https://images.unsplash.com/photo-1548199973-03cce0bbc87b?w=400", 3);

		// --- EDUCACIÓN ---
		crearProducto("Curso Online de Java - Udemy", "Aprende Java desde cero hasta nivel avanzado", "Udemy", educacion,
				"https://images.unsplash.com/photo-1587620962725-abab7fe55159?w=400", 4);
		crearProducto("Pizarra Wacom Intuos Pro", "Tableta gráfica profesional para diseño y educación", "Wacom", educacion,
				"https://images.unsplash.com/photo-1626785774573-4b799315345d?w=400", 4);
		crearProducto("Osmo Genius Kit", "Kit educativo interactivo para niños con iPad", "Osmo", educacion,
				"https://images.unsplash.com/photo-1503676260728-1c00da094a0b?w=400", 5);

		List<Producto> productos = productoRepository.findAll();
		Random random = new Random();

		// ==========================
		// 5️⃣ RESEÑAS (150 reseñas)
		// ==========================
		String[] titulos = {
			"Excelente producto", "Muy buena compra", "Totalmente recomendable", "Superó mis expectativas",
			"Buena relación calidad-precio", "Lo esperaba mejor", "Funciona perfecto", "Increíble calidad",
			"Muy satisfecho", "No lo volvería a comprar", "Para siempre", "Una maravilla",
			"Cumple lo prometido", "Mucho mejor de lo esperado", "Decepcionante", "Vale cada euro",
			"Producto top", "Regular tirando a malo", "Fantástico", "Recomiendo al 100%"
		};

		String[] comentarios = {
			"Lo compré hace un mes y sigo encantado. La calidad es impresionante y el rendimiento supera lo que esperaba.",
			"Muy buen producto en general. El envío fue rápido y el embalaje perfecto. Lo recomiendo sin dudarlo.",
			"He probado varias alternativas y esta es sin duda la mejor. Merece totalmente la pena.",
			"Al principio dudé si comprarlo pero ha sido una de las mejores decisiones. No me arrepiento.",
			"La relación calidad-precio es inmejorable. Por este precio no encontrarás nada mejor en el mercado.",
			"Esperaba más por el precio. No está mal pero hay opciones mejores por el mismo dinero.",
			"Llevo usándolo semanas y funciona exactamente como se describe. Sin problemas de ningún tipo.",
			"La construcción es sólida y los materiales de primera calidad. Se nota que está bien hecho.",
			"Mi tercer compra de esta marca y siguen sin defraudar. Calidad constante y garantía impecable.",
			"Devolvería el producto si pudiera. No cumple con lo que promete y el servicio fue pésimo.",
			"Ya llevo años usándolo y sigue como el primer día. Durabilidad garantizada sin ninguna duda.",
			"Es una auténtica maravilla. Cada detalle está cuidado al máximo y la experiencia es única.",
			"Exactamente lo que se anuncia, ni más ni menos. Hace su trabajo sin complicaciones.",
			"Me sorprendió gratamente. Las fotos no le hacen justicia a la calidad real del producto.",
			"No cumplió con mis expectativas. La descripción es engañosa y la calidad deja mucho que desear.",
			"Cada euro está más que justificado. Estoy usando todos los días y es un auténtico placer.",
			"Sin duda el mejor de su categoría. He probado muchos y este está en otro nivel completamente.",
			"No es malo del todo pero tampoco destaca. Para uso ocasional puede servir pero no para uso intensivo.",
			"Fantástico desde el primer día. La instalación fue sencilla y el rendimiento es sobresaliente.",
			"Lo recomiendo a todo el mundo. Ya se lo he dicho a todos mis amigos y familia sin excepción."
		};

		for (int i = 0; i < 150; i++) {
			Resena r = new Resena();
			r.setTitulo(titulos[random.nextInt(titulos.length)]);
			r.setComentario(comentarios[random.nextInt(comentarios.length)]);
			r.setFecha(LocalDate.now().minusDays(random.nextInt(180)));
			r.setUsuario(usuarios.get(random.nextInt(usuarios.size())));
			r.setProducto(productos.get(random.nextInt(productos.size())));
			resenaRepository.save(r);
		}

		List<Resena> resenas = resenaRepository.findAll();

		// ==========================
		// 6️⃣ SUSCRIPCIONES Y PAGOS
		// ==========================
		String[] tiposSuscripcion = {"FREE", "BASIC", "PREMIUM"};
		BigDecimal[] precios = {BigDecimal.ZERO, new BigDecimal("4.99"), new BigDecimal("9.99")};

		for (int i = 0; i < usuarios.size(); i++) {
			Usuario u = usuarios.get(i);
			int tipoIdx = i % 3;

			Suscripcion s = new Suscripcion();
			s.setTipo(tiposSuscripcion[tipoIdx]);
			s.setPrecio(precios[tipoIdx]);
			s.setFechaInicio(LocalDate.now().minusMonths(random.nextInt(6) + 1));
			s.setFechaFin(LocalDate.now().plusMonths(random.nextInt(12) + 1));
			s.setEstado(true);
			s.setUsuario(u);
			suscripcionRepository.save(s);

			if (tipoIdx > 0) {
				Pago pago = new Pago();
				pago.setImporte(precios[tipoIdx]);
				pago.setFechaPago(LocalDate.now().minusDays(random.nextInt(30)));
				pago.setMetodoPago(random.nextBoolean() ? "TARJETA" : "PAYPAL");
				pago.setEstado("COMPLETADO");
				pago.setUsuario(u);
				pago.setSuscripcion(s);
				pagoRepository.save(pago);
			}
		}

		// ==========================
		// 7️⃣ COMENTARIOS (100 comentarios)
		// ==========================
		String[] contenidosComentario = {
			"Totalmente de acuerdo con esta reseña, yo tuve la misma experiencia.",
			"Gracias por el análisis tan detallado, me ha ayudado mucho a decidir.",
			"Discrepo un poco, a mí me funcionó perfectamente desde el primer día.",
			"¿Dónde lo compraste? Estoy buscando el mejor precio posible.",
			"Llevamos usándolo en familia y todos estamos muy contentos con él.",
			"Muy útil tu reseña, justo lo que necesitaba saber antes de comprarlo.",
			"Yo también lo tengo y confirmo absolutamente todo lo que dices.",
			"¿Sigue funcionando bien después de varios meses de uso intensivo?",
			"Excelente reseña, muy completa y bien explicada. Muchas gracias.",
			"Lo acabo de pedir gracias a tu comentario. Espero que no me decepcione.",
			"¿Tiene garantía oficial? ¿Has tenido que usarla en algún momento?",
			"Mi experiencia fue diferente, quizás me tocó una unidad defectuosa.",
			"Completamente de acuerdo, la relación calidad-precio es imbatible.",
			"¿Cómo es el servicio postventa de la marca? ¿Responden rápido?",
			"Llevaba meses dudando y tu reseña me convenció para comprarlo finalmente."
		};

		for (int i = 0; i < 100; i++) {
			Comentario c = new Comentario();
			c.setContenido(contenidosComentario[random.nextInt(contenidosComentario.length)]);
			c.setFecha(LocalDate.now().minusDays(random.nextInt(90)));
			c.setResena(resenas.get(random.nextInt(resenas.size())));
			comentarioRepository.save(c);
		}

		System.out.println("✅ Datos inicializados correctamente:");
		System.out.println("   - 31 usuarios (1 admin + 30 usuarios)");
		System.out.println("   - 20 categorías");
		System.out.println("   - " + productos.size() + " productos");
		System.out.println("   - 150 reseñas");
		System.out.println("   - 100 comentarios");
		System.out.println("   - Suscripciones y pagos para todos los usuarios");
	}

	// ==========================
	// MÉTODO AUXILIAR
	// ==========================
	private void crearProducto(String nombre, String descripcion, String marca, Categoria categoria, String imagen, Integer valoracion) {
		Producto p = new Producto();
		p.setNombre(nombre);
		p.setDescripcion(descripcion);
		p.setMarca(marca);
		p.setFechaLanzamiento(LocalDate.now().minusMonths(new Random().nextInt(24) + 1));
		p.setCategoria(categoria);
		p.setImageUrl(imagen);
		p.setValoracion(valoracion);
		productoRepository.save(p);
	}
}