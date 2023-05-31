package jscode.service;

import jscode.domain.Article;
import jscode.dto.ArticleDto;
import jscode.repository.dao.ArticleDAOImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleDAOImpl articleDAO;

    // 게시글 저장
    public ArticleDto saveArticle(ArticleDto articleDto) {
        Article article = Article.builder()
                .title(articleDto.getTitle())
                .content(articleDto.getContent())
                .build();
        Article savedArticle = articleDAO.insertArticle(article);

        return new ArticleDto(savedArticle.getId(), savedArticle.getTitle(), savedArticle.getContent(), savedArticle.getCreatedAt(), savedArticle.getUpdatedAt());
    }

    // 게시글 가져오기
    public ArticleDto getArticle(Long id) {
        Article article = articleDAO.selectArticle(id);
        return new ArticleDto(article.getId(), article.getTitle(), article.getContent(), article.getCreatedAt(), article.getUpdatedAt());
    }

    // 모든 게시글 id 순서로 가져오기
    public List<ArticleDto> getAllArticles() {
        List<Article> articles = articleDAO.selectAllArticle();
        List<ArticleDto> articleDtos = new ArrayList<>();
        articles.stream().forEach(article -> articleDtos.add(new ArticleDto(article.getId(), article.getTitle(), article.getContent(), article.getCreatedAt(), article.getUpdatedAt())));
        return articleDtos;
    }

    // 모든 게시글 최신순 100개까지 가져오기
    public List<ArticleDto> getSortedAllArticles() {
        List<Article> articles = articleDAO.findTopNByOrderByCreatedAtDesc(100);
        List<ArticleDto> articleDtos = new ArrayList<>();
        articles.stream().forEach(article -> articleDtos.add(new ArticleDto(article.getId(), article.getTitle(), article.getContent(), article.getCreatedAt(), article.getUpdatedAt())));
        return articleDtos;
    }

    // 게시글 수정
    @Transactional
    public ArticleDto updateArticle(ArticleDto articleDto) {
        Article selectedArticle = articleDAO.updateArticle(articleDto);
        return new ArticleDto(selectedArticle.getId(), selectedArticle.getTitle(), selectedArticle.getContent(), selectedArticle.getCreatedAt(), selectedArticle.getUpdatedAt());
    }

    // 게시글 검색
    public List<ArticleDto> searchArticles(Pageable page, String keyword) {
        if (keyword.isBlank()) {
            throw new RuntimeException("키워드는 한 글자 이상이여야 합니다");
        } else {
            List<Article> articles = articleDAO.searchTopNOrderByCreatedAtDesc(page, keyword, 100);
            List<ArticleDto> articleDtos = new ArrayList<>();
            articles.stream().forEach(article -> articleDtos.add(new ArticleDto(article.getId(), article.getTitle(), article.getContent(), article.getCreatedAt(), article.getUpdatedAt())));
            return articleDtos;
        }
    }

    // 게시글 삭제
    public void deleteArticle(Long id) throws RuntimeException {
        articleDAO.deleteArticle(id);
    }
}
