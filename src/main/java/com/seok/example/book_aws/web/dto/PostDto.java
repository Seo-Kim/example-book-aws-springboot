package com.seok.example.book_aws.web.dto;

import com.seok.example.book_aws.entity.posts.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PostDto {
    @Getter
    @NoArgsConstructor
    public static class List {
        private Long id;
        private String title;
        private String author;
        private LocalDateTime modifiedDate;

        public List( Post entity ) {
            this.id = entity.getId();
            this.title = entity.getTitle();
            this.author = entity.getAuthor();
            this.modifiedDate = entity.getModifiedDate();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Select {
        private Long id;
        private String title;
        private String content;
        private String author;
        private Long createId;

        public Select( Post entity ) {
            this.id = entity.getId();
            this.title = entity.getTitle();
            this.content = entity.getContent();
            this.author = entity.getAuthor();
            this.createId = entity.getCreateId();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class SaveRequest {
        private String title;
        private String content;
        private String author;
        private Long createId;

        @Builder
        public SaveRequest( String title, String content, String author, Long createId ) {
            this.title = title;
            this.content = content;
            this.author = author;
            this.createId = createId;
        }

        public Post toEntity() {
            return Post.builder()
                    .title( title )
                    .content( content )
                    .author( author )
                    .createId( createId )
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateRequest {
        private String title;
        private String content;
        private Long modifyId;

        @Builder
        public UpdateRequest( String title, String content, Long modifyId ) {
            this.title = title;
            this.content = content;
            this.modifyId = modifyId;
        }
    }
}