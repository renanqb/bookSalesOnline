/*
 * DEPRECATED - Esta classe foi removida em favor de @Service no GetEntityByIdUseCaseImpl
 *
 * Antes: GetEntityByIdUseCaseToggle criava um @Bean de GetEntityByIdUseCase com feature flag
 * Agora: GetEntityByIdUseCaseImpl possui @Service e é injetado automaticamente
 *
 * O feature flag features.cache.experimental agora é controlado em GetEntityByIdUseCaseImpl
 * se necessário usar lógica condicional.
 */
