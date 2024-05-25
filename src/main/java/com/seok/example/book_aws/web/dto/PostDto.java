package com.seok.example.book_aws.web.dto;

import com.seok.example.book_aws.domain.posts.PostEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PostDto {
    @Getter
    @NoArgsConstructor
    public static class Select {
        private Long id;
        private String title;
        private String content;
        private String author;

        public Select( PostEntity entity ) {
            this.id = entity.getId();
            this.title = entity.getTitle();
            this.content = entity.getContent();
            this.author = entity.getAuthor();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class SaveRequest {
        private String title;
        private String content;
        private String author;

        @Builder
        public SaveRequest( String title, String content, String author ) {
            this.title = title;
            this.content = content;
            this.author = author;
        }

        public PostEntity toEntity() {
            return PostEntity.builder()
                    .title( title )
                    .content( content )
                    .author( author )
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateRequest {
        private String title;
        private String content;

        @Builder
        public UpdateRequest( String title, String content ) {
            this.title = title;
            this.content = content;
        }
    }
}
