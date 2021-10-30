package com.suki.controller;

import com.github.pagehelper.PageInfo;
import com.mysql.cj.protocol.x.Notice;
import com.suki.pojo.*;
import com.suki.service.*;
import org.apache.ibatis.annotations.Param;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StartController {


    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private MenuService menuService;

    /**
     * Suki
     * 主页
     * @return
     */
    @RequestMapping("/")
    public String startSSM(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                           @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                           Model model){
        System.out.println("a");

        HashMap<String, Object> criteria = new HashMap<>(1);
        criteria.put("status",1); //状态是发布状态的

        //文章列表
        PageInfo<Article> articleList = articleService.pageArticle(pageIndex, pageSize, criteria);
        model.addAttribute("pageInfo", articleList);

        //友情链接
        List<Link> linkList = linkService.listLink(1); //1为显示
        model.addAttribute("linkList", linkList);


        //分类
        /*List<Category> linkList = linkService.listLink(1); //1为显示
        model.addAttribute("linkList", linkList);*/

        //菜单
        List<Menu> menuList = menuService.listMenu();
        model.addAttribute("menuList",menuList);


        //侧边栏显示
        //标签列表显示
        List<Tag> allTagList = tagService.listTag();
        model.addAttribute("allTagList", allTagList);

        //最新评论
        List<Comment> recentCommentList = commentService.listRecentComment(null, 10);

        model.addAttribute("recentCommentList", recentCommentList);
        model.addAttribute("pageUrlPrefix", "/article?pageIndex");
        return "Home/index";
    }


    @RequestMapping("/404")
    public String NotFound(@RequestParam(required = false) String message, Model model) {
        model.addAttribute("message", message);
        return "Home/Error/404";
    }


    @RequestMapping("/403")
    public String Page403(@RequestParam(required = false) String message, Model model) {
        model.addAttribute("message", message);
        return "Home/Error/403";
    }

    @RequestMapping("/500")
    public String ServerError(@RequestParam(required = false) String message, Model model) {
        model.addAttribute("message", message);
        return "Home/Error/500";
    }

    /**
     * Suki
     * 注册页面跳转
     * @return
     */
    @RequestMapping("/register")
    public String regist(){
        System.out.println("b");
        return "Admin/register";
        //   return "Admin/reg";

    }
    /**
     * Suki
     * 登录页面跳转
     * @return
     */
    @RequestMapping("/login")
    public String toLogin(){
        System.out.println("c");
        return "Admin/login";
    }


    /**
     * Suki
     * 登录
     * @return
     */
    @RequestMapping(value = "/doLogin",method = RequestMethod.POST,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String doLogin(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          @RequestParam("rememberme") String rememberme,
                          HttpServletRequest request,
                          HttpServletResponse response){

        System.out.println("d");
        System.out.println(username);
        System.out.println(password);
        System.out.println(rememberme);

        Map<String, Object> map = new HashMap<>();

        /*先根据用户名字或者邮箱查出人，再对比*/
        User user = userService.getUserByNameOrEmail(username);


        if (user == null) {
            map.put("code", 0);
            map.put("msg", "用户名无效！");

            System.out.println("用户名无效");
        } else if (!user.getUserPass().equals(password)) {
            map.put("code", 0);
            map.put("msg", "密码错误！");

            System.out.println("密码无效");
        } else if (user.getUserStatus() == 0) {
            map.put("code", 0);
            map.put("msg", "账号已禁用！");

            System.out.println("无效");
        } else {
            //登录成功
            map.put("code", 1);
            map.put("msg", "登陆成功");

            System.out.println("成功");

            //添加session
            request.getSession().setAttribute("user", user);
            //添加cookie 实现记住我
            if (rememberme != null) {
                //创建两个Cookie对象
                Cookie nameCookie = new Cookie("username", username);
                //设置Cookie的有效期为3天
                nameCookie.setMaxAge(60 * 60 * 24 * 3);
                Cookie pwdCookie = new Cookie("password", password);
                pwdCookie.setMaxAge(60 * 60 * 24 * 3);
                response.addCookie(nameCookie);
                response.addCookie(pwdCookie);
            }
            user.setUserLastLoginTime(new Date());
            user.setUserLastLoginIp("192.168.10.24");
            userService.updateUser(user);

        }

        String result = new JSONObject(map).toString(); //转为json串

        System.out.println(result);        //{"msg":"密码错误！","code":0}
        return result;
    }




    /**
     * Suki
     * 登陆成功去到后台页面
     * @returne
     */
    @RequestMapping("/admin")
    public String toadmin(HttpSession session,Model model){
        System.out.println("e");


        User user = (User) session.getAttribute("user");
        System.out.println(user);
        Integer userId = null;
        if (!user.getUserRole().equals("admin")) {
            // 用户只能够查自己id下面的文章
            userId = user.getUserId();
            System.out.println(userId);
        }
        //影评文章列表
        List<Article> articleList = articleService.listRecentArticle(userId, 5);
        model.addAttribute("articleList", articleList);

        //评论列表
        List<Comment> commentList = commentService.listRecentComment(userId, 5);
        model.addAttribute("commentList", commentList);
        return "Admin/index";

    }



    /**
     * Suki
     * 注册成功
     * @returne
     */
    @RequestMapping(value = "/doRegister",method = RequestMethod.POST,produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String doRegister(@RequestParam("userName") String username,
                           @RequestParam("userEmail") String email,
                           @RequestParam("userNickname") String nickname,
                           @RequestParam("userPass") String password,
                           HttpServletRequest request,
                           HttpServletResponse response){

        System.out.println("f");

        System.out.println(username);

        Map<String, Object> map = new HashMap<>();

        //检查是否已经存在用户
        User checkUserName = userService.getUserByName(username);

        //检查是否已经存在邮箱
        User checkEmail = userService.getUserByEmail(email);
        if (checkUserName != null) {
            map.put("code", 0);
            map.put("msg", "用户已经存在");
        }else if(checkEmail != null){
            map.put("code", 0);
            map.put("msg", "邮箱已经存在");
        }else{
            // 添加用户
            User user = new User();
            user.setUserAvatar("/img/avatar/avatar.png");
            user.setUserName(username);
            user.setUserNickname(nickname);
            user.setUserPass(password);
            user.setUserEmail(email);
            user.setUserStatus(1);
            user.setArticleCount(0);
            user.setUserRole("user");

            userService.insertUser(user);

            map.put("code", 1);
            map.put("msg", "注册成功");
        }
        String result = new JSONObject(map).toString(); //转为json串
        System.out.println(result);
        return result;
    }



    @RequestMapping(value = "/search")
    public String search(
            @RequestParam("keywords") String keywords,
            @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize, Model model) {
        //文章列表
        HashMap<String, Object> criteria = new HashMap<>(2);
        criteria.put("status", 1);
        criteria.put("keywords", keywords);
        PageInfo<Article> articlePageInfo = articleService.pageArticle(pageIndex, pageSize, criteria);
        model.addAttribute("pageInfo", articlePageInfo);

        //侧边栏显示
        //标签列表显示
        List<Tag> allTagList = tagService.listTag();
        model.addAttribute("allTagList", allTagList);
        //获得随机文章
        List<Article> randomArticleList = articleService.listRandomArticle(8);
        model.addAttribute("randomArticleList", randomArticleList);
        //获得热评文章
        List<Article> mostCommentArticleList = articleService.listArticleByCommentCount(8);
        model.addAttribute("mostCommentArticleList", mostCommentArticleList);
        //最新评论
        List<Comment> recentCommentList = commentService.listRecentComment(null, 10);
        model.addAttribute("recentCommentList", recentCommentList);
        model.addAttribute("pageUrlPrefix", "/search?pageIndex");
        return "Home/Page/search";
    }













}
