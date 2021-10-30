package com.suki.controller;

import cn.hutool.http.HtmlUtil;
import com.github.pagehelper.PageInfo;
import com.suki.pojo.*;
import com.suki.service.ArticleService;
import com.suki.service.CommentService;
import com.suki.service.UserService;
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
    private ArticleService articleService;

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



}
