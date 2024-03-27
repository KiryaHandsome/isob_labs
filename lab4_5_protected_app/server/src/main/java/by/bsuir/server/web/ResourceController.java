package by.bsuir.server.web;

import by.bsuir.server.domain.Resource;
import by.bsuir.server.domain.Role;
import by.bsuir.server.repository.ResourceRepository;
import by.bsuir.server.web.dto.ResourceCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceRepository resourceRepository;

    @GetMapping
    public ResponseEntity<List<Resource>> getResource(@RequestHeader("role") Role role) {
        log.info("Get resource with role={}", role.name());
        if (!List.of(Role.ADMIN, Role.USER).contains(role)) {
            log.warn("User isn't authenticated");
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        return ResponseEntity.ok(resourceRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createResource(@RequestHeader("role") Role role,
                                            @RequestBody ResourceCreateRequest request) {
        log.info("Create resource with role={}, body={}", role.name(), request);
        if (!Role.ADMIN.equals(role)) {
            log.warn("User is not ADMIN. Request aborted");
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .build();
        }

        Resource resource = resourceRepository.save(new Resource(null, request.data()));
        log.info("Resource created: {}", resource);
        return ResponseEntity.ok(resource);
    }
}
