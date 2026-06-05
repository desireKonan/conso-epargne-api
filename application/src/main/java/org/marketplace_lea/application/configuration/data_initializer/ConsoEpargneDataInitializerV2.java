package org.marketplace_lea.application.configuration.data_initializer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ConsoEpargneDataInitializerV2 implements ApplicationListener<ContextRefreshedEvent> {
    private final JsonDataInitializationService jsonDataInitializationService;

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        // Le service utilise @PostConstruct, donc l'initialisation a déjà eu lieu.
        // On peut soit appeler explicitement, soit ne rien faire.
        // Pour éviter double appel, on retire @PostConstruct du service et on appelle ici.
        // Choix : laisser @PostConstruct et ne rien mettre ici, ou enlever @PostConstruct et appeler.
        // Je conseille d'enlever @PostConstruct et d'appeler ici.
        jsonDataInitializationService.initialize();
    }
}
