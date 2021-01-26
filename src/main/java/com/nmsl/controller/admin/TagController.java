package com.nmsl.controller.admin;

import com.nmsl.domain.Tag;
import com.nmsl.service.TagService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Author Paracosm
 * @Date 2021/1/19 21:07
 * @Version 1.0
 */

@Controller
@RequestMapping("/admin")
public class TagController {

    private String TAGS = "admin/tags";
    private String TAGS_INPUT = "admin/tags-input";
    private String TAGS_REDIRECT = "redirect:/admin/tags";

    @Resource
    private TagService tagService;

    /**
     * 分页查询按照id的倒序排序
     *
     * @param pageable
     * @return
     */
    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                                Pageable pageable, Model model) {
        model.addAttribute("tagNum", tagService.listTag());
        model.addAttribute("page", tagService.listTag(pageable));
        return TAGS;
    }

    /**
     * 新增标签页面
     * @return
     */
    @GetMapping("/tags/input")
    public String input(Model model){
        model.addAttribute("tag",new Tag());
        return TAGS_INPUT;
    }


    /**
     * 修改标签名称
     */
    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {

        model.addAttribute("tag",tagService.getTag(id));
        return TAGS_INPUT;
    }


    /**
     * 新增标签提交
     * @Valid 代表要校验对象
     * @return
     */
    @PostMapping("/tags")
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes){

        Tag tagName = tagService.getTagByName(tag.getName());
        if (tagName != null){
            /*说明已经存在,自定义错误*/
            result.rejectValue("name","nameError","不能使用重复的标签");
        }

        /*非空验证*/
        if (result.hasErrors()){
            return TAGS_INPUT;
        }

        Tag tagSave = tagService.saveTag(tag);
        if (tagSave == null) {
            //没保存成功
            attributes.addFlashAttribute("msg","添加失败,请重试!" );

        } else {
            //保存成功
            attributes.addFlashAttribute("msg","添加成功!" );
        }
        return TAGS_REDIRECT;
    }

    /**
     * 更新标签名称
     * @param tag
     * @param result
     * @param id
     * @param attributes
     * @return
     */
    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {

        Tag tagName = tagService.getTagByName(tag.getName());
        if (tagName != null) {
            /*说明已经存在,自定义错误*/
            result.rejectValue("name", "nameError", "不能使用重复的标签");
        }

        /*非空验证*/
        if (result.hasErrors()) {
            return  TAGS_INPUT;
        }

        Tag tagUpdate = tagService.updateTag(id,tag);
        if (tagUpdate == null) {
            //没保存成功
            attributes.addFlashAttribute("msg", "更新失败,请重试!");

        } else {
            //保存成功
            attributes.addFlashAttribute("msg", "更新成功!");
        }
        return TAGS_REDIRECT;
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        tagService.deleteTag(id);
        attributes.addFlashAttribute("msg", "删除成功!");
        return TAGS_REDIRECT;
    }

}
