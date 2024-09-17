package yourssu.blog.domain.comment.jpa;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import yourssu.blog.domain.BaseTimeEntity;
import yourssu.blog.domain.article.jpa.ArticleEntity;
import yourssu.blog.domain.user.jpa.UserEntity;

@Entity
@Table(name = "comment")
@Getter @Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class CommentEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private ArticleEntity articleEntity; // 어떤 게시글에 (댓글이) 달렸는지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity; // 어떤 유저가 (댓글을) 달았는지
}
