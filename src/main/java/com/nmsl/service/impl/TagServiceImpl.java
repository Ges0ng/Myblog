package com.nmsl.service.impl;


import com.nmsl.exception.NotFoundException;
import com.nmsl.dao.TagRepository;
import com.nmsl.entity.Tag;
import com.nmsl.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Paracosm
 * @Date 2021/1/19 17:17
 * @Version 1.0
 */

@Service
public class TagServiceImpl implements TagService {

    @Resource
    private TagRepository tagRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    /**
     * 分页查询
     */
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagRepository.findById(id).get();
        if (t == null){
            throw new NotFoundException("该标签不存在");
        }

        BeanUtils.copyProperties(tag,t);
        return tagRepository.save(t);
    }

    @Override
    public Tag getTag(Long id) {
        return tagRepository.findById(id).get();
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "blogs.size"));
        return tagRepository.findTop(pageable);
    }

    @Override
    public List<Tag> listTag(String ids) {
        return tagRepository.findAllById(convertToList(ids));
    }

    private List<Long> convertToList(String ids){
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idsArray = ids.split(",");
            for (int i = 0; i < idsArray.length; i++) {
                list.add(new Long(idsArray[i]));
            }
        }
        return list;
    }
}
