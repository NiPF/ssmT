package club.nipengfei.ssm.controller;

import club.nipengfei.ssm.domain.Permission;
import club.nipengfei.ssm.domain.Role;
import club.nipengfei.ssm.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequestMapping("/role")
@Controller
public class RoleController {

    @Autowired
    private IRoleService roleService;

    // 给角色添加权限
    @RequestMapping("/addPermissionToRole.do")
    public String addPermissionToRole(@RequestParam(name = "roleId",required = true)String roleId,@RequestParam(name = "ids",required = true)String[] permissionIds) throws Exception {
        roleService.addPermissionToRole(roleId,permissionIds);
        return "redirect:findAll.do";
    }

    // 根据roleId查询role，并查询可以添加的权限
    @RequestMapping("/findRoleByIdAndAllPermission.do")
    public ModelAndView findRoleByIdAndAllPermission(@RequestParam(name = "id",required = true) String roleId) throws Exception {
        ModelAndView mv = new ModelAndView();
        // 根据roleId查询role
        Role role = roleService.findById(roleId);
        // 根据roleId查询可以添加权限
        List<Permission> otherPermissions = roleService.findOtherPermission(roleId);
        mv.addObject("role",role);
        mv.addObject("permissionList",otherPermissions);
        mv.setViewName("role-permission-add");
        return mv;
    }

    @RequestMapping("/save.do")
    public String save(Role role) throws Exception {
        roleService.save(role);
        return "redirect:findAll.do";
    }

    @RequestMapping("/findAll.do")
    public ModelAndView findAll() throws Exception {
        ModelAndView mv = new ModelAndView();
        List<Role> roleList = roleService.findAll();
        mv.addObject("roleList",roleList);
        mv.setViewName("role-list");
        return mv;
    }
}
