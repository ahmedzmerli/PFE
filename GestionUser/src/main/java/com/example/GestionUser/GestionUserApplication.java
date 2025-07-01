package com.example.GestionUser;

import com.example.GestionUser.entities.Permission;
import com.example.GestionUser.entities.PermissionList;
import com.example.GestionUser.entities.Role;
import com.example.GestionUser.entities.User;
import com.example.GestionUser.repositories.PermissionListRepository;
import com.example.GestionUser.repositories.PermissionRepository;
import com.example.GestionUser.repositories.RoleRepository;
import com.example.GestionUser.repositories.UserRepository;
import com.example.GestionUser.utils.PermissionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class GestionUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionUserApplication.class, args);
	}

	@Bean
	public CommandLineRunner generatePermissionsWithReflections(
			final PermissionRepository permissionRepository,
			final PermissionListRepository permissionListRepository,
			final RoleRepository roleRepository,
			final UserRepository userRepository,
			final PermissionUtils permissionUtils,
			final PasswordEncoder passwordEncoder
			) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				System.out.println("🔍 Scan avec Reflections pour générer les permissions...");

				Reflections reflections = new Reflections(
						new ConfigurationBuilder()
								.setUrls(ClasspathHelper.forPackage("com.example.GestionUser.controllers"))
								.setScanners(
										new SubTypesScanner(false),
										new TypeAnnotationsScanner(),
										new MethodAnnotationsScanner()
								)
								.filterInputsBy(new FilterBuilder()
										.includePackage("com.example.GestionUser.controllers"))
				);

				Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(RestController.class);

				Set<String> existingLabels = permissionRepository.findAll().stream()
						.map(p -> p.getFeature() + "." + p.getAction())
						.collect(Collectors.toSet());

				Set<String> seen = new HashSet<>();
				List<Permission> newPermissions = new ArrayList<>();

				for (Class<?> controller : controllerClasses) {
					String basePath = "";
					if (controller.isAnnotationPresent(RequestMapping.class)) {
						RequestMapping rm = controller.getAnnotation(RequestMapping.class);
						basePath = (rm.value().length > 0) ? rm.value()[0] : "";
					}

					for (Method method : controller.getDeclaredMethods()) {
						String path = "";
						String httpMethod = "";

						if (method.isAnnotationPresent(org.springframework.web.bind.annotation.GetMapping.class)) {
							httpMethod = "GET";
							path = method.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class)
									.value().length > 0
									? method.getAnnotation(org.springframework.web.bind.annotation.GetMapping.class).value()[0]
									: "";
						} else if (method.isAnnotationPresent(org.springframework.web.bind.annotation.PostMapping.class)) {
							httpMethod = "POST";
							path = method.getAnnotation(org.springframework.web.bind.annotation.PostMapping.class)
									.value().length > 0
									? method.getAnnotation(org.springframework.web.bind.annotation.PostMapping.class).value()[0]
									: "";
						} else if (method.isAnnotationPresent(org.springframework.web.bind.annotation.PutMapping.class)) {
							httpMethod = "PUT";
							path = method.getAnnotation(org.springframework.web.bind.annotation.PutMapping.class)
									.value().length > 0
									? method.getAnnotation(org.springframework.web.bind.annotation.PutMapping.class).value()[0]
									: "";
						} else if (method.isAnnotationPresent(org.springframework.web.bind.annotation.DeleteMapping.class)) {
							httpMethod = "DELETE";
							path = method.getAnnotation(org.springframework.web.bind.annotation.DeleteMapping.class)
									.value().length > 0
									? method.getAnnotation(org.springframework.web.bind.annotation.DeleteMapping.class).value()[0]
									: "";
						} else {
							continue;
						}

						String fullPath = (basePath + "/" + path).replaceAll("//+", "/");
						String feature = permissionUtils.extractFeatureFromPath(fullPath);
						String action = permissionUtils.mapHttpToAction(httpMethod);
						String label = feature + "." + action;

						if (!"unknown".equals(feature)
								&& !"unknown".equals(action)
								&& seen.add(label)
								&& !existingLabels.contains(label)) {
							System.out.println("✅ Nouvelle permission : " + label);

							Permission p = new Permission();
							p.setFeature(feature);
							p.setAction(action);
							newPermissions.add(p);
						}
					}
				}

				if (!newPermissions.isEmpty()) {
					permissionRepository.saveAll(newPermissions);
				}

				PermissionList adminPL = permissionListRepository.findAll().stream()
						.filter(pl -> "ADMIN_PL".equals(pl.getName()))
						.findFirst()
						.orElseGet(() -> {
							PermissionList pl = new PermissionList();
							pl.setName("ADMIN_PL");
							pl.setPermissions(new HashSet<>());
							return permissionListRepository.save(pl);
						});

				Set<String> plLabels = new HashSet<>();
				for (Permission p : adminPL.getPermissions()) {
					plLabels.add(p.getFeature() + "." + p.getAction());
				}

				List<Permission> allPermissions = permissionRepository.findAll();
				List<Permission> missing = new ArrayList<>();
				for (Permission p : allPermissions) {
					String lbl = p.getFeature() + "." + p.getAction();
					if (!plLabels.contains(lbl)) {
						missing.add(p);
					}
				}

				if (!missing.isEmpty()) {
					adminPL.getPermissions().addAll(missing);
					permissionListRepository.save(adminPL);
				}

				Role adminRole = roleRepository.findByName("ADMIN")
						.orElseGet(() -> {
							Role role = new Role();
							role.setName("ADMIN");
							role.setPermissionLists(new ArrayList<>());
							return roleRepository.save(role);
						});

				boolean hasPl = false;
				for (PermissionList pl : adminRole.getPermissionLists()) {
					if ("ADMIN_PL".equals(pl.getName())) {
						hasPl = true;
						break;
					}
				}

				if (!hasPl) {
					adminRole.getPermissionLists().add(adminPL);
					roleRepository.save(adminRole);
				}

				// 1. Création de l'utilisateur admin@gmail.com s'il n'existe pas déjà
				userRepository.findByEmail("admin@gmail.com").orElseGet(() -> {
					User admin = new User();
					admin.setFirstname("Super");
					admin.setLastname("Admin");
					admin.setEmail("admin@gmail.com");
					admin.setPassword(passwordEncoder.encode("admin123!")); // Utilise le PasswordEncoder !
					admin.setAccount_locked(false);
					admin.setEnabled(true);
					Set<Role> roles = new HashSet<>();
					roles.add(adminRole);
					admin.setRoles(roles);
					admin.setTokens(new ArrayList<>());
					return userRepository.save(admin);
				});


				System.out.println("🎉 Permissions et rôle admin générés proprement !");
			}
		};
	}
}
