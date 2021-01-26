package com.nmsl.controller.admin;

import com.nmsl.Exception.NotFoundException;
import com.nmsl.domain.Type;
import com.nmsl.service.TypeService;
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
public class TypeController {

    private String TYPES = "admin/types";
    private String TYPES_INPUT = "admin/types-input";
    private String TYPES_REDIRECT = "redirect:/admin/types";

    @Resource
    private TypeService typeService;

    /**
     * 分页查询按照id的倒序排序
     *
     * @param pageable
     * @return
     */
    @GetMapping("/types")
    public String types(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                                Pageable pageable, Model model) {
        model.addAttribute("typeNum", typeService.listType());
        model.addAttribute("page", typeService.listType(pageable));
        return TYPES;
    }

    /**
     * 新增分类页面
     * @return
     */
    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type",new Type());
        return TYPES_INPUT;
    }


    /**
     * 修改分类名称
     */
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {

        model.addAttribute("type",typeService.getType(id));
        return TYPES_INPUT;
    }


    /**
     * 新增分类提交
     * @Valid 代表要校验对象
     * @return
     */
    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes){

        Type typeName = typeService.getTypeByName(type.getName());
        if (typeName != null){
            /*说明已经存在,自定义错误*/
            result.rejectValue("name","nameError","不能使用重复的分类");
        }

        /*非空验证*/
        if (result.hasErrors()){
            return TYPES_INPUT;
        }

        Type typeSave = typeService.saveType(type);
        if (typeSave == null) {
            //没保存成功
            attributes.addFlashAttribute("msg","添加失败,请重试!" );

        } else {
            //保存成功
            attributes.addFlashAttribute("msg","添加成功!" );
        }
        return TYPES_REDIRECT;
    }

    /**
     * 更新分类名称
     * @param type
     * @param result
     * @param id
     * @param attributes
     * @return
     */
    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {

        Type typeName = typeService.getTypeByName(type.getName());
        if (typeName != null) {
            /*说明已经存在,自定义错误*/
            result.rejectValue("name", "nameError", "不能使用重复的分类");
        }

        /*非空验证*/
        if (result.hasErrors()) {
            return TYPES_INPUT;
        }

        Type typeUpdate = typeService.updateType(id,type);
        if (typeUpdate == null) {
            //没保存成功
            attributes.addFlashAttribute("msg", "更新失败,请重试!");

        } else {
            //保存成功
            attributes.addFlashAttribute("msg", "更新成功!");
        }
        return TYPES_REDIRECT;
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("msg", "删除成功!");
        return TYPES_REDIRECT;
    }

}
