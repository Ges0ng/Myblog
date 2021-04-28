package com.nmsl.controller.admin;

import com.nmsl.common.CommonUrl;
import com.nmsl.entity.Type;
import com.nmsl.service.TypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * 分类管理
 * @Author Paracosm
 * @Date 2021/1/19 21:07
 * @Version 1.0
 */

@Controller
@RequestMapping("/admin")
@Api(tags = "后台分类管理模块")
public class TypeController {

    @Resource
    private TypeService typeService;

    /**
     * 分页查询按照id的倒序排序
     */
    @GetMapping("/types")
    @ApiOperation(value = "分页查询按照id的倒序排序")
    public String types(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                                Pageable pageable, Model model) {
        model.addAttribute("typeNum", typeService.listType());
        model.addAttribute("page", typeService.listType(pageable));
        return CommonUrl.TYPES;
    }

    /**
     * 新增分类页面
     */
    @GetMapping("/types/input")
    @ApiOperation(value = "新增分类页面")
    public String input(Model model){
        model.addAttribute("type",new Type());
        return CommonUrl.TYPES_INPUT;
    }


    /**
     * 修改分类名称
     */
    @GetMapping("/types/{id}/input")
    @ApiOperation(value = "修改分类名称")
    public String editInput(@PathVariable Long id, Model model) {

        model.addAttribute("type",typeService.getType(id));
        return CommonUrl.TYPES_INPUT;
    }


    /**
     * 新增分类提交
     * @Valid 代表要校验对象
     */
    @PostMapping("/types")
    @ApiOperation(value = "新增分类提交")
    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes){

        Type typeName = typeService.getTypeByName(type.getName());
        if (typeName != null){
            /*说明已经存在,自定义错误*/
            result.rejectValue("name","nameError","不能使用重复的分类");
        }

        /*非空验证*/
        if (result.hasErrors()){
            return CommonUrl.TYPES_INPUT;
        }

        Type typeSave = typeService.saveType(type);
        if (typeSave == null) {
            //没保存成功
            attributes.addFlashAttribute("msg","添加失败,请重试!" );

        } else {
            //保存成功
            attributes.addFlashAttribute("msg","添加成功!" );
        }
        return CommonUrl.TYPES_REDIRECT;
    }

    /**
     * 更新分类名称
     */
    @PostMapping("/types/{id}")
    @ApiOperation(value = "更新分类名称")
    public String editPost(@Valid Type type, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {

        Type typeName = typeService.getTypeByName(type.getName());
        if (typeName != null) {
            /*说明已经存在,自定义错误*/
            result.rejectValue("name", "nameError", "不能使用重复的分类");
        }
        /*非空验证*/
        if (result.hasErrors()) {
            return CommonUrl.TYPES_INPUT;
        }

        Type typeUpdate = typeService.updateType(id,type);
        if (typeUpdate == null) {
            //没保存成功
            attributes.addFlashAttribute("msg", "更新失败,请重试!");
        } else {
            //保存成功
            attributes.addFlashAttribute("msg", "更新成功!");
        }
        return CommonUrl.TYPES_REDIRECT;
    }

    /**
     * 根据id删除标签
     */
    @GetMapping("/types/{id}/delete")
    @ApiOperation(value = "根据id删除标签")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("msg", "删除成功!");
        return CommonUrl.TYPES_REDIRECT;
    }

}
