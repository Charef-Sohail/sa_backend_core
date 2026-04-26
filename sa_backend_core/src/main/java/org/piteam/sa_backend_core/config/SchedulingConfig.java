package org.piteam.sa_backend_core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    // Cette classe sert uniquement à forcer l'allumage du moteur CRON au démarrage
}