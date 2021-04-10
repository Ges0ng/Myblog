package com.nmsl.service;

import com.nmsl.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @Author Paracosm
 * @Date 2021/1/19 17:14
 * @Version 1.0
 */
public interface TagService {

    Tag saveTag(Tag tag);

    void deleteTag(Long id);

    Tag getTagByName(String name);

    Page<Tag> listTag(Pageable pageable);

    Tag updateTag(Long id,Tag tag);

    Tag getTag(Long id);

    List<Tag> listTag();

    List<Tag> listTagTop(Integer size);

    List<Tag> listTag(String ids);
}
