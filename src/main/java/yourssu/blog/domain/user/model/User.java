package yourssu.blog.domain.user.model;

import lombok.Builder;
import lombok.Getter;
import yourssu.blog.domain.article.model.Article;
import yourssu.blog.domain.article.service.port.ArticleRepository;
import yourssu.blog.domain.comment.model.Comment;
import yourssu.blog.domain.comment.service.port.CommentRepository;

import java.util.List;

@Getter
public class User {
    private final Long id;
    private final String email;
    private final String password;
    private final String username;

    @Builder
    public User(Long id, String email, String password, String username){
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    // 회원 탈퇴 시, 해당 유저가 작성한 게시글과 댓글이 있다면 삭제
    public void deleteUserRelatedContent(ArticleRepository articleRepository, CommentRepository commentRepository){
        // 유저가 작성한 모든 '게시글'을 가져와서, 삭제할 것
        List<Article> articles = articleRepository.findByUserId(this.id);
        for (Article article : articles) {
            // 게시글과 연관된 댓글이 존재한다면, '댓글' 삭제
            article.deleteCommentsIfExist(commentRepository);
            // '게시글' 삭제
            articleRepository.delete(article);
        }

        // 유저가 작성한 모든 '댓글' 가져와서 삭제할 것
        List<Comment> comments = commentRepository.findByUserId(this.id);
        for (Comment comment : comments) {
            commentRepository.delete(comment);
        }
    }

}
