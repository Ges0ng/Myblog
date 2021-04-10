package com.nmsl.service.impl;

import com.nmsl.exception.NotFoundException;
import com.nmsl.dao.TypeRepository;
import com.nmsl.entity.Type;
import com.nmsl.service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author Paracosm
 * @Date 2021/1/19 17:17
 * @Version 1.0
 */

@Service
public class TypeServiceImpl implements TypeService {

    @Resource
    private TypeRepository typeRepository;

    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }

    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }

    /**
     * 分页查询
     * @param pageable
     * @return
     */
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRepository.findById(id).get();
        if (t == null){
            throw new NotFoundException("类型不存在");
        }

        BeanUtils.copyProperties(type,t);
        return typeRepository.save(t);
    }

    @Override
    public Type getType(Long id) {
        return typeRepository.findById(id).get();
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Override
    public List<Type> listTypeToTop(Integer size) {
        /*Sort.Order sort =new Sort.Order(Sort.Direction.DESC,"blogs.size");*/
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "blogs.size"));
        return typeRepository.findTop(pageable);
    }
}
