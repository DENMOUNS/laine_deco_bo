package cm.dolers.laine_deco.infrastructure.i18n;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * Configuration de l'internationalisation (i18n) pour Spring Boot
 * Prend en charge FR et EN via Accept-Language header
 */
@Configuration
@RequiredArgsConstructor
public class I18nConfiguration implements WebMvcConfigurer {
    private final UserLanguageInterceptor userLanguageInterceptor;

    /**
     * Configure la source de messages (fichiers .properties)
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = 
            new ReloadableResourceBundleMessageSource();
        
        // Chemin vers les fichiers messages
        messageSource.setBasename("classpath:i18n/messages");
        
        // Encoding UTF-8 pour supporter les caractères spéciaux
        messageSource.setDefaultEncoding("UTF-8");
        
        // Délai de cache (60 secondes)
        messageSource.setCacheSeconds(60);
        
        // Locale par défaut
        messageSource.setDefaultLocale(Locale.FRENCH);
        
        return messageSource;
    }

    /**
     * Configure le resolving de la locale basé sur Accept-Language header
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.FRENCH);
        return resolver;
    }

    /**
     * Intercepteur pour changer la langue via paramètre ?lang=en ou Accept-Language header
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");  // Accepte ?lang=fr ou ?lang=en
        return interceptor;
    }

    /**
     * Enregistre les intercepteurs de locale
     * Priorité:
     * 1. UserLanguageInterceptor -> langue utilisateur
     * 2. LocaleChangeInterceptor -> paramètre ?lang
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // D'abord vérifier la langue de l'utilisateur connecté
        registry.addInterceptor(userLanguageInterceptor)
            .addPathPatterns("/**")
            .order(1);
        
        // Ensuite paramètre ?lang
        registry.addInterceptor(localeChangeInterceptor())
            .order(2);
    }
}
