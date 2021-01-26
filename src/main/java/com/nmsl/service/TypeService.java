package com.nmsl.service;

import com.nmsl.domain.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Author Paracosm
 * @Date 2021/1/19 17:14
 * @Version 1.0
 */
public interface TypeService {

    Type saveType(Type type);

    void deleteType(Long id);

    Type getTypeByName(String name);

    Page<Type> listType(Pageable pageable);

    Type updateType(Long id,Type type);

    Type getType(Long id);

    List<Type> listType();

    List<Type> listTypeToTop(Integer size);

}
