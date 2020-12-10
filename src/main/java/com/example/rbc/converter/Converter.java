package com.example.rbc.converter;

import java.util.List;
import java.util.stream.Collectors;

public interface Converter<D, E> {
    E convertDtoToEntity(D dto);
    D convertEntityToDto(E entity);

    default List<E> convertAllDtoToEntities(List<D> dtos){
        return dtos
                .stream()
                .map(this::convertDtoToEntity)
                .collect(Collectors.toList());
    };

    default List<D> convertAllEntityToDtos(List<E> entities){
        return entities
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    };
}
