package yourssu.blog.domain.article.model;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import yourssu.blog.domain.comment.model.Comment;
import yourssu.blog.domain.comment.service.port.CommentRepository;
import yourssu.blog.domain.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Article {
    private final Long id;
    private final String content;
    private final String title;
    private final User user;

    @Builder
    public Article(Long id, String content, String title, User user){
        this.id = id;
        this.content = content;
        this.title = title;
        this.user = user;
    }

    // setter 대신, 값 변경하는 것 모두 모은 메서드 작성
    // 객체의 상태를 직접 변경하지 않고, 변경될 때마다 새로운 상태를 반환하는 불변 객체 !!
    public Article editArticle(String content, String title){
        return Article.builder()
                .id(this.id)
                .content(content)
                .title(title)
                .user(this.user)
                .build();
    }

    // 게시글과 관련된 댓글이 존재할 때만, 삭제
    public void deleteCommentsIfExist(CommentRepository commentRepository){
        List<Comment> comments = commentRepository.findByArticleId(this.id);

        if (!comments.isEmpty()){
            comments.forEach(comment -> commentRepository.delete(comment));
        }
    }
}
