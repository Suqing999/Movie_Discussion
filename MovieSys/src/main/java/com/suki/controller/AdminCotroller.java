package com.suki.controller;

import cn.hutool.http.HtmlUtil;
import com.github.pagehelper.PageInfo;
import com.suki.pojo.*;
import com.suki.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminCotroller {

    @Autowired
    private UserService userService;

    @Autowired
    private OptionsService optionsService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;
    @RequestMapping("/profile")
    public String toProfile(HttpSession session, Model model){
        User sessionUser = (User) session.getAttribute("user");
        User user = userService.getUserById(sessionUser.getUserId());
        model.addAttribute("user", user);
        return "Admin/User/profile";
    }
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        session.invalidate();
        return "redirect:/login";
    }

    @RequestMapping("/profile/edit")
    public String profileEdit(HttpSession session,Model model){
        User loginUser = (User) session.getAttribute("user");
        User user = userService.getUserById(loginUser.getUserId());
        model.addAttribute("user", user);
        return "Admin/User/editProfile";
    }

    @RequestMapping("/profile/save")
    public String profileSave(User user,HttpSession session){
        User dbUser = (User) session.getAttribute("user");
        user.setUserId(dbUser.getUserId());
        userService.updateUser(user);
        return "redirect:/admin/profile";
    }


    @RequestMapping("/article/insertDraftSubmit")
    public String insertDraftSubmit(HttpSession session, ArticleParam articleParam) {
        Article article = new Article();
        //用户ID
        User user = (User) session.getAttribute("user");
        if (user != null) {
            article.setArticleUserId(user.getUserId());
        }
        article.setArticleTitle(articleParam.getArticleTitle());
        //文章摘要
        int summaryLength = 150;
        String summaryText = HtmlUtil.cleanHtmlTag(articleParam.getArticleContent());
        if (summaryText.length() > summaryLength) {
            String summary = summaryText.substring(0, summaryLength);
            article.setArticleSummary(summary);
        } else {
            article.setArticleSummary(summaryText);
        }
        article.setArticleThumbnail(articleParam.getArticleThumbnail());
        article.setArticleContent(articleParam.getArticleContent());
        article.setArticleStatus(articleParam.getArticleStatus());
        //填充分类
        List<Category> categoryList = new ArrayList<>();
        if (articleParam.getArticleChildCategoryId() != null) {
            categoryList.add(new Category(articleParam.getArticleParentCategoryId()));
        }
        if (articleParam.getArticleChildCategoryId() != null) {
            categoryList.add(new Category(articleParam.getArticleChildCategoryId()));
        }
        article.setCategoryList(categoryList);
        //填充标签
        List<Tag> tagList = new ArrayList<>();
        if (articleParam.getArticleTagIds() != null) {
            for (int i = 0; i < articleParam.getArticleTagIds().size(); i++) {
                Tag tag = new Tag(articleParam.getArticleTagIds().get(i));
                tagList.add(tag);
            }
        }
        article.setTagList(tagList);

        articleService.insertArticle(article);
        return "redirect:/admin";
    }



    @RequestMapping("/comment")
    public String commentList(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                              @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                              HttpSession session,
                              Model model) {
        User user = (User) session.getAttribute("user");
        HashMap<String, Object> criteria = new HashMap<>();
        if (!"admin".equals(user.getUserRole())) {
            // 用户查询自己的文章, 管理员查询所有的
            criteria.put("userId", user.getUserId());
        }
        PageInfo<Comment> commentPageInfo = commentService.listCommentByPage(pageIndex, pageSize, criteria);
        model.addAttribute("pageInfo", commentPageInfo);
        model.addAttribute("pageUrlPrefix", "/admin/comment?pageIndex");
        return "Admin/Comment/index";
    }

    @RequestMapping("/comment/receive")
    public String myReceiveComment(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                                   @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                   HttpSession session,
                                   Model model) {
        User user = (User) session.getAttribute("user");
        PageInfo<Comment> commentPageInfo = commentService.listReceiveCommentByPage(pageIndex, pageSize, user.getUserId());
        model.addAttribute("pageInfo", commentPageInfo);
        model.addAttribute("pageUrlPrefix", "/admin/comment?pageIndex");
        return "Admin/Comment/index";
    }

    @RequestMapping("/comment/reply/{id}")
    public String replyCommentView(@PathVariable("id") Integer id, Model model) {
        Comment comment = commentService.getCommentById(id);
        model.addAttribute("comment", comment);
        return "Admin/Comment/reply";
    }
    @RequestMapping("/comment/delete/{id}")
    public String deleteCommentView(@PathVariable("id") Integer id, Model model) {
        commentService.deleteCommentById(id);
        return "redirect:/admin/comment";
    }
    @RequestMapping("/comment/replySubmit")
    public String replyCommentSubmit(HttpServletRequest request, Comment comment, HttpSession session) {
        //文章评论数+1
        Article article = articleService.getArticleByStatusAndId(1, comment.getCommentArticleId());
        if (article == null) {
            return "redirect:/404";
        }
        User user = (User) session.getAttribute("user");
        comment.setCommentContent(HtmlUtil.escape(comment.getCommentContent()));
        comment.setCommentAuthorName(user.getUserNickname());
        comment.setCommentAuthorEmail(user.getUserEmail());
        comment.setCommentAuthorUrl(user.getUserUrl());
        article.setArticleCommentCount(article.getArticleCommentCount() + 1);
        articleService.updateArticle(article); //更新文章评论
        //添加评论
        comment.setCommentCreateTime(new Date());
        comment.setCommentIp("192.168.1.100");
        if (Objects.equals(user.getUserId(), article.getArticleUserId())) {
            comment.setCommentRole(1);//博主
        } else {
            comment.setCommentRole(0);//设置评论人为其他用户
        }
        commentService.insertComment(comment);//提交
        return "redirect:/admin/comment";
    }


    @RequestMapping("/user")
    public ModelAndView userList() {
        ModelAndView modelandview = new ModelAndView();

        List<User> userList = userService.listUser();
        modelandview.addObject("userList", userList);

        modelandview.setViewName("Admin/User/index");
        return modelandview;

    }
   @RequestMapping("/user/insert")
   public ModelAndView insertUserView() {
       ModelAndView modelAndView = new ModelAndView();
       modelAndView.setViewName("Admin/User/insert");
       return modelAndView;
   }
    @RequestMapping("/user/insertSubmit")
    public String insertUserSubmit(User user) {
        User user2 = userService.getUserByName(user.getUserName());
        User user3 = userService.getUserByEmail(user.getUserEmail());
        if (user2 == null && user3 == null) {
            user.setUserRegisterTime(new Date());
            user.setUserStatus(1);
            user.setUserRole("user");
            userService.insertUser(user);
        }
        return "redirect:/admin/user";
    }


    @RequestMapping("/user/edit/{id}")
    public ModelAndView editUserView(@PathVariable("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView();

        User user = userService.getUserById(id);
        modelAndView.addObject("user", user);

        modelAndView.setViewName("Admin/User/edit");
        return modelAndView;
    }



    @RequestMapping("/user/editSubmit")
    public String editUserSubmit(User user) {
        userService.updateUser(user);
        return "redirect:/admin/user";
    }
    @RequestMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return "redirect:/admin/user";
    }




    @RequestMapping("/options")
    public ModelAndView index()  {
        ModelAndView modelAndView = new ModelAndView();
        Options option = optionsService.getOptions();
        modelAndView.addObject("option",option);

        modelAndView.setViewName("Admin/Options/index");
        return modelAndView;
    }




    @RequestMapping("/options/editSubmit")
    public String editOptionSubmit(Options options)  {
        //如果记录不存在，那就新建
        Options optionsCustom = optionsService.getOptions();
        if(optionsCustom.getOptionId()==null) {
            optionsService.insertOptions(options);
        } else {
            optionsService.updateOptions(options);
        }
        return "redirect:/admin/options";
    }








    @RequestMapping("/article")
    public String indexArticle(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                        @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                        @RequestParam(required = false) String status, Model model,
                        HttpSession session) {
        HashMap<String, Object> criteria = new HashMap<>(1);
        if (status == null) {
            model.addAttribute("pageUrlPrefix", "/admin/article?pageIndex");
        } else {
            criteria.put("status", status);
            model.addAttribute("pageUrlPrefix", "/admin/article?status=" + status + "&pageIndex");
        }

        User user = (User) session.getAttribute("user");
        if (!user.getUserRole().equals("admin")) {
            // 用户查询自己的文章, 管理员查询所有的
            criteria.put("userId", user.getUserId());
        }
        PageInfo<Article> articlePageInfo = articleService.pageArticle(pageIndex, pageSize, criteria);
        model.addAttribute("pageInfo", articlePageInfo);
        return "Admin/Article/index";
    }




    @RequestMapping("/article/insert")
    public String insertArticleView(Model model) {
        List<Category> categoryList = categoryService.listCategory();
        List<Tag> tagList = tagService.listTag();
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("tagList", tagList);
        return "Admin/Article/insert";
    }



    @RequestMapping("/category")
    public ModelAndView categoryList()  {
        ModelAndView modelandview = new ModelAndView();
        List<Category> categoryList = categoryService.listCategoryWithCount();
        modelandview.addObject("categoryList",categoryList);
        modelandview.setViewName("Admin/Category/index");
        return modelandview;
    }


    @RequestMapping("/category/insertSubmit")
    public String insertCategorySubmit(Category category)  {
        categoryService.insertCategory(category);
        return "redirect:/admin/category";
    }
  /* <a href="/admin/category/edit/${c.categoryId}" class="layui-btn layui-btn-mini">编辑</a>
                                <c:if test="${c.articleCount==0}">
                                    <a href="/admin/category/delete/${c.categoryId}" class="layui-btn layui-btn-danger layui-btn-mini" onclick="return confirmDelete()">删除</a>
                                </c:if>*/
  @RequestMapping("/category/edit/{categoryId}")
  public ModelAndView editCategoryView(@PathVariable("categoryId") Integer id)  {
      ModelAndView modelAndView = new ModelAndView();

      Category category =  categoryService.getCategoryById(id);
      modelAndView.addObject("category",category);

      List<Category> categoryList = categoryService.listCategoryWithCount();
      modelAndView.addObject("categoryList",categoryList);

      modelAndView.setViewName("Admin/Category/edit");
      return modelAndView;
  }

    @RequestMapping("/category/editSubmit")
    public String editCategorySubmit(Category category)  {
        categoryService.updateCategory(category);
        return "redirect:/admin/category";
    }


    @RequestMapping("/category/delete/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") Integer id)  {
        //禁止删除有文章的分类
        int count = articleService.countArticleByCategoryId(id);

        if (count == 0) {
            categoryService.deleteCategory(id);
        }
        return "redirect:/admin/category";
    }














    @RequestMapping("/tag")
    public ModelAndView indexTag()  {
        ModelAndView modelandview = new ModelAndView();
        List<Tag> tagList = tagService.listTagWithCount();
        modelandview.addObject("tagList",tagList);
        modelandview.setViewName("Admin/Tag/index");
        return modelandview;

    }

  /*
                            <a href="/admin/article/del/${a.articleId}"*/


    @RequestMapping("/article/edit/{articleId}")
    public String editArticleView(@PathVariable("articleId") Integer id, Model model, HttpSession session) {

        Article article = articleService.getArticleByStatusAndId(null, id);
        if (article == null) {
            return "redirect:/404";
        }
        User user = (User) session.getAttribute("user");
        // 如果不是管理员，访问其他用户的数据，则跳转403
        if (!Objects.equals(article.getArticleUserId(), user.getUserId()) && !Objects.equals(user.getUserRole(), "admin")) {
            return "redirect:/403";
        }
        model.addAttribute("article", article);
        List<Category> categoryList = categoryService.listCategory();
        model.addAttribute("categoryList", categoryList);
        List<Tag> tagList = tagService.listTag();
        model.addAttribute("tagList", tagList);
        return "Admin/Article/edit";
    }

    @RequestMapping("/article/del/{articleId}")
    public String delArticle(@PathVariable("articleId") Integer id) {
        articleService.delArticle(id);
        return "redirect:/admin/article";
    }



    @RequestMapping("/article/editSubmit")
    public String editArticleSubmit(ArticleParam articleParam, HttpSession session) {
        Article dbArticle = articleService.getArticleByStatusAndId(null, articleParam.getArticleId());
        if (dbArticle == null) {
            return "redirect:/404";
        }
        User user = (User) session.getAttribute("user");
        // 如果不是管理员，访问其他用户的数据，则跳转403
        if (!Objects.equals(dbArticle.getArticleUserId(), user.getUserId()) && !Objects.equals(user.getUserRole(), "admin")) {
            return "redirect:/403";
        }
        Article article = new Article();
        article.setArticleThumbnail(articleParam.getArticleThumbnail());
        article.setArticleId(articleParam.getArticleId());
        article.setArticleTitle(articleParam.getArticleTitle());
        article.setArticleContent(articleParam.getArticleContent());
        article.setArticleStatus(articleParam.getArticleStatus());
        //文章摘要
        int summaryLength = 150;
        String summaryText = HtmlUtil.cleanHtmlTag(article.getArticleContent());
        if (summaryText.length() > summaryLength) {
            String summary = summaryText.substring(0, summaryLength);
            article.setArticleSummary(summary);
        } else {
            article.setArticleSummary(summaryText);
        }
        //填充分类
        List<Category> categoryList = new ArrayList<>();
        if (articleParam.getArticleChildCategoryId() != null) {
            categoryList.add(new Category(articleParam.getArticleParentCategoryId()));
        }
        if (articleParam.getArticleChildCategoryId() != null) {
            categoryList.add(new Category(articleParam.getArticleChildCategoryId()));
        }
        article.setCategoryList(categoryList);
        //填充标签
        List<Tag> tagList = new ArrayList<>();
        if (articleParam.getArticleTagIds() != null) {
            for (int i = 0; i < articleParam.getArticleTagIds().size(); i++) {
                Tag tag = new Tag(articleParam.getArticleTagIds().get(i));
                tagList.add(tag);
            }
        }
        article.setTagList(tagList);
        articleService.updateArticleDetail(article);
        return "redirect:/admin/article";
    }


    @RequestMapping("/article/insertSubmit")
    public String insertArticleSubmit(HttpSession session, ArticleParam articleParam) {
        Article article = new Article();
        //用户ID
        User user = (User) session.getAttribute("user");
        if (user != null) {
            article.setArticleUserId(user.getUserId());
        }
        article.setArticleTitle(articleParam.getArticleTitle());
        //文章摘要
        int summaryLength = 150;
        String summaryText = HtmlUtil.cleanHtmlTag(articleParam.getArticleContent());
        if (summaryText.length() > summaryLength) {
            String summary = summaryText.substring(0, summaryLength);
            article.setArticleSummary(summary);
        } else {
            article.setArticleSummary(summaryText);
        }
        article.setArticleThumbnail(articleParam.getArticleThumbnail());
        article.setArticleContent(articleParam.getArticleContent());
        article.setArticleStatus(articleParam.getArticleStatus());
        //填充分类
        List<Category> categoryList = new ArrayList<>();
        if (articleParam.getArticleChildCategoryId() != null) {
            categoryList.add(new Category(articleParam.getArticleParentCategoryId()));
        }
        if (articleParam.getArticleChildCategoryId() != null) {
            categoryList.add(new Category(articleParam.getArticleChildCategoryId()));
        }
        article.setCategoryList(categoryList);
        //填充标签
        List<Tag> tagList = new ArrayList<>();
        if (articleParam.getArticleTagIds() != null) {
            for (int i = 0; i < articleParam.getArticleTagIds().size(); i++) {
                Tag tag = new Tag(articleParam.getArticleTagIds().get(i));
                tagList.add(tag);
            }
        }
        article.setTagList(tagList);

        articleService.insertArticle(article);
        return "redirect:/admin/article";
    }











}
