package ru.practicum.shareit.generic;

import java.util.List;

/**
 * The GenericMapper interface represents a generic mapper for converting entity to dto and vice versa.
 *
 * @param <E> The entity type.
 * @param <T> The dto type.
 */
public interface GenericMapper<E extends BaseEntity, T extends BaseDto> {

    /**
     * Converts a DTO object to an entity.
     *
     * @param requestDto The DTO object to be converted.
     * @return The corresponding entity.
     */
    E toEntity(T requestDto);

    /**
     * Converts an entity object to a DTO.
     *
     * @param entity The entity object to be converted.
     * @return The corresponding requestDTO.
     */
    T toDto(E entity);

    /**
     * Converts a list of DTOs to a list of entities.
     *
     * @param requestDtos The list of DTOs to be converted.
     * @return The corresponding list of entities.
     */
    List<E> toEntities(List<T> requestDtos);

    /**
     * Converts a list of entities to a list of DTOs.
     *
     * @param entities The list of entities to be converted.
     * @return The corresponding list of requestDTOs.
     */
    List<T> toDtos(List<E> entities);
}
